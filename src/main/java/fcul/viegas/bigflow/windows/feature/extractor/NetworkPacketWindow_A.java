/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.extractors.Features_ORUNADA_Extractor;
import fcul.viegas.bigflow.extractors.Features_VIEGAS_Extractor;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.FoldFunction;
import org.apache.flink.api.common.functions.RichFoldFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindow_A implements FoldFunction<NetworkPacketDTO, Features_A_DTO> {
    
    private void initializeFeatures(Features_A_DTO featA, NetworkPacketDTO networkPacket) {
        featA.setFirstTime(false);
        featA.setAddressHash(networkPacket.getSourceIP().hashCode());
        featA.setAddress(networkPacket.getSourceIP());
    }
    
    private void extract_ORUNADA_Features(Features_A_DTO featA, NetworkPacketDTO networkPacket) {
        Features_ORUNADA_Extractor.extractFeatures_A(featA, networkPacket);
    }
    
    private void extract_VIEGAS_Features(Features_A_DTO featA, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_Extractor.extractFeatures_A(featA, networkPacket);
    }
    
    @Override
    public Features_A_DTO fold(Features_A_DTO featA, NetworkPacketDTO networkPacket) throws Exception {
        if (featA.getFirstTime()) {
            this.initializeFeatures(featA, networkPacket);
        }
        this.extract_ORUNADA_Features(featA, networkPacket);
        this.extract_VIEGAS_Features(featA, networkPacket);
        
        return featA;
    }
    
}
