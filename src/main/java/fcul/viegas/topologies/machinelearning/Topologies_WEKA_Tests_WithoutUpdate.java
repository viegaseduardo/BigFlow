/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.spark.ml.source.libsvm.LibSVMDataSource;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_Tests_WithoutUpdate {

    private ArrayList<String> testFiles = new ArrayList();
    public String folderPath;
    public String featureSET;

    
    public void runTopology() throws Exception {
        
        MachineLearningModelBuilders mlModelBuilder = new MachineLearningModelBuilders();
        
        System.out.println("Path to test directory: " + this.folderPath + " searching for feature set: " + this.featureSET);
        mlModelBuilder.findFilesForTest(this.folderPath, featureSET, testFiles);
        java.util.Collections.sort(testFiles);

        for (String s : this.testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = mlModelBuilder.openFile(this.testFiles.get(0));
        for (int i = 1; i < 7; i++) {
            Instances dataTrainInc = mlModelBuilder.openFile(this.testFiles.get(i));
            for (Instance inst : dataTrainInc) {
                dataTrain.add(inst);
            }
        }
        //dataTrain = this.selectFeatures(dataTrain);

        System.out.println("Training trainClassifierNaive....");
        Classifier classifier = mlModelBuilder.trainClassifierNaive(dataTrain);

        System.out.println("Testing... ");
        for (String testPath : this.testFiles) {
            Instances[] dataTest = mlModelBuilder.openFileForTest(testPath);

            Evaluation evalNormal = new Evaluation(dataTest[0]);
            evalNormal.evaluateModel(classifier, dataTest[0]);

            Evaluation evalSuspicious = new Evaluation(dataTest[1]);
            evalSuspicious.evaluateModel(classifier, dataTest[1]);

            Evaluation evalAnomalous = new Evaluation(dataTest[2]);
            evalAnomalous.evaluateModel(classifier, dataTest[2]);

            String print = testPath + ";"
                    + (dataTest[0].size() + dataTest[1].size() + dataTest[2].size()) + ";"
                    + dataTest[0].size() + ";"
                    + dataTest[2].size() + ";"
                    + dataTest[1].size() + ";"
                    + String.format("%.4f", ((evalNormal.pctCorrect() * dataTest[0].size()
                            + evalSuspicious.pctCorrect() * dataTest[1].size()
                            + evalAnomalous.pctCorrect() * dataTest[2].size()) / (dataTest[0].size() + dataTest[1].size() + dataTest[2].size())) / 100.0f) + ";"
                    + String.format("%.4f", evalNormal.pctCorrect() / 100.0f) + ";"
                    + String.format("%.4f", evalAnomalous.pctCorrect() / 100.0f) + ";"
                    + String.format("%.4f", evalSuspicious.pctCorrect() / 100.0f);
            System.out.println(print.replace(",", "."));
        }

    }

}
