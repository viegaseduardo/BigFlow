/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.Vote;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;
import weka.filters.unsupervised.attribute.ClassAssigner;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_Tests_WithUpdateThreaded extends Thread {

    private ArrayList<String> testFiles = new ArrayList();
    public ArrayList<String> resultList = new ArrayList();

    public float totalFMeasure = 0.0f;
    public float totalAUC = 0;
    public float totalACC = 0.0f;
    public float falsePositive = 0.0f;
    public float falseNegative = 0.0f;
    public float totalrecall = 0.0f;
    public float totalprecision = 0.0f;

    public int start = 0;
    public int end = 0;
    public String testDirect;
    public int modelLife = -1;

    public void findFilesForTest(String pathTestDirectory) {
        File directory = new File(pathTestDirectory);
        String[] directoryContents = directory.list();

        for (String fileName : directoryContents) {
            File temp = new File(String.valueOf(directory), fileName);
            if (temp.isFile() && !String.valueOf(temp).contains("week")) {
                testFiles.add(String.valueOf(temp));
            }
        }
        java.util.Collections.sort(testFiles);
    }

    public Instances selectFeatures(Instances path) throws Exception {

        AttributeSelection attsel = new AttributeSelection();
        weka.attributeSelection.InfoGainAttributeEval selector = new InfoGainAttributeEval();
        weka.attributeSelection.Ranker ranker = new Ranker();

        ranker.setNumToSelect(10);
        attsel.setEvaluator(selector);
        attsel.setSearch(ranker);
        attsel.SelectAttributes(path);

        return attsel.reduceDimensionality(path);
    }

    public Instances makeSuspiciousNormal(Instances data) {

        for (Instance inst : data) {
            if (inst.stringValue(inst.numAttributes() - 1).equals("suspicious")) {
                inst.setClassValue(0.0d);
            }
        }

        return data;
    }

    public Instances removeParticularAttributes(Instances data) {

        data.deleteAttributeAt(data.attribute("VIEGAS_numberOfDifferentDestinations_A").index());
        data.deleteAttributeAt(data.attribute("VIEGAS_numberOfDifferentServices_A").index());

        return data;
    }

    public Instances openFile(String path) throws Exception {
        BufferedReader reader
                = new BufferedReader(new FileReader(path));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader);
        Instances dataTrain = arff.getData();
        dataTrain.setClassIndex(dataTrain.numAttributes() - 1);

//        dataTrain = this.makeSuspiciousNormal(dataTrain);
//        dataTrain = this.removeParticularAttributes(dataTrain);
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

//        dataTrain = this.makeSuspiciousNormal(dataTrain);
//        dataTrain = this.removeParticularAttributes(dataTrain);
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
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        J48 classifier = new J48();

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierEnsemble(Instances train) throws Exception {
        
        FilteredClassifier filteredClassifierTree = new FilteredClassifier();
        filteredClassifierTree.setFilter(new ClassBalancer());

        J48 classifierTree = new J48();
        filteredClassifierTree.setClassifier(classifierTree);

        FilteredClassifier filteredClassifierRandomForest = new FilteredClassifier();
        filteredClassifierRandomForest.setFilter(new ClassBalancer());

        RandomForest classifierRandomForest = new RandomForest();

        classifierRandomForest.setSeed(12345);
        classifierRandomForest.setNumIterations(50);
        classifierRandomForest.buildClassifier(train);

        filteredClassifierRandomForest.setClassifier(classifierRandomForest);

        FilteredClassifier filteredClassifierAdaboost = new FilteredClassifier();
        filteredClassifierAdaboost.setFilter(new ClassBalancer());

        AdaBoostM1 classifierAda = new AdaBoostM1();

        classifierAda.setClassifier(new J48());
        classifierAda.setNumIterations(50);

        filteredClassifierAdaboost.setClassifier(classifierAda);

        weka.classifiers.meta.Vote ensemble = new Vote();
        ensemble.setCombinationRule(new SelectedTag(weka.classifiers.meta.Vote.MAJORITY_VOTING_RULE, weka.classifiers.meta.Vote.TAGS_RULES));

        ensemble.addPreBuiltClassifier(filteredClassifierTree);
        ensemble.addPreBuiltClassifier(filteredClassifierRandomForest);
        ensemble.addPreBuiltClassifier(filteredClassifierAdaboost);

        ensemble.buildClassifier(train);
        
        return ensemble;
    }

    public Classifier trainClassifierAdaboostTree(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        AdaBoostM1 classifier = new AdaBoostM1();

        classifier.setClassifier(new J48());
        classifier.setNumIterations(50);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierNaive(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        NaiveBayes classifier = new NaiveBayes();

        classifier.setUseSupervisedDiscretization(true);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierForest(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        RandomForest classifier = new RandomForest();

        classifier.setSeed(12345);
        classifier.setNumIterations(50);
        classifier.buildClassifier(train);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierSMO(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        weka.classifiers.functions.SMO classifier = new weka.classifiers.functions.SMO();

        classifier.setKernel(new RBFKernel());
        classifier.setFilterType(new SelectedTag(SMO.FILTER_NONE, SMO.TAGS_FILTER));

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public int getMonthFromTestFile(String testFile) {
        String file = testFile.replaceAll("\\D+", "");
        int year = Integer.valueOf(file.substring(0, 4));
        int month = Integer.valueOf(file.substring(4, 6));
        int day = Integer.valueOf(file.substring(6, 8));
        return month;
    }

    public Classifier trainClassifierHoeffing(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifierRandom = new FilteredClassifier();
        filteredClassifierRandom.setFilter(new Randomize());
        
        FilteredClassifier filteredClassifierBalancer = new FilteredClassifier();
        filteredClassifierBalancer.setFilter(new ClassBalancer());
        

        HoeffdingTree classifier = new HoeffdingTree();

        filteredClassifierRandom.setClassifier(classifier);
        filteredClassifierBalancer.setClassifier(filteredClassifierRandom);
        

        inputMapped.setClassifier(filteredClassifierBalancer);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public void runTopology(String pathTestDirectory, int start, int end) throws Exception {
        System.out.println("Start: " + start);

        System.out.println("Path to test directory: " + pathTestDirectory);
        this.findFilesForTest(pathTestDirectory);
        for (String s : this.testFiles) {
            System.out.println("\t" + s);
        }

        Classifier classifier = null;

        int currentModelLife = 1;

        System.out.println("Testing... ");

        for (int i = start; i < end && i < this.testFiles.size(); i++) {
            String testPath = this.testFiles.get(i);
            currentModelLife--;

            //must update model
            if (currentModelLife <= 0) {
                currentModelLife = modelLife;
                Instances newDataTrainNewMonth = this.openFile(this.testFiles.get(i - 1));

                for (int j = (i - 2); j >= (i - 7); j--) {
                    if (j < this.testFiles.size()) {
                        testPath = this.testFiles.get(j);
                        Instances newDataTrain = this.openFile(testPath);
                        for (Instance inst : newDataTrain) {
                            newDataTrainNewMonth.add(inst);
                        }
                    }
                }

                //newDataTrainNewMonth = this.selectFeatures(newDataTrainNewMonth);
                //System.out.println(newDataTrainNewMonth.size());
                classifier = this.trainClassifierEnsemble(newDataTrainNewMonth);

            }

            testPath = this.testFiles.get(i);

            Instances dataTestAUC = this.openFile(testPath);

            Evaluation evalAUC = new Evaluation(dataTestAUC);
            evalAUC.evaluateModel(classifier, dataTestAUC);

            float auc = (float) evalAUC.areaUnderROC(1);
            float fmeasure = (float) evalAUC.fMeasure(1);
            float acc = (float) evalAUC.pctCorrect();
            float fp = (float) evalAUC.falsePositiveRate(1);
            float fn = (float) evalAUC.falseNegativeRate(1);
            float recall = (float) evalAUC.recall(1);
            float precision = (float) evalAUC.precision(1);

            this.totalAUC += auc;
            this.totalFMeasure += fmeasure;
            this.totalACC += acc;
            this.falsePositive += fp;
            this.falseNegative += fn;
            this.totalrecall += recall;
            this.totalprecision += precision;

            String print = this.testFiles.get(start) + ";" + testPath + ";"
                    + (dataTestAUC.size()) + ";"
                    + auc + ";"
                    + acc + ";"
                    + fp + ";"
                    + fn + ";"
                    + fmeasure + ";"
                    + recall + ";"
                    + precision;
            print = print.replace(",", ".");

            this.resultList.add(print);

            System.out.println(print);

        }

    }

    public void run() {
        try {
            this.runTopology(testDirect, start, end);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
