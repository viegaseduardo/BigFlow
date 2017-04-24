/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.dto.Features_A_B;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import org.apache.flink.api.common.functions.FoldFunction;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindowAB implements FoldFunction<NetworkPacketDTO, Features_A_B> {

    private void initializeFeatures(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        featAB.setFirstTime(false);
        featAB.setSourceAddressHash(networkPacket.getSourceIP().hashCode());
        featAB.setDestinationAddressHash(networkPacket.getDestinationIP().hashCode());
        featAB.setSourceAddress(networkPacket.getSourceIP());
        featAB.setDestinationAddress(networkPacket.getDestinationIP());
    }

    private void extract_NIGEL_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {

    }

    private void extract_ORUNADA_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {

        //number of packets
        featAB.getFeatures_ORUNADA_A_B_Middleware().setNumberOfPackets(featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfPackets() + 1);

        //we extract percentage features by computing the average of flag values (1 if set, 0 otherwise)
        //average SYN
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfSYN().addNumber((networkPacket.getTcp_syn()) ? 1 : 0);
        //average ACK
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfACK().addNumber((networkPacket.getTcp_ack()) ? 1 : 0);
        //average RST
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfRST().addNumber((networkPacket.getTcp_rst()) ? 1 : 0);
        //average FIN
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfFIN().addNumber((networkPacket.getTcp_fin()) ? 1 : 0);
        //need to extract CWR flag
        //featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfCWR().addNumber((networkPacket.get()) ? 1 : 0);
        //average URG
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber((networkPacket.getTcp_urg()) ? 1 : 0);

        //average network packet size
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAveragePacketSize().addNumber(networkPacket.getPacket_size());

        //average TTL
        featAB.getFeatures_ORUNADA_A_B_Middleware().getAverageTTL().addNumber(networkPacket.getTimeToLive());

        //average ICMP redirect
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber((networkPacket.getIcmp_type() == 5) ? 1 : 0);
        
        //average ICMP time Exceeded
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber((networkPacket.getIcmp_type() == 11) ? 1 : 0);
        
        //average ICMP Unreachable
        featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber((networkPacket.getIcmp_type() == 3) ? 1 : 0);
        
        //average ICMP others
        if(networkPacket.getIcmp_type() != 5 && networkPacket.getIcmp_type() != 11 && networkPacket.getIcmp_type() != 3){
            featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber(1);
        }else{
            featAB.getFeatures_ORUNADA_A_B_Middleware().getNumberOfURG().addNumber(0);
        }
        
    }

    @Override
    public Features_A_B fold(Features_A_B featAB, NetworkPacketDTO networkPacket) throws Exception {
        if (featAB.getFirstTime()) {
            this.initializeFeatures(featAB, networkPacket);
        }

        this.extract_NIGEL_Features(featAB, networkPacket);
        this.extract_ORUNADA_Features(featAB, networkPacket);

        return featAB;
    }

}
