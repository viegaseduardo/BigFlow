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
public class Features_MOORE_Direction_Middleware_DTO {

    private NetworkPacketDTO lastNetworkPacket;

    private Long minimumInterArrivalTime;
    private Long maximumInterArrivalTime;
    private MathUtilsArray interArrivalTime;

    //for us dataWire = packetLength
    private Long minimumDataWire;
    private Long maximumDataWire;
    private MathUtilsArray dataWire;

    private Long total_packets;
    private Long ack_pkts_sent;
    private Long pure_acks_sent;
    private Long pushed_pkts_sent;
    private Long syn_pkts_sent;
    private Long fin_pkts_sent;
    private Long urgent_pkts_sent;
    private Long first_timestamp;
    private Long throughput;

    public Features_MOORE_Direction_Middleware_DTO() {
        minimumInterArrivalTime = Definitions.NO_VALUE_MINIMUM;
        maximumInterArrivalTime = Definitions.NO_VALUE_MAXIMUM;
        interArrivalTime = new MathUtilsArray();
        //for us dataWire = packetLength
        minimumDataWire = Definitions.NO_VALUE_MINIMUM;
        maximumDataWire = Definitions.NO_VALUE_MAXIMUM;
        dataWire = new MathUtilsArray();

        total_packets = 0l;
        ack_pkts_sent = 0l;
        pure_acks_sent = 0l;
        pushed_pkts_sent = 0l;
        syn_pkts_sent = 0l;
        fin_pkts_sent = 0l;
        urgent_pkts_sent = 0l;
        throughput = 0l;
        first_timestamp = 0l;

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

    public Long getTotal_packets() {
        return total_packets;
    }

    public void setTotal_packets(Long total_packets) {
        this.total_packets = total_packets;
    }

    public Long getAck_pkts_sent() {
        return ack_pkts_sent;
    }

    public void setAck_pkts_sent(Long ack_pkts_sent) {
        this.ack_pkts_sent = ack_pkts_sent;
    }

    public Long getPure_acks_sent() {
        return pure_acks_sent;
    }

    public void setPure_acks_sent(Long pure_acks_sent) {
        this.pure_acks_sent = pure_acks_sent;
    }

    public Long getPushed_pkts_sent() {
        return pushed_pkts_sent;
    }

    public void setPushed_pkts_sent(Long pushed_pkts_sent) {
        this.pushed_pkts_sent = pushed_pkts_sent;
    }

    public Long getSyn_pkts_sent() {
        return syn_pkts_sent;
    }

    public void setSyn_pkts_sent(Long syn_pkts_sent) {
        this.syn_pkts_sent = syn_pkts_sent;
    }

    public Long getFin_pkts_sent() {
        return fin_pkts_sent;
    }

    public void setFin_pkts_sent(Long fin_pkts_sent) {
        this.fin_pkts_sent = fin_pkts_sent;
    }

    public Long getUrgent_pkts_sent() {
        return urgent_pkts_sent;
    }

    public void setUrgent_pkts_sent(Long urgent_pkts_sent) {
        this.urgent_pkts_sent = urgent_pkts_sent;
    }

    public Long getFirst_timestamp() {
        return first_timestamp;
    }

    public void setFirst_timestamp(Long first_timestamp) {
        this.first_timestamp = first_timestamp;
    }

    public Long getThroughput() {
        return throughput;
    }

    public void setThroughput(Long throughput) {
        this.throughput = throughput;
    }
}
