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
public class Features_ORUNADA_DTO {

    //aggregated by IPSRC xOR IPDST
    private Long numberOfPackets;
    private Float percentageOfSYNPackets;
    private Float percentageOfACKPackets;
    private Float percentageOfRSTPackets;
    private Float percentageOfFINPackets;
    private Float percentageOfCWRPackets;
    private Float percentageOfURGPackets;
    private Float averagePacketSize;
    private Float meanTTL;
    private Float percentageICMPRedirect;
    private Float percentageICMPTimeExceeded;
    private Float percentageICMPUnreacheable;
    private Float percentageICMPOtherTypes;

    //aggregated by IPSrc
    private Integer numberOfDifferentDestinations;
    //aggregated by IPSrc
    private Integer numberOfDifferentServices;

    public Long getNumberOfPackets() {
        return numberOfPackets;
    }

    public void setNumberOfPackets(Long numberOfPackets) {
        this.numberOfPackets = numberOfPackets;
    }

    public Float getPercentageOfSYNPackets() {
        return percentageOfSYNPackets;
    }

    public void setPercentageOfSYNPackets(Float percentageOfSYNPackets) {
        this.percentageOfSYNPackets = percentageOfSYNPackets;
    }

    public Float getPercentageOfACKPackets() {
        return percentageOfACKPackets;
    }

    public void setPercentageOfACKPackets(Float percentageOfACKPackets) {
        this.percentageOfACKPackets = percentageOfACKPackets;
    }

    public Float getPercentageOfRSTPackets() {
        return percentageOfRSTPackets;
    }

    public void setPercentageOfRSTPackets(Float percentageOfRSTPackets) {
        this.percentageOfRSTPackets = percentageOfRSTPackets;
    }

    public Float getPercentageOfFINPackets() {
        return percentageOfFINPackets;
    }

    public void setPercentageOfFINPackets(Float percentageOfFINPackets) {
        this.percentageOfFINPackets = percentageOfFINPackets;
    }

    public Float getPercentageOfCWRPackets() {
        return percentageOfCWRPackets;
    }

    public void setPercentageOfCWRPackets(Float percentageOfCWRPackets) {
        this.percentageOfCWRPackets = percentageOfCWRPackets;
    }

    public Float getPercentageOfURGPackets() {
        return percentageOfURGPackets;
    }

    public void setPercentageOfURGPackets(Float percentageOfURGPackets) {
        this.percentageOfURGPackets = percentageOfURGPackets;
    }

    public Float getAveragePacketSize() {
        return averagePacketSize;
    }

    public void setAveragePacketSize(Float averagePacketSize) {
        this.averagePacketSize = averagePacketSize;
    }

    public Float getMeanTTL() {
        return meanTTL;
    }

    public void setMeanTTL(Float meanTTL) {
        this.meanTTL = meanTTL;
    }

    public Float getPercentageICMPRedirect() {
        return percentageICMPRedirect;
    }

    public void setPercentageICMPRedirect(Float percentageICMPRedirect) {
        this.percentageICMPRedirect = percentageICMPRedirect;
    }

    public Float getPercentageICMPTimeExceeded() {
        return percentageICMPTimeExceeded;
    }

    public void setPercentageICMPTimeExceeded(Float percentageICMPTimeExceeded) {
        this.percentageICMPTimeExceeded = percentageICMPTimeExceeded;
    }

    public Float getPercentageICMPUnreacheable() {
        return percentageICMPUnreacheable;
    }

    public void setPercentageICMPUnreacheable(Float percentageICMPUnreacheable) {
        this.percentageICMPUnreacheable = percentageICMPUnreacheable;
    }

    public Float getPercentageICMPOtherTypes() {
        return percentageICMPOtherTypes;
    }

    public void setPercentageICMPOtherTypes(Float percentageICMPOtherTypes) {
        this.percentageICMPOtherTypes = percentageICMPOtherTypes;
    }

    public Integer getNumberOfDifferentDestinations() {
        return numberOfDifferentDestinations;
    }

    public void setNumberOfDifferentDestinations(Integer numberOfDifferentDestinations) {
        this.numberOfDifferentDestinations = numberOfDifferentDestinations;
    }

    public Integer getNumberOfDifferentServices() {
        return numberOfDifferentServices;
    }

    public void setNumberOfDifferentServices(Integer numberOfDifferentServices) {
        this.numberOfDifferentServices = numberOfDifferentServices;
    }

    @Override
    public String toString() {
        String ret = "";

        ret = ret + this.numberOfPackets + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfSYNPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfACKPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfRSTPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfFINPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfCWRPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageOfURGPackets) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.averagePacketSize) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.meanTTL) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageICMPRedirect) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageICMPTimeExceeded) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageICMPUnreacheable) + Definitions.FIELD_DELIM;
        ret = ret + String.format("%.2f", this.percentageICMPOtherTypes) + Definitions.FIELD_DELIM;
        ret = ret + this.numberOfDifferentDestinations + Definitions.FIELD_DELIM;
        ret = ret + this.numberOfDifferentServices;

        return ret;
    }

}
