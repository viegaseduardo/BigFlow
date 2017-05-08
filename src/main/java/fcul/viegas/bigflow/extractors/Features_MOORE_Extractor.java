/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_MOORE_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_MOORE_DTO;
import fcul.viegas.bigflow.dto.Features_MOORE_Direction_Middleware_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;
import java.util.Objects;

/**
 *
 * @author viegas
 */
public class Features_MOORE_Extractor {

    public static void extractFeatures(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_MOORE_A_B_Middleware_DTO featuresMoore = featAB.getFeatures_MOORE_A_B_Middleware();
        //check if forward
        if (networkPacket.getSourceIP().equals(featAB.getSourceAddress())) {
            Features_MOORE_Extractor.extractDirection(featuresMoore.getForward(), networkPacket);
        } else {
            Features_MOORE_Extractor.extractDirection(featuresMoore.getBackward(), networkPacket);
        }
        Features_MOORE_Extractor.extractBoth(featuresMoore, networkPacket);
    }

    public static void extractBoth(Features_MOORE_A_B_Middleware_DTO featuresMoore, NetworkPacketDTO networkPacket) {
        //maximum data wire
        if (featuresMoore.getMaximumDataWire() < networkPacket.getPacket_size()) {
            featuresMoore.setMaximumDataWire(networkPacket.getPacket_size().longValue());
        }
        //minimum data wire
        if (networkPacket.getPacket_size() < featuresMoore.getMinimumDataWire()) {
            featuresMoore.setMinimumDataWire(networkPacket.getPacket_size().longValue());
        }
        //data wire
        featuresMoore.getDataWire().addNumber(networkPacket.getPacket_size());

        //inter arrival
        if (featuresMoore.getLastNetworkPacket() != null) {
            Long interArrival = networkPacket.getTimestamp() - featuresMoore.getLastNetworkPacket().getTimestamp();
            if (interArrival >= 0) {
                //minimum inter arrival
                if (interArrival < featuresMoore.getMinimumInterArrivalTime()) {
                    featuresMoore.setMinimumInterArrivalTime(interArrival);
                }
                //maximum
                if (interArrival > featuresMoore.getMaximumInterArrivalTime()) {
                    featuresMoore.setMaximumInterArrivalTime(interArrival);
                }
                //std and deviation inter arrival
                featuresMoore.getInterArrivalTime().addNumber(interArrival.intValue());
            }
        }

        //set last networkpacket
        featuresMoore.setLastNetworkPacket(networkPacket);
    }

    public static void extractDirection(Features_MOORE_Direction_Middleware_DTO featuresMoore, NetworkPacketDTO networkPacket) {
        //maximum data wire
        if (featuresMoore.getMaximumDataWire() < networkPacket.getPacket_size()) {
            featuresMoore.setMaximumDataWire(networkPacket.getPacket_size().longValue());
        }
        //minimum data wire
        if (networkPacket.getPacket_size() < featuresMoore.getMinimumDataWire()) {
            featuresMoore.setMinimumDataWire(networkPacket.getPacket_size().longValue());
        }
        //data wire
        featuresMoore.getDataWire().addNumber(networkPacket.getPacket_size());
        //total packets
        featuresMoore.setTotal_packets(featuresMoore.getTotal_packets() + 1);
        //acks
        if (networkPacket.getTcp_ack()) {
            featuresMoore.setAck_pkts_sent(featuresMoore.getAck_pkts_sent() + 1);
        }
        //pure acks
        if (networkPacket.getPacket_size() == 64
                && !networkPacket.getTcp_syn()
                && !networkPacket.getTcp_fin()
                && !networkPacket.getTcp_rst()
                && networkPacket.getTcp_ack()) {
            featuresMoore.setPure_acks_sent(featuresMoore.getPure_acks_sent() + 1);
        }
        //pushed
        if (networkPacket.getTcp_psh()) {
            featuresMoore.setPushed_pkts_sent(featuresMoore.getPushed_pkts_sent() + 1);
        }
        //syn
        if (networkPacket.getTcp_syn()) {
            featuresMoore.setSyn_pkts_sent(featuresMoore.getSyn_pkts_sent() + 1);
        }
        //fin
        if (networkPacket.getTcp_fin()) {
            featuresMoore.setFin_pkts_sent(featuresMoore.getFin_pkts_sent() + 1);
        }
        //urgent
        if (networkPacket.getTcp_urg()) {
            featuresMoore.setUrgent_pkts_sent(featuresMoore.getUrgent_pkts_sent() + 1);
        }
        //throughput
        if (featuresMoore.getFirst_timestamp() == 0) {
            featuresMoore.setFirst_timestamp(networkPacket.getTimestamp());
        }

        //inter arrival
        if (featuresMoore.getLastNetworkPacket() != null) {
            Long interArrival = networkPacket.getTimestamp() - featuresMoore.getLastNetworkPacket().getTimestamp();
            if (interArrival > 0) {
                //minimum inter arrival
                if (interArrival < featuresMoore.getMinimumInterArrivalTime()) {
                    featuresMoore.setMinimumInterArrivalTime(interArrival);
                }
                //maximum
                if (interArrival > featuresMoore.getMaximumInterArrivalTime()) {
                    featuresMoore.setMaximumInterArrivalTime(interArrival);
                }
                //std and deviation inter arrival
                featuresMoore.getInterArrivalTime().addNumber(interArrival.intValue());
            }
        }

        //set last networkpacket
        featuresMoore.setLastNetworkPacket(networkPacket);
    }

