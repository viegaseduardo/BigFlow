/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.parser;

import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import org.apache.flink.api.common.functions.MapFunction;

/**
 *
 * @author viegas
 */
public class NetworkPacketParserMapFunction implements MapFunction<String, NetworkPacketDTO> {

    @Override
    public NetworkPacketDTO map(String t) throws Exception {
        String[] split = t.split(";");
        
        NetworkPacketDTO networkPacketDTO = new NetworkPacketDTO();
        
        networkPacketDTO.setTimestamp(Long.valueOf(split[0]));
        networkPacketDTO.setSourceIP(split[1]);
        networkPacketDTO.setDestinationIP(split[2]);
        networkPacketDTO.setProtocol(split[3]);
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
        networkPacketDTO.setIcmp_type(Integer.valueOf(split[18]));
        networkPacketDTO.setIcmp_code(Integer.valueOf(split[19]));
        networkPacketDTO.setPacket_size(Integer.valueOf(split[20]));
        if(networkPacketDTO.getUdp_source() == 0){
            networkPacketDTO.setSourcePort(networkPacketDTO.getTcp_source());
            networkPacketDTO.setDestinationPort(networkPacketDTO.getTcp_dest());
        }else{
            networkPacketDTO.setSourcePort(networkPacketDTO.getUdp_source());
            networkPacketDTO.setDestinationPort(networkPacketDTO.getUdp_dest());
        }
        
        return networkPacketDTO;
    }

}
