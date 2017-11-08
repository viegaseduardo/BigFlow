/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.method;

import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.Serializable;
import java.util.ArrayList;
import moa.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;

/*
 * @author viegas
 */
public class WekaMoaClassifierWrapper implements Serializable {

    //this array holds the featureset to use be used for each classifier 
    private ArrayList<String> featureSetToLookWeka;
    private ArrayList<String> featureSetToLookMoa;

    //here we hold the first subset of classifers, the static ones!
    private ArrayList<Classifier> wekaClassifiers;
    //operation points for the static classifiers
    private ArrayList<OperationPoints> wekaOperationPoints;
    //here we hold the second subset of classifiers, the stream ones!
    private ArrayList<moa.classifiers.AbstractClassifier> moaClassifiers;
    //operation points for the stream learning for them
    private ArrayList<OperationPoints> moaOperationPoints;
    //conformal evaluators
    private Transcend_ConformalPredictor conformalEvaluatorVIEGAS;
    private Transcend_ConformalPredictor conformalEvaluatorMOORE;
    private Transcend_ConformalPredictor conformalEvaluatorNIGEL;
    private Transcend_ConformalPredictor conformalEvaluatorORUNADA;

    public WekaMoaClassifierWrapper() {
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

    public Transcend_ConformalPredictor getConformalEvaluatorVIEGAS() {
        return conformalEvaluatorVIEGAS;
    }

    public void setConformalEvaluatorVIEGAS(Transcend_ConformalPredictor conformalEvaluatorVIEGAS) {
        this.conformalEvaluatorVIEGAS = conformalEvaluatorVIEGAS;
    }

    public Transcend_ConformalPredictor getConformalEvaluatorMOORE() {
        return conformalEvaluatorMOORE;
    }

    public void setConformalEvaluatorMOORE(Transcend_ConformalPredictor conformalEvaluatorMOORE) {
        this.conformalEvaluatorMOORE = conformalEvaluatorMOORE;
    }

    public Transcend_ConformalPredictor getConformalEvaluatorNIGEL() {
        return conformalEvaluatorNIGEL;
    }

    public void setConformalEvaluatorNIGEL(Transcend_ConformalPredictor conformalEvaluatorNIGEL) {
        this.conformalEvaluatorNIGEL = conformalEvaluatorNIGEL;
    }

    public Transcend_ConformalPredictor getConformalEvaluatorORUNADA() {
        return conformalEvaluatorORUNADA;
    }

    public void setConformalEvaluatorORUNADA(Transcend_ConformalPredictor conformalEvaluatorORUNADA) {
        this.conformalEvaluatorORUNADA = conformalEvaluatorORUNADA;
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
