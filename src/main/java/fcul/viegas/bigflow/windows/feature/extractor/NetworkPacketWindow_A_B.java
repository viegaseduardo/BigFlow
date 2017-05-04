/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.dto.Features_A_B;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import fcul.viegas.bigflow.extractors.Features_MOORE_Extractor;
import fcul.viegas.bigflow.extractors.Features_NIGEL_Extractor;
import fcul.viegas.bigflow.extractors.Features_ORUNADA_Extractor;
import fcul.viegas.bigflow.extractors.Features_VIEGAS_Extractor;
import org.apache.flink.api.common.functions.FoldFunction;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindow_A_B implements FoldFunction<NetworkPacketDTO, Features_A_B> {

    private void initializeFeatures(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        featAB.setFirstTime(false);
        featAB.setSourceAddressHash(networkPacket.getSourceIP().hashCode());
        featAB.setDestinationAddressHash(networkPacket.getDestinationIP().hashCode());
        featAB.setSourceAddress(networkPacket.getSourceIP());
        featAB.setDestinationAddress(networkPacket.getDestinationIP());
    }

    private void extract_NIGEL_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        Features_NIGEL_Extractor.extractFeatures(featAB, networkPacket);
    }

    private void extract_ORUNADA_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        Features_ORUNADA_Extractor.extractFeatures_A_B(featAB, networkPacket);
    }

    private void extract_MOORE_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        Features_MOORE_Extractor.extractFeatures(featAB, networkPacket);
    }

    private void extract_VIEGAS_Features(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_Extractor.extractFeatures_A_B(featAB, networkPacket);
    }

    @Override
    public Features_A_B fold(Features_A_B featAB, NetworkPacketDTO networkPacket) throws Exception {
        if (featAB.getFirstTime()) {
            this.initializeFeatures(featAB, networkPacket);
        }

        this.extract_NIGEL_Features(featAB, networkPacket);
        this.extract_ORUNADA_Features(featAB, networkPacket);
        this.extract_MOORE_Features(featAB, networkPacket);

        return featAB;
    }

}
