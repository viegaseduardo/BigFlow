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
public class NetworkPacketWindowAB implements FoldFunction<NetworkPacketDTO, Features_A_B>  {
    
    
    private void extractNigelFeatures(Features_A_B featAB, NetworkPacketDTO networkPacket){
        
    }
    

    @Override
    public Features_A_B fold(Features_A_B featAB, NetworkPacketDTO networkPacket) throws Exception {
        
        return featAB;
    }  
    
}
