/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.flinkdistributed.EvaluateClassifierMapFunctionWithRejection;
import fcul.viegas.topologies.machinelearning.flinkdistributed.Topologies_FLINK_DISTRIBUTED_TestWithoutUpdateWithRejection;
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
        public double probability;
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
        //dataTrain = mlModelBuilder.selectFeatures(dataTrain);

        final Classifier classifier = classifierToBuild.equals("naive")
                ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : classifierToBuild.equals("hoeffding")
                ? mlModelBuilder.trainClassifierHoeffing(dataTrain) : null;

        ArrayList<ValueForRejectEvaluation> listValues = new ArrayList<>();

        for (String s : testFiles) {
            Instances dataTest = mlModelBuilder.openFile(s);

            for (Instance inst : dataTest) {
                //if (inst.classValue() != 0.0d) {
                ValueForRejectEvaluation values = new ValueForRejectEvaluation();

                values.instClass = inst.classValue();

                double[] prob = classifier.distributionForInstance(inst);
                if (prob[0] > prob[1]) {
                    values.predictClass = 0.0d;
                    values.probability = prob[0];
                } else {
                    values.predictClass = 1.0d;
                    values.probability = prob[1];
                }

                listValues.add(values);
                // }
            }
            System.out.println(s);
        }

        Collections.sort(listValues, new Comparator<ValueForRejectEvaluation>() {
            @Override
            public int compare(ValueForRejectEvaluation o1, ValueForRejectEvaluation o2) {
                return Double.compare(o2.probability, o1.probability);
            }
        });

        for (ValueForRejectEvaluation value : listValues) {
            //System.out.println(value.probability);
        }

        for (int i = 1; i < 100; i++) {
            int index = (int) ((int) listValues.size() - (listValues.size() * (i / (float) 100)));

            int n = 0;
            int nAcc = 0;

            for (int j = 0; j < index; j++) {
                n++;
                if (Double.compare(listValues.get(j).instClass, listValues.get(j).predictClass) == 0) {
                    nAcc++;
                }

            }

            System.out.println(i + ";" 
                    + (1 - (nAcc / (float) n)) + ";" 
                    + index);

        }

    }

}
