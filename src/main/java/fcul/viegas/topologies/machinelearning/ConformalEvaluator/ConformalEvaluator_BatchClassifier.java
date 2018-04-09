package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.core.Instance;
import weka.core.Instances;

public interface ConformalEvaluator_BatchClassifier {

    int getNClassifiers();
    void setNClassifiers(int nClassifiers);
    int getSizeBagPercent();
    void setSizeBagPercent(int sizeBagPercent);
    double computeNonConformityForClass(Instance inst, double classValue) throws Exception;
    void buildClassifier(Instances insts) throws Exception;

}


