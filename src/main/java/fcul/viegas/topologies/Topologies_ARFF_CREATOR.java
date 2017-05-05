/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.parser.NetworkPacketParserMapFunction;
import fcul.viegas.bigflow.timestamp.NetworkPacketTimestampAssigner;
import fcul.viegas.bigflow.windows.feature.extractor.FeatureClassAssigner;
import fcul.viegas.bigflow.windows.feature.extractor.NetworkPacketWindowJoiner;
import fcul.viegas.bigflow.windows.feature.extractor.NetworkPacketWindow_A;
import fcul.viegas.bigflow.windows.feature.extractor.NetworkPacketWindow_A_B;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem.WriteMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import scala.App$class;

/**
 *
 * @author viegas
 */
public class Topologies_ARFF_CREATOR {

    public static void runTopology(
            String networkPacketFilePath, 
            String networkClassDescriptionPath,
            String networkArffPath) 
            throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        
        //env.setParallelism(5);

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //read file
        DataStreamSource<String> dataStreamSource = env.readTextFile(networkPacketFilePath);
        dataStreamSource.setParallelism(1);

        //parse data and correct order
        SingleOutputStreamOperator<NetworkPacketDTO> singleOutput = dataStreamSource.map(new NetworkPacketParserMapFunction())
                .assignTimestampsAndWatermarks(new NetworkPacketTimestampAssigner());

        //key stream by ips regardless of source and dest order
        KeyedStream<NetworkPacketDTO, Long> keyIPSrcDst = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Long>() {
            @Override
            public Long getKey(NetworkPacketDTO in) throws Exception {
                Long srcDstHash = Long.valueOf((in.getSourceIP() + in.getDestinationIP()).hashCode());
                Long dstSrcHash = Long.valueOf((in.getDestinationIP() + in.getSourceIP()).hashCode());
                Long hash = (srcDstHash > dstSrcHash)
                        ? srcDstHash << 32 + dstSrcHash
                        : dstSrcHash << 32 + srcDstHash;
                return hash;
            }
        });

        //key stream by source ip
        KeyedStream<NetworkPacketDTO, Integer> keyIPSrc = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Integer>() {
            @Override
            public Integer getKey(NetworkPacketDTO in) throws Exception {
                return (in.getSourceIP()).hashCode();
            }
        });

        //A to B window operator
        SingleOutputStreamOperator<Features_A_B_DTO> networkPacketWindow_A_B
                = keyIPSrcDst.timeWindow(Time.milliseconds(Definitions.TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A_B))
                        .fold(new Features_A_B_DTO(), new NetworkPacketWindow_A_B());

        //A window operator
        SingleOutputStreamOperator<Features_A_DTO> networkPacketWindow_A
                = keyIPSrcDst.timeWindow(Time.milliseconds(Definitions.TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A))
                        .fold(new Features_A_DTO(), new NetworkPacketWindow_A());

        //join A and AtoB back together
        DataStream<Features_DTO> networkFeatures = networkPacketWindow_A_B.join(networkPacketWindow_A)
                .where(new KeySelector<Features_A_B_DTO, Integer>() {
                    @Override
                    public Integer getKey(Features_A_B_DTO in) throws Exception {
                        return (in.getSourceAddress()).hashCode();
                    }
                }).equalTo(new KeySelector<Features_A_DTO, Integer>() {
            @Override
            public Integer getKey(Features_A_DTO in) throws Exception {
                return (in.getAddress()).hashCode();
            }
        }).window(TumblingEventTimeWindows.of(Time.milliseconds(2000l)))
                .apply(new NetworkPacketWindowJoiner());

        SingleOutputStreamOperator<Features_DTO> networkFeaturesAssingedClass
                = networkFeatures.map(new FeatureClassAssigner(networkClassDescriptionPath));
        
        networkFeaturesAssingedClass.writeAsText(networkArffPath, WriteMode.OVERWRITE).setParallelism(1);

        long startTime = System.currentTimeMillis();
        
        env.execute(networkPacketFilePath + "_JOB");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Execution Time: " + elapsedTime);
    }

}
