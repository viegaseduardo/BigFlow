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
public class Features_MOORE_DTO {

    private Integer minimumInterArrivalTime;
    private Integer quartileFirstInterArrivalTime;
    private Integer medianInterArrivalTime;
    private Integer avgInterArrivalTime;
    private Integer quartileThirdInterArrivalTime;
    private Integer maximumInterArrivalTime;
    private Double varianceInterArrivalTime;

    private Integer minimumInterArrivalTime_a_b;
    private Integer quartileFirstInterArrivalTime_a_b;
    private Integer medianInterArrivalTime_a_b;
    private Integer avgInterArrivalTime_a_b;
    private Integer quartileThirdInterArrivalTime_a_b;
    private Integer maximumInterArrivalTime_a_b;
    private Double varianceInterArrivalTime_a_b;

    private Integer minimumInterArrivalTime_b_a;
    private Integer quartileFirstInterArrivalTime_b_a;
    private Integer medianInterArrivalTime_b_a;
    private Integer avgInterArrivalTime_b_a;
    private Integer quartileThirdInterArrivalTime_b_a;
    private Integer maximumInterArrivalTime_b_a;
    private Double varianceInterArrivalTime_b_a;

    //for us dataWire = packetLength
    private Integer minimumDataWire;
    private Integer quartileFirstDataWire;
    private Integer medianDataWire;
    private Integer avgDataWire;
    private Integer quartileThirdDataWire;
    private Integer maximumDataWire;
    private Double varianceDataWire;

    private Integer minimumDataWire_a_b;
    private Integer quartileFirstDataWire_a_b;
    private Integer medianDataWire_a_b;
    private Integer avgDataWire_a_b;
    private Integer quartileThirdDataWire_a_b;
    private Integer maximumDataWire_a_b;
    private Double varianceDataWire_a_b;

    private Integer minimumDataWire_b_a;
    private Integer quartileFirstDataWire_b_a;
    private Integer medianDataWire_b_a;
    private Integer avgDataWire_b_a;
    private Integer quartileThirdDataWire_b_a;
    private Integer maximumDataWire_b_a;
    private Double varianceDataWire_b_a;

    private Integer total_packets_a_b;
    private Integer total_packets_b_a;

    private Integer ack_pkts_sent_a_b;
    private Integer ack_pkts_sent_b_a;

    private Integer pure_acks_sent_a_b;
    private Integer pure_acks_sent_b_a;

    private Integer pushed_pkts_sent_a_b;
    private Integer pushed_pkts_sent_b_a;

    private Integer syn_pkts_sent_a_b;
    private Integer syn_pkts_sent_b_a;

    private Integer fin_pkts_sent_a_b;
    private Integer fin_pkts_sent_b_a;

    private Integer urgent_pkts_sent_a_b;
    private Integer urgent_pkts_sent_b_a;

    private Integer throughput_a_b;
    private Integer throughput_b_a;

    public Integer getMinimumInterArrivalTime() {
        return minimumInterArrivalTime;
    }

    public void setMinimumInterArrivalTime(Integer minimumInterArrivalTime) {
        this.minimumInterArrivalTime = minimumInterArrivalTime;
    }

    public Integer getQuartileFirstInterArrivalTime() {
        return quartileFirstInterArrivalTime;
    }

    public void setQuartileFirstInterArrivalTime(Integer quartileFirstInterArrivalTime) {
        this.quartileFirstInterArrivalTime = quartileFirstInterArrivalTime;
    }

    public Integer getMedianInterArrivalTime() {
        return medianInterArrivalTime;
    }

    public void setMedianInterArrivalTime(Integer medianInterArrivalTime) {
        this.medianInterArrivalTime = medianInterArrivalTime;
    }

    public Integer getAvgInterArrivalTime() {
        return avgInterArrivalTime;
    }

    public void setAvgInterArrivalTime(Integer avgInterArrivalTime) {
        this.avgInterArrivalTime = avgInterArrivalTime;
    }

    public Integer getQuartileThirdInterArrivalTime() {
        return quartileThirdInterArrivalTime;
    }

    public void setQuartileThirdInterArrivalTime(Integer quartileThirdInterArrivalTime) {
        this.quartileThirdInterArrivalTime = quartileThirdInterArrivalTime;
    }

    public Integer getMaximumInterArrivalTime() {
        return maximumInterArrivalTime;
    }

    public void setMaximumInterArrivalTime(Integer maximumInterArrivalTime) {
        this.maximumInterArrivalTime = maximumInterArrivalTime;
    }

    public Double getVarianceInterArrivalTime() {
        return varianceInterArrivalTime;
    }

    public void setVarianceInterArrivalTime(Double varianceInterArrivalTime) {
        this.varianceInterArrivalTime = varianceInterArrivalTime;
    }

