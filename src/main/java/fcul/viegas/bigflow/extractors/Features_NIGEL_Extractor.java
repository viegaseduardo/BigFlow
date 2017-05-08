/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_NIGEL_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_NIGEL_DTO;
import fcul.viegas.bigflow.dto.Features_NIGEL_Direction_Middleware_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;

/**
 *
 * @author viegas
 */
public class Features_NIGEL_Extractor {

    public static void extractFeatures(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
        Features_NIGEL_A_B_Middleware_DTO featuresNigel = featAB.getFeatures_NIGEL_A_B_Middleware();

        //check if forward
        if (networkPacket.getSourceIP().equals(featAB.getSourceAddress())) {
            Features_NIGEL_Extractor.extractDirection(featuresNigel.getForward(), networkPacket);
        } else {
            Features_NIGEL_Extractor.extractDirection(featuresNigel.getBackward(), networkPacket);
        }

        //protocol
        featuresNigel.setProtocol(networkPacket.getProtocol());
    }

    public static void extractDirection(Features_NIGEL_Direction_Middleware_DTO featuresNigel, NetworkPacketDTO networkPacket) {
        //minimum packet length 
        if (networkPacket.getPacket_size() < featuresNigel.getMinimumPacketLength()) {
            featuresNigel.setMinimumPacketLength(networkPacket.getPacket_size().longValue());
        }
        //maximum packet length 
        if (networkPacket.getPacket_size() > featuresNigel.getMaximumPacketLength()) {
            featuresNigel.setMaximumPacketLength(networkPacket.getPacket_size().longValue());
        }
        //average and stdDev packet size
        featuresNigel.getAverageAndStdDevPacketLength().addNumber(networkPacket.getPacket_size());
        
        //inter arrival
        if (featuresNigel.getLastNetworkPacket() != null) {
            Long interArrival = networkPacket.getTimestamp() - featuresNigel.getLastNetworkPacket().getTimestamp();
            if (interArrival >= 0) {
                //minimum inter arrival
                if (interArrival < featuresNigel.getMinimumInterArrivalTime()) {
                    featuresNigel.setMinimumInterArrivalTime(interArrival);
                }
                //maximum
                if (interArrival > featuresNigel.getMaximumInterArrivalTime()) {
                    featuresNigel.setMaximumInterArrivalTime(interArrival);
                }
                //std and deviation inter arrival
                featuresNigel.getAverageAndStdDevInterArrivalTime().addNumber(interArrival.intValue());
            }
        }
        //number of packets
        featuresNigel.setNumberOfPackets(featuresNigel.getNumberOfPackets() + 1);
        //number of bytes
        featuresNigel.setNumberOfBytes(featuresNigel.getNumberOfBytes() + networkPacket.getPacket_size());

        //set last networkpacket
        featuresNigel.setLastNetworkPacket(networkPacket);
    }

    public static void extractFeatureDTO(Features_NIGEL_DTO featNigel, Features_A_B_DTO featAB, Features_A_DTO featA) {
        Features_NIGEL_A_B_Middleware_DTO featNigelAB = featAB.getFeatures_NIGEL_A_B_Middleware();

        featNigel.setMinimumForwardPacketLength(featNigelAB.getForward().getMinimumPacketLength());
        featNigel.setMeanForwardPacketLength(featNigelAB.getForward().getAverageAndStdDevPacketLength().getAverage());
        featNigel.setMaximumForwardPacketLength(featNigelAB.getForward().getMaximumPacketLength());
        featNigel.setStandardDeviationForwardPacketLength((float) featNigelAB.getForward().getAverageAndStdDevPacketLength().getStandardDeviation());

        featNigel.setMinimumBackwardPacketLength(featNigelAB.getBackward().getMinimumPacketLength());
        featNigel.setMeanBackwardPacketLength(featNigelAB.getBackward().getAverageAndStdDevPacketLength().getAverage());
        featNigel.setMaximumBackwardPacketLength(featNigelAB.getBackward().getMaximumPacketLength());
        featNigel.setStandardDeviationBackwardPacketLength((float) featNigelAB.getBackward().getAverageAndStdDevPacketLength().getStandardDeviation());

        featNigel.setMinimumForwardInterArrivalTime(featNigelAB.getForward().getMinimumInterArrivalTime());
        featNigel.setMeanForwardInterArrivalTime(featNigelAB.getForward().getAverageAndStdDevInterArrivalTime().getAverage());
        featNigel.setMaximumForwardInterArrivalTime(featNigelAB.getForward().getMaximumInterArrivalTime());
        featNigel.setStandardDeviationForwardInterArrivalTime((float) featNigelAB.getForward().getAverageAndStdDevInterArrivalTime().getStandardDeviation());

        featNigel.setMinimumBackwardInterArrivalTime(featNigelAB.getBackward().getMinimumInterArrivalTime());
        featNigel.setMeanBackwardInterArrivalTime(featNigelAB.getBackward().getAverageAndStdDevInterArrivalTime().getAverage());
        featNigel.setMaximumBackwardInterArrivalTime(featNigelAB.getBackward().getMaximumInterArrivalTime());
        featNigel.setStandardDeviationBackwardInterArrivalTime((float) featNigelAB.getBackward().getAverageAndStdDevInterArrivalTime().getStandardDeviation());

        featNigel.setProtocol(featNigelAB.getProtocol());

        featNigel.setNumberOfPacketsInForwardDirection(featNigelAB.getForward().getNumberOfPackets());
        featNigel.setNumberOfBytesInForwardDirection(featNigelAB.getForward().getNumberOfBytes());
        featNigel.setNumberOfPacketsInBackwardDirection(featNigelAB.getBackward().getNumberOfPackets());
        featNigel.setNumberOfBytesInBackwardDirection(featNigelAB.getBackward().getNumberOfBytes());
    }

}
