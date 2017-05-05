/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.windows.feature.extractor;

import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_DTO;
import fcul.viegas.bigflow.extractors.Features_MOORE_Extractor;
import fcul.viegas.bigflow.extractors.Features_NIGEL_Extractor;
import fcul.viegas.bigflow.extractors.Features_ORUNADA_Extractor;
import fcul.viegas.bigflow.extractors.Features_VIEGAS_Extractor;
import org.apache.flink.api.common.functions.JoinFunction;

/**
 *
 * @author viegas
 */
public class NetworkPacketWindowJoiner implements JoinFunction<Features_A_B_DTO, Features_A_DTO, Features_DTO> {

    @Override
    public Features_DTO join(Features_A_B_DTO featAB, Features_A_DTO featA) throws Exception {
        Features_DTO featDTO = new Features_DTO();

        Features_VIEGAS_Extractor.extractFeatureDTO(featDTO.getFeatureVIEGAS(), featAB, featA);
        Features_ORUNADA_Extractor.extractFeatureDTO(featDTO.getFeatureORUNADA(), featAB, featA);
        Features_MOORE_Extractor.extractFeatureDTO(featDTO.getFeatureMOORE(), featAB, featA);
        Features_NIGEL_Extractor.extractFeatureDTO(featDTO.getFeatureNIGEL(), featAB, featA);

        return featDTO;
    }

}
