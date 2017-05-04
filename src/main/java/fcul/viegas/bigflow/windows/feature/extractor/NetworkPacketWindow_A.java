/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.dto.Features_A;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.extractors.Features_ORUNADA_Extractor;
import fcul.viegas.bigflow.extractors.Features_VIEGAS_Extractor;
import org.apache.flink.api.common.functions.FoldFunction;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindow_A implements FoldFunction<NetworkPacketDTO, Features_A> {
    
    private void initializeFeatures(Features_A featA, NetworkPacketDTO networkPacket) {
        featA.setFirstTime(false);
        featA.setAddressHash(networkPacket.getSourceIP().hashCode());
        featA.setAddress(networkPacket.getSourceIP());
    }
    
    private void extract_ORUNADA_Features(Features_A featA, NetworkPacketDTO networkPacket) {
        Features_ORUNADA_Extractor.extractFeatures_A(featA, networkPacket);
    }
    
    private void extract_VIEGAS_Features(Features_A featA, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_Extractor.extractFeatures_A(featA, networkPacket);
    }
    
    @Override
    public Features_A fold(Features_A featA, NetworkPacketDTO networkPacket) throws Exception {
        if (featA.getFirstTime()) {
            this.initializeFeatures(featA, networkPacket);
        }
        this.extract_ORUNADA_Features(featA, networkPacket);
        this.extract_VIEGAS_Features(featA, networkPacket);
        
        return featA;
    }
    
}
