/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.topologies.Topologies_ARFF_CREATOR;
import fcul.viegas.topologies.Topologies_ARFF_SPLIT_FEATURE_SET;
import fcul.viegas.topologies.Topologies_SPARK_ml;
import fcul.viegas.topologies.machinelearning.Topologies_BATCH_No_Update;

/*
 * @author viegas
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {

            String param1 = "/home/viegas/Desktop/saida/201601101400.txt";
            String param2 = "/home/viegas/Desktop/saida/20160110_anomalous_suspicious.csv";
            String param3 = "/home/viegas/Desktop/saida/20160110.arff";

            Topologies_ARFF_CREATOR.runTopology(
                    param1, param2, param3);
        } else if (args[0].equals("extractor")) {
            Topologies_ARFF_CREATOR.runTopology(
                    args[1],
                    args[2],
                    args[3]);
        } else if (args[0].equals("stratification")) {
            Topologies_ARFF_SPLIT_FEATURE_SET.runTopology(
                    args[1], args[2]);
        } else if (args[0].equals("batchnoupdate")) {
            Topologies_BATCH_No_Update.runTopology(
                    args[1],
                    args[2]);
        } else if (args[0].equals("spark")) {
            Topologies_SPARK_ml.runTopology(args[1], args[2]);
        }
    }
}
