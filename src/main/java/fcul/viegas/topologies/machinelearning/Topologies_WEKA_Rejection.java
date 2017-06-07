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
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 *
 * @author viegas
 */
public class Topologies_WEKA_Rejection {

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
        optRemove = optRemove + 16 + "," + 17 + "," + 18 + "," + 19;
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
        optRemove = optRemove + 16 + "," + 17 + "," + 18 + "," + 19;
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

        remAllButNormal.setAttributeIndex("19");
        remAllButSuspicious.setAttributeIndex("19");
        remAllButAnomalous.setAttributeIndex("19");

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
        optRemove = optRemove + 16 + "," + 17 + "," + 18 + "," + 19;
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

        remAllButNormal.setAttributeIndex("19");
        remAllButSuspicious.setAttributeIndex("19");
        remAllButAnomalous.setAttributeIndex("19");

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
        optRemove = optRemove + 16 + "," + 17 + "," + 18 + "," + 19;
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

        classifier.buildClassifier(train);

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

    public float[] evaluateOnDataset(Classifier classifier, Instances instData, float thresholdReject) throws Exception {
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
            //decides if reject
            if (prob < thresholdReject) {
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

        float[] ret = new float[5];
        ret[0] = nRejected;
        ret[1] = acceptedCorrectlyClassified;
        ret[2] = acceptedMissclassified;
        ret[3] = rejectedCorrectlyClassified;
        ret[4] = rejectedMissclassified;

        return ret;
    }

    public void runTopology(String pathTrain, String pathTestDirectory) throws Exception {
        System.out.println("Path to training: " + pathTrain);

        System.out.println("Path to test directory: " + pathTestDirectory);
        this.findFilesForTest(pathTestDirectory);
        for (String s : this.testFiles) {
            System.out.println("\t" + s);
        }

        System.out.println("Opening training file....");
        Instances dataTrain = this.openFile(pathTrain);

        System.out.println("Training trainClassifierTree....");
        Classifier classifier = this.trainClassifierTree(dataTrain);

        System.out.println("Testing... ");

        String testPath = this.testFiles.get(100);
        Set<Double> set = new LinkedHashSet<Double>();
        Instances dataTest[] = this.openFileForTest(testPath);

        //normal
        for (Instance inst : dataTest[0]) {
            double probs[] = classifier.distributionForInstance(inst);
            set.add(probs[0]);
            set.add(probs[1]);
        }

        //suspicious
        for (Instance inst : dataTest[1]) {
            double probs[] = classifier.distributionForInstance(inst);
            set.add(probs[0]);
            set.add(probs[1]);
        }

        //anomalous
        for (Instance inst : dataTest[2]) {
            double probs[] = classifier.distributionForInstance(inst);
            set.add(probs[0]);
            set.add(probs[1]);
        }

        System.out.println("Unique probabilities: " + set.size());

        ArrayList<Double> probs = new ArrayList(Arrays.asList(set.toArray()));
        java.util.Collections.sort(probs);

        for (Double prob : probs) {
            float[] rejectionForNormal = this.evaluateOnDataset(classifier, dataTest[0], prob.floatValue());
            float[] rejectionForSuspicious = this.evaluateOnDataset(classifier, dataTest[1], prob.floatValue());
            float[] rejectionForAnomalous = this.evaluateOnDataset(classifier, dataTest[2], prob.floatValue());

            String print = pathTrain + ";" + testPath + ";ORUNADA;"
                    + (dataTest[0].size() + dataTest[1].size() + dataTest[2].size()) + ";"
                    + dataTest[0].size() + ";"
                    + dataTest[2].size() + ";"
                    + dataTest[1].size() + ";";
            print = print + rejectionForNormal[0] + ";";
            print = print + rejectionForNormal[1] + ";";
            print = print + rejectionForNormal[2] + ";";
            print = print + rejectionForNormal[3] + ";";
            print = print + rejectionForNormal[4] + ";";
            
            print = print + rejectionForSuspicious[0] + ";";
            print = print + rejectionForSuspicious[1] + ";";
            print = print + rejectionForSuspicious[2] + ";";
            print = print + rejectionForSuspicious[3] + ";";
            print = print + rejectionForSuspicious[4] + ";";
            
            print = print + rejectionForAnomalous[0] + ";";
            print = print + rejectionForAnomalous[1] + ";";
            print = print + rejectionForAnomalous[2] + ";";
            print = print + rejectionForAnomalous[3] + ";";
            print = print + rejectionForAnomalous[4];
                    

            System.out.println(print.replace(",", "."));
        }

//        
//        for (String testPath : this.testFiles) {
//            Instances[] dataTest = this.openFileForTest(testPath);
//            
//            Evaluation evalNormal = new Evaluation(dataTest[0]);
//            evalNormal.evaluateModel(classifier, dataTest[0]);
//            
//            Evaluation evalSuspicious = new Evaluation(dataTest[1]);
//            evalSuspicious.evaluateModel(classifier, dataTest[1]);
//            
//            Evaluation evalAnomalous = new Evaluation(dataTest[2]);
//            evalAnomalous.evaluateModel(classifier, dataTest[2]);
//            
//            String print = pathTrain + ";" + testPath + ";ORUNADA;"
//                    + (dataTest[0].size() + dataTest[1].size() + dataTest[2].size()) + ";"
//                    + dataTest[0].size() + ";"
//                    + dataTest[2].size() + ";"
//                    + dataTest[1].size() + ";"
//                    + String.format("%.4f", ((evalNormal.pctCorrect() * dataTest[0].size()
//                            + evalSuspicious.pctCorrect() * dataTest[1].size()
//                            + evalAnomalous.pctCorrect() * dataTest[2].size()) / (dataTest[0].size() + dataTest[1].size() + dataTest[2].size())) / 100.0f) + ";"
//                    + String.format("%.4f", evalNormal.pctCorrect() / 100.0f) + ";"
//                    + String.format("%.4f", evalAnomalous.pctCorrect() / 100.0f) + ";"
//                    + String.format("%.4f", evalSuspicious.pctCorrect() / 100.0f);
//            System.out.println(print.replace(",", "."));
//        }
    }

}
