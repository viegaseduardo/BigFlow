/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A_B;
import fcul.viegas.bigflow.dto.Features_MOORE_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_MOORE_Direction_Middleware_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;

/**
 *
 * @author viegas
 */
public class Features_MOORE_Extractor {

    public static void extractFeatures(Features_A_B featAB, NetworkPacketDTO networkPacket) {
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

        //set last networkpacket
        featuresMoore.setLastNetworkPacket(networkPacket);
    }

}
