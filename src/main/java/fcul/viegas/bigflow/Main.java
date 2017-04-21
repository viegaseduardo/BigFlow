/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.parser.NetworkPacketParserMapFunction;
import fcul.viegas.bigflow.timestamp.NetworkPacketTimestampAssigner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

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

        //
        KeyedStream<NetworkPacketDTO, Integer> keyIPSrcDst = singleOutput.keyBy(new KeySelector<NetworkPacketDTO, Integer>() {
            @Override
            public Integer getKey(NetworkPacketDTO in) throws Exception {
                Integer srcDstHash = (in.getSourceIP() + in.getDestinationIP()).hashCode();
                Integer dstSrcHash = (in.getDestinationIP() + in.getSourceIP()).hashCode();
                Integer hash = (srcDstHash > dstSrcHash)
                        ? srcDstHash ^ dstSrcHash
                        : dstSrcHash ^ srcDstHash;
                return hash;
            }
        });
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        long startTime = System.currentTimeMillis();

        env.execute("meu job");

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Tempo de execucao: " + elapsedTime);

    }

}
