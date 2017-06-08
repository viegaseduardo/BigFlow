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
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_RejectionThresholds;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Rejection_Evaluation;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithUpdate;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithoutUpdate;
import java.util.ArrayList;

/*
 * @author viegas
 */
public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
//
//            Topologies_WEKA_Tests_WithoutUpdate topo = new Topologies_WEKA_Tests_WithoutUpdate();
//
//            topo.runTopology("/home/viegas/Desktop/saida/arffOrunada/1weekprop_ORUNADA.arff", 
//                    "/home/viegas/Desktop/saida/arffOrunada");


            ArrayList<Topologies_WEKA_Tests_WithUpdate> listThreads = new ArrayList();
            for(int i = 1; i <= 12; i++){
                Topologies_WEKA_Tests_WithUpdate thread = new Topologies_WEKA_Tests_WithUpdate();
                thread.month = i;
                thread.pathTestDirectory = "/home/viegas/Desktop/saida/arffOrunada";
                thread.start();
                listThreads.add(thread);
            }
            for(int i = 0; i < 12; i++){
                listThreads.get(i).join();
            }
            for(int i = 0; i < 12; i++){
                for(String s : listThreads.get(i).resultList){
                    System.out.println(s);
                }
                System.out.println(i);
            }
            
//            Topologies_WEKA_Tests_WithUpdate topo = new Topologies_WEKA_Tests_WithUpdate();
//
//            topo.runTopology("/home/viegas/Desktop/saida/arffOrunada/1weekprop_ORUNADA.arff",
//                    "/home/viegas/Desktop/saida/arffOrunada");
//            
//            Topologies_WEKA_RejectionThresholds topo = new Topologies_WEKA_RejectionThresholds();
//
//            topo.runTopology("/home/viegas/Desktop/saida/arffOrunada/1weekprop_ORUNADA.arff", 
//                    "/home/viegas/Desktop/saida/arffOrunada");
//            Topologies_WEKA_Rejection_Evaluation topo = new Topologies_WEKA_Rejection_Evaluation();
//
//            topo.runTopology("/home/viegas/Desktop/saida/arffOrunada/1weekprop_ORUNADA.arff",
//                    "/home/viegas/Desktop/saida/arffOrunada");

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
