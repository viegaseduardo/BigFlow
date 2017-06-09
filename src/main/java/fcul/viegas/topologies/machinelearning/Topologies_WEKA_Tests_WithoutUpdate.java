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
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;
import weka.filters.unsupervised.attribute.Normalize;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_Tests_WithoutUpdate {
    
    private ArrayList<String> testFiles = new ArrayList();
    
    public void findFilesForTest(String pathTestDirectory) {
        File directory = new File(pathTestDirectory);
        String[] directoryContents = directory.list();
        
        for (String fileName : directoryContents) {
            File temp = new File(String.valueOf(directory), fileName);
            if (!String.valueOf(temp).contains("week")) {
                testFiles.add(String.valueOf(temp));
            }
        }
        java.util.Collections.sort(testFiles);
    }
    
    public Instances openFile(String path) throws Exception {
        BufferedReader reader
                = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances dataTrain = arff.getData();
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        
        String[] options = new String[2];
        options[0] = "-R";
        
        String optRemove = "";
        optRemove = optRemove + (dataTrain.numAttributes() - 4) + "," 
                + (dataTrain.numAttributes() - 3) + ","
                + (dataTrain.numAttributes() - 2) + "," 
                + (dataTrain.numAttributes() - 1);
        options[1] = optRemove;
        
        Remove remove = new Remove();
        remove.setOptions(options);
        //remove.setInvertSelection(true);
        remove.setInputFormat(dataTrain);
        
        Instances newdataFeat = Filter.useFilter(dataTrain, remove);
        newdataFeat.setClassIndex(newdataFeat.numAttributes() - 1);

//        Enumeration<Attribute> e = newdataFeat.enumerateAttributes();
//        
//        while (e.hasMoreElements()) {
//            Attribute param = e.nextElement();
//            System.out.println(param.name());
//        }
//        System.out.println(newdataFeat.classAttribute().name());
        return newdataFeat;
    }
    
    public Instances openFileNormalized(String path) throws Exception {
        BufferedReader reader
                = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances dataTrain = arff.getData();
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        
        String[] options = new String[2];
        options[0] = "-R";
        
        String optRemove = "";
        optRemove = optRemove + (dataTrain.numAttributes() - 4) + "," 
                + (dataTrain.numAttributes() - 3) + ","
                + (dataTrain.numAttributes() - 2) + "," 
                + (dataTrain.numAttributes() - 1);
        options[1] = optRemove;
        
        Remove remove = new Remove();
        remove.setOptions(options);
        //remove.setInvertSelection(true);
        remove.setInputFormat(dataTrain);
        
        Instances newdataFeat = Filter.useFilter(dataTrain, remove);
        newdataFeat.setClassIndex(newdataFeat.numAttributes() - 1);
        
        Normalize norm = new Normalize();
        
        norm.setInputFormat(newdataFeat);
        norm.setScale(2.0d);
        norm.setTranslation(-1.0d);
        
        Instances normData = Filter.useFilter(dataTrain, norm);
        normData.setClassIndex(normData.numAttributes() - 1);
        
        return newdataFeat;
    }
    
    public Instances[] openFileForTest(String path) throws Exception {
        BufferedReader reader
                = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances dataTrain = arff.getData();
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        
        RemoveWithValues remAllButNormal = new RemoveWithValues();
        RemoveWithValues remAllButSuspicious = new RemoveWithValues();
        RemoveWithValues remAllButAnomalous = new RemoveWithValues();
        
        remAllButNormal.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        remAllButSuspicious.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        remAllButAnomalous.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        
        remAllButNormal.setNominalIndices("2,3");
        remAllButSuspicious.setNominalIndices("1,2");
        remAllButAnomalous.setNominalIndices("1,3");
        
        remAllButNormal.setInputFormat(dataTrain);
        remAllButSuspicious.setInputFormat(dataTrain);
        remAllButAnomalous.setInputFormat(dataTrain);
        
        Instances dataTrainNormal = Filter.useFilter(dataTrain, remAllButNormal);
        Instances dataTrainSuspicious = Filter.useFilter(dataTrain, remAllButSuspicious);
        Instances dataTrainAnomalous = Filter.useFilter(dataTrain, remAllButAnomalous);
        
        String[] options = new String[2];
        options[0] = "-R";
        
        String optRemove = "";
        optRemove = optRemove + (dataTrain.numAttributes() - 4) + "," 
                + (dataTrain.numAttributes() - 3) + ","
                + (dataTrain.numAttributes() - 2) + "," 
                + (dataTrain.numAttributes() - 1);
        options[1] = optRemove;
        
        Remove remove = new Remove();
        remove.setOptions(options);
        //remove.setInvertSelection(true);
        remove.setInputFormat(dataTrain);
        
        Instances newdataNormal = Filter.useFilter(dataTrainNormal, remove);
        Instances newdataSuspicious = Filter.useFilter(dataTrainSuspicious, remove);
        Instances newdataAnomalous = Filter.useFilter(dataTrainAnomalous, remove);
        
        newdataNormal.setClassIndex(newdataNormal.numAttributes() - 1);
        newdataSuspicious.setClassIndex(newdataSuspicious.numAttributes() - 1);
        newdataAnomalous.setClassIndex(newdataAnomalous.numAttributes() - 1);
        
        Instances[] ret = new Instances[3];
        ret[0] = newdataNormal;
        ret[1] = newdataSuspicious;
        ret[2] = newdataAnomalous;
        
        return ret;
    }
    
    public Instances[] openFileForTestNormalized(String path) throws Exception {
        BufferedReader reader
                = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances dataTrain = arff.getData();
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        
        Normalize norm = new Normalize();
        
        norm.setInputFormat(dataTrain);
        norm.setScale(2.0d);
        norm.setTranslation(-1.0d);
        
        dataTrain = Filter.useFilter(dataTrain, norm);
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);
        
        RemoveWithValues remAllButNormal = new RemoveWithValues();
        RemoveWithValues remAllButSuspicious = new RemoveWithValues();
        RemoveWithValues remAllButAnomalous = new RemoveWithValues();
        
        remAllButNormal.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        remAllButSuspicious.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        remAllButAnomalous.setAttributeIndex("" + (dataTrain.numAttributes() - 1));
        
        remAllButNormal.setNominalIndices("2,3");
        remAllButSuspicious.setNominalIndices("1,2");
        remAllButAnomalous.setNominalIndices("1,3");
        
        remAllButNormal.setInputFormat(dataTrain);
        remAllButSuspicious.setInputFormat(dataTrain);
        remAllButAnomalous.setInputFormat(dataTrain);
        
        Instances dataTrainNormal = Filter.useFilter(dataTrain, remAllButNormal);
        Instances dataTrainSuspicious = Filter.useFilter(dataTrain, remAllButSuspicious);
        Instances dataTrainAnomalous = Filter.useFilter(dataTrain, remAllButAnomalous);
        
        String[] options = new String[2];
        options[0] = "-R";
        
        String optRemove = "";
        optRemove = optRemove + (dataTrain.numAttributes() - 4) + "," 
                + (dataTrain.numAttributes() - 3) + ","
                + (dataTrain.numAttributes() - 2) + "," 
                + (dataTrain.numAttributes() - 1);
        options[1] = optRemove;
        
        Remove remove = new Remove();
        remove.setOptions(options);
        //remove.setInvertSelection(true);
        remove.setInputFormat(dataTrain);
        
        Instances newdataNormal = Filter.useFilter(dataTrainNormal, remove);
        Instances newdataSuspicious = Filter.useFilter(dataTrainSuspicious, remove);
        Instances newdataAnomalous = Filter.useFilter(dataTrainAnomalous, remove);
        
        newdataNormal.setClassIndex(newdataNormal.numAttributes() - 1);
        newdataSuspicious.setClassIndex(newdataSuspicious.numAttributes() - 1);
        newdataAnomalous.setClassIndex(newdataAnomalous.numAttributes() - 1);
        
        Instances[] ret = new Instances[3];
        ret[0] = newdataNormal;
        ret[1] = newdataSuspicious;
        ret[2] = newdataAnomalous;
        
        return ret;
    }
    
    public Classifier trainClassifierTree(Instances train) throws Exception {
        J48 classifier = new J48();
        
        weka.classifiers.meta.CostSensitiveClassifier cost = new CostSensitiveClassifier();
        CostMatrix costMatrix = new CostMatrix(2);
        
        costMatrix.setElement(0, 1, 1.0);
        costMatrix.setElement(1, 0, 2.0);
        
        cost.setCostMatrix(costMatrix);
        cost.setClassifier(classifier);
        
        cost.buildClassifier(train);
        
        //classifier.buildClassifier(train);
        
        return classifier;
    }
    
    public Classifier trainClassifierAdaboostTree(Instances train) throws Exception {
        AdaBoostM1 classifier = new AdaBoostM1();
        
        classifier.setClassifier(new J48());
        classifier.setNumIterations(100);
        
        classifier.buildClassifier(train);
        
        return classifier;
    }
    
    public Classifier trainClassifierNaive(Instances train) throws Exception {
        NaiveBayes classifier = new NaiveBayes();
        
        classifier.setUseSupervisedDiscretization(true);
        classifier.buildClassifier(train);
        
        return classifier;
    }
    
    public Classifier trainClassifierForest(Instances train) throws Exception {
        RandomForest classifier = new RandomForest();
        
        classifier.setSeed(12345);
        classifier.setNumIterations(100);
        classifier.buildClassifier(train);
        
        return classifier;
    }
    
    public Classifier trainClassifierSMO(Instances train) throws Exception {
        weka.classifiers.functions.SMO classifier = new weka.classifiers.functions.SMO();
        
        classifier.setKernel(new RBFKernel());
        classifier.setFilterType(new SelectedTag(SMO.FILTER_NONE, SMO.TAGS_FILTER));
        
        classifier.buildClassifier(train);
        
        return classifier;
    }
    
    public void runTopology(String pathTestDirectory) throws Exception {        
        System.out.println("Path to test directory: " + pathTestDirectory);
        this.findFilesForTest(pathTestDirectory);
        for (String s : this.testFiles) {
            System.out.println("\t" + s);
        }
        
        System.out.println("Opening training file....");
        Instances dataTrain = this.openFile(this.testFiles.get(0));
        for(int i = 1; i < 7; i++){
            Instances dataTrainInc = this.openFile(this.testFiles.get(i));
            for(Instance inst:dataTrainInc){
                dataTrain.add(inst);
            }
        }
        
        System.out.println("Training trainClassifierTree....");
        Classifier classifier = this.trainClassifierTree(dataTrain);
        
        System.out.println("Testing... ");
        for (String testPath : this.testFiles) {
            Instances[] dataTest = this.openFileForTest(testPath);
            
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