    public Integer getMinimumInterArrivalTime_a_b() {
        return minimumInterArrivalTime_a_b;
    }

    public void setMinimumInterArrivalTime_a_b(Integer minimumInterArrivalTime_a_b) {
        this.minimumInterArrivalTime_a_b = minimumInterArrivalTime_a_b;
    }

    public Integer getQuartileFirstInterArrivalTime_a_b() {
        return quartileFirstInterArrivalTime_a_b;
    }

    public void setQuartileFirstInterArrivalTime_a_b(Integer quartileFirstInterArrivalTime_a_b) {
        this.quartileFirstInterArrivalTime_a_b = quartileFirstInterArrivalTime_a_b;
    }

    public Integer getMedianInterArrivalTime_a_b() {
        return medianInterArrivalTime_a_b;
    }

    public void setMedianInterArrivalTime_a_b(Integer medianInterArrivalTime_a_b) {
        this.medianInterArrivalTime_a_b = medianInterArrivalTime_a_b;
    }

    public Integer getAvgInterArrivalTime_a_b() {
        return avgInterArrivalTime_a_b;
    }

    public void setAvgInterArrivalTime_a_b(Integer avgInterArrivalTime_a_b) {
        this.avgInterArrivalTime_a_b = avgInterArrivalTime_a_b;
    }

    public Integer getQuartileThirdInterArrivalTime_a_b() {
        return quartileThirdInterArrivalTime_a_b;
    }

    public void setQuartileThirdInterArrivalTime_a_b(Integer quartileThirdInterArrivalTime_a_b) {
        this.quartileThirdInterArrivalTime_a_b = quartileThirdInterArrivalTime_a_b;
    }

    public Integer getMaximumInterArrivalTime_a_b() {
        return maximumInterArrivalTime_a_b;
    }

    public void setMaximumInterArrivalTime_a_b(Integer maximumInterArrivalTime_a_b) {
        this.maximumInterArrivalTime_a_b = maximumInterArrivalTime_a_b;
    }

    public Double getVarianceInterArrivalTime_a_b() {
        return varianceInterArrivalTime_a_b;
    }

    public void setVarianceInterArrivalTime_a_b(Double varianceInterArrivalTime_a_b) {
        this.varianceInterArrivalTime_a_b = varianceInterArrivalTime_a_b;
    }

    public Integer getMinimumInterArrivalTime_b_a() {
        return minimumInterArrivalTime_b_a;
    }

    public void setMinimumInterArrivalTime_b_a(Integer minimumInterArrivalTime_b_a) {
        this.minimumInterArrivalTime_b_a = minimumInterArrivalTime_b_a;
    }

    public Integer getQuartileFirstInterArrivalTime_b_a() {
        return quartileFirstInterArrivalTime_b_a;
    }

    public void setQuartileFirstInterArrivalTime_b_a(Integer quartileFirstInterArrivalTime_b_a) {
        this.quartileFirstInterArrivalTime_b_a = quartileFirstInterArrivalTime_b_a;
    }

    public Integer getMedianInterArrivalTime_b_a() {
        return medianInterArrivalTime_b_a;
    }

    public void setMedianInterArrivalTime_b_a(Integer medianInterArrivalTime_b_a) {
        this.medianInterArrivalTime_b_a = medianInterArrivalTime_b_a;
    }

    public Integer getAvgInterArrivalTime_b_a() {
        return avgInterArrivalTime_b_a;
    }

    public void setAvgInterArrivalTime_b_a(Integer avgInterArrivalTime_b_a) {
        this.avgInterArrivalTime_b_a = avgInterArrivalTime_b_a;
    }

    public Integer getQuartileThirdInterArrivalTime_b_a() {
        return quartileThirdInterArrivalTime_b_a;
    }

    public void setQuartileThirdInterArrivalTime_b_a(Integer quartileThirdInterArrivalTime_b_a) {
        this.quartileThirdInterArrivalTime_b_a = quartileThirdInterArrivalTime_b_a;
    }

    public Integer getMaximumInterArrivalTime_b_a() {
        return maximumInterArrivalTime_b_a;
    }

    public void setMaximumInterArrivalTime_b_a(Integer maximumInterArrivalTime_b_a) {
        this.maximumInterArrivalTime_b_a = maximumInterArrivalTime_b_a;
    }

    public Double getVarianceInterArrivalTime_b_a() {
        return varianceInterArrivalTime_b_a;
    }

    public void setVarianceInterArrivalTime_b_a(Double varianceInterArrivalTime_b_a) {
        this.varianceInterArrivalTime_b_a = varianceInterArrivalTime_b_a;
    }

    public Integer getMinimumDataWire() {
        return minimumDataWire;
    }

