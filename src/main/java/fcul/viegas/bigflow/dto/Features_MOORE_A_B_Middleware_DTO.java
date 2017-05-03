/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.math.MathUtilsArray;

/**
 *
 * @author viegas
 */
public class Features_MOORE_A_B_Middleware_DTO {

    private Features_MOORE_Direction_Middleware_DTO forward;
    private Features_MOORE_Direction_Middleware_DTO backward;

    private Long minimumInterArrivalTime;
    private Long maximumInterArrivalTime;
    private MathUtilsArray interArrivalTime;

    //for us dataWire = packetLength
    private Long minimumDataWire;
    private Long maximumDataWire;
    private MathUtilsArray dataWire;

    private NetworkPacketDTO lastNetworkPacket;

    public Features_MOORE_A_B_Middleware_DTO() {
        this.forward = new Features_MOORE_Direction_Middleware_DTO();
        this.backward = new Features_MOORE_Direction_Middleware_DTO();

        minimumInterArrivalTime = Definitions.NO_VALUE_MINIMUM;
        maximumInterArrivalTime = Definitions.NO_VALUE_MAXIMUM;
        interArrivalTime = new MathUtilsArray();
        //for us dataWire = packetLength
        minimumDataWire = Definitions.NO_VALUE_MINIMUM;
        maximumDataWire = Definitions.NO_VALUE_MAXIMUM;
        dataWire = new MathUtilsArray();

        this.lastNetworkPacket = null;
    }

    public NetworkPacketDTO getLastNetworkPacket() {
        return lastNetworkPacket;
    }

    public void setLastNetworkPacket(NetworkPacketDTO lastNetworkPacket) {
        this.lastNetworkPacket = lastNetworkPacket;
    }

    public Long getMinimumInterArrivalTime() {
        return minimumInterArrivalTime;
    }

    public void setMinimumInterArrivalTime(Long minimumInterArrivalTime) {
        this.minimumInterArrivalTime = minimumInterArrivalTime;
    }

    public Long getMaximumInterArrivalTime() {
        return maximumInterArrivalTime;
    }

    public void setMaximumInterArrivalTime(Long maximumInterArrivalTime) {
        this.maximumInterArrivalTime = maximumInterArrivalTime;
    }

    public MathUtilsArray getInterArrivalTime() {
        return interArrivalTime;
    }

    public void setInterArrivalTime(MathUtilsArray interArrivalTime) {
        this.interArrivalTime = interArrivalTime;
    }

    public Long getMinimumDataWire() {
        return minimumDataWire;
    }

    public void setMinimumDataWire(Long minimumDataWire) {
        this.minimumDataWire = minimumDataWire;
    }

    public Long getMaximumDataWire() {
        return maximumDataWire;
    }

    public void setMaximumDataWire(Long maximumDataWire) {
        this.maximumDataWire = maximumDataWire;
    }

    public MathUtilsArray getDataWire() {
        return dataWire;
    }

    public void setDataWire(MathUtilsArray dataWire) {
        this.dataWire = dataWire;
    }

    public Features_MOORE_Direction_Middleware_DTO getForward() {
        return forward;
    }

    public void setForward(Features_MOORE_Direction_Middleware_DTO forward) {
        this.forward = forward;
    }

    public Features_MOORE_Direction_Middleware_DTO getBackward() {
        return backward;
    }

    public void setBackward(Features_MOORE_Direction_Middleware_DTO backward) {
        this.backward = backward;
    }

}
