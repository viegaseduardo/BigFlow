/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import java.util.ArrayList;
import moa.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;

/**
 *
 * @author viegas
 */
public class WekaMoaClassifierWrapper {

    private class OperationPoints {

        private double normalThreshold;
        private double attackThreshold;

        public double getNormalThreshold() {
            return normalThreshold;
        }

        public void setNormalThreshold(double normalThreshold) {
            this.normalThreshold = normalThreshold;
        }

        public double getAttackThreshold() {
            return attackThreshold;
        }

        public void setAttackThreshold(double attackThreshold) {
            this.attackThreshold = attackThreshold;
        }

    }
    
    private Integer[] featureSetToLook;

    private ArrayList<Classifier> wekaClassifiers;
    private ArrayList<OperationPoints> wekaOperationPoints;
    private ArrayList<moa.classifiers.AbstractClassifier> moaClassifiers;
    private ArrayList<OperationPoints> moaOperationPoints;

    public WekaMoaClassifierWrapper() {
        
        
        
    }
    
    
    

    public Integer[] getFeatureSetToLook() {
        return featureSetToLook;
    }

    public void setFeatureSetToLook(Integer[] featureSetToLook) {
        this.featureSetToLook = featureSetToLook;
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
