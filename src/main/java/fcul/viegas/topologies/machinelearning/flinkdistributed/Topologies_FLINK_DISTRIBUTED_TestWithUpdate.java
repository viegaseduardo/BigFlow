/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.flinkdistributed;

import fcul.viegas.output.ParseRawOutputFlinkNoUpdate;
import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.util.Collector;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_FLINK_DISTRIBUTED_TestWithUpdate {

    public String folderPath;
    public String featureSET;
    public static String PathToModel = "/home/viegas/testes/model/";

    public void run(String pathArffs,
            String featureSet,
            String outputPath,
            String classifierToBuild,
            int daysToUseForTraining,
            int daysModelLife) throws Exception {
        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        ArrayList<String> testFiles = new ArrayList();
        ArrayList<Integer> indexForTraining = new ArrayList();

        this.folderPath = pathArffs;
        this.featureSET = featureSet;

        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : testFiles) {
            System.out.println("\t" + s);
        }

        for (int index = daysToUseForTraining - 1; index < testFiles.size(); index += daysModelLife) {
            indexForTraining.add(index);
        }

        int numberOfModelsToBeGenerated = indexForTraining.size();

        System.out.println("Number of test days: " + testFiles.size());
        System.out.println("Number of models to be generated: " + numberOfModelsToBeGenerated);

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> testFilesDataset = env.fromCollection(testFiles);
        DataSet<Integer> indexForTrainingDataset = env.fromCollection(indexForTraining);

        indexForTrainingDataset.rebalance().flatMap(new RichFlatMapFunction<Integer, Integer>() {

            private Collection<String> broadcastSet;

            @Override
            public void open(Configuration parameters) throws Exception {
                this.broadcastSet = getRuntimeContext().getBroadcastVariable("testFilesDataset");
            }

            @Override
            public void flatMap(Integer in, Collector<Integer> out) throws Exception {
                Instances dataTrain = mlModelBuilder.openFile(testFiles.get(in));
                for (int i = in - 1; i > (in - daysToUseForTraining); i--) {
                    Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
                    for (Instance inst : dataTrainInc) {
                        dataTrain.add(inst);
                    }
                }
                Classifier classifier = classifierToBuild.equals("naive")
                        ? mlModelBuilder.trainClassifierNaive(dataTrain) : classifierToBuild.equals("tree")
                        ? mlModelBuilder.trainClassifierTree(dataTrain) : classifierToBuild.equals("forest")
                        ? mlModelBuilder.trainClassifierForest(dataTrain) : classifierToBuild.equals("bagging")
                        ? mlModelBuilder.trainClassifierBagging(dataTrain) : classifierToBuild.equals("extratrees")
                        ? mlModelBuilder.trainClassifierExtraTrees(dataTrain) : classifierToBuild.equals("adaboost")
                        ? mlModelBuilder.trainClassifierAdaboostTree(dataTrain) : null;

                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(
                                Topologies_FLINK_DISTRIBUTED_TestWithUpdate.PathToModel + "model_" + in));
                oos.writeObject(classifier);
                oos.flush();
                oos.close();
                out.collect(in);
                
                
                oos = null;
                dataTrain = null;
                classifier = null;
                System.gc();
            }
        }).withBroadcastSet(testFilesDataset, "testFilesDataset")
                .setParallelism(env.getParallelism())
                .print();

        //env.execute(pathArffs + "_GENERATING_MODELS");
        env = ExecutionEnvironment.getExecutionEnvironment();
        testFilesDataset = env.fromCollection(testFiles);
        indexForTrainingDataset = env.fromCollection(indexForTraining);

        testFilesDataset.map(new RichMapFunction<String, String>() {
            int lastIndex = -1;
            Classifier classifier = null;

            @Override
            public String map(String in) throws Exception {
                int index = testFiles.indexOf(in);
                int indexModel = daysToUseForTraining - 1;

                while (index > (indexModel + daysModelLife)) {
                    indexModel += daysModelLife;
                }

                if (lastIndex != indexModel) {
                    lastIndex = indexModel;

                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(Topologies_FLINK_DISTRIBUTED_TestWithUpdate.PathToModel + "model_" + indexModel));
                    this.classifier = (Classifier) ois.readObject();
                    ois.close();
                }

                return mlModelBuilder.evaluateClassifier(in, this.classifier);
            }
        }).setParallelism(env.getParallelism())
                .sortPartition(new KeySelector<String, String>() {
                    @Override
                    public String getKey(String in) throws Exception {
                        return in;
                    }
                }, Order.ASCENDING).setParallelism(1).
                writeAsText(outputPath + "_raw_output.csv", FileSystem.WriteMode.OVERWRITE).
                setParallelism(1);

        env.execute(pathArffs + "_DISTRIBUTED_WITH_UPDATE");

        ParseRawOutputFlinkNoUpdate.generateSummaryFile(outputPath + "_raw_output.csv", outputPath + "_summarized_monthly.csv",
                ParseRawOutputFlinkNoUpdate.MonthRange);

        ParseRawOutputFlinkNoUpdate.generateSummaryFile(outputPath + "_raw_output.csv", outputPath + "_summarized_yearly.csv",
                ParseRawOutputFlinkNoUpdate.YearRange);

    }
}
