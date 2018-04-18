package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;

import java.util.ArrayList;

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

        this.conformalTranscend = new ConformalEvaluator_Batch_Transcend(new ConformalEvaluator_BatchClassifier_NaiveBayes(false));
        this.conformalFeaturesEvaluator = new ConformalEvaluator_Batch_Features();

        System.out.println("ConformalEvaluator_Batch_Classifier - Building feature evaluator...");
        this.conformalFeaturesEvaluator.buildConformalEvaluator_Batch_Features(dataTrain);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building conformal evaluator...");
        this.conformalTranscend.buildConformal(dataTrain);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building feature for classifiers...");
        ArrayList<double[]> instancesConformalNormal = new ArrayList<>();
        ArrayList<double[]> instancesConformalAttack = new ArrayList<>();
        for (int i = 0; i < dataTrain.size(); i++) {
            Instance inst = dataTrain.get(i);


            double[] featureConformal = this.conformalFeaturesEvaluator.getFeatureStatistics(inst, classGivenByClassifier[i]);
            double[] featVec = new double[featureConformal.length + 5];

            int j = 0;
            for (j = 0; j < featureConformal.length; j++) {
                featVec[j] = featureConformal[j];
            }
            featVec[j] = probabilities[i];
            j++;
            if (classGivenByClassifier[i] == 0.0d) {
                featVec[j] = this.conformalTranscend.getPValueForNormal(inst);
                j++;
                featVec[j] = this.conformalTranscend.getPValueForAttack(inst);
                j++;
                featVec[j] = this.conformalTranscend.getNonConformity(inst, 0.0d);
                j++;

                if (Double.compare(inst.classValue(), classGivenByClassifier[i]) == 0) {
                    featVec[j] = 0.0d;
                } else {
                    featVec[j] = 1.0d;
                }

                instancesConformalNormal.add(featVec);
            } else {
                featVec[j] = this.conformalTranscend.getPValueForAttack(inst);
                j++;
                featVec[j] = this.conformalTranscend.getPValueForNormal(inst);
                j++;
                featVec[j] = this.conformalTranscend.getNonConformity(inst, 1.0d);
                j++;

                if (Double.compare(inst.classValue(), classGivenByClassifier[i]) == 0) {
                    featVec[j] = 0.0d;
                } else {
                    featVec[j] = 1.0d;
                }

                instancesConformalAttack.add(featVec);
            }

        }

        ArrayList<Attribute> atts = new ArrayList<Attribute>(instancesConformalNormal.get(0).length);
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add("correct");
        classVal.add("wrong");

        for (int i = 0; i < instancesConformalNormal.get(0).length - 1; i++) {
            atts.add(new Attribute("feat_" + i, (ArrayList<String>) null));
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

        this.attackInstanceFormat = dataAttack.get(0);
        this.normalInstanceFormat = dataNormal.get(0);

        System.out.println("ConformalEvaluator_Batch_Classifier - Building NORMAL classifier...");
        NaiveBayes normalBayes = new NaiveBayes();
        normalBayes.setUseSupervisedDiscretization(false);

        ClassBalancer balancer = new ClassBalancer();
        balancer.setInputFormat(dataNormal);
        normalBayes.buildClassifier(Filter.useFilter(dataNormal, balancer));

        System.out.println("ConformalEvaluator_Batch_Classifier - Building ATTACK classifier...");
        NaiveBayes attackBayes = new NaiveBayes();
        attackBayes.setUseSupervisedDiscretization(false);

        balancer = new ClassBalancer();
        balancer.setInputFormat(dataAttack);
        attackBayes.buildClassifier(Filter.useFilter(dataAttack, balancer));

        this.normalClassifier = normalBayes;
        this.attackClassifier = attackBayes;
    }

    public double probabilityForCorrectNormal(Instance inst, double probability) throws Exception {

        double[] featureConformal = this.conformalFeaturesEvaluator.getFeatureStatistics(inst, 0.0d);
        double[] featVec = new double[featureConformal.length + 5];

        int j = 0;
        for (j = 0; j < featureConformal.length; j++) {
            featVec[j] = featureConformal[j];
        }
        featVec[j] = probability;
        j++;

        featVec[j] = this.conformalTranscend.getPValueForNormal(inst);
        j++;
        featVec[j] = this.conformalTranscend.getPValueForAttack(inst);
        j++;
        featVec[j] = this.conformalTranscend.getNonConformity(inst, 0.0d);
        j++;
        featVec[j] = 0.0d;

        Instance copyOfNormalTemplate = this.normalInstanceFormat.copy(featVec);

        return this.normalClassifier.distributionForInstance(copyOfNormalTemplate)[0];
    }


    public double probabilityForCorrectAttack(Instance inst, double probability) throws Exception {

        double[] featureConformal = this.conformalFeaturesEvaluator.getFeatureStatistics(inst, 0.0d);
        double[] featVec = new double[featureConformal.length + 5];

        int j = 0;
        for (j = 0; j < featureConformal.length; j++) {
            featVec[j] = featureConformal[j];
        }
        featVec[j] = probability;
        j++;

        featVec[j] = this.conformalTranscend.getPValueForAttack(inst);
        j++;
        featVec[j] = this.conformalTranscend.getPValueForNormal(inst);
        j++;
        featVec[j] = this.conformalTranscend.getNonConformity(inst, 1.0d);
        j++;
        featVec[j] = 0.0d;

        Instance copyOfAttackTemplate = this.attackInstanceFormat.copy(featVec);

        return this.attackClassifier.distributionForInstance(copyOfAttackTemplate)[0];
    }

}
