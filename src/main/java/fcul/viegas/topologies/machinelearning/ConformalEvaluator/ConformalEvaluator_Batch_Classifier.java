package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ConformalEvaluator_Batch_Classifier {

    private ConformalEvaluator_Batch_Features conformalFeaturesEvaluator;
    private ConformalEvaluator_Batch_Transcend conformalTranscend;
    private Instance normalInstanceFormat;
    private Instance attackInstanceFormat;
    private Classifier normalClassifier;
    private Classifier attackClassifier;

    public ConformalEvaluator_Batch_Classifier() {

    }

    public void buildEvaluator(Instances dataTrain, double[] classGivenByClassifier, double[] probabilities) throws Exception {

        //this.conformalTranscend = new ConformalEvaluator_Batch_Transcend(new ConformalEvaluator_BatchClassifier_RandomForest(1000, 100));
        this.conformalTranscend = new ConformalEvaluator_Batch_Transcend(new ConformalEvaluator_BatchClassifier_NaiveBayes(false));
        this.conformalFeaturesEvaluator = new ConformalEvaluator_Batch_Features();

        System.out.println("ConformalEvaluator_Batch_Classifier - Building feature evaluator...");
        this.conformalFeaturesEvaluator.buildConformalEvaluator_Batch_Features(dataTrain);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building conformal evaluator...");
        this.conformalTranscend.buildConformal(dataTrain);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building feature for classifiers...");
        ArrayList<double[]> instancesConformalNormal = new ArrayList<>();
        ArrayList<double[]> instancesConformalAttack = new ArrayList<>();
        List<double[]> listValuesThreadedNormal = Collections.synchronizedList(new ArrayList<double[]>());
        List<double[]> listValuesThreadedAttack = Collections.synchronizedList(new ArrayList<double[]>());


        class TestClass implements Runnable {
            int i;
            int iUpper;

            TestClass(int i, int iUpper) {
                this.i = i;
                this.iUpper = iUpper;
            }

            public void run() {
                try {
                    int pct = 0;
                    for (int k = i; k < iUpper; k++) {
                        if (k >= i) {
                            if (k % ((dataTrain.size()) / 100) == 0) {
                                pct++;
                                System.out.println("\tConformalEvaluator_Batch_Classifier " + pct + "% ...[" + k + "/" + iUpper + "]");
                            }
                        }

                        Instance inst = dataTrain.get(k);

                        double[] featureConformal = conformalFeaturesEvaluator.getFeatureStatistics(inst, classGivenByClassifier[i]);
                        double[] featureConformalOther = conformalFeaturesEvaluator.getFeatureStatistics(inst, (classGivenByClassifier[i] == 0.0d) ? 1.0d : 0.0d);
                        double[] featVec = new double[featureConformal.length + featureConformalOther.length + 5];

                        int j = 0;
                        for (j = 0; j < featureConformal.length; j++) {
                            featVec[j] = featureConformal[j];
                        }
                        for (; j < (featureConformal.length + featureConformalOther.length); j++) {
                            featVec[j] = featureConformalOther[j - featureConformal.length];
                        }
                        featVec[j] = probabilities[k];
                        j++;
                        if (Double.compare(classGivenByClassifier[k], 0.0d) == 0) {
                            featVec[j] = conformalTranscend.getPValueForNormal(inst);
                            j++;
                            featVec[j] = conformalTranscend.getPValueForAttack(inst);
                            j++;
                            featVec[j] = conformalTranscend.getNonConformity(inst, 0.0d);
                            j++;

                            if (Double.compare(inst.classValue(), classGivenByClassifier[k]) == 0) {
                                featVec[j] = 0.0d;
                            } else {
                                featVec[j] = 1.0d;
                            }

                            listValuesThreadedNormal.add(featVec);
                        } else {
                            featVec[j] = conformalTranscend.getPValueForAttack(inst);
                            j++;
                            featVec[j] = conformalTranscend.getPValueForNormal(inst);
                            j++;
                            featVec[j] = conformalTranscend.getNonConformity(inst, 1.0d);
                            j++;

                            if (Double.compare(inst.classValue(), classGivenByClassifier[k]) == 0) {
                                featVec[j] = 0.0d;
                            } else {
                                featVec[j] = 1.0d;
                            }

                            listValuesThreadedAttack.add(featVec);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


        ArrayList<Thread> threads = new ArrayList<>();
        int jump = dataTrain.size() / 20;
        int start = 0;
        for (int nThreads = 0; nThreads < 20; nThreads++) {
            if (nThreads + 1 == 20) {
                Thread t = new Thread(new TestClass(start, dataTrain.size()));
                t.start();
                threads.add(t);
                start += jump;
            } else {
                Thread t = new Thread(new TestClass(start, start + jump));
                t.start();
                threads.add(t);
                start += jump;
            }
        }

        for (Thread t : threads) {
            t.join();
        }

        for (double[] d : listValuesThreadedNormal) {
            instancesConformalNormal.add(d);
        }
        for (double[] d : listValuesThreadedAttack) {
            instancesConformalAttack.add(d);
        }

        System.out.println("ConformalEvaluator_Batch_Classifier - NormalInstances: " + instancesConformalNormal.size());
        System.out.println("ConformalEvaluator_Batch_Classifier - AttackInstances: " + instancesConformalAttack.size());


        ArrayList<Attribute> atts = new ArrayList<Attribute>(instancesConformalNormal.get(0).length);
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("correct");
        classVal.add("wrong");

        for (int i = 0; i < instancesConformalNormal.get(0).length - 1; i++) {
            atts.add(new Attribute("feat_" + i));
        }

        atts.add(new Attribute("class", classVal));

        Instances dataNormal = new Instances("datasetNormal", atts, 0);
        Instances dataAttack = new Instances("datasetAttack", atts, 0);

        for (double[] features : instancesConformalNormal) {
            dataNormal.add(new DenseInstance(1.0d, features));
        }

        for (double[] features : instancesConformalAttack) {
            dataAttack.add(new DenseInstance(1.0d, features));
        }

        dataNormal.setClassIndex(dataNormal.numAttributes() - 1);
        dataAttack.setClassIndex(dataAttack.numAttributes() - 1);


        this.attackInstanceFormat = dataAttack.get(0);
        this.normalInstanceFormat = dataNormal.get(0);
        this.attackInstanceFormat.setDataset(dataAttack);
        this.normalInstanceFormat.setDataset(dataNormal);
        //a
/*
        ArffSaver saverNormal = new ArffSaver();
        saverNormal.setInstances(dataNormal);
        saverNormal.setFile(new File("normal_more_feats.arff"));
        saverNormal.writeBatch();

        ArffSaver saverAttack = new ArffSaver();
        saverAttack.setInstances(dataAttack);
        saverAttack.setFile(new File("attack_more_feats.arff"));
        saverAttack.writeBatch();
*/

        System.out.println("ConformalEvaluator_Batch_Classifier - Building NORMAL classifier...");
        RandomForest normalTree = new RandomForest();
        normalTree.setNumExecutionSlots(20);
        normalTree.setNumIterations(100);
        normalTree.buildClassifier(dataNormal);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building ATTACK classifier...");
        RandomForest attackTree = new RandomForest();
        attackTree.setNumExecutionSlots(20);
        attackTree.setNumIterations(100);
        attackTree.buildClassifier(dataAttack);

        this.normalClassifier = normalTree;
        this.attackClassifier = attackTree;

        Evaluation evalNormal = new Evaluation(dataNormal);
        evalNormal.evaluateModel(this.normalClassifier, dataNormal);
        System.out.println(evalNormal.toSummaryString("\n\n\n\n\n\nNORMAL Results\n======\n", true));
        System.out.println(evalNormal.toClassDetailsString());


        Evaluation evalAttack = new Evaluation(dataAttack);
        evalAttack.evaluateModel(this.attackClassifier, dataAttack);
        System.out.println(evalAttack.toSummaryString("\n\n\n\n\n\nATTACK Results\n======\n", true));
        System.out.println(evalAttack.toClassDetailsString());

    }

    public double probabilityForCorrectNormal(Instance inst, double probability) throws Exception {

        double[] featureConformal = conformalFeaturesEvaluator.getFeatureStatistics(inst, 0.0d);
        double[] featureConformalOther = conformalFeaturesEvaluator.getFeatureStatistics(inst, 1.0d);
        double[] featVec = new double[featureConformal.length + featureConformalOther.length + 5];

        int j = 0;
        for (j = 0; j < featureConformal.length; j++) {
            featVec[j] = featureConformal[j];
        }
        for (; j < (featureConformal.length + featureConformalOther.length); j++) {
            featVec[j] = featureConformalOther[j - featureConformal.length];
        }
        featVec[j] = probability;
        j++;
        featVec[j] = conformalTranscend.getPValueForNormal(inst);
        j++;
        featVec[j] = conformalTranscend.getPValueForAttack(inst);
        j++;
        featVec[j] = conformalTranscend.getNonConformity(inst, 0.0d);
        j++;

        featVec[j] = 0.0d;


        Instance copyOfNormalTemplate = new DenseInstance(this.normalInstanceFormat);
        copyOfNormalTemplate.setDataset(this.normalInstanceFormat.dataset());
        for (int i = 0; i < copyOfNormalTemplate.numAttributes(); i++) {
            copyOfNormalTemplate.setValue(i, featVec[i]);
        }

        return this.normalClassifier.distributionForInstance(copyOfNormalTemplate)[0];
    }


    public double probabilityForCorrectAttack(Instance inst, double probability) throws Exception {

        double[] featureConformal = conformalFeaturesEvaluator.getFeatureStatistics(inst, 1.0d);
        double[] featureConformalOther = conformalFeaturesEvaluator.getFeatureStatistics(inst, 0.0d);
        double[] featVec = new double[featureConformal.length + featureConformalOther.length + 5];

        int j = 0;
        for (j = 0; j < featureConformal.length; j++) {
            featVec[j] = featureConformal[j];
        }
        for (; j < (featureConformal.length + featureConformalOther.length); j++) {
            featVec[j] = featureConformalOther[j - featureConformal.length];
        }
        featVec[j] = probability;
        j++;

        featVec[j] = conformalTranscend.getPValueForAttack(inst);
        j++;
        featVec[j] = conformalTranscend.getPValueForNormal(inst);
        j++;
        featVec[j] = conformalTranscend.getNonConformity(inst, 1.0d);
        j++;

        featVec[j] = 0.0d;

        Instance copyOfAttackTemplate = new DenseInstance(this.attackInstanceFormat);
        copyOfAttackTemplate.setDataset(this.attackInstanceFormat.dataset());
        for (int i = 0; i < copyOfAttackTemplate.numAttributes(); i++) {
            copyOfAttackTemplate.setValue(i, featVec[i]);
        }

        return this.attackClassifier.distributionForInstance(copyOfAttackTemplate)[0];
    }

}
