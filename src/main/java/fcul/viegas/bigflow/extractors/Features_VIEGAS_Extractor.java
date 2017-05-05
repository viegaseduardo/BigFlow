/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.extractors;

import fcul.viegas.bigflow.dto.Features_A_DTO;
import fcul.viegas.bigflow.dto.Features_A_B_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_B_Direction_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_B_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_A_Middleware_DTO;
import fcul.viegas.bigflow.dto.Features_VIEGAS_DTO;
import fcul.viegas.bigflow.dto.NetworkPacketDTO;

/**
 *
 * @author viegas
 */
public class Features_VIEGAS_Extractor {

    public static void extractFeatures_A_B(Features_A_B_DTO featAB, NetworkPacketDTO networkPacket) {
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

    public static void extractFeatures_A(Features_A_DTO featA, NetworkPacketDTO networkPacket) {
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

    public static void extractFeatureDTO(Features_VIEGAS_DTO featViegas, Features_A_B_DTO featAB, Features_A_DTO featA) {
        Features_VIEGAS_A_Middleware_DTO featViegasA = featA.getFeatures_VIEGAS_A_Middleware();
        Features_VIEGAS_A_B_Middleware_DTO featViegasAB = featAB.getFeatures_VIEGAS_A_B_Middleware();

        featViegas.setCountPackets(featViegasAB.getCountPackets());
        featViegas.setCountBytes(featViegasAB.getCountBytes());
        featViegas.setAveragePacketSize(featViegasAB.getAveragePacketSize().getAverage());
        featViegas.setPercentagePushed(featViegasAB.getPercentagePushed().getAverage());
        featViegas.setPercentageSynFin(featViegasAB.getPercentageSynFin().getAverage());
        featViegas.setPercentageFin(featViegasAB.getPercentageFin().getAverage());
        featViegas.setPercentageSyn(featViegasAB.getPercentageSyn().getAverage());
        featViegas.setPercentageAck(featViegasAB.getPercentageAck().getAverage());
        featViegas.setPercentageRst(featViegasAB.getPercentageRst().getAverage());
        featViegas.setPercentageICMPRedirect(featViegasAB.getPercentageICMPRedirect().getAverage());
        featViegas.setPercentageICMPTimeExceeded(featViegasAB.getPercentageICMPTimeExceeded().getAverage());
        featViegas.setPercentageICMPUnreacheable(featViegasAB.getPercentageICMPUnreacheable().getAverage());
        featViegas.setPercentageICMPOtherTypes(featViegasAB.getPercentageICMPOtherTypes().getAverage());

        featViegas.setAveragePacketSize_A_B(featViegasAB.getForward().getAveragePacketSize().getAverage());
        featViegas.setPercentagePushed_A_B(featViegasAB.getForward().getPercentagePushed().getAverage());
        featViegas.setPercentageSynFin_A_B(featViegasAB.getForward().getPercentageSynFin().getAverage());
        featViegas.setPercentageFin_A_B(featViegasAB.getForward().getPercentageFin().getAverage());
        featViegas.setPercentageSyn_A_B(featViegasAB.getForward().getPercentageSyn().getAverage());
        featViegas.setPercentageAck_A_B(featViegasAB.getForward().getPercentageAck().getAverage());
        featViegas.setPercentageRst_A_B(featViegasAB.getForward().getPercentageRst().getAverage());
        featViegas.setPercentageICMPRedirect_A_B(featViegasAB.getForward().getPercentageICMPRedirect().getAverage());
        featViegas.setPercentageICMPTimeExceeded_A_B(featViegasAB.getForward().getPercentageICMPTimeExceeded().getAverage());
        featViegas.setPercentageICMPUnreacheable_A_B(featViegasAB.getForward().getPercentageICMPUnreacheable().getAverage());
        featViegas.setPercentageICMPOtherTypes_A_B(featViegasAB.getForward().getPercentageICMPOtherTypes().getAverage());

        featViegas.setAveragePacketSize_B_A(featViegasAB.getBackward().getAveragePacketSize().getAverage());
        featViegas.setPercentagePushed_B_A(featViegasAB.getBackward().getPercentagePushed().getAverage());
        featViegas.setPercentageSynFin_B_A(featViegasAB.getBackward().getPercentageSynFin().getAverage());
        featViegas.setPercentageFin_B_A(featViegasAB.getBackward().getPercentageFin().getAverage());
        featViegas.setPercentageSyn_B_A(featViegasAB.getBackward().getPercentageSyn().getAverage());
        featViegas.setPercentageAck_B_A(featViegasAB.getBackward().getPercentageAck().getAverage());
        featViegas.setPercentageRst_B_A(featViegasAB.getBackward().getPercentageRst().getAverage());
        featViegas.setPercentageICMPRedirect_B_A(featViegasAB.getBackward().getPercentageICMPRedirect().getAverage());
        featViegas.setPercentageICMPTimeExceeded_B_A(featViegasAB.getBackward().getPercentageICMPTimeExceeded().getAverage());
        featViegas.setPercentageICMPUnreacheable_B_A(featViegasAB.getBackward().getPercentageICMPUnreacheable().getAverage());
        featViegas.setPercentageICMPOtherTypes_B_A(featViegasAB.getBackward().getPercentageICMPOtherTypes().getAverage());

        featViegas.setNumberOfDifferentDestinations_A(featViegasA.getUniqueDestinationIPs().size());
        featViegas.setNumberOfDifferentServices_A(featViegasA.getUniqueDestinationPorts().size());
        featViegas.setAveragePacketSize_A(featViegasAB.getBackward().getAveragePacketSize().getAverage());
        featViegas.setPercentagePushed_A(featViegasA.getPercentagePushed().getAverage());
        featViegas.setPercentageSynFin_A(featViegasA.getPercentageSynFin().getAverage());
        featViegas.setPercentageFin_A(featViegasA.getPercentageFin().getAverage());
        featViegas.setPercentageSyn_A(featViegasA.getPercentageSyn().getAverage());
        featViegas.setPercentageAck_A(featViegasA.getPercentageAck().getAverage());
        featViegas.setPercentageRst_A(featViegasA.getPercentageRst().getAverage());
        featViegas.setPercentageICMPRedirect_A(featViegasA.getPercentageICMPRedirect().getAverage());
        featViegas.setPercentageICMPTimeExceeded_A(featViegasA.getPercentageICMPTimeExceeded().getAverage());
        featViegas.setPercentageICMPUnreacheable_A(featViegasA.getPercentageICMPUnreacheable().getAverage());
        featViegas.setPercentageICMPOtherTypes_A(featViegasA.getPercentageICMPOtherTypes().getAverage());
    }

}
