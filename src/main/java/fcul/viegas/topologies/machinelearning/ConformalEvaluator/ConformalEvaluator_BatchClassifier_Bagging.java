package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.meta.Bagging;
import weka.core.Instance;

public class ConformalEvaluator_BatchClassifier_Bagging extends Bagging implements ConformalEvaluator_BatchClassifier_Transcend {

    int nClassifiers;
    int sizeBagPercent;

    public ConformalEvaluator_BatchClassifier_Bagging(int nClassifiers, int sizeBagPercent){
        this.nClassifiers = nClassifiers;
        this.setNumIterations(nClassifiers);
        this.setNumExecutionSlots(20);
        this.sizeBagPercent = sizeBagPercent;
        this.setBagSizePercent(this.sizeBagPercent);
    }

    @Override
    public int getNClassifiers() {
        return nClassifiers;
    }

    @Override
    public void setNClassifiers(int nClassifiers) {
        this.nClassifiers = nClassifiers;
    }

    @Override
    public int getSizeBagPercent() {
        return this.sizeBagPercent;
    }

    @Override
    public void setSizeBagPercent(int sizeBagPercent) {
        this.sizeBagPercent = sizeBagPercent;
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
