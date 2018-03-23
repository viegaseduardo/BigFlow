/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.InstancesHeader;
import com.yahoo.labs.samoa.instances.WekaToSamoaInstanceConverter;
import fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper;
import fcul.viegas.topologies.machinelearning.method.WekaMoaClassifierWrapper;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import moa.classifiers.meta.AdaptiveRandomForest;
import moa.classifiers.meta.LeveragingBag;
import moa.classifiers.meta.OCBoost;
import moa.classifiers.meta.OzaBag;
import moa.classifiers.meta.OzaBoost;
import moa.classifiers.trees.AdaHoeffdingOptionTree;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.classifiers.trees.HoeffdingTree;
import moa.options.ClassOption;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.meta.RandomCommittee;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.ExtraTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;
import weka.filters.supervised.instance.SpreadSubsample;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.Randomize;
import weka.filters.unsupervised.instance.RemoveWithValues;

/**
 *
 * @author viegas
 */
public class MachineLearningModelBuilders implements Serializable {

    public Instances removeParticularAttributesViegas(Instances data) {
        if (data == null) {
            return data;
        }
        data.deleteAttributeAt(data.attribute("VIEGAS_numberOfDifferentDestinations_A").index());
        data.deleteAttributeAt(data.attribute("VIEGAS_numberOfDifferentServices_A").index());
        return data;
    }

    public Instances removeParticularAttributesOrunada(Instances data) {
        if (data == null) {
            return data;
        }
        data.deleteAttributeAt(data.attribute("ORUNADA_numberOfDifferentDestinations").index());
        data.deleteAttributeAt(data.attribute("ORUNADA_numberOfDifferentServices").index());
        return data;
    }

