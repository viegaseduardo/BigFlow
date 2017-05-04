/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.math.MathUtils;
import java.util.HashSet;

/**
 *
 * @author viegas
 */
public class Features_VIEGAS_A_Middleware_DTO {

    private HashSet<Integer> uniqueDestinationIPs;
    private HashSet<Integer> uniqueDestinationPorts;

    private MathUtils averagePacketSize;
    private MathUtils percentagePushed;
    private MathUtils percentageSynFin;
    private MathUtils percentageFin;
    private MathUtils percentageSyn;
    private MathUtils percentageAck;
    private MathUtils percentageRst;
    private MathUtils percentageICMPRedirect;
    private MathUtils percentageICMPTimeExceeded;
    private MathUtils percentageICMPUnreacheable;
    private MathUtils percentageICMPOtherTypes;
    
    public Features_VIEGAS_A_Middleware_DTO(){
        this.uniqueDestinationIPs = new HashSet<>();
        this.uniqueDestinationPorts = new HashSet<>();
        
        this.averagePacketSize = new MathUtils();
        this.percentagePushed = new MathUtils();
        this.percentageSynFin = new MathUtils();
        this.percentageFin = new MathUtils();
        this.percentageSyn = new MathUtils();
        this.percentageAck = new MathUtils();
        this.percentageRst = new MathUtils();
        this.percentageICMPRedirect = new MathUtils();
        this.percentageICMPTimeExceeded = new MathUtils();
        this.percentageICMPUnreacheable = new MathUtils();
        this.percentageICMPOtherTypes = new MathUtils();   
    }

    public HashSet<Integer> getUniqueDestinationIPs() {
        return uniqueDestinationIPs;
    }

    public void setUniqueDestinationIPs(HashSet<Integer> uniqueDestinationIPs) {
        this.uniqueDestinationIPs = uniqueDestinationIPs;
    }

    public HashSet<Integer> getUniqueDestinationPorts() {
        return uniqueDestinationPorts;
    }

    public void setUniqueDestinationPorts(HashSet<Integer> uniqueDestinationPorts) {
        this.uniqueDestinationPorts = uniqueDestinationPorts;
    }

    public MathUtils getAveragePacketSize() {
        return averagePacketSize;
    }

    public void setAveragePacketSize(MathUtils averagePacketSize) {
        this.averagePacketSize = averagePacketSize;
    }

    public MathUtils getPercentagePushed() {
        return percentagePushed;
    }

    public void setPercentagePushed(MathUtils percentagePushed) {
        this.percentagePushed = percentagePushed;
    }

    public MathUtils getPercentageSynFin() {
        return percentageSynFin;
    }

    public void setPercentageSynFin(MathUtils percentageSynFin) {
        this.percentageSynFin = percentageSynFin;
    }

    public MathUtils getPercentageFin() {
        return percentageFin;
    }

    public void setPercentageFin(MathUtils percentageFin) {
        this.percentageFin = percentageFin;
    }

    public MathUtils getPercentageSyn() {
        return percentageSyn;
    }

    public void setPercentageSyn(MathUtils percentageSyn) {
        this.percentageSyn = percentageSyn;
    }

    public MathUtils getPercentageAck() {
        return percentageAck;
    }

    public void setPercentageAck(MathUtils percentageAck) {
        this.percentageAck = percentageAck;
    }

    public MathUtils getPercentageRst() {
        return percentageRst;
    }

    public void setPercentageRst(MathUtils percentageRst) {
        this.percentageRst = percentageRst;
    }

    public MathUtils getPercentageICMPRedirect() {
        return percentageICMPRedirect;
    }

    public void setPercentageICMPRedirect(MathUtils percentageICMPRedirect) {
        this.percentageICMPRedirect = percentageICMPRedirect;
    }

    public MathUtils getPercentageICMPTimeExceeded() {
        return percentageICMPTimeExceeded;
    }

    public void setPercentageICMPTimeExceeded(MathUtils percentageICMPTimeExceeded) {
        this.percentageICMPTimeExceeded = percentageICMPTimeExceeded;
    }

    public MathUtils getPercentageICMPUnreacheable() {
        return percentageICMPUnreacheable;
    }

    public void setPercentageICMPUnreacheable(MathUtils percentageICMPUnreacheable) {
        this.percentageICMPUnreacheable = percentageICMPUnreacheable;
    }

    public MathUtils getPercentageICMPOtherTypes() {
        return percentageICMPOtherTypes;
    }

    public void setPercentageICMPOtherTypes(MathUtils percentageICMPOtherTypes) {
        this.percentageICMPOtherTypes = percentageICMPOtherTypes;
    }

}
