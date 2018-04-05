package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.meta.AdaBoostM1;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.supervised.instance.ClassBalancer;

public class ConformalEvaluator_BatchClassifier_RandomForest extends AdaBoostM1 implements ConformalEvaluator_BatchClassifier {

    public ConformalEvaluator_BatchClassifier_RandomForest(){
        this.setNumIterations(100);
        //this.setNumExecutionSlots(20);
    }

    @Override
    public double computeNonConformityForClass(Instance inst, double classValue) throws Exception{
        int nClassifiersNotChoosenClassValue = 0;
        for(int i = 0; i < this.m_Classifiers.length; i++){
            if(this.m_Classifiers[i].classifyInstance(inst) != classValue){
                nClassifiersNotChoosenClassValue++;
            }
        }
        //return dissimilarity
        return (nClassifiersNotChoosenClassValue/(float)this.m_Classifiers.length);
    }

}
