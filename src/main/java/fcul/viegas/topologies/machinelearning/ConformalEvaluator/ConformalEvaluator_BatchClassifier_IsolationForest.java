package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.misc.IsolationForest;
import weka.core.Instance;

public class ConformalEvaluator_BatchClassifier_IsolationForest extends IsolationForest implements ConformalEvaluator_BatchClassifier_Transcend  {
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
    public double computeNonConformityForClass(Instance inst, double classValue) throws Exception {
        return this.distributionForInstance(inst)[1];
    }
}
