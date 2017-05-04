/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A;
import fcul.viegas.bigflow.dto.Features_A_B;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_B_Direction_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_Middleware_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;

/**
 *
 * @author viegas
 */
public class Features_VIEGAS_Extractor {

    public static void extractFeatures_A_B(Features_A_B featAB, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_A_B_Middleware_DTO featuresViegas_A_B = featAB.getFeatures_VIEGAS_A_B_Middleware();

        if (networkPacket.getSourceIP().equals(featAB.getSourceAddress())) {
            Features_VIEGAS_Extractor.extractFeaturesDirection(featuresViegas_A_B.getForward(), networkPacket);
        } else {
            Features_VIEGAS_Extractor.extractFeaturesDirection(featuresViegas_A_B.getBackward(), networkPacket);
        }
        Features_VIEGAS_Extractor.extractBoth(featuresViegas_A_B, networkPacket);
    }

    private static void extractBoth(Features_VIEGAS_A_B_Middleware_DTO featuresViegas_A_B, NetworkPacketDTO networkPacket) {
        //packet count
        featuresViegas_A_B.setCountPackets(featuresViegas_A_B.getCountPackets() + 1);

        //byte count
        featuresViegas_A_B.setCountBytes(featuresViegas_A_B.getCountBytes() + networkPacket.getPacket_size());

        //average network packet size
        featuresViegas_A_B.getAveragePacketSize().addNumber(networkPacket.getPacket_size());

        //average pushed
        featuresViegas_A_B.getPercentagePushed().addNumber((networkPacket.getTcp_psh()) ? 1 : 0);

        //average synfin
        featuresViegas_A_B.getPercentageSynFin().addNumber((networkPacket.getTcp_syn() && networkPacket.getTcp_fin()) ? 1 : 0);

        //average fin
        featuresViegas_A_B.getPercentageFin().addNumber((networkPacket.getTcp_fin()) ? 1 : 0);

        //average syn
        featuresViegas_A_B.getPercentageSyn().addNumber((networkPacket.getTcp_syn()) ? 1 : 0);

        //average ack
        featuresViegas_A_B.getPercentageAck().addNumber((networkPacket.getTcp_ack()) ? 1 : 0);

        //average rst
        featuresViegas_A_B.getPercentageRst().addNumber((networkPacket.getTcp_rst()) ? 1 : 0);

        //average ICMP redirect
        featuresViegas_A_B.getPercentageICMPRedirect().addNumber((networkPacket.getIcmp_type() == 5) ? 1 : 0);

        //average ICMP time Exceeded
        featuresViegas_A_B.getPercentageICMPTimeExceeded().addNumber((networkPacket.getIcmp_type() == 11) ? 1 : 0);

        //average ICMP Unreachable
        featuresViegas_A_B.getPercentageICMPUnreacheable().addNumber((networkPacket.getIcmp_type() == 3) ? 1 : 0);

        //average ICMP others
        if (networkPacket.getIcmp_type() != 5 && networkPacket.getIcmp_type() != 11 && networkPacket.getIcmp_type() != 3) {
            featuresViegas_A_B.getPercentageICMPOtherTypes().addNumber(1);
        } else {
            featuresViegas_A_B.getPercentageICMPOtherTypes().addNumber(0);
        }
    }

    private static void extractFeaturesDirection(Features_VIEGAS_A_B_Direction_Middleware_DTO featureViegas, NetworkPacketDTO networkPacket) {
        //average network packet size
        featureViegas.getAveragePacketSize().addNumber(networkPacket.getPacket_size());

        //average pushed
        featureViegas.getPercentagePushed().addNumber((networkPacket.getTcp_psh()) ? 1 : 0);

        //average synfin
        featureViegas.getPercentageSynFin().addNumber((networkPacket.getTcp_syn() && networkPacket.getTcp_fin()) ? 1 : 0);

        //average fin
        featureViegas.getPercentageFin().addNumber((networkPacket.getTcp_fin()) ? 1 : 0);

        //average syn
        featureViegas.getPercentageSyn().addNumber((networkPacket.getTcp_syn()) ? 1 : 0);

        //average ack
        featureViegas.getPercentageAck().addNumber((networkPacket.getTcp_ack()) ? 1 : 0);

        //average rst
        featureViegas.getPercentageRst().addNumber((networkPacket.getTcp_rst()) ? 1 : 0);

        //average ICMP redirect
        featureViegas.getPercentageICMPRedirect().addNumber((networkPacket.getIcmp_type() == 5) ? 1 : 0);

        //average ICMP time Exceeded
        featureViegas.getPercentageICMPTimeExceeded().addNumber((networkPacket.getIcmp_type() == 11) ? 1 : 0);

        //average ICMP Unreachable
        featureViegas.getPercentageICMPUnreacheable().addNumber((networkPacket.getIcmp_type() == 3) ? 1 : 0);

        //average ICMP others
        if (networkPacket.getIcmp_type() != 5 && networkPacket.getIcmp_type() != 11 && networkPacket.getIcmp_type() != 3) {
            featureViegas.getPercentageICMPOtherTypes().addNumber(1);
        } else {
            featureViegas.getPercentageICMPOtherTypes().addNumber(0);
        }
    }

    public static void extractFeatures_A(Features_A featA, NetworkPacketDTO networkPacket) {
        Features_VIEGAS_A_Middleware_DTO featureViegas = featA.getFeatures_VIEGAS_A_Middleware();

        //add different destination ip addresses
        featureViegas.getUniqueDestinationIPs().add(networkPacket.getDestinationIP().hashCode());
        //add different destination services
        featureViegas.getUniqueDestinationPorts().add((networkPacket.getDestinationIP() + networkPacket.getDestinationPort()).hashCode());

        //average network packet size
        featureViegas.getAveragePacketSize().addNumber(networkPacket.getPacket_size());

        //average pushed
        featureViegas.getPercentagePushed().addNumber((networkPacket.getTcp_psh()) ? 1 : 0);

        //average synfin
        featureViegas.getPercentageSynFin().addNumber((networkPacket.getTcp_syn() && networkPacket.getTcp_fin()) ? 1 : 0);

        //average fin
        featureViegas.getPercentageFin().addNumber((networkPacket.getTcp_fin()) ? 1 : 0);

        //average syn
        featureViegas.getPercentageSyn().addNumber((networkPacket.getTcp_syn()) ? 1 : 0);

        //average ack
        featureViegas.getPercentageAck().addNumber((networkPacket.getTcp_ack()) ? 1 : 0);

        //average rst
        featureViegas.getPercentageRst().addNumber((networkPacket.getTcp_rst()) ? 1 : 0);

        //average ICMP redirect
        featureViegas.getPercentageICMPRedirect().addNumber((networkPacket.getIcmp_type() == 5) ? 1 : 0);

        //average ICMP time Exceeded
        featureViegas.getPercentageICMPTimeExceeded().addNumber((networkPacket.getIcmp_type() == 11) ? 1 : 0);

        //average ICMP Unreachable
        featureViegas.getPercentageICMPUnreacheable().addNumber((networkPacket.getIcmp_type() == 3) ? 1 : 0);

        //average ICMP others
        if (networkPacket.getIcmp_type() != 5 && networkPacket.getIcmp_type() != 11 && networkPacket.getIcmp_type() != 3) {
            featureViegas.getPercentageICMPOtherTypes().addNumber(1);
        } else {
            featureViegas.getPercentageICMPOtherTypes().addNumber(0);
        }
    }

}
