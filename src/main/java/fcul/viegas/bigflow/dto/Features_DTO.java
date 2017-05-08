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

    private String sourceIP;
    private String destinationIP;
    private Integer sourcePort;
    private Integer destinationPort;

    private String taxonomy;
    private Float distance;
    private Integer nbDetectors;
    private String label;
    private String labelARFF;

    private Features_MOORE_DTO featureMOORE;
    private Features_NIGEL_DTO featureNIGEL;
    private Features_ORUNADA_DTO featureORUNADA;
    private Features_VIEGAS_DTO featureVIEGAS;

    public Features_DTO() {
        this.featureMOORE = new Features_MOORE_DTO();
        this.featureNIGEL = new Features_NIGEL_DTO();
        this.featureORUNADA = new Features_ORUNADA_DTO();
        this.featureVIEGAS = new Features_VIEGAS_DTO();
    }

    public String getLabelARFF() {
        return labelARFF;
    }

    public void setLabelARFF(String labelARFF) {
        this.labelARFF = labelARFF;
    }

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Integer getNbDetectors() {
        return nbDetectors;
    }

    public void setNbDetectors(Integer nbDetectors) {
        this.nbDetectors = nbDetectors;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(Integer sourcePort) {
        this.sourcePort = sourcePort;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(Integer destinationPort) {
        this.destinationPort = destinationPort;
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

    public String toString() {
        String ret = "";

        ret = ret + this.featureORUNADA.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureNIGEL.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureMOORE.toString() + Definitions.FIELD_DELIM;
        ret = ret + this.featureVIEGAS.toString() + Definitions.FIELD_DELIM;

        ret = ret + this.taxonomy + Definitions.FIELD_DELIM;
        ret = ret + this.distance + Definitions.FIELD_DELIM;
        ret = ret + this.nbDetectors + Definitions.FIELD_DELIM;
        ret = ret + this.label + Definitions.FIELD_DELIM;
        ret = ret + this.labelARFF;

        return ret;
    }

}
