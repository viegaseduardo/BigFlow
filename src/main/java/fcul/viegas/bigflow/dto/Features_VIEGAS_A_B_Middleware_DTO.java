/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.math.MathUtils;

/**
 *
 * @author viegas
 */
public class Features_VIEGAS_A_B_Middleware_DTO {
    
    private Integer countPackets;
    private Integer countBytes;
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
    
    private Features_VIEGAS_A_B_Direction_Middleware_DTO forward;
    private Features_VIEGAS_A_B_Direction_Middleware_DTO backward;
    
    public Features_VIEGAS_A_B_Middleware_DTO(){
        this.countBytes = 0;
        this.countPackets = 0;
        
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
        
        this.forward = new Features_VIEGAS_A_B_Direction_Middleware_DTO();
        this.backward = new Features_VIEGAS_A_B_Direction_Middleware_DTO();
    }

    public Integer getCountPackets() {
        return countPackets;
    }

    public void setCountPackets(Integer countPackets) {
        this.countPackets = countPackets;
    }

    public Integer getCountBytes() {
        return countBytes;
    }

    public void setCountBytes(Integer countBytes) {
        this.countBytes = countBytes;
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

    public Features_VIEGAS_A_B_Direction_Middleware_DTO getForward() {
        return forward;
    }

    public void setForward(Features_VIEGAS_A_B_Direction_Middleware_DTO forward) {
        this.forward = forward;
    }

    public Features_VIEGAS_A_B_Direction_Middleware_DTO getBackward() {
        return backward;
    }

    public void setBackward(Features_VIEGAS_A_B_Direction_Middleware_DTO backward) {
        this.backward = backward;
    }
    
    
    
}
