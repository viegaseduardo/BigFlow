/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.definitions;

/**
 *
 * @author viegas
 */
public class Definitions {

    public static final Float PROTOCOL_NONE = 0.0f;
    public static final Float PROTOCOL_TCP = 1.0f;
    public static final Float PROTOCOL_UDP = 2.0f;
    public static final Float PROTOCOL_ICMP = 3.0f;
    public static final Float PROTOCOL_OTHER = 4.0f;

    public static final Long TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A_B = 5000l;
    public static final Long TIME_WINDOW_NETWORK_PACKET_FEATURE_EXTRACTOR_A = 5000l;

    public static final Long NO_VALUE_MAXIMUM = 0l;
    public static final Long NO_VALUE_MINIMUM = 2000l;

    public static final String FIELD_DELIM = ";";

    public static String CLASS_DESCRIPTION_FILE = "";
    
    public static String ANY_IP_ADDRESS = "any";
    public static Integer ANY_PORT_ADDRESS = -1;
    
    public static String FEATURES_CLASS_ASSIGNER_NORMAL_TAXONOMY = "normal_taxonomy";
    public static Float FEATURES_CLASS_ASSIGNER_NORMAL_DISTANCE = 0.0f;
    public static Integer FEATURES_CLASS_ASSIGNER_NORMAL_NB_DETECTORS = 0;
    public static String FEATURES_CLASS_ASSIGNER_NORMAL_LABEL = "normal";
    public static String FEATURES_CLASS_ASSIGNER_ATTACK_LABEL = "attack";

}
