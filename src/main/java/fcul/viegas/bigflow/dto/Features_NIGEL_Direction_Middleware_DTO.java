/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.math.MathUtils;

/**
 *
 * @author viegas
 */
public class Features_NIGEL_Direction_Middleware_DTO {

    private NetworkPacketDTO lastNetworkPacket;

    private Long minimumPacketLength;
    private MathUtils averageAndStdDevPacketLength;
    private Long maximumPacketLength;

    private Long minimumInterArrivalTime;
    private MathUtils averageAndStdDevInterArrivalTime;
    private Long maximumInterArrivalTime;

    private Long numberOfPackets;
    private Long numberOfBytes;

    public Features_NIGEL_Direction_Middleware_DTO() {
        this.lastNetworkPacket = null;

        this.minimumPacketLength = Definitions.NO_VALUE_MINIMUM;
        this.averageAndStdDevPacketLength = new MathUtils();
        this.maximumPacketLength = Definitions.NO_VALUE_MAXIMUM;

        this.minimumInterArrivalTime = Definitions.NO_VALUE_MINIMUM;
        this.averageAndStdDevInterArrivalTime = new MathUtils();
        this.maximumInterArrivalTime = Definitions.NO_VALUE_MAXIMUM;

        this.numberOfPackets = 0l;
        this.numberOfBytes = 0l;
    }

    public NetworkPacketDTO getLastNetworkPacket() {
        return lastNetworkPacket;
    }

    public void setLastNetworkPacket(NetworkPacketDTO lastNetworkPacket) {
        this.lastNetworkPacket = lastNetworkPacket;
    }

    public Long getMinimumPacketLength() {
        return minimumPacketLength;
    }

    public void setMinimumPacketLength(Long minimumPacketLength) {
        this.minimumPacketLength = minimumPacketLength;
    }

    public MathUtils getAverageAndStdDevPacketLength() {
        return averageAndStdDevPacketLength;
    }

    public void setAverageAndStdDevPacketLength(MathUtils averageAndStdDevPacketLength) {
        this.averageAndStdDevPacketLength = averageAndStdDevPacketLength;
    }

    public Long getMaximumPacketLength() {
        return maximumPacketLength;
    }

    public void setMaximumPacketLength(Long maximumPacketLength) {
        this.maximumPacketLength = maximumPacketLength;
    }

    public Long getMinimumInterArrivalTime() {
        return minimumInterArrivalTime;
    }

    public void setMinimumInterArrivalTime(Long minimumInterArrivalTime) {
        this.minimumInterArrivalTime = minimumInterArrivalTime;
    }

    public MathUtils getAverageAndStdDevInterArrivalTime() {
        return averageAndStdDevInterArrivalTime;
    }

    public void setAverageAndStdDevInterArrivalTime(MathUtils averageAndStdDevInterArrivalTime) {
        this.averageAndStdDevInterArrivalTime = averageAndStdDevInterArrivalTime;
    }

    public Long getMaximumInterArrivalTime() {
        return maximumInterArrivalTime;
    }

    public void setMaximumInterArrivalTime(Long maximumInterArrivalTime) {
        this.maximumInterArrivalTime = maximumInterArrivalTime;
    }

    public Long getNumberOfPackets() {
        return numberOfPackets;
    }

    public void setNumberOfPackets(Long numberOfPackets) {
        this.numberOfPackets = numberOfPackets;
    }

    public Long getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(Long numberOfBytes) {
        this.numberOfBytes = numberOfBytes;
    }

}
