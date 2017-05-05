/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.dto.Features_Class_DTO;
import fcul.viegas.bigflow.dto.Features_DTO;
import java.util.ArrayList;

/**
 *
 * @author viegas
 */
public class Features_CLASS_ASSIGNER {

    public static void extractFeatures(ArrayList<Features_Class_DTO> featuresClasses, Features_DTO featDTO) {
        for (Features_Class_DTO anomaly : featuresClasses) {
            //source ip
            if (anomaly.getSrcIP().equals(featDTO.getSourceIP())
                    || anomaly.getSrcIP().equals(Definitions.ANY_IP_ADDRESS)) {
                //source port
                if (anomaly.getSrcPort().equals(featDTO.getSourcePort())
                        || anomaly.getSrcPort().equals(Definitions.ANY_PORT_ADDRESS)) {
                    //destination ip
                    if (anomaly.getDstIP().equals(featDTO.getDestinationIP())
                            || anomaly.getDstIP().equals(Definitions.ANY_IP_ADDRESS)) {
                        //destination port
                        if (anomaly.getDstPort().equals(featDTO.getDestinationPort())
                                || anomaly.getDstPort().equals(Definitions.ANY_PORT_ADDRESS)) {
                            Features_CLASS_ASSIGNER.isAnomaly(featDTO, anomaly);
                            return;
                        }
                    }
                }
            }

        }
        Features_CLASS_ASSIGNER.isNormal(featDTO);
    }

    private static void isNormal(Features_DTO featDTO) {
        featDTO.setTaxonomy(Definitions.FEATURES_CLASS_ASSIGNER_NORMAL_TAXONOMY);
        featDTO.setDistance(Definitions.FEATURES_CLASS_ASSIGNER_NORMAL_DISTANCE);
        featDTO.setNbDetectors(Definitions.FEATURES_CLASS_ASSIGNER_NORMAL_NB_DETECTORS);
        featDTO.setLabel(Definitions.FEATURES_CLASS_ASSIGNER_NORMAL_LABEL);
    }

    private static void isAnomaly(Features_DTO featDTO, Features_Class_DTO anomaly) {
        featDTO.setTaxonomy(anomaly.getTaxonomy());
        featDTO.setDistance(anomaly.getDistance());
        featDTO.setNbDetectors(anomaly.getNbDetectors());
        featDTO.setLabel(anomaly.getLabel());
    }

}
