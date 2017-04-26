/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_A_B;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.parser.NetworkPacketParserMapFunction;
import fcul.viegas.bigflow.timestamp.NetworkPacketTimestampAssigner;
import fcul.viegas.bigflow.windows.feature.extractor.NetworkPacketWindow_A_B;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 *
 * @author viegas
 */
public class Main {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //env.setParallelism(5);

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //read file
        DataStreamSource<String> dataStreamSource = env.readTextFile("/home/viegas/Desktop/saida/cenario1.txt");
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
        
        //key stream by ips regardless of source and dest order
        KeyedStream<NetworkPacketDTO, Integer> keyIPSrc = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Integer>() {
            @Override
            public Integer getKey(NetworkPacketDTO in) throws Exception {
                return (in.getSourceIP()).hashCode();
            }
        });
        
        
        
        

        keyIPSrcDst.timeWindow(Time.milliseconds(Definitions.TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A_B))
                .fold(new Features_A_B(), new NetworkPacketWindow_A_B());
        
        

        long startTime = System.currentTimeMillis();

        env.execute("meu job");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tempo de execucao: " + elapsedTime);

    }

}
