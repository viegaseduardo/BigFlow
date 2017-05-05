/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.definitions.Definitions;

/**
 *
 * @author viegas
 */
public class Features_DTO {
    
    private Features_MOORE_DTO featureMOORE;
    private Features_NIGEL_DTO featureNIGEL;
    private Features_ORUNADA_DTO featureORUNADA;
    private Features_VIEGAS_DTO featureVIEGAS;
    
    public Features_DTO(){
        this.featureMOORE = new Features_MOORE_DTO();
        this.featureNIGEL = new Features_NIGEL_DTO();
        this.featureORUNADA = new Features_ORUNADA_DTO();
        this.featureVIEGAS = new Features_VIEGAS_DTO();
    }

    public Features_MOORE_DTO getFeatureMOORE() {
        return featureMOORE;
    }

    public void setFeatureMOORE(Features_MOORE_DTO featureMOORE) {
        this.featureMOORE = featureMOORE;
    }

    public Features_NIGEL_DTO getFeatureNIGEL() {
        return featureNIGEL;
    }

    public void setFeatureNIGEL(Features_NIGEL_DTO featureNIGEL) {
        this.featureNIGEL = featureNIGEL;
    }

    public Features_ORUNADA_DTO getFeatureORUNADA() {
        return featureORUNADA;
    }

    public void setFeatureORUNADA(Features_ORUNADA_DTO featureORUNADA) {
        this.featureORUNADA = featureORUNADA;
    }

    public Features_VIEGAS_DTO getFeatureVIEGAS() {
        return featureVIEGAS;
    }

    public void setFeatureVIEGAS(Features_VIEGAS_DTO featureVIEGAS) {
        this.featureVIEGAS = featureVIEGAS;
    }
    
    public String toString(){
        String ret = "";
        
        ret = ret + this.featureORUNADA.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureNIGEL.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureMOORE.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureVIEGAS.toString() + Definitions.FIELD_DELIM;
        
        return ret;
    }
    
}
