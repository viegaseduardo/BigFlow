package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

import java.util.Random;

public class ConformalEvaluator_BatchClassifier_NearestNeighbor implements ConformalEvaluator_BatchClassifier_Transcend  {

    private Modified_IBK knnNormal;
    private Modified_IBK knnAttack;

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
        double distChoosenClass;
        double distOtherClass;
        if(classValue == 0.0d){
            distChoosenClass = this.knnNormal.getDistanceNearest(inst);
            distOtherClass = this.knnAttack.getDistanceNearest(inst);
        }else{
            distOtherClass = this.knnNormal.getDistanceNearest(inst);
            distChoosenClass = this.knnAttack.getDistanceNearest(inst);
        }
        return distChoosenClass / distOtherClass;
    }

    @Override
    public void buildClassifier(Instances insts) throws Exception {
        Instances testInstNormal = new Instances(insts);
        Instances testInstAttack = new Instances(insts);
        testInstNormal.clear();
        testInstAttack.clear();

        for(Instance inst : insts){
            if(inst.classValue() == 0.0d){
                testInstNormal.add(inst);
            }else{
                testInstAttack.add(inst);
            }
        }

        testInstAttack.randomize(new Random(1));
        testInstNormal.randomize(new Random(1));

        this.knnAttack = new Modified_IBK();
        this.knnNormal = new Modified_IBK();

        this.knnNormal.setKNN(1);
        this.knnAttack.setKNN(1);

        this.knnAttack.buildClassifier(new Instances(testInstAttack, 0, 1000));
        this.knnNormal.buildClassifier(new Instances(testInstNormal, 0, 1000));
    }
}
