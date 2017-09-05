/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.util.ArrayList;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author viegas
 */
public class Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate {
    
    public String folderPath;
    public String featureSET;

    public void run(String pathArffs, String featureSet, String outputPath) throws Exception {
        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        ArrayList<String> testFiles = new ArrayList();

        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = mlModelBuilder.openFile(testFiles.get(0));
        for (int i = 1; i < 7; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        //dataTrain = this.selectFeatures(dataTrain);

        System.out.println("Training trainClassifierNaive....");
        Classifier classifier = mlModelBuilder.trainClassifierNaive(dataTrain);
        
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        
        DataSet<String> testFilesDataset = env.fromCollection(testFiles);
        
        
        testFilesDataset.map(new MapFunction<String, String>() {
            @Override
            public String map(String path) throws Exception {
                return mlModelBuilder.evaluateClassifier(path, classifier);
            }
        }).map((String t) -> t).setParallelism(1).writeAsText(outputPath);
        
        env.execute(pathArffs + "_DISTRIBUTED_NO_UPDATE");
        
        
    }

}
