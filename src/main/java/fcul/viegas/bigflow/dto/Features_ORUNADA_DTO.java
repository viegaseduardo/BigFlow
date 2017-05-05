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

    public Features_ORUNADA_DTO() {
        
        this.numberOfPackets = 0l;
        this.percentageOfSYNPackets = 0.0f;
        this.percentageOfACKPackets = 0.0f;
        this.percentageOfRSTPackets = 0.0f;
        this.percentageOfFINPackets = 0.0f;
        this.percentageOfCWRPackets = 0.0f;
        this.percentageOfURGPackets = 0.0f;
        this.averagePacketSize = 0.0f;
        this.meanTTL = 0.0f;
        this.percentageICMPRedirect = 0.0f;
        this.percentageICMPTimeExceeded = 0.0f;
        this.percentageICMPUnreacheable = 0.0f;
        this.percentageICMPOtherTypes = 0.0f;

        this.numberOfDifferentDestinations = 0;
        this.numberOfDifferentServices = 0;
    }

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

    

    

}