    public static void extractFeatureDTO(Features_MOORE_DTO featMOORE, Features_A_B_DTO featAB, Features_A_DTO featA) {
        Features_MOORE_A_B_Middleware_DTO featMooreAB = featAB.getFeatures_MOORE_A_B_Middleware();

        featMOORE.setMinimumInterArrivalTime((featMooreAB.getMinimumInterArrivalTime()).intValue());
        featMOORE.setQuartileFirstInterArrivalTime(featMooreAB.getInterArrivalTime().getQuartile(0.25));
        featMOORE.setMedianInterArrivalTime(featMooreAB.getInterArrivalTime().getMedian());
        featMOORE.setAvgInterArrivalTime(featMooreAB.getInterArrivalTime().getAverage().intValue());
        featMOORE.setQuartileThirdInterArrivalTime(featMooreAB.getInterArrivalTime().getQuartile(0.75));
        featMOORE.setMaximumInterArrivalTime(featMooreAB.getMaximumInterArrivalTime().intValue());
        featMOORE.setVarianceInterArrivalTime(featMooreAB.getInterArrivalTime().getVariance());

        featMOORE.setMinimumInterArrivalTime_a_b((featMooreAB.getForward().getMinimumInterArrivalTime()).intValue());
        featMOORE.setQuartileFirstInterArrivalTime_a_b(featMooreAB.getForward().getInterArrivalTime().getQuartile(0.25));
        featMOORE.setMedianInterArrivalTime_a_b(featMooreAB.getForward().getInterArrivalTime().getMedian());
        featMOORE.setAvgInterArrivalTime_a_b(featMooreAB.getForward().getInterArrivalTime().getAverage().intValue());
        featMOORE.setQuartileThirdInterArrivalTime_a_b(featMooreAB.getForward().getInterArrivalTime().getQuartile(0.75));
        featMOORE.setMaximumInterArrivalTime_a_b(featMooreAB.getForward().getMaximumInterArrivalTime().intValue());
        featMOORE.setVarianceInterArrivalTime_a_b(featMooreAB.getForward().getInterArrivalTime().getVariance());

        featMOORE.setMinimumInterArrivalTime_b_a((featMooreAB.getBackward().getMinimumInterArrivalTime()).intValue());
        featMOORE.setQuartileFirstInterArrivalTime_b_a(featMooreAB.getBackward().getInterArrivalTime().getQuartile(0.25));
        featMOORE.setMedianInterArrivalTime_b_a(featMooreAB.getBackward().getInterArrivalTime().getMedian());
        featMOORE.setAvgInterArrivalTime_b_a(featMooreAB.getBackward().getInterArrivalTime().getAverage().intValue());
        featMOORE.setQuartileThirdInterArrivalTime_b_a(featMooreAB.getBackward().getInterArrivalTime().getQuartile(0.75));
        featMOORE.setMaximumInterArrivalTime_b_a(featMooreAB.getBackward().getMaximumInterArrivalTime().intValue());
        featMOORE.setVarianceInterArrivalTime_b_a(featMooreAB.getBackward().getInterArrivalTime().getVariance());

        featMOORE.setMinimumDataWire(featMooreAB.getMinimumDataWire().intValue());
        featMOORE.setQuartileFirstDataWire(featMooreAB.getDataWire().getQuartile(0.25));
        featMOORE.setMedianDataWire(featMooreAB.getDataWire().getMedian());
        featMOORE.setAvgDataWire(featMooreAB.getDataWire().getAverage().intValue());
        featMOORE.setQuartileThirdDataWire(featMooreAB.getDataWire().getQuartile(0.75));
        featMOORE.setMaximumDataWire(featMooreAB.getMaximumDataWire().intValue());
        featMOORE.setVarianceDataWire(featMooreAB.getDataWire().getVariance());

        featMOORE.setMinimumDataWire_a_b(featMooreAB.getForward().getMinimumDataWire().intValue());
        featMOORE.setQuartileFirstDataWire_a_b(featMooreAB.getForward().getDataWire().getQuartile(0.25));
        featMOORE.setMedianDataWire_a_b(featMooreAB.getForward().getDataWire().getMedian());
        featMOORE.setAvgDataWire_a_b(featMooreAB.getForward().getDataWire().getAverage().intValue());
        featMOORE.setQuartileThirdDataWire_a_b(featMooreAB.getForward().getDataWire().getQuartile(0.75));
        featMOORE.setMaximumDataWire_a_b(featMooreAB.getForward().getMaximumDataWire().intValue());
        featMOORE.setVarianceDataWire_a_b(featMooreAB.getForward().getDataWire().getVariance());

        featMOORE.setMinimumDataWire_b_a(featMooreAB.getBackward().getMinimumDataWire().intValue());
        featMOORE.setQuartileFirstDataWire_b_a(featMooreAB.getBackward().getDataWire().getQuartile(0.25));
        featMOORE.setMedianDataWire_b_a(featMooreAB.getBackward().getDataWire().getMedian());
        featMOORE.setAvgDataWire_b_a(featMooreAB.getBackward().getDataWire().getAverage().intValue());
        featMOORE.setQuartileThirdDataWire_b_a(featMooreAB.getBackward().getDataWire().getQuartile(0.75));
        featMOORE.setMaximumDataWire_b_a(featMooreAB.getBackward().getMaximumDataWire().intValue());
        featMOORE.setVarianceDataWire_b_a(featMooreAB.getBackward().getDataWire().getVariance());

        featMOORE.setTotal_packets_a_b(featMooreAB.getForward().getTotal_packets().intValue());
        featMOORE.setTotal_packets_b_a(featMooreAB.getBackward().getTotal_packets().intValue());

        featMOORE.setAck_pkts_sent_a_b(featMooreAB.getForward().getAck_pkts_sent().intValue());
        featMOORE.setAck_pkts_sent_b_a(featMooreAB.getBackward().getAck_pkts_sent().intValue());

        featMOORE.setPure_acks_sent_a_b(featMooreAB.getForward().getPure_acks_sent().intValue());
        featMOORE.setPure_acks_sent_b_a(featMooreAB.getBackward().getPure_acks_sent().intValue());

        featMOORE.setPushed_pkts_sent_a_b(featMooreAB.getForward().getPushed_pkts_sent().intValue());
        featMOORE.setPushed_pkts_sent_b_a(featMooreAB.getBackward().getPushed_pkts_sent().intValue());

        featMOORE.setSyn_pkts_sent_a_b(featMooreAB.getForward().getSyn_pkts_sent().intValue());
        featMOORE.setSyn_pkts_sent_b_a(featMooreAB.getBackward().getSyn_pkts_sent().intValue());

        featMOORE.setFin_pkts_sent_a_b(featMooreAB.getForward().getFin_pkts_sent().intValue());
        featMOORE.setFin_pkts_sent_b_a(featMooreAB.getBackward().getFin_pkts_sent().intValue());

        featMOORE.setUrgent_pkts_sent_a_b(featMooreAB.getForward().getUrgent_pkts_sent().intValue());
        featMOORE.setUrgent_pkts_sent_b_a(featMooreAB.getBackward().getUrgent_pkts_sent().intValue());

        if (featMooreAB.getForward().getLastNetworkPacket() == null
                || Objects.equals(featMooreAB.getForward().getFirst_timestamp(), featMooreAB.getForward().getLastNetworkPacket().getTimestamp())) {
            featMOORE.setThroughput_a_b(0);
        } else {
            Long sum = featMooreAB.getForward().getDataWire().getMathUtils().getSum();
            Long time = featMooreAB.getForward().getLastNetworkPacket().getTimestamp() - featMooreAB.getForward().getFirst_timestamp();
            Long throughput = sum / time;
            featMOORE.setThroughput_a_b(throughput.intValue());
        }

        if (featMooreAB.getBackward().getLastNetworkPacket() == null
                || Objects.equals(featMooreAB.getBackward().getFirst_timestamp(), featMooreAB.getBackward().getLastNetworkPacket().getTimestamp())) {
            featMOORE.setThroughput_b_a(0);
        } else {
            Long sum = featMooreAB.getBackward().getDataWire().getMathUtils().getSum();
            Long time = featMooreAB.getBackward().getLastNetworkPacket().getTimestamp() - featMooreAB.getBackward().getFirst_timestamp();
            Long throughput = sum / time;
            featMOORE.setThroughput_b_a(throughput.intValue());
        }

    }

}
