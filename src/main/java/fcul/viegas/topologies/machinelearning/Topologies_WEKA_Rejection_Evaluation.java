/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.AdaBoostM1;
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
import weka.filters.supervised.instance.Resample;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_Rejection_Evaluation {

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
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

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

        inputMapped.setClassifier(ensemble);
        inputMapped.buildClassifier(train);

        return inputMapped;
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

    public Classifier trainClassifierHoeffing(Instances train) throws Exception {

        Resample resample = new Resample();
        resample.setBiasToUniformClass(1.0d);
        resample.setInputFormat(train);

        Instances dataTrain = Filter.useFilter(train, resample);

        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(dataTrain);

        FilteredClassifier filteredClassifierRandom = new FilteredClassifier();
        filteredClassifierRandom.setFilter(new Randomize());

        HoeffdingTree classifier = new HoeffdingTree();

        filteredClassifierRandom.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifierRandom);
        inputMapped.buildClassifier(dataTrain);

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

    public float[] evaluateOnDataset(Classifier classifier, Instances instData, float thresholdRejectNormal, float thresholdRejectAttack) throws Exception {
        int nRejected = 0;
        int acceptedCorrectlyClassified = 0;
        int acceptedMissclassified = 0;
        int rejectedCorrectlyClassified = 0;
        int rejectedMissclassified = 0;
        for (Instance inst : instData) {
            double predictProb[] = classifier.distributionForInstance(inst);
            double prob = predictProb[0];
            double predictedClass = 0.0d;
            if (prob < predictProb[1]) {
                prob = predictProb[1];
                predictedClass = 1.0d;
            }
            if (predictedClass == 0.0d) {
                //decides if reject
                if (prob < thresholdRejectNormal) {
                    nRejected++;
                    //missrejected
                    if (inst.classValue() == predictedClass) {
                        rejectedCorrectlyClassified++;
                    } else {
                        //sucessfully rejected
                        rejectedMissclassified++;
                    }
                } else {
                    //correctly accepted
                    if (inst.classValue() == predictedClass) {
                        acceptedCorrectlyClassified++;
                    } else {
                        //sucessfully rejected
                        acceptedMissclassified++;
                    }
                }
            } else {
                //decides if reject
                if (prob < thresholdRejectAttack) {
                    nRejected++;
                    //missrejected
                    if (inst.classValue() == predictedClass) {
                        rejectedCorrectlyClassified++;
                    } else {
                        //sucessfully rejected
                        rejectedMissclassified++;
                    }
                } else {
                    //correctly accepted
                    if (inst.classValue() == predictedClass) {
                        acceptedCorrectlyClassified++;
                    } else {
                        //sucessfully rejected
                        acceptedMissclassified++;
                    }
                }
            }
        }

        float[] ret = new float[5];
        ret[0] = nRejected;
        ret[1] = acceptedCorrectlyClassified;
        ret[2] = acceptedMissclassified;
        ret[3] = rejectedCorrectlyClassified;
        ret[4] = rejectedMissclassified;

        return ret;
    }

    public String printMeasuresRecognition(float[] measures) {
        String ret = "";

        float recognitionRate;
        float errorRate;
        float rejectionRate;
        float reliability;

        recognitionRate = measures[1] / (measures[1] + measures[2]);
        errorRate = measures[2] / (measures[1] + measures[2]);
        rejectionRate = (measures[3] + measures[4]) / (measures[1] + measures[2] + measures[3] + measures[4]);
        reliability = recognitionRate / (recognitionRate + errorRate);

        ret = ret + recognitionRate + ";" + errorRate + ";" + rejectionRate + ";" + reliability;

        return ret;
    }

    public Instances[] splitNormalAnomaly(Instances data, String testFile) throws Exception {
        Instances[] instVect = new Instances[2];
        instVect[0] = this.openFile(testFile);
        instVect[1] = this.openFile(testFile);

        instVect[0].delete();
        instVect[1].delete();

        for (Instance inst : data) {
            if (inst.classValue() == 0.0d) {
                instVect[0].add(inst);
            } else {
                instVect[1].add(inst);
            }
        }

        return instVect;
    }

    public void runTopology(String pathTestDirectory) throws Exception {
        System.out.println("Path to test directory: " + pathTestDirectory);
        this.findFilesForTest(pathTestDirectory);
        for (String s : this.testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = this.openFile(this.testFiles.get(0));

        for (int j = 1; j <= 6; j++) {
            if (j < this.testFiles.size()) {
                Instances newDataTrain = this.openFile(this.testFiles.get(j));
                for (Instance inst : newDataTrain) {
                    dataTrain.add(inst);
                }
            }
        }

        System.out.println("Training trainClassifierHoeffing....");
        Classifier classifier = this.trainClassifierHoeffing(dataTrain);

        System.out.println(classifier.toString());
        
        Double probNormal = 0.95d;
        Double probAttack = 0.5d;

        System.out.println("Testing... ");

        for (int i = 0; i < this.testFiles.size(); i++) {
            Instances[] instVect = this.splitNormalAnomaly(dataTrain, this.testFiles.get(i));

            float[] rejectionNormal = this.evaluateOnDataset(classifier, instVect[0], probNormal.floatValue(), probAttack.floatValue());
            float[] rejectionAttack = this.evaluateOnDataset(classifier, instVect[1], probNormal.floatValue(), probAttack.floatValue());

            float[] allreject = new float[5];
            allreject[0] = rejectionNormal[0] + rejectionAttack[0];
            allreject[1] = rejectionNormal[1] + rejectionAttack[1];
            allreject[2] = rejectionNormal[2] + rejectionAttack[2];
            allreject[3] = rejectionNormal[3] + rejectionAttack[3];
            allreject[4] = rejectionNormal[4] + rejectionAttack[4];

            String print = probNormal + ";" + probAttack + ";"
                    + dataTrain.size() + ";";
            print = print + this.printMeasuresRecognition(allreject) + ";";
            print = print + this.printMeasuresRecognition(rejectionNormal) + ";";
            print = print + this.printMeasuresRecognition(rejectionAttack) + ";";

            System.out.println(print.replace(",", "."));
        }

    }

}
