package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Utils;

public class ConformalEvaluator_BatchClassifier_NaiveBayes extends NaiveBayes implements ConformalEvaluator_BatchClassifier {


    public ConformalEvaluator_BatchClassifier_NaiveBayes(boolean supervised){
        this.setUseSupervisedDiscretization(supervised);
    }

    @Override
    public int getNClassifiers() {
        return 0;
    }

    @Override
    public void setNClassifiers(int nClassifiers) {
    }

    @Override
    public int getSizeBagPercent() {
        return 0;
    }

    @Override
    public void setSizeBagPercent(int sizeBagPercent) {

    }

    @Override
    public double computeNonConformityForClass(Instance inst, double classValue) throws Exception{
        double[] prob = this.distributionForInstance(inst);
        int index = Utils.minIndex(prob);
        return 1.0f - prob[index];
    }
}
