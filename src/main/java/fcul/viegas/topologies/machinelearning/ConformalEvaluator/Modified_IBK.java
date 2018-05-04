package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.lazy.IBk;
import weka.core.Instance;

public class Modified_IBK extends IBk {

    public double getDistanceNearest(Instance inst){
        try {
            return this.m_NNSearch.getDistanceFunction().distance(this.m_NNSearch.nearestNeighbour(inst), inst);
        }catch(Exception ex){
            ex.printStackTrace();
            return 0.0d;
        }
    }

}