    public void findFilesForTest(String pathTestDirectory, String featureSet, ArrayList<String> testFiles) {
        File directory = new File(pathTestDirectory);
        String[] directoryContents = directory.list();

        for (String fileName : directoryContents) {
            File temp = new File(String.valueOf(directory), fileName);
            if (temp.isDirectory()) {
                this.findFilesForTest(String.valueOf(temp), featureSet, testFiles);
            } else if (String.valueOf(temp).contains("_" + featureSet + ".arff")) {
                testFiles.add(String.valueOf(temp));
            }
        }
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

    public Instances getAsNormalizeFeatures(Instances path) throws Exception {

        if (path == null) {
            return path;
        }

//        throw new Exception();
        Normalize norm = new Normalize();

        norm.setInputFormat(path);
        norm.setScale(2.0d);
        norm.setTranslation(-1.0d);

        Instances normData = Filter.useFilter(path, norm);
        normData.setClassIndex(normData.numAttributes() - 1);

        return normData;
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

        return normData;
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

    public Classifier trainClassifierAdaboostTree(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        AdaBoostM1 classifier = new AdaBoostM1();

        classifier.setClassifier(new J48());
        classifier.setNumIterations(20);
        classifier.buildClassifier(train);

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

        classifier.setNumExecutionSlots(20);
        classifier.setNumIterations(20);
        classifier.buildClassifier(train);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierExtraTrees(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        RandomCommittee classifier = new RandomCommittee();

        classifier.setNumExecutionSlots(20);
        classifier.setNumIterations(20);
        classifier.setClassifier(new ExtraTree());
        classifier.buildClassifier(train);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierBagging(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        FilteredClassifier filteredClassifier = new FilteredClassifier();
        filteredClassifier.setFilter(new ClassBalancer());

        Bagging classifier = new Bagging();

        classifier.setClassifier(new J48());
        classifier.setNumExecutionSlots(20);
        classifier.setNumIterations(20);
        classifier.buildClassifier(train);

        filteredClassifier.setClassifier(classifier);

        inputMapped.setClassifier(filteredClassifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public Classifier trainClassifierHoeffing(Instances train) throws Exception {
        InputMappedClassifier inputMapped = new InputMappedClassifier();
        inputMapped.setSuppressMappingReport(true);
        inputMapped.setModelHeader(train);

        //FilteredClassifier filteredClassifierBalancer = new FilteredClassifier();
        //filteredClassifierBalancer.setFilter(new ClassBalancer());

        weka.classifiers.trees.HoeffdingTree classifier = new weka.classifiers.trees.HoeffdingTree();

        //filteredClassifierBalancer.setClassifier(classifier);

        //inputMapped.setClassifier(filteredClassifierBalancer);
        inputMapped.setClassifier(classifier);
        inputMapped.buildClassifier(train);

        return inputMapped;
    }

    public moa.classifiers.AbstractClassifier trainClassifierHoeffingAdaptiveTreeMOA(Instances train) throws Exception {
        HoeffdingAdaptiveTree classifier = new HoeffdingAdaptiveTree();


        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(train);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (train.size() / 100.0f);
        for (int i = 0; i < train.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    //should be working now...
    //  there is an error with weka API that internally uses grace period + 1 instead of chosen grace period
    //  the fix here is to adjust grace period + 1 also to match weka results...
    //  actually the fix was to make a wekatreewrapper, we are internally using weka o.O
    public moa.classifiers.AbstractClassifier trainClassifierHoeffdingTreeMOA(Instances train) throws Exception {
        //moa.classifiers.AbstractClassifier classifier = new fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper();
        moa.classifiers.AbstractClassifier classifier = new moa.classifiers.trees.HoeffdingTree();

//a





       // ClassBalancer balancer = new ClassBalancer();
      //  balancer.setInputFormat(train);
      //  Instances newTrain = Filter.useFilter(train, balancer);

        SpreadSubsample subsample = new SpreadSubsample();
        subsample.setInputFormat(train);
        subsample.setDistributionSpread(1.0d);
        Instances newTrain = Filter.useFilter(train, subsample);

        Randomize randomTrain = new Randomize();
        randomTrain.setInputFormat(newTrain);
        newTrain = Filter.useFilter(newTrain, randomTrain);

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(newTrain);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (moaTrain.size() / 100.0f);
        for (int i = 0; i < moaTrain.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    //funcionando teoricamente...
    public moa.classifiers.AbstractClassifier trainClassifierOzaBaggingMOA(Instances train, int numberEnsemble) throws Exception {
        OzaBag classifier = new OzaBag();

        classifier.ensembleSizeOption = new IntOption("ensembleSize", 's',
                "The number of models to boost.", numberEnsemble, 1, Integer.MAX_VALUE);
        classifier.baseLearnerOption = new ClassOption("baseLearner", 'l',
                "Classifier to train.", fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper.class,
                "fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper");


        SpreadSubsample subsample = new SpreadSubsample();
        subsample.setInputFormat(train);
        subsample.setDistributionSpread(1.0d);
        Instances newTrain = Filter.useFilter(train, subsample);

        Randomize randomTrain = new Randomize();
        randomTrain.setInputFormat(newTrain);
        newTrain = Filter.useFilter(newTrain, randomTrain);

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(newTrain);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (moaTrain.size() / 100.0f);
        for (int i = 0; i < moaTrain.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    public moa.classifiers.AbstractClassifier trainClassifierOzaBoostingMOA(Instances train, int numberEnsemble) throws Exception {
        OzaBoost classifier = new OzaBoost();

        System.out.println("Ozaboost, training: " + numberEnsemble + " of models");

    //    ClassBalancer balancer = new ClassBalancer();
  //      balancer.setInputFormat(train);
//        Instances newTrain = Filter.useFilter(train, balancer);

        SpreadSubsample subsample = new SpreadSubsample();
        subsample.setInputFormat(train);
        subsample.setDistributionSpread(1.0d);
        Instances newTrain = Filter.useFilter(train, subsample);

        Randomize randomTrain = new Randomize();
        randomTrain.setInputFormat(newTrain);
        newTrain = Filter.useFilter(newTrain, randomTrain);

        classifier.ensembleSizeOption = new IntOption("ensembleSize", 's',
                "The number of models to boost.", numberEnsemble, 1, Integer.MAX_VALUE);

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(newTrain);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (moaTrain.size() / 100.0f);
        for (int i = 0; i < moaTrain.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    public moa.classifiers.AbstractClassifier trainClassifierAdaptiveRandomForestMOA(Instances train) throws Exception {

        AdaptiveRandomForest classifier = new AdaptiveRandomForest();

        classifier.ensembleSizeOption = new IntOption("ensembleSize", 's',
                "The number of trees.", 20, 1, Integer.MAX_VALUE);

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(train);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (train.size() / 100.0f);
        for (int i = 0; i < train.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    public moa.classifiers.AbstractClassifier trainClassifierAdaHoeffdingOptionTreeMOA(Instances train) throws Exception {
        AdaHoeffdingOptionTree classifier = new AdaHoeffdingOptionTree();

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(train);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (train.size() / 100.0f);
        for (int i = 0; i < train.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    //theoricaly it is working now
    public moa.classifiers.AbstractClassifier trainClassifierOCBoostMOA(Instances train, int nEnsemble) throws Exception {

        OCBoost classifier = new OCBoost();

        //ClassBalancer balancer = new ClassBalancer();
        //balancer.setInputFormat(train);
        //Instances newTrain = Filter.useFilter(train, balancer);
        SpreadSubsample subsample = new SpreadSubsample();
        subsample.setInputFormat(train);
        subsample.setDistributionSpread(1.0d);
        Instances newTrain = Filter.useFilter(train, subsample);

        Randomize randomTrain = new Randomize();
        randomTrain.setInputFormat(newTrain);
        newTrain = Filter.useFilter(newTrain, randomTrain);

        classifier.baseLearnerOption = new ClassOption("baseLearner", 'l',
                "Classifier to train.", fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper.class,
                "fcul.viegas.topologies.machinelearning.classifier.HoeffdingTreeWekaWrapper");
        classifier.ensembleSizeOption = new IntOption("ensembleSize", 's',
                "The number of models to boost.", nEnsemble, 1, Integer.MAX_VALUE);

        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(newTrain);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (moaTrain.size() / 100.0f);
        for (int i = 0; i < moaTrain.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    public moa.classifiers.AbstractClassifier trainClassifierLeveragingBagMOA(Instances train, int nEnsemble) throws Exception {
        LeveragingBag classifier = new LeveragingBag();


        classifier.ensembleSizeOption = new IntOption("ensembleSize", 's',
                "The number of models to boost.", nEnsemble, 1, Integer.MAX_VALUE);

        SpreadSubsample subsample = new SpreadSubsample();
        subsample.setInputFormat(train);
        subsample.setDistributionSpread(1.0d);
        Instances newTrain = Filter.useFilter(train, subsample);

        Randomize randomTrain = new Randomize();
        randomTrain.setInputFormat(newTrain);
        newTrain = Filter.useFilter(newTrain, randomTrain);


        WekaToSamoaInstanceConverter converter = new WekaToSamoaInstanceConverter();
        com.yahoo.labs.samoa.instances.Instances moaTrain = converter.samoaInstances(newTrain);
        InstancesHeader instH = new InstancesHeader(moaTrain);

        classifier.setModelContext(instH);
        classifier.prepareForUse();

        int pct = (int) (moaTrain.size() / 100.0f);
        for (int i = 0; i < moaTrain.size(); i++) {
            if (i % pct == 0) {
                System.out.println("Trained with " + (i / pct) + " % of training instances...");
            }
            classifier.trainOnInstance(moaTrain.get(i));
        }

        return classifier;
    }

    public String evaluateClassifier(String path, Classifier classifier) {
        try {
            Instances[] dataTest = this.openFileForTest(path);

            Evaluation evalNormal = new Evaluation(dataTest[0]);
            evalNormal.evaluateModel(classifier, dataTest[0]);

            Evaluation evalSuspicious = new Evaluation(dataTest[1]);
            evalSuspicious.evaluateModel(classifier, dataTest[1]);

            Evaluation evalAnomalous = new Evaluation(dataTest[2]);
            evalAnomalous.evaluateModel(classifier, dataTest[2]);

            String print = path + ";"
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
            return print.replace(",", ".");
        } catch (Exception ex) {
            return ex.getStackTrace().toString();
        }
    }


    public ArrayList<String> evaluateClassifierWithRejection(String path, Classifier classifier) {
        try {
            Instances dataTest = this.openFile(path);

            ArrayList<String> returnArray = new ArrayList<>();

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_EVEN);

            for (int normalT = 50; normalT <= 100; normalT += 5) {
                for (int attackT = 50; attackT <= 100; attackT += 5) {

                    float normalThreshold = normalT / 100.0f;
                    float attackThreshold = attackT / 100.0f;

                    int nNormal = 0; //ok
                    int nAttack = 0; //ok
                    int nRejectedNormal = 0; //ok
                    int nRejectedAttack = 0; //ok
                    int nAcceptedNormal = 0; //ok
                    int nAcceptedAttack = 0; //ok
                    int nCorrectlyAcceptedNormal = 0; //ok
                    int nCorrectlyAcceptedAttack = 0;
                    int nCorrectlyRejectedNormal = 0;
                    int nCorrectlyRejectedAttack = 0; //ok

                    for (Instance inst : dataTest) {

                        double prob[] = classifier.distributionForInstance(inst);

                        //if is normal
                        if (inst.classValue() == 0.0d) {
                            nNormal++;
                        } else {
                            //is attack
                            nAttack++;
                        }

                        //classified as normal
                        if (prob[0] > prob[1]) {
                            //if should accept
                            if (prob[0] >= normalThreshold) {
                                //if correctly classified
                                if (inst.classValue() == 0.0d) {
                                    nAcceptedNormal++;
                                    nCorrectlyAcceptedNormal++;
                                } else {
                                    //misclassified
                                    nAcceptedAttack++;
                                }
                            } else {
                                //check if correctly rejected
                                if (inst.classValue() != 0.0d) {
                                    nRejectedAttack++;
                                    nCorrectlyRejectedAttack++;
                                } else {
                                    //misrejected
                                    nRejectedNormal++;
                                }
                            }
                        } else {
                            //classified as attack
                            //if should accept
                            if (prob[1] >= attackThreshold) {
                                //correctly classified
                                if (inst.classValue() == 1.0d) {
                                    nAcceptedAttack++;
                                    nCorrectlyAcceptedAttack++;
                                } else {
                                    //misclassified
                                    nAcceptedNormal++;
                                }
                            } else {
                                //check if correctly rejected
                                if (inst.classValue() != 1.0d) {
                                    nRejectedNormal++;
                                    nCorrectlyRejectedNormal++;
                                } else {
                                    //misrejected
                                    nRejectedAttack++;
                                }
                            }

                        }
                    }

                    if (nRejectedNormal == 0) {
                        nRejectedNormal = 1;
                    }
                    if (nRejectedAttack == 0) {
                        nRejectedAttack = 1;
                    }
                    if (nAcceptedNormal == 0) {
                        nAcceptedNormal = 1;
                    }
                    if (nAcceptedAttack == 0) {
                        nAcceptedAttack = 1;
                    }

                    float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));
                    float corretamenteRej = ((nCorrectlyRejectedNormal + nCorrectlyRejectedAttack) / (float) (nRejectedNormal + nRejectedAttack));

                    String print = path + ";";
                    print = print + normalT + ";";
                    print = print + attackT + ";";
                    print = print + (dataTest.size()) + ";";
                    print = print + nNormal + ";";
                    print = print + nAttack + ";";
                    print = print + nAttack + ";";
                    print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
                    print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
                    print = print + accAceito + ";";
                    print = print + (nCorrectlyRejectedNormal / (float) nRejectedNormal) + ";";
                    print = print + (nCorrectlyRejectedAttack / (float) nRejectedAttack) + ";";
                    print = print + corretamenteRej + ";";
                    print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
                    print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
                    print = print + (((nCorrectlyAcceptedAttack + nCorrectlyAcceptedNormal) + (nCorrectlyRejectedAttack + nCorrectlyRejectedNormal)) / (float) (nNormal + nAttack)) + ";";
                    print = print + ((nRejectedAttack) / (float) nAttack) + ";";
                    print = print + ((nRejectedNormal) / (float) nNormal);

                    returnArray.add(print.replace(",", "."));
                }
            }

            return returnArray;

        } catch (Exception ex) {
            return null;
        }
    }

    public String evaluateClassifierWithRejectionThroughConformal(String path,
            Classifier classifier,
            Transcend_ConformalPredictor conformalEvaluator,
            float normalThreshold,
            float attackThreshold,
            String featureSet) {
        try {
            Instances dataTest = this.openFile(path);

            dataTest = this.getAsNormalizeFeatures(dataTest);
            if (featureSet.equals("VIEGAS")) {
                dataTest = this.removeParticularAttributesViegas(dataTest);
            } else if (featureSet.equals("ORUNADA")) {
                dataTest = this.removeParticularAttributesOrunada(dataTest);
            }

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_EVEN);

            int nNormal = 0; //ok
            int nAttack = 0; //ok
            int nRejectedNormal = 0; //ok
            int nRejectedAttack = 0; //ok
            int nAcceptedNormal = 0; //ok
            int nAcceptedAttack = 0; //ok
            int nCorrectlyAcceptedNormal = 0; //ok
            int nCorrectlyAcceptedAttack = 0;
            int nCorrectlyRejectedNormal = 0;
            int nCorrectlyRejectedAttack = 0; //ok

            for (Instance inst : dataTest) {

                double prob[] = classifier.distributionForInstance(inst);

                double alpha;
                double confidence;
                double credibility;

                //if is normal
                if (inst.classValue() == 0.0d) {
                    nNormal++;
                } else {
                    //is attack
                    nAttack++;
                }

                //classified as normal
                if (prob[0] > prob[1]) {
                    //if should accept
                    credibility = conformalEvaluator.getPValueForNormal(inst);
                    confidence = 1.0f - conformalEvaluator.getPValueForAttack(inst);
                    alpha = credibility + confidence;

                    if (alpha >= normalThreshold) {
                        //if correctly classified
                        if (inst.classValue() == 0.0d) {
                            nAcceptedNormal++;
                            nCorrectlyAcceptedNormal++;
                        } else {
                            //misclassified
                            nAcceptedAttack++;
                        }
                    } else {
                        //check if correctly rejected
                        if (inst.classValue() != 0.0d) {
                            nRejectedAttack++;
                            nCorrectlyRejectedAttack++;
                        } else {
                            //misrejected
                            nRejectedNormal++;
                        }
                    }
                } else {
                    //classified as attack
                    //if should accept
                    credibility = conformalEvaluator.getPValueForAttack(inst);
                    confidence = 1.0f - conformalEvaluator.getPValueForNormal(inst);
                    alpha = credibility + confidence;

                    if (alpha >= attackThreshold) {
                        //correctly classified
                        if (inst.classValue() == 1.0d) {
                            nAcceptedAttack++;
                            nCorrectlyAcceptedAttack++;
                        } else {
                            //misclassified
                            nAcceptedNormal++;
                        }
                    } else {
                        //check if correctly rejected
                        if (inst.classValue() != 1.0d) {
                            nRejectedNormal++;
                            nCorrectlyRejectedNormal++;
                        } else {
                            //misrejected
                            nRejectedAttack++;
                        }
                    }

                }
            }

            if (nRejectedNormal == 0) {
                nRejectedNormal = 1;
            }
            if (nRejectedAttack == 0) {
                nRejectedAttack = 1;
            }
            if (nAcceptedNormal == 0) {
                nAcceptedNormal = 1;
            }
            if (nAcceptedAttack == 0) {
                nAcceptedAttack = 1;
            }

            float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));
            float corretamenteRej = ((nCorrectlyRejectedNormal + nCorrectlyRejectedAttack) / (float) (nRejectedNormal + nRejectedAttack));

            String print = path + ";";
            print = print + normalThreshold + ";";
            print = print + attackThreshold + ";";
            print = print + (dataTest.size()) + ";";
            print = print + nNormal + ";";
            print = print + nAttack + ";";
            print = print + nAttack + ";";
            print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
            print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
            print = print + accAceito + ";";
            print = print + (nCorrectlyRejectedNormal / (float) nRejectedNormal) + ";";
            print = print + (nCorrectlyRejectedAttack / (float) nRejectedAttack) + ";";
            print = print + corretamenteRej + ";";
            print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
            print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
            print = print + (((nCorrectlyAcceptedAttack + nCorrectlyAcceptedNormal) + (nCorrectlyRejectedAttack + nCorrectlyRejectedNormal)) / (float) (nNormal + nAttack)) + ";";
            print = print + ((nRejectedAttack) / (float) nAttack) + ";";
            print = print + ((nRejectedNormal) / (float) nNormal);

            return print;

        } catch (Exception ex) {
            return null;
        }
    }

    //0 rejected, 1 correctly classified, 2 incorrectly classified
    private int evaluateInstanceConformalCascade(
            int indexClassifier,
            WekaMoaClassifierWrapper wekaMoa,
            Instance instanceViegas,
            Instance instanceNigel,
            Instance instanceMoore,
            Instance instanceOrunada) throws Exception {
        if (indexClassifier < wekaMoa.getWekaClassifiers().size()) {
            Classifier classifier = wekaMoa.getWekaClassifiers().get(indexClassifier);
            Transcend_ConformalPredictor conformalPredictor = null;
            Instance inst = null;

            //se nao for A nem B vai dar pau....
            if (wekaMoa.getFeatureSetToLookWeka().get(indexClassifier).equals("VIEGAS")) {
                inst = instanceViegas;
                conformalPredictor = wekaMoa.getConformalEvaluatorVIEGAS();
            } else if (wekaMoa.getFeatureSetToLookWeka().get(indexClassifier).equals("NIGEL")) {
                inst = instanceNigel;
                conformalPredictor = wekaMoa.getConformalEvaluatorNIGEL();
            } else if (wekaMoa.getFeatureSetToLookWeka().get(indexClassifier).equals("MOORE")) {
                inst = instanceMoore;
                conformalPredictor = wekaMoa.getConformalEvaluatorMOORE();
            } else if (wekaMoa.getFeatureSetToLookWeka().get(indexClassifier).equals("ORUNADA")) {
                inst = instanceOrunada;
                conformalPredictor = wekaMoa.getConformalEvaluatorORUNADA();
            }

            double prob[] = classifier.distributionForInstance(inst);

            double alpha;
            double confidence;
            double credibility;
            double normalThreshold = wekaMoa.getWekaOperationPoints().get(indexClassifier).getNormalThreshold();
            double attackThreshold = wekaMoa.getWekaOperationPoints().get(indexClassifier).getAttackThreshold();

            //classified as normal
            if (prob[0] > prob[1]) {
                //if should accept
                credibility = conformalPredictor.getPValueForNormal(inst);
                confidence = 1.0f - conformalPredictor.getPValueForAttack(inst);
                alpha = credibility + confidence;

                if (alpha >= normalThreshold) {
                    //if correctly classified
                    if (inst.classValue() == 0.0d) {
                        //correctly classified
                        return 1;
                    } else {
                        //incorrectly classified
                        return 2;
                    }
                } else {
                    //check if correctly rejected
                    if (inst.classValue() != 0.0d) {
                        return evaluateInstanceConformalCascade(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada);
                    } else {
                        //misrejected
                        return evaluateInstanceConformalCascade(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada);
                    }
                }
            } else {
                //classified as attack
                //if should accept
                credibility = conformalPredictor.getPValueForAttack(inst);
                confidence = 1.0f - conformalPredictor.getPValueForNormal(inst);
                alpha = credibility + confidence;

                if (alpha >= attackThreshold) {
                    //correctly classified
                    if (inst.classValue() == 1.0d) {
                        //correctly classified
                        return 1;
                    } else {
                        //incorrectly classified
                        return 2;
                    }
                } else {
                    //check if correctly rejected
                    if (inst.classValue() != 1.0d) {
                        return evaluateInstanceConformalCascade(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada);
                    } else {
                        //misrejected
                        return evaluateInstanceConformalCascade(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada);
                    }
                }
            }
        }
        return 0;
    }

    public String evaluateClassifierWithRejectionThroughConformalAndCascade(String[] arffPaths, WekaMoaClassifierWrapper wekaMoa) throws Exception {

        boolean willUseViegas = false;
        boolean willUseNigel = false;
        boolean willUseMoore = false;
        boolean willUseOrunada = false;

        for (int i = 0; i < wekaMoa.getFeatureSetToLookWeka().size(); i++) {
            if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        for (int i = 0; i < wekaMoa.getFeatureSetToLookMoa().size(); i++) {
            if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        Instances dataTestVIEGAS = (willUseViegas) ? this.openFile(arffPaths[0]) : null;
        Instances dataTestNIGEL = (willUseNigel) ? this.openFile(arffPaths[1]) : null;
        Instances dataTestMOORE = (willUseMoore) ? this.openFile(arffPaths[2]) : null;
        Instances dataTestORUNADA = (willUseOrunada) ? this.openFile(arffPaths[3]) : null;

        dataTestVIEGAS = this.getAsNormalizeFeatures(dataTestVIEGAS);
        dataTestNIGEL = this.getAsNormalizeFeatures(dataTestNIGEL);
        dataTestMOORE = this.getAsNormalizeFeatures(dataTestMOORE);
        dataTestORUNADA = this.getAsNormalizeFeatures(dataTestORUNADA);

        dataTestVIEGAS = this.removeParticularAttributesViegas(dataTestVIEGAS);
        dataTestORUNADA = this.removeParticularAttributesOrunada(dataTestORUNADA);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        int nNormal = 0; //ok
        int nAttack = 0; //ok
        int nRejectedNormal = 0; //ok
        int nRejectedAttack = 0; //ok
        int nAcceptedNormal = 0; //ok
        int nAcceptedAttack = 0; //ok
        int nCorrectlyAcceptedNormal = 0; //ok
        int nCorrectlyAcceptedAttack = 0;

        for (int i = 0; i < dataTestVIEGAS.size(); i++) {
            Instance instViegas = (willUseViegas) ? dataTestVIEGAS.get(i) : null;
            Instance instMoore = (willUseMoore) ? dataTestMOORE.get(i) : null;
            Instance instNigel = (willUseNigel) ? dataTestNIGEL.get(i) : null;
            Instance instOrunada = (willUseOrunada) ? dataTestORUNADA.get(i) : null;

            int decision = this.evaluateInstanceConformalCascade(0, wekaMoa, instViegas, instNigel, instMoore, instOrunada);

            if (instViegas.classValue() == 0.0d) {
                nNormal++;
            } else {
                //is attack
                nAttack++;
            }

            if (decision == 0) {
                if (instViegas.classValue() == 0.0d) {
                    nRejectedNormal++;
                } else {
                    nRejectedAttack++;
                }
            } else {
                if (instViegas.classValue() == 0.0d) {
                    nAcceptedNormal++;
                    if (decision == 1) {
                        nCorrectlyAcceptedNormal++;
                    }
                } else {
                    nAcceptedAttack++;
                    if (decision == 1) {
                        nCorrectlyAcceptedAttack++;
                    }
                }
            }
        }

        if (nRejectedNormal == 0) {
            nRejectedNormal = 1;
        }
        if (nRejectedAttack == 0) {
            nRejectedAttack = 1;
        }
        if (nAcceptedNormal == 0) {
            nAcceptedNormal = 1;
        }
        if (nAcceptedAttack == 0) {
            nAcceptedAttack = 1;
        }

        float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));

        String print = arffPaths[0] + ";";
        print = print + arffPaths[1] + ";";
        print = print + arffPaths[2] + ";";
        print = print + arffPaths[3] + ";";
        print = print + (dataTestVIEGAS.size()) + ";";
        print = print + nNormal + ";";
        print = print + nAttack + ";";
        print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
        print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
        print = print + accAceito + ";";
        print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
        print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
        print = print + ((nRejectedAttack) / (float) nAttack) + ";";
        print = print + ((nRejectedNormal) / (float) nNormal);

        return print;
    }

    private int evaluateInstanceConformalCascadeStream(
            int indexClassifier,
            WekaMoaClassifierWrapper wekaMoa,
            Instance instanceViegas,
            Instance instanceNigel,
            Instance instanceMoore,
            Instance instanceOrunada,
            com.yahoo.labs.samoa.instances.Instance instanceMoaViegas,
            com.yahoo.labs.samoa.instances.Instance instanceMoaNigel,
            com.yahoo.labs.samoa.instances.Instance instanceMoaMoore,
            com.yahoo.labs.samoa.instances.Instance instanceMoaOrunada) throws Exception {
        if (indexClassifier < wekaMoa.getMoaClassifiers().size()) {

            moa.classifiers.AbstractClassifier classifier = wekaMoa.getMoaClassifiers().get(indexClassifier);
            Transcend_ConformalPredictor conformalPredictor = null;
            com.yahoo.labs.samoa.instances.Instance instMoa = null;
            Instance instWeka = null;

            //se nao for A nem B vai dar pau....
            if (wekaMoa.getFeatureSetToLookMoa().get(indexClassifier).equals("VIEGAS")) {
                instMoa = instanceMoaViegas;
                instWeka = instanceViegas;
                conformalPredictor = wekaMoa.getConformalEvaluatorVIEGAS();
            } else if (wekaMoa.getFeatureSetToLookMoa().get(indexClassifier).equals("NIGEL")) {
                instMoa = instanceMoaNigel;
                instWeka = instanceNigel;
                conformalPredictor = wekaMoa.getConformalEvaluatorNIGEL();
            } else if (wekaMoa.getFeatureSetToLookMoa().get(indexClassifier).equals("MOORE")) {
                instMoa = instanceMoaMoore;
                instWeka = instanceMoore;
                conformalPredictor = wekaMoa.getConformalEvaluatorMOORE();
            } else if (wekaMoa.getFeatureSetToLookMoa().get(indexClassifier).equals("ORUNADA")) {
                instMoa = instanceMoaOrunada;
                instWeka = instanceOrunada;
                conformalPredictor = wekaMoa.getConformalEvaluatorORUNADA();
            }

//            double prob[] = classifier.getVotesForInstance(instMoa);
            boolean correclyClassifies = classifier.correctlyClassifies(instMoa);
            double classifiedClass = (correclyClassifies) ? instMoa.classValue() : (instMoa.classValue() == 0.0d) ? 1.0d : 0.0d;

//            if(prob.length == 0){
//                System.out.println("a");
//            }
            double alpha;
            double confidence;
            double credibility;
            double normalThreshold = wekaMoa.getMoaOperationPoints().get(indexClassifier).getNormalThreshold();
            double attackThreshold = wekaMoa.getMoaOperationPoints().get(indexClassifier).getAttackThreshold();

            //classified as normal
            if (classifiedClass == 0.0d) {
                //if should accept
                if (!(normalThreshold <= 0.0001d && attackThreshold <= 0.0001d)) {
                    credibility = conformalPredictor.getPValueForNormal(instWeka);
                    confidence = 1.0f - conformalPredictor.getPValueForAttack(instWeka);
                    alpha = credibility + confidence;
                } else {
                    alpha = 0;
                }

                if (alpha >= normalThreshold) {
                    //if correctly classified
                    if (instMoa.classValue() == 0.0d) {
                        //correctly classified
                        return 1;
                    } else {
                        //incorrectly classified
                        return 2;
                    }
                } else {
                    //check if correctly rejected
                    if (instMoa.classValue() != 0.0d) {
                        return evaluateInstanceConformalCascadeStream(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada,
                                instanceMoaViegas, instanceMoaNigel, instanceMoaMoore, instanceMoaOrunada);
                    } else {
                        //misrejected
                        return evaluateInstanceConformalCascadeStream(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada,
                                instanceMoaViegas, instanceMoaNigel, instanceMoaMoore, instanceMoaOrunada);
                    }
                }
            } else {
                //classified as attack
                //if should accept
                if (!(normalThreshold <= 0.0001d && attackThreshold <= 0.0001d)) {
                    credibility = conformalPredictor.getPValueForAttack(instWeka);
                    confidence = 1.0f - conformalPredictor.getPValueForNormal(instWeka);
                    alpha = credibility + confidence;
                } else {
                    alpha = 0;
                }

                if (alpha >= attackThreshold) {
                    //correctly classified
                    if (instMoa.classValue() == 1.0d) {
                        //correctly classified
                        return 1;
                    } else {
                        //incorrectly classified
                        return 2;
                    }
                } else {
                    //check if correctly rejected
                    if (instMoa.classValue() != 1.0d) {
                        return evaluateInstanceConformalCascadeStream(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada,
                                instanceMoaViegas, instanceMoaNigel, instanceMoaMoore, instanceMoaOrunada);
                    } else {
                        //misrejected
                        return evaluateInstanceConformalCascadeStream(indexClassifier + 1, wekaMoa, instanceViegas, instanceNigel, instanceMoore, instanceOrunada,
                                instanceMoaViegas, instanceMoaNigel, instanceMoaMoore, instanceMoaOrunada);
                    }
                }
            }
        }
        return 0;
    }

    public String evaluateClassifierWithRejectionThroughConformalHybrid(
            String[] arffPaths,
            WekaMoaClassifierWrapper wekaMoa) throws Exception {

        boolean willUseViegas = false;
        boolean willUseNigel = false;
        boolean willUseMoore = false;
        boolean willUseOrunada = false;

        for (int i = 0; i < wekaMoa.getFeatureSetToLookWeka().size(); i++) {
            if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        for (int i = 0; i < wekaMoa.getFeatureSetToLookMoa().size(); i++) {
            if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        Instances dataTestVIEGAS = (willUseViegas) ? this.openFile(arffPaths[0]) : null;
        Instances dataTestNIGEL = (willUseNigel) ? this.openFile(arffPaths[1]) : null;
        Instances dataTestMOORE = (willUseMoore) ? this.openFile(arffPaths[2]) : null;
        Instances dataTestORUNADA = (willUseOrunada) ? this.openFile(arffPaths[3]) : null;

        dataTestVIEGAS = this.getAsNormalizeFeatures(dataTestVIEGAS);
        dataTestNIGEL = this.getAsNormalizeFeatures(dataTestNIGEL);
        dataTestMOORE = this.getAsNormalizeFeatures(dataTestMOORE);
        dataTestORUNADA = this.getAsNormalizeFeatures(dataTestORUNADA);

        dataTestVIEGAS = this.removeParticularAttributesViegas(dataTestVIEGAS);
        dataTestORUNADA = this.removeParticularAttributesOrunada(dataTestORUNADA);

        WekaToSamoaInstanceConverter converterViegas = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterMoore = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterNigel = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterOrunada = new WekaToSamoaInstanceConverter();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        int nNormal = 0; //ok
        int nAttack = 0; //ok
        int nRejectedNormal = 0; //ok
        int nRejectedAttack = 0; //ok
        int nAcceptedNormal = 0; //ok
        int nAcceptedAttack = 0; //ok
        int nCorrectlyAcceptedNormal = 0; //ok
        int nCorrectlyAcceptedAttack = 0;

        int nInstances =    (dataTestVIEGAS == null) ?
                (dataTestMOORE == null) ?
                        (dataTestNIGEL == null) ?
                                dataTestORUNADA.size() :
                                dataTestNIGEL.size() :
                        dataTestMOORE.size() :
                dataTestVIEGAS.size();

        for (int i = 0; i < nInstances; i++) {

            com.yahoo.labs.samoa.instances.Instance instMoaViegas = (willUseViegas) ? converterViegas.samoaInstance(dataTestVIEGAS.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaMoore = (willUseMoore) ? converterMoore.samoaInstance(dataTestMOORE.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaNigel = (willUseNigel) ? converterNigel.samoaInstance(dataTestNIGEL.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaOrunada = (willUseOrunada) ? converterOrunada.samoaInstance(dataTestORUNADA.get(i)) : null;

            Instance instViegas = (willUseViegas) ? dataTestVIEGAS.get(i) : null;
            Instance instMoore = (willUseMoore) ? dataTestMOORE.get(i) : null;
            Instance instNigel = (willUseNigel) ? dataTestNIGEL.get(i) : null;
            Instance instOrunada = (willUseOrunada) ? dataTestORUNADA.get(i) : null;

            int decision = this.evaluateInstanceConformalCascade(0, wekaMoa, instViegas, instNigel, instMoore, instOrunada);

            if (decision == 0) {
                decision = this.evaluateInstanceConformalCascadeStream(0, wekaMoa,
                        instViegas, instNigel, instMoore, instOrunada,
                        instMoaViegas, instMoaNigel, instMoaMoore, instMoaOrunada);
            }

            double classValue =    (dataTestVIEGAS == null) ?
                    (dataTestMOORE == null) ?
                            (dataTestNIGEL == null) ?
                                    instOrunada.classValue() :
                                    instNigel.classValue() :
                            instMoore.classValue() :
                    instViegas.classValue();

            if (classValue == 0.0d) {
                nNormal++;
            } else {
                //is attack
                nAttack++;
            }

            if (decision == 0) {
                if (classValue == 0.0d) {
                    nRejectedNormal++;
                } else {
                    nRejectedAttack++;
                }
            } else {
                if (classValue == 0.0d) {
                    nAcceptedNormal++;
                    if (decision == 1) {
                        nCorrectlyAcceptedNormal++;
                    }
                } else {
                    nAcceptedAttack++;
                    if (decision == 1) {
                        nCorrectlyAcceptedAttack++;
                    }
                }
            }
        }

        if (nRejectedNormal == 0) {
            nRejectedNormal = 1;
        }
        if (nRejectedAttack == 0) {
            nRejectedAttack = 1;
        }
        if (nAcceptedNormal == 0) {
            nAcceptedNormal = 1;
        }
        if (nAcceptedAttack == 0) {
            nAcceptedAttack = 1;
        }

        float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));



        String print = arffPaths[0] + ";";
        print = print + arffPaths[1] + ";";
        print = print + arffPaths[2] + ";";
        print = print + arffPaths[3] + ";";
        print = print + (nInstances) + ";";
        print = print + nNormal + ";";
        print = print + nAttack + ";";
        print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
        print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
        print = print + accAceito + ";";
        print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
        print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
        print = print + ((nRejectedAttack) / (float) nAttack) + ";";
        print = print + ((nRejectedNormal) / (float) nNormal);

        dataTestMOORE = null;
        dataTestNIGEL = null;
        dataTestORUNADA = null;
        dataTestVIEGAS = null;

        System.gc();

        return print;
    }

    public String evaluateClassifierWithRejectionThroughConformalHybridEnsemble(
            String[] arffPaths,
            WekaMoaClassifierWrapper wekaMoa) throws Exception {

        boolean willUseViegas = false;
        boolean willUseNigel = false;
        boolean willUseMoore = false;
        boolean willUseOrunada = false;

        for (int i = 0; i < wekaMoa.getFeatureSetToLookWeka().size(); i++) {
            if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookWeka().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        for (int i = 0; i < wekaMoa.getFeatureSetToLookMoa().size(); i++) {
            if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("VIEGAS")) {
                willUseViegas = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("NIGEL")) {
                willUseNigel = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("MOORE")) {
                willUseMoore = true;
            } else if (wekaMoa.getFeatureSetToLookMoa().get(i).equals("ORUNADA")) {
                willUseOrunada = true;
            }
        }

        Instances dataTestVIEGAS = (willUseViegas) ? this.openFile(arffPaths[0]) : null;
        Instances dataTestNIGEL = (willUseNigel) ? this.openFile(arffPaths[1]) : null;
        Instances dataTestMOORE = (willUseMoore) ? this.openFile(arffPaths[2]) : null;
        Instances dataTestORUNADA = (willUseOrunada) ? this.openFile(arffPaths[3]) : null;

        dataTestVIEGAS = this.getAsNormalizeFeatures(dataTestVIEGAS);
        dataTestNIGEL = this.getAsNormalizeFeatures(dataTestNIGEL);
        dataTestMOORE = this.getAsNormalizeFeatures(dataTestMOORE);
        dataTestORUNADA = this.getAsNormalizeFeatures(dataTestORUNADA);

        dataTestVIEGAS = this.removeParticularAttributesViegas(dataTestVIEGAS);
        dataTestORUNADA = this.removeParticularAttributesOrunada(dataTestORUNADA);

        WekaToSamoaInstanceConverter converterViegas = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterMoore = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterNigel = new WekaToSamoaInstanceConverter();
        WekaToSamoaInstanceConverter converterOrunada = new WekaToSamoaInstanceConverter();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        int nNormal = 0; //ok
        int nAttack = 0; //ok
        int nRejectedNormal = 0; //ok
        int nRejectedAttack = 0; //ok
        int nAcceptedNormal = 0; //ok
        int nAcceptedAttack = 0; //ok
        int nCorrectlyAcceptedNormal = 0; //ok
        int nCorrectlyAcceptedAttack = 0;

        int nInstances =    (dataTestVIEGAS == null) ?
                (dataTestMOORE == null) ?
                        (dataTestNIGEL == null) ?
                                dataTestORUNADA.size() :
                                dataTestNIGEL.size() :
                        dataTestMOORE.size() :
                dataTestVIEGAS.size();

        for (int i = 0; i < nInstances; i++) {

            com.yahoo.labs.samoa.instances.Instance instMoaViegas = (willUseViegas) ? converterViegas.samoaInstance(dataTestVIEGAS.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaMoore = (willUseMoore) ? converterMoore.samoaInstance(dataTestMOORE.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaNigel = (willUseNigel) ? converterNigel.samoaInstance(dataTestNIGEL.get(i)) : null;
            com.yahoo.labs.samoa.instances.Instance instMoaOrunada = (willUseOrunada) ? converterOrunada.samoaInstance(dataTestORUNADA.get(i)) : null;

            Instance instViegas = (willUseViegas) ? dataTestVIEGAS.get(i) : null;
            Instance instMoore = (willUseMoore) ? dataTestMOORE.get(i) : null;
            Instance instNigel = (willUseNigel) ? dataTestNIGEL.get(i) : null;
            Instance instOrunada = (willUseOrunada) ? dataTestORUNADA.get(i) : null;


            int nClassifiedAsAttack = 0;
            int nClassifiedAsNormal = 0;
            for(int j = 0; j < wekaMoa.getMoaClassifiers().size(); j++){
                moa.classifiers.AbstractClassifier classifier = wekaMoa.getMoaClassifiers().get(j);

                boolean correclyClassifies = classifier.correctlyClassifies(instMoaViegas);
                double classifiedClass = (correclyClassifies) ? instMoaViegas.classValue() : (instMoaViegas.classValue() == 0.0d) ? 1.0d : 0.0d;

                if(classifiedClass == 0.0d){
                    nClassifiedAsNormal++;
                }else{
                    nClassifiedAsAttack++;
                }
            }


            int decision = 0;
            if(nClassifiedAsNormal > nClassifiedAsAttack){
                if(instMoaViegas.classValue() == 0.0d){
                    decision = 1;
                }else{
                    decision = 2;
                }
            }else{
                if(instMoaViegas.classValue() == 1.0d){
                    decision = 1;
                }else{
                    decision = 2;
                }
            }


            double classValue =    (dataTestVIEGAS == null) ?
                    (dataTestMOORE == null) ?
                            (dataTestNIGEL == null) ?
                                    instOrunada.classValue() :
                                    instNigel.classValue() :
                            instMoore.classValue() :
                    instViegas.classValue();

            if (classValue == 0.0d) {
                nNormal++;
            } else {
                //is attack
                nAttack++;
            }

            if (decision == 0) {
                if (classValue == 0.0d) {
                    nRejectedNormal++;
                } else {
                    nRejectedAttack++;
                }
            } else {
                if (classValue == 0.0d) {
                    nAcceptedNormal++;
                    if (decision == 1) {
                        nCorrectlyAcceptedNormal++;
                    }
                } else {
                    nAcceptedAttack++;
                    if (decision == 1) {
                        nCorrectlyAcceptedAttack++;
                    }
                }
            }
        }

        if (nRejectedNormal == 0) {
            nRejectedNormal = 1;
        }
        if (nRejectedAttack == 0) {
            nRejectedAttack = 1;
        }
        if (nAcceptedNormal == 0) {
            nAcceptedNormal = 1;
        }
        if (nAcceptedAttack == 0) {
            nAcceptedAttack = 1;
        }

        float accAceito = ((nCorrectlyAcceptedNormal + nCorrectlyAcceptedAttack) / (float) (nAcceptedNormal + nAcceptedAttack));



        String print = arffPaths[0] + ";";
        print = print + arffPaths[1] + ";";
        print = print + arffPaths[2] + ";";
        print = print + arffPaths[3] + ";";
        print = print + (nInstances) + ";";
        print = print + nNormal + ";";
        print = print + nAttack + ";";
        print = print + (nCorrectlyAcceptedNormal / (float) nAcceptedNormal) + ";";
        print = print + (nCorrectlyAcceptedAttack / (float) nAcceptedAttack) + ";";
        print = print + accAceito + ";";
        print = print + ((nRejectedNormal + nRejectedAttack) / (float) (nNormal + nAttack)) + ";";
        print = print + ((((nCorrectlyAcceptedNormal / (float) nAcceptedNormal)) + ((nCorrectlyAcceptedAttack / (float) nAcceptedAttack))) / 2.0f) + ";";
        print = print + ((nRejectedAttack) / (float) nAttack) + ";";
        print = print + ((nRejectedNormal) / (float) nNormal);

        dataTestMOORE = null;
        dataTestNIGEL = null;
        dataTestORUNADA = null;
        dataTestVIEGAS = null;

        System.gc();

        return print;
    }

}