    public void setMinimumDataWire(Integer minimumDataWire) {
        this.minimumDataWire = minimumDataWire;
    }

    public Integer getQuartileFirstDataWire() {
        return quartileFirstDataWire;
    }

    public void setQuartileFirstDataWire(Integer quartileFirstDataWire) {
        this.quartileFirstDataWire = quartileFirstDataWire;
    }

    public Integer getMedianDataWire() {
        return medianDataWire;
    }

    public void setMedianDataWire(Integer medianDataWire) {
        this.medianDataWire = medianDataWire;
    }

    public Integer getAvgDataWire() {
        return avgDataWire;
    }

    public void setAvgDataWire(Integer avgDataWire) {
        this.avgDataWire = avgDataWire;
    }

    public Integer getQuartileThirdDataWire() {
        return quartileThirdDataWire;
    }

    public void setQuartileThirdDataWire(Integer quartileThirdDataWire) {
        this.quartileThirdDataWire = quartileThirdDataWire;
    }

    public Integer getMaximumDataWire() {
        return maximumDataWire;
    }

    public void setMaximumDataWire(Integer maximumDataWire) {
        this.maximumDataWire = maximumDataWire;
    }

    public Double getVarianceDataWire() {
        return varianceDataWire;
    }

    public void setVarianceDataWire(Double varianceDataWire) {
        this.varianceDataWire = varianceDataWire;
    }

    public Integer getMinimumDataWire_a_b() {
        return minimumDataWire_a_b;
    }

    public void setMinimumDataWire_a_b(Integer minimumDataWire_a_b) {
        this.minimumDataWire_a_b = minimumDataWire_a_b;
    }

    public Integer getQuartileFirstDataWire_a_b() {
        return quartileFirstDataWire_a_b;
    }

    public void setQuartileFirstDataWire_a_b(Integer quartileFirstDataWire_a_b) {
        this.quartileFirstDataWire_a_b = quartileFirstDataWire_a_b;
    }

    public Integer getMedianDataWire_a_b() {
        return medianDataWire_a_b;
    }

    public void setMedianDataWire_a_b(Integer medianDataWire_a_b) {
        this.medianDataWire_a_b = medianDataWire_a_b;
    }

    public Integer getAvgDataWire_a_b() {
        return avgDataWire_a_b;
    }

    public void setAvgDataWire_a_b(Integer avgDataWire_a_b) {
        this.avgDataWire_a_b = avgDataWire_a_b;
    }

    public Integer getQuartileThirdDataWire_a_b() {
        return quartileThirdDataWire_a_b;
    }

    public void setQuartileThirdDataWire_a_b(Integer quartileThirdDataWire_a_b) {
        this.quartileThirdDataWire_a_b = quartileThirdDataWire_a_b;
    }

    public Integer getMaximumDataWire_a_b() {
        return maximumDataWire_a_b;
    }

    public void setMaximumDataWire_a_b(Integer maximumDataWire_a_b) {
        this.maximumDataWire_a_b = maximumDataWire_a_b;
    }

    public Double getVarianceDataWire_a_b() {
        return varianceDataWire_a_b;
    }

    public void setVarianceDataWire_a_b(Double varianceDataWire_a_b) {
        this.varianceDataWire_a_b = varianceDataWire_a_b;
    }

    public Integer getMinimumDataWire_b_a() {
        return minimumDataWire_b_a;
    }

    public void setMinimumDataWire_b_a(Integer minimumDataWire_b_a) {
        this.minimumDataWire_b_a = minimumDataWire_b_a;
    }

    public Integer getQuartileFirstDataWire_b_a() {
        return quartileFirstDataWire_b_a;
    }

    public void setQuartileFirstDataWire_b_a(Integer quartileFirstDataWire_b_a) {
        this.quartileFirstDataWire_b_a = quartileFirstDataWire_b_a;
    }

    public Integer getMedianDataWire_b_a() {
        return medianDataWire_b_a;
    }

    public void setMedianDataWire_b_a(Integer medianDataWire_b_a) {
        this.medianDataWire_b_a = medianDataWire_b_a;
    }

    public Integer getAvgDataWire_b_a() {
        return avgDataWire_b_a;
    }

    public void setAvgDataWire_b_a(Integer avgDataWire_b_a) {
        this.avgDataWire_b_a = avgDataWire_b_a;
    }

    public Integer getQuartileThirdDataWire_b_a() {
        return quartileThirdDataWire_b_a;
    }

    public void setQuartileThirdDataWire_b_a(Integer quartileThirdDataWire_b_a) {
        this.quartileThirdDataWire_b_a = quartileThirdDataWire_b_a;
    }

    public Integer getMaximumDataWire_b_a() {
        return maximumDataWire_b_a;
    }

