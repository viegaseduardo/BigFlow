/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.topologies.Topologies_ARFF_CREATOR;
import fcul.viegas.topologies.Topologies_ARFF_SPLIT_FEATURE_SET;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_OBTAIN_MODEL_FOREST;
import fcul.viegas.topologies.machinelearning.Topologies_BATCH_No_Update;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_CREATE_CLUSTERS;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_OBTAIN_MODEL_GRADIENT;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_OBTAIN_MODEL_NAIVE;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_OBTAIN_MODEL_SVM;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_TEST_MODEL_FOREST;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_TEST_MODEL_GRADIENT;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_TEST_MODEL_NAIVE;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_TEST_MODEL_SVM;

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
            /*
                args[1] = path to input
                args[2] = path to output
             */

            Topologies_ARFF_SPLIT_FEATURE_SET.runTopology(
                    args[1], args[2]);
        } else if (args[0].equals("batchnoupdate")) {
            Topologies_BATCH_No_Update.runTopology(
                    args[1],
                    args[2]);
        } else if (args[0].equals("sparktrainforest")) {
            /*
                args[1] = path to train arff
                args[2] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_OBTAIN_MODEL_FOREST.runTopology(args[1], args[2]);
        } else if (args[0].equals("sparktestforest")) {
            /*
                args[1] = path to model
                args[2] = path to test arff
                args[3] = output path
                args[4] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_TEST_MODEL_FOREST.runTopology(args[1], args[2], args[3], args[4]);
        } else if (args[0].equals("sparktraingradient")) {
            /*
                args[1] = path to train arff
                args[2] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_OBTAIN_MODEL_GRADIENT.runTopology(args[1], args[2]);
        } else if (args[0].equals("sparktestgradient")) {
            /*
                args[1] = path to model
                args[2] = path to test arff
                args[3] = output path
                args[4] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_TEST_MODEL_GRADIENT.runTopology(args[1], args[2], args[3], args[4]);
        } else if (args[0].equals("sparktrainsvm")) {
            /*
                args[1] = path to train arff
                args[2] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_OBTAIN_MODEL_SVM.runTopology(args[1], args[2]);
        } else if (args[0].equals("sparktestsvm")) {
            /*
                args[1] = path to model
                args[2] = path to test arff
                args[3] = output path
                args[4] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_TEST_MODEL_SVM.runTopology(args[1], args[2], args[3], args[4]);
        } else if (args[0].equals("sparktrainnaive")) {
            /*
                args[1] = path to train arff
                args[2] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_OBTAIN_MODEL_NAIVE.runTopology(args[1], args[2]);
        } else if (args[0].equals("sparktestnaive")) {
            /*
                args[1] = path to model
                args[2] = path to test arff
                args[3] = output path
                args[4] = feature set (NIGEL, MOORE, VIEGAS or ORUNADA)
             */
            Topologies_SPARK_TEST_MODEL_NAIVE.runTopology(args[1], args[2], args[3], args[4]);
        } else if (args[0].equals("sparkcluster")) {
            /*
                args[1] = path to arff
                args[2] = number of clusters
                args[3] = outputpath
                args[4] = featureset
             */
            Topologies_SPARK_CREATE_CLUSTERS.runTopology(args[1], Integer.valueOf(args[2]), args[3], args[4]);
        }
    }
}
