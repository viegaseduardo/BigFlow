/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

/**
 *
 * @author viegas
 */
public class Features_Class_DTO {

    private Integer anomalyID;
    private String srcIP;
    private Integer srcPort;
    private String dstIP;
    private Integer dstPort;
    private String taxonomy;
    private Float distance;
    private Integer nbDetectors;
    private String label;

    public Integer getAnomalyID() {
        return anomalyID;
    }

    public void setAnomalyID(Integer anomalyID) {
        this.anomalyID = anomalyID;
    }

    public String getSrcIP() {
        return srcIP;
    }

    public void setSrcIP(String srcIP) {
        this.srcIP = srcIP;
    }

    public Integer getSrcPort() {
        return srcPort;
    }

    public void setSrcPort(Integer srcPort) {
        this.srcPort = srcPort;
    }

    public String getDstIP() {
        return dstIP;
    }

    public void setDstIP(String dstIP) {
        this.dstIP = dstIP;
    }

    public Integer getDstPort() {
        return dstPort;
    }

    public void setDstPort(Integer dstPort) {
        this.dstPort = dstPort;
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

}
