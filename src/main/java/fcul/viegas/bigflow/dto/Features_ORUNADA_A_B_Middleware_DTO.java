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
public class Features_ORUNADA_A_B_Middleware_DTO {

    private int averageOfPackets;

    private MathUtils averageOfSYN;
    private MathUtils averageOfACK;
    private MathUtils averageOfRST;
    private MathUtils averageOfFIN;
    private MathUtils averageOfCWR;
    private MathUtils averageOfURG;
    private MathUtils averagePacketSize;
    private MathUtils averageTTL;
    private MathUtils averageICMPRedirect;
    private MathUtils averageICMPTimeExceeded;
    private MathUtils averageICMPUnreachable;
    private MathUtils averageICMPOther;
    

    public Features_ORUNADA_A_B_Middleware_DTO() {
        this.averageOfPackets = 0;

        this.averageOfSYN = new MathUtils();
        this.averageOfACK = new MathUtils();
        this.averageOfRST = new MathUtils();
        this.averageOfFIN = new MathUtils();
        this.averageOfCWR = new MathUtils();
        this.averageOfURG = new MathUtils();
        this.averagePacketSize = new MathUtils();
        this.averageTTL = new MathUtils();
        this.averageICMPRedirect = new MathUtils();
        this.averageICMPTimeExceeded = new MathUtils();
        this.averageICMPUnreachable = new MathUtils();
        this.averageICMPOther = new MathUtils();
    }

    public MathUtils getAverageICMPRedirect() {
        return averageICMPRedirect;
    }

    public MathUtils getAverageICMPTimeExceeded() {
        return averageICMPTimeExceeded;
    }

    public MathUtils getAverageICMPUnreachable() {
        return averageICMPUnreachable;
    }

    public MathUtils getAverageICMPOther() {
        return averageICMPOther;
    }

    public MathUtils getAverageTTL() {
        return averageTTL;
    }

    public MathUtils getAveragePacketSize() {
        return averagePacketSize;
    }

    public int getNumberOfPackets() {
        return averageOfPackets;
    }

    public void setNumberOfPackets(int averageOfPackets) {
        this.averageOfPackets = averageOfPackets;
    }

    public MathUtils getNumberOfSYN() {
        return averageOfSYN;
    }

    public void setNumberOfSYN(MathUtils averageOfSYN) {
        this.averageOfSYN = averageOfSYN;
    }

    public MathUtils getNumberOfACK() {
        return averageOfACK;
    }

    public void setNumberOfACK(MathUtils averageOfACK) {
        this.averageOfACK = averageOfACK;
    }

    public MathUtils getNumberOfRST() {
        return averageOfRST;
    }

    public void setNumberOfRST(MathUtils averageOfRST) {
        this.averageOfRST = averageOfRST;
    }

    public MathUtils getNumberOfFIN() {
        return averageOfFIN;
    }

    public void setNumberOfFIN(MathUtils averageOfFIN) {
        this.averageOfFIN = averageOfFIN;
    }

    public MathUtils getNumberOfCWR() {
        return averageOfCWR;
    }

    public void setNumberOfCWR(MathUtils averageOfCWR) {
        this.averageOfCWR = averageOfCWR;
    }

    public MathUtils getNumberOfURG() {
        return averageOfURG;
    }

    public void setNumberOfURG(MathUtils averageOfURG) {
        this.averageOfURG = averageOfURG;
    }

}
