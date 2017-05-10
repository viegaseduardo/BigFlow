/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.parser;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class NetworkPacketParserMapFunction extends RichMapFunction<String, NetworkPacketDTO> {

    public static int n = 0;
    public static String path = "";
    private IntCounter numPackets = new IntCounter();

    @Override
    public void open(Configuration parameters) {
        getRuntimeContext().addAccumulator("num-packets", this.numPackets);
    }

    @Override
    public NetworkPacketDTO map(String t) throws Exception {
        
        this.numPackets.add(1);

        NetworkPacketParserMapFunction.n++;
        if (NetworkPacketParserMapFunction.n % 100000 == 0) {
            //System.out.println(NetworkPacketParserMapFunction.path + "\t" + NetworkPacketParserMapFunction.n);
        }

        String[] split = t.split(";");

        NetworkPacketDTO networkPacketDTO = new NetworkPacketDTO();

        networkPacketDTO.setTimestamp(Long.valueOf(split[0]));
        networkPacketDTO.setSourceIP(split[1]);
        networkPacketDTO.setDestinationIP(split[2]);
        String protocol = split[3];
        if (protocol.equals("TCP")) {
            networkPacketDTO.setProtocol(Definitions.PROTOCOL_TCP);
        } else if (protocol.equals("UDP")) {
            networkPacketDTO.setProtocol(Definitions.PROTOCOL_UDP);
        } else if (protocol.equals("ICMP")) {
            networkPacketDTO.setProtocol(Definitions.PROTOCOL_ICMP);
        } else {
            networkPacketDTO.setProtocol(Definitions.PROTOCOL_OTHER);
        }
        networkPacketDTO.setTimeToLive(Integer.valueOf(split[4]));
        networkPacketDTO.setUdp_source(Integer.valueOf(split[5]));
        networkPacketDTO.setUdp_dest(Integer.valueOf(split[6]));
        networkPacketDTO.setUdp_len(Integer.valueOf(split[7]));
        networkPacketDTO.setTcp_source(Integer.valueOf(split[8]));
        networkPacketDTO.setTcp_dest(Integer.valueOf(split[9]));
        networkPacketDTO.setTcp_seq(Integer.valueOf(split[10]));
        networkPacketDTO.setTcp_ack_seq(Integer.valueOf(split[11]));
        networkPacketDTO.setTcp_fin(split[12].equals("1"));
        networkPacketDTO.setTcp_syn(split[13].equals("1"));
        networkPacketDTO.setTcp_rst(split[14].equals("1"));
        networkPacketDTO.setTcp_psh(split[15].equals("1"));
        networkPacketDTO.setTcp_ack(split[16].equals("1"));
        networkPacketDTO.setTcp_urg(split[17].equals("1"));
        networkPacketDTO.setTcp_cwr(split[18].equals("1"));
        networkPacketDTO.setIcmp_type(Integer.valueOf(split[19]));
        networkPacketDTO.setIcmp_code(Integer.valueOf(split[20]));
        networkPacketDTO.setPacket_size(Integer.valueOf(split[21]));
        if (networkPacketDTO.getUdp_source() == 0) {
            networkPacketDTO.setSourcePort(networkPacketDTO.getTcp_source());
            networkPacketDTO.setDestinationPort(networkPacketDTO.getTcp_dest());
        } else {
            networkPacketDTO.setSourcePort(networkPacketDTO.getUdp_source());
            networkPacketDTO.setDestinationPort(networkPacketDTO.getUdp_dest());
        }

        return networkPacketDTO;
    }

}