    public void setMaximumDataWire_b_a(Integer maximumDataWire_b_a) {
        this.maximumDataWire_b_a = maximumDataWire_b_a;
    }

    public Double getVarianceDataWire_b_a() {
        return varianceDataWire_b_a;
    }

    public void setVarianceDataWire_b_a(Double varianceDataWire_b_a) {
        this.varianceDataWire_b_a = varianceDataWire_b_a;
    }

    public Integer getTotal_packets_a_b() {
        return total_packets_a_b;
    }

    public void setTotal_packets_a_b(Integer total_packets_a_b) {
        this.total_packets_a_b = total_packets_a_b;
    }

    public Integer getTotal_packets_b_a() {
        return total_packets_b_a;
    }

    public void setTotal_packets_b_a(Integer total_packets_b_a) {
        this.total_packets_b_a = total_packets_b_a;
    }

    public Integer getAck_pkts_sent_a_b() {
        return ack_pkts_sent_a_b;
    }

    public void setAck_pkts_sent_a_b(Integer ack_pkts_sent_a_b) {
        this.ack_pkts_sent_a_b = ack_pkts_sent_a_b;
    }

    public Integer getAck_pkts_sent_b_a() {
        return ack_pkts_sent_b_a;
    }

    public void setAck_pkts_sent_b_a(Integer ack_pkts_sent_b_a) {
        this.ack_pkts_sent_b_a = ack_pkts_sent_b_a;
    }

    public Integer getPure_acks_sent_a_b() {
        return pure_acks_sent_a_b;
    }

    public void setPure_acks_sent_a_b(Integer pure_acks_sent_a_b) {
        this.pure_acks_sent_a_b = pure_acks_sent_a_b;
    }

    public Integer getPure_acks_sent_b_a() {
        return pure_acks_sent_b_a;
    }

    public void setPure_acks_sent_b_a(Integer pure_acks_sent_b_a) {
        this.pure_acks_sent_b_a = pure_acks_sent_b_a;
    }

    public Integer getPushed_pkts_sent_a_b() {
        return pushed_pkts_sent_a_b;
    }

    public void setPushed_pkts_sent_a_b(Integer pushed_pkts_sent_a_b) {
        this.pushed_pkts_sent_a_b = pushed_pkts_sent_a_b;
    }

    public Integer getPushed_pkts_sent_b_a() {
        return pushed_pkts_sent_b_a;
    }

    public void setPushed_pkts_sent_b_a(Integer pushed_pkts_sent_b_a) {
        this.pushed_pkts_sent_b_a = pushed_pkts_sent_b_a;
    }

    public Integer getSyn_pkts_sent_a_b() {
        return syn_pkts_sent_a_b;
    }

    public void setSyn_pkts_sent_a_b(Integer syn_pkts_sent_a_b) {
        this.syn_pkts_sent_a_b = syn_pkts_sent_a_b;
    }

    public Integer getSyn_pkts_sent_b_a() {
        return syn_pkts_sent_b_a;
    }

    public void setSyn_pkts_sent_b_a(Integer syn_pkts_sent_b_a) {
        this.syn_pkts_sent_b_a = syn_pkts_sent_b_a;
    }

    public Integer getFin_pkts_sent_a_b() {
        return fin_pkts_sent_a_b;
    }

    public void setFin_pkts_sent_a_b(Integer fin_pkts_sent_a_b) {
        this.fin_pkts_sent_a_b = fin_pkts_sent_a_b;
    }

    public Integer getFin_pkts_sent_b_a() {
        return fin_pkts_sent_b_a;
    }

    public void setFin_pkts_sent_b_a(Integer fin_pkts_sent_b_a) {
        this.fin_pkts_sent_b_a = fin_pkts_sent_b_a;
    }

    public Integer getUrgent_pkts_sent_a_b() {
        return urgent_pkts_sent_a_b;
    }

    public void setUrgent_pkts_sent_a_b(Integer urgent_pkts_sent_a_b) {
        this.urgent_pkts_sent_a_b = urgent_pkts_sent_a_b;
    }

    public Integer getUrgent_pkts_sent_b_a() {
        return urgent_pkts_sent_b_a;
    }

    public void setUrgent_pkts_sent_b_a(Integer urgent_pkts_sent_b_a) {
        this.urgent_pkts_sent_b_a = urgent_pkts_sent_b_a;
    }

    public Integer getThroughput_a_b() {
        return throughput_a_b;
    }

    public void setThroughput_a_b(Integer throughput_a_b) {
        this.throughput_a_b = throughput_a_b;
    }

    public Integer getThroughput_b_a() {
        return throughput_b_a;
    }

    public void setThroughput_b_a(Integer throughput_b_a) {
        this.throughput_b_a = throughput_b_a;
    }

}
