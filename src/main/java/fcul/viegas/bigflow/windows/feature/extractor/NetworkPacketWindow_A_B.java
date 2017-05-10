/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.extractors.Features_MOORE_Extractor;
import fcul.viegas.bigflow.extractors.Features_NIGEL_Extractor;
import fcul.viegas.bigflow.extractors.Features_ORUNADA_Extractor;
import fcul.viegas.bigflow.extractors.Features_VIEGAS_Extractor;
import java.util.Objects;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.RichFoldFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindow_A_B extends RichFoldFunction<NetworkPacketDTO, Features_A_B_DTO> {
    
    private IntCounter windowAB = new IntCounter();

    @Override
    public void open(Configuration parameters) {
        getRuntimeContext().addAccumulator(Definitions.DEBUG_COUNTER_FEATURE_A_B, this.windowAB);
    }
    


    private void initializeFeatures(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        this.windowAB.add(1);
        
        featAB.setFirstTime(false);
        //for now we assume that whoever have the first packet in our window is the server
        //  for the MAWI dataset it shouldnt be a problem as according to 
        //      Seven Years and One Day:Sketching the Evolution of Internet Traffic
        //  the network traffic is always client-side or server-side only
        featAB.setSourceAddressHash(networkPacket.getSourceIP().hashCode());
        featAB.setDestinationAddressHash(networkPacket.getDestinationIP().hashCode());
        featAB.setSourceAddress(networkPacket.getSourceIP());
        featAB.setDestinationAddress(networkPacket.getDestinationIP());

        featAB.setDestinationPortAddress(networkPacket.getDestinationPort());
        featAB.setSourcePortAddress(networkPacket.getSourcePort());
        
        


        /*
        //the trick here is that we may get a window in the middle of a flow
        //thereby we assume that the server is always the one with the lowest port 
        //  according to IANA in general the servers are hosted in ports < 1024
        //  if the two servers are behind a NAT, and are for instance both server and client regarding only their IPs
        //      there is not much we can do
        if (networkPacket.getSourcePort() > networkPacket.getDestinationPort() || 
                Objects.equals(networkPacket.getProtocol(), Definitions.PROTOCOL_ICMP)) {
            featAB.setSourceAddressHash(networkPacket.getSourceIP().hashCode());
            featAB.setDestinationAddressHash(networkPacket.getDestinationIP().hashCode());
            featAB.setSourceAddress(networkPacket.getSourceIP());
            featAB.setDestinationAddress(networkPacket.getDestinationIP());
        }else{
            featAB.setDestinationAddressHash(networkPacket.getSourceIP().hashCode());
            featAB.setSourceAddressHash(networkPacket.getDestinationIP().hashCode());
            featAB.setDestinationAddress(networkPacket.getSourceIP());
            featAB.setSourceAddress(networkPacket.getDestinationIP());
        }
         */
    }

    private void extract_NIGEL_Features(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_NIGEL_Extractor.extractFeatures(featAB, networkPacket);
    }

    private void extract_ORUNADA_Features(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_ORUNADA_Extractor.extractFeatures_A_B(featAB, networkPacket);
    }

    private void extract_MOORE_Features(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_MOORE_Extractor.extractFeatures(featAB, networkPacket);
    }

    private void extract_VIEGAS_Features(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_Extractor.extractFeatures_A_B(featAB, networkPacket);
    }

    @Override
    public Features_A_B_DTO fold(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) throws Exception {
        if (featAB.getFirstTime()) {
            this.initializeFeatures(featAB, networkPacket);
        }

        this.extract_NIGEL_Features(featAB, networkPacket);
        this.extract_ORUNADA_Features(featAB, networkPacket);
        this.extract_MOORE_Features(featAB, networkPacket);
        this.extract_VIEGAS_Features(featAB, networkPacket);

        return featAB;
    }

}
