/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.topologies.Topologies_ARFF_CREATOR;

/*
 * @author viegas
 */
public class Main {

    public static void main(String[] args) throws Exception {

        String param1 = "/home/viegas/Desktop/saida/201601101400.txt";
        String param2 = "/home/viegas/Desktop/saida/20160110_anomalous_suspicious.csv";
        String param3 = "/home/viegas/Desktop/saida/20160110.arff";

        Topologies_ARFF_CREATOR.runTopology(
                param1, param2, param3);

        /*        Topologies_ARFF_CREATOR.runTopology(
                args[0], 
                args[1], 
                args[2]);
         */    }

}
