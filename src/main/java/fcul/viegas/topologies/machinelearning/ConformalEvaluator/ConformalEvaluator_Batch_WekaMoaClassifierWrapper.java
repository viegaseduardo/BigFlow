package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import fcul.viegas.topologies.machinelearning.method.OperationPoints;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import moa.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;

import java.util.ArrayList;

public class ConformalEvaluator_Batch_WekaMoaClassifierWrapper {

    //this array holds the featureset to use be used for each classifier
    private ArrayList<String> featureSetToLookWeka;
    //sae thing but for moa
    private ArrayList<String> featureSetToLookMoa;

    //here we hold the first subset of classifers, the static ones!
    private ArrayList<Classifier> wekaClassifiers;
    //operation points for the static classifiers
    private ArrayList<OperationPoints> wekaOperationPoints;
    //here we hold the second subset of classifiers, the stream ones!
    private ArrayList<moa.classifiers.AbstractClassifier> moaClassifiers;
    //operation points for the stream learning for them
    private ArrayList<OperationPoints> moaOperationPoints;
    //conformal evaluators, different from usenix one!
    private ConformalEvaluator_Batch conformalEvaluatorVIEGAS_Batch;
    private ConformalEvaluator_Batch conformalEvaluatorMOORE_Batch;
    private ConformalEvaluator_Batch conformalEvaluatorNIGEL_Batch;
    private ConformalEvaluator_Batch conformalEvaluatorORUNADA_Batch;

    public ConformalEvaluator_Batch_WekaMoaClassifierWrapper() {
        this.featureSetToLookMoa = new ArrayList<>();
        this.featureSetToLookWeka = new ArrayList<>();
        this.wekaClassifiers = new ArrayList<>();
        this.wekaOperationPoints = new ArrayList<>();
        this.moaClassifiers = new ArrayList<>();
        this.moaOperationPoints = new ArrayList<>();
    }

    public ArrayList<String> getFeatureSetToLookWeka() {
        return featureSetToLookWeka;
    }

    public void setFeatureSetToLookWeka(ArrayList<String> featureSetToLookWeka) {
        this.featureSetToLookWeka = featureSetToLookWeka;
    }

    public ArrayList<String> getFeatureSetToLookMoa() {
        return featureSetToLookMoa;
    }

    public void setFeatureSetToLookMoa(ArrayList<String> featureSetToLookMoa) {
        this.featureSetToLookMoa = featureSetToLookMoa;
    }

    public ConformalEvaluator_Batch getConformalEvaluatorMOORE_Batch() {
        return conformalEvaluatorMOORE_Batch;
    }

    public void setConformalEvaluatorMOORE_Batch(ConformalEvaluator_Batch conformalEvaluatorMOORE_Batch) {
        this.conformalEvaluatorMOORE_Batch = conformalEvaluatorMOORE_Batch;
    }

    public ConformalEvaluator_Batch getConformalEvaluatorNIGEL_Batch() {
        return conformalEvaluatorNIGEL_Batch;
    }

    public void setConformalEvaluatorNIGEL_Batch(ConformalEvaluator_Batch conformalEvaluatorNIGEL_Batch) {
        this.conformalEvaluatorNIGEL_Batch = conformalEvaluatorNIGEL_Batch;
    }

    public ConformalEvaluator_Batch getConformalEvaluatorORUNADA_Batch() {
        return conformalEvaluatorORUNADA_Batch;
    }

    public void setConformalEvaluatorORUNADA_Batch(ConformalEvaluator_Batch conformalEvaluatorORUNADA_Batch) {
        this.conformalEvaluatorORUNADA_Batch = conformalEvaluatorORUNADA_Batch;
    }

    public ConformalEvaluator_Batch getConformalEvaluatorVIEGAS_Batch() {
        return conformalEvaluatorVIEGAS_Batch;
    }

    public void setConformalEvaluatorVIEGAS_Batch(ConformalEvaluator_Batch conformalEvaluatorVIEGAS_Batch) {
        this.conformalEvaluatorVIEGAS_Batch = conformalEvaluatorVIEGAS_Batch;
    }

    public ArrayList<Classifier> getWekaClassifiers() {
        return wekaClassifiers;
    }

    public void setWekaClassifiers(ArrayList<Classifier> wekaClassifiers) {
        this.wekaClassifiers = wekaClassifiers;
    }

    public ArrayList<OperationPoints> getWekaOperationPoints() {
        return wekaOperationPoints;
    }

    public void setWekaOperationPoints(ArrayList<OperationPoints> wekaOperationPoints) {
        this.wekaOperationPoints = wekaOperationPoints;
    }

    public ArrayList<AbstractClassifier> getMoaClassifiers() {
        return moaClassifiers;
    }

    public void setMoaClassifiers(ArrayList<AbstractClassifier> moaClassifiers) {
        this.moaClassifiers = moaClassifiers;
    }

    public ArrayList<OperationPoints> getMoaOperationPoints() {
        return moaOperationPoints;
    }

    public void setMoaOperationPoints(ArrayList<OperationPoints> moaOperationPoints) {
        this.moaOperationPoints = moaOperationPoints;
    }
}
