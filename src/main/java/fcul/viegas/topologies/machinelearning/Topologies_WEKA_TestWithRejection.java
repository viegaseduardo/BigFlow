/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.flinkdistributed.EvaluateClassifierMapFunctionWithRejection;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithoutUpdateWithRejection;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.core.fs.FileSystem;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_TestWithRejection {

    public class ValueForRejectEvaluation {

        public double instClass;
        public double predictClass;
        public double credibility;
        public double confidence;
    }

    public String folderPath;
    public String featureSET;
    //public static String PathToModel = "/home/viegas/Bases2/model/model";
    public static String PathToModel = "/home/viegas/Downloads/model/model";

    public void run(String pathArffs, String featureSet, String outputPath, String classifierToBuild, int daysToUseForTraining) throws Exception {

        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        ArrayList<String> testFiles = new ArrayList();

        this.folderPath = pathArffs;
        this.featureSET = featureSet;

        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = mlModelBuilder.openFile(testFiles.get(0));
        for (int i = 1; i < daysToUseForTraining; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        dataTrain = mlModelBuilder.getAsNormalizeFeatures(dataTrain);
        //dataTrain = mlModelBuilder.selectFeatures(dataTrain);

        System.out.println("building conformal");
        Transcend_ConformalPredictor conformal = new Transcend_ConformalPredictor();
        conformal.setDataset(dataTrain);

        final Classifier classifier = classifierToBuild.equals("naive")
                ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffing(dataTrain) : null;

        ArrayList<ValueForRejectEvaluation> listValues = new ArrayList<>();

        int j = 0;
        for (String s : testFiles) {
            if(j++ >= 30){
                break;
            }
            Instances dataTest = mlModelBuilder.openFile(s);
            dataTest = mlModelBuilder.getAsNormalizeFeatures(dataTest);

            for (Instance inst : dataTest) {
                if (inst.classValue() != 0.0d) {
                    ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                    values.instClass = inst.classValue();

                    double[] prob = classifier.distributionForInstance(inst);
                    if (prob[0] > prob[1]) {
                        values.predictClass = 0.0d;
                        values.credibility = conformal.getPValueForNormal(inst);
                        values.confidence = conformal.getPValueForAttack(inst);
                    } else {
                        values.predictClass = 1.0d;
                        values.confidence = conformal.getPValueForNormal(inst);
                        values.credibility = conformal.getPValueForAttack(inst);
                    }

                    listValues.add(values);
                }
            }
            System.out.println(s);
        }

        Collections.sort(listValues, new Comparator<ValueForRejectEvaluation>() {
            @Override
            public int compare(ValueForRejectEvaluation o1, ValueForRejectEvaluation o2) {
                return Double.compare(o2.credibility, o1.credibility);
            }
        });

        for (ValueForRejectEvaluation value : listValues) {
            String acertou = "0";
            if(value.instClass != value.predictClass){
                acertou = "0";
                System.out.println(value.confidence + ";" + value.credibility + ";" + acertou);
            }
            
        }
//
//        for (int i = 1; i < 100; i++) {
//            int index = (int) ((int) listValues.size() - (listValues.size() * (i / (float) 100)));
//
//            int n = 0;
//            int nAcc = 0;
//
//            for (j = 0; j < index; j++) {
//                n++;
//                if (Double.compare(listValues.get(j).instClass, listValues.get(j).predictClass) == 0) {
//                    nAcc++;
//                }
//            }
//
//            System.out.println(i + ";"
//                    + (1 - (nAcc / (float) n)) + ";"
//                    + index);
//
//        }

    }

}
