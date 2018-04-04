package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;

public class ConformalEvaluator_BatchClassifier_RandomForest extends RandomForest implements ConformalEvaluator_BatchClassifier {

    public ConformalEvaluator_BatchClassifier_RandomForest(){
        this.setNumIterations(100);
        this.setNumExecutionSlots(20);
    }

    @Override
    public double computeNonConformityForClass(Instance inst, double classValue) throws Exception{
        int nClassifiersChoosenClassValue = 0;
        for(int i = 0; i < this.m_Classifiers.length; i++){
            if(this.m_Classifiers[i].classifyInstance(inst) == classValue){
                nClassifiersChoosenClassValue++;
            }
        }
        return nClassifiersChoosenClassValue/(float)this.m_Classifiers.length;
    }

}
