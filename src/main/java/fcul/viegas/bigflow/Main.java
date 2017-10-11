/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.Topologies_ARFF_CREATOR;
import fcul.viegas.topologies.Topologies_ARFF_SPLIT_FEATURE_SET;
import fcul.viegas.topologies.machinelearning.Topologies_SPARK_OBTAIN_MODEL_FOREST;
import fcul.viegas.topologies.machinelearning.Topologies_BATCH_No_Update;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate;
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
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_ConformalThresholdFinder;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithUpdate;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithUpdateStream;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithUpdateThreaded;
import fcul.viegas.topologies.machinelearning.Topologies_WEKA_Tests_WithoutUpdate;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithUpdate;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithoutUpdateWithRejection;
import fcul.viegas.topologies.machinelearning.method.Topologies_EVALUATION_DISTRIBUTED_WithConformalEvaluator;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/*
 * @author viegas
 */
public class Main {

    public static void main(String[] args) throws Exception {
        
        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(
                "/home/viegas/testes/tree_orunada_conformal_raw_output.csv",
                "/home/viegas/testes/tree_orunada_conformal_summarized_monthly.csv",
                ParseRawOutputFlinkNoUpdate.MonthRange
        );

        ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection(
                "/home/viegas/testes/tree_orunada_conformal_raw_output.csv",
                "/home/viegas/testes/tree_orunada_conformal_summarized_yearly.csv",
                ParseRawOutputFlinkNoUpdate.YearRange
        );
        System.exit(0);

        if (args.length == 1) {

            Main.startTopologies_WEKA_Tests_WithUpdateStream();

//            Main.startTopologies_WEKA_Tests_WithUpdateThreaded(args[0]);
            // 
            //           Main.startTopologies_WEKA_Tests_WithUpdateThreaded(args[0]);
//            Main.startTopologies_WEKA_Tests_WithUpdate();
//            
//            Main.startTopologies_WEKA_RejectionThresholds();
//                Main.startTopologies_WEKA_Rejection_Evaluation();
        } else if (args[0].equals("testwithoutupdate")) {
            /*
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = number of threads
             */
            Main.startTopologies_WEKA_Tests_WithoutUpdate(
                    args[1],
                    args[2]);
        } else if (args[0].equals("evaluateconformal")) {

            /*
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = number of threads
             */
            
//            System.out.println("foi");
//            new Topologies_WEKA_ConformalThresholdFinder().findOperationPoint("/home/viegas/Downloads/tree_orunada_non_dominated.dat");
//
//            
//            System.exit(0);
//
//            new Topologies_WEKA_ConformalThresholdFinder().getNonDominatedSolutions("/home/viegas/Downloads/tree_moore.dat");
//
//            System.out.println("foi");
//            System.exit(0);
//
//            ParseRawOutputFlinkNoUpdate.generateSummaryFileWithRejection("/home/viegas/Downloads/tree_orunada_raw.csv",
//                    "/home/viegas/Downloads/tree_orunada_summarized_monthly.csv",
//                    0.98847742f,
//                    0.79715146f,
//                    ParseRawOutputFlinkNoUpdate.MonthRange);
//
//            new Topologies_WEKA_ConformalThresholdFinder().run(
//                    "/home/viegas/Downloads/2007",
//                    "ORUNADA",
//                    "/home/viegas/Downloads/saida",
//                    "tree",
//                    30);
//            System.out.println("foi");
//            System.exit(0);
//            
//
//            Main.startTopologies_WEKA_Tests_WithoutUpdate(
//                    args[1],
//                    args[2]);
        } else if (args[0].equals("testwithoutupdatedistributed")) {
            /*
            note that this version is way faster than "testwithupdatedistributed" 
                due to the need to load from disk the models
            
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = output
                args[4] = classifier {naive, tree, forest, extratrees, adaboost, bagging, hoeffding}
                args[5] = days to use for training
            
            ./bin/flink run -p 144 /home/viegas/BigFlow/target/BigFlow-1.0-SNAPSHOT-jar-with-dependencies.jar testwithoutupdatedistributed /home/viegas/Bases/arrfs/ NIGEL /home/viegas/testes/forest forest 7
             
             */
            new Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate().run(
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    Integer.valueOf(args[5]));
        } else if (args[0].equals("testwithoutupdateconformalevaluator")) {
            /*
            note that this version is way faster than "testwithupdatedistributed" 
                due to the need to load from disk the models
            
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = output
                args[4] = classifier {naive, tree, forest, extratrees, adaboost, bagging, hoeffding}
                args[5] = days to use for training
                args[6] = normal threshold
                args[7] = attack threshold
            
            ./bin/flink run -p 144 /home/viegas/BigFlow/target/BigFlow-1.0-SNAPSHOT-jar-with-dependencies.jar testwithoutupdatedistributed /home/viegas/Bases/arrfs/ NIGEL /home/viegas/testes/forest forest 7
             
             */
            new Topologies_EVALUATION_DISTRIBUTED_WithConformalEvaluator().run(
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    Integer.valueOf(args[5]),
                    Float.valueOf(args[6]),
                    Float.valueOf(args[7]));
        } else if (args[0].equals("testwithoutupdatedistributedwithrejection")) {
            /*
            note that this version is way faster than "testwithupdatedistributed" 
                due to the need to load from disk the models
            
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = output
                args[4] = classifier {naive, tree, forest, extratrees, adaboost, bagging, hoeffding}
                args[5] = days to use for training
            
            ./bin/flink run -p 144 /home/viegas/BigFlow/target/BigFlow-1.0-SNAPSHOT-jar-with-dependencies.jar testwithoutupdatedistributed /home/viegas/Bases/arrfs/ NIGEL /home/viegas/testes/forest forest 7
             
             */
            new Topologies_FLINK_DISTRIBUTED_TestWithoutUpdateWithRejection().run(
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    Integer.valueOf(args[5]));
        } else if (args[0].equals("testwithupdatedistributed")) {
            /*
                args[1] = path to folder
                args[2] = feature set {VIEGAS, MOORE, NIGEL or ORUNADA}
                args[3] = output
                args[4] = classifier {naive, tree, forest, extratrees, adaboost, bagging, hoeffding}
                args[5] = days to use for training
                args[6] = days model life
             */
            new Topologies_FLINK_DISTRIBUTED_TestWithUpdate().run(
                    args[1],
                    args[2],
                    args[3],
                    args[4],
                    Integer.valueOf(args[5]),
                    Integer.valueOf(args[6]));
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

    public static void startTopologies_WEKA_RejectionThresholds() {
        try {
            Topologies_WEKA_RejectionThresholds topo = new Topologies_WEKA_RejectionThresholds();

            topo.runTopology("/home/viegas/arffs/viegas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTopologies_WEKA_Tests_WithUpdateStream() {
        try {
            Topologies_WEKA_Tests_WithUpdateStream topo = new Topologies_WEKA_Tests_WithUpdateStream();

            topo.runTopology("/home/viegas/arffs/viegas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTopologies_WEKA_Tests_WithUpdate() {
        try {
            ArrayList<Topologies_WEKA_Tests_WithUpdate> listThreads = new ArrayList();
            for (int i = 1; i <= 12; i++) {
                Topologies_WEKA_Tests_WithUpdate thread = new Topologies_WEKA_Tests_WithUpdate();
                thread.month = i;
                thread.pathTestDirectory = "/home/projeto/disco/stratweka/arffs/viegas";
                thread.start();
                listThreads.add(thread);
            }
            for (int i = 0; i < 12; i++) {
                listThreads.get(i).join();
            }
            for (int i = 0; i < 12; i++) {
                for (String s : listThreads.get(i).resultList) {
                    System.out.println(s);
                }
                System.out.println(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTopologies_WEKA_Rejection_Evaluation() {
        try {
            Topologies_WEKA_Rejection_Evaluation topo = new Topologies_WEKA_Rejection_Evaluation();

            topo.runTopology("/home/viegas/arffs/viegas");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTopologies_WEKA_Tests_WithoutUpdate(String folderPath, String featureSet) {
        try {
            Topologies_WEKA_Tests_WithoutUpdate topo = new Topologies_WEKA_Tests_WithoutUpdate();
            topo.folderPath = folderPath;
            topo.featureSET = featureSet;
            topo.runTopology();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void startTopologies_WEKA_Tests_WithUpdateThreaded(String arg) {
        try {
            float totalFMeasure = 0.0f;
            float totalAUC = 0;
            float totalACC = 0.0f;
            float falsePositive = 0.0f;
            float falseNegative = 0.0f;
            float totalrecall = 0.0f;
            float totalprecision = 0.0f;
            float toDivide = 0;

            ArrayList<Topologies_WEKA_Tests_WithUpdateThreaded> listThreads = new ArrayList();
            int modellife = Integer.valueOf(arg);
            for (int i = 7; i < 278;) {
                Topologies_WEKA_Tests_WithUpdateThreaded thread = new Topologies_WEKA_Tests_WithUpdateThreaded();
                thread.start = i;
                thread.modelLife = modellife;
                if ((i + modellife) >= 278) {
                    thread.end = 278;
                    i = i + modellife;
                } else {
                    if (modellife <= 25) {
                        int toJump = 0;
                        while (toJump < 25) {
                            toJump += modellife;
                        }
                        thread.end = (i + toJump);
                        i = i + toJump;
                    } else {
                        thread.end = (i + modellife);
                        i = i + modellife;
                    }
                }

                thread.testDirect = "/home/projeto/disco/stratweka/arffs/viegas";
//                thread.testDirect = "/home/viegas/arffs/viegas";
                thread.start();
                listThreads.add(thread);
            }
            for (int i = 0; i < listThreads.size(); i++) {
                listThreads.get(i).join();
            }
            for (int k = 0; k < 10; k++) {
                System.out.println();
            }
            for (int i = 0; i < listThreads.size(); i++) {
                for (String s : listThreads.get(i).resultList) {
                    System.out.println(s);
                }

                falsePositive += listThreads.get(i).falsePositive;
                falseNegative += listThreads.get(i).falseNegative;
                totalFMeasure += listThreads.get(i).totalFMeasure;
                totalAUC += listThreads.get(i).totalAUC;
                totalACC += listThreads.get(i).totalACC;
                totalrecall += listThreads.get(i).totalrecall;
                totalprecision += listThreads.get(i).totalprecision;
                toDivide += (listThreads.get(i).end - listThreads.get(i).start);
            }

            String output = modellife + ";"
                    + (falsePositive / toDivide) + ";"
                    + (falseNegative / toDivide) + ";"
                    + (totalFMeasure / toDivide) + ";"
                    + (totalAUC / toDivide) + ";"
                    + (totalACC / toDivide) + ";"
                    + (totalrecall / toDivide) + ";"
                    + (totalprecision / toDivide) + ";"
                    + "\n";

            try {
                Files.write(Paths.get("result"), output.getBytes(), StandardOpenOption.APPEND);
//                Files.write(Paths.get("/home/viegas/BigFlow/result"), output.getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {
                //exception handling left as an exercise for the reader
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
