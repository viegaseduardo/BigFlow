package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.classifiers.Classifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.ClassBalancer;
import weka.filters.supervised.instance.Resample;

import java.util.ArrayList;
import java.util.Arrays;

public class ConformalEvaluator_Batch {

    private ConformalEvaluator_BatchClassifier conformalEvaluatorClassifier;
    private Double[][] nonConformityMeasures;
    private Double[][] pvalues;

    public ConformalEvaluator_Batch(ConformalEvaluator_BatchClassifier conformalEvaluatorClassifier) {
        this.conformalEvaluatorClassifier = conformalEvaluatorClassifier;
    }

    public void buildConformal(Instances insts) throws Exception {

        System.out.println("CONFORMAL: building classifier");

        ClassBalancer balancer = new ClassBalancer();
        balancer.setInputFormat(insts);
        Instances newinsts = Filter.useFilter(insts, balancer);

        this.conformalEvaluatorClassifier.buildClassifier(newinsts);

        this.nonConformityMeasures = new Double[2][];
        this.pvalues = new Double[2][];


        int nInstancesNormal = 0;
        int nInstancesAttack = 0;
        for (Instance inst : insts) {
            if(inst.classValue() == 0.0d){
                nInstancesNormal++;
            }else{
                nInstancesAttack++;
            }
        }

        this.nonConformityMeasures[0] = new Double[nInstancesNormal];
        this.nonConformityMeasures[1] = new Double[nInstancesAttack];

        this.pvalues[0] = new Double[nInstancesNormal];
        this.pvalues[1] = new Double[nInstancesAttack];

        System.out.println("CONFORMAL: computing non-conformities");

        class TestClass implements Runnable {
            int i;
            int iUpper;
            Instances insts;
            int iNormal = 0;
            int iAttack = 0;
            int pct = 0;
            int index = 0;

            TestClass(int i, int iUpper, Instances insts) {
                this.i = i;
                this.iUpper = iUpper;
                this.insts = insts;
            }

            public void run() {
                try {
                    for (int k = 0; k < iUpper; k++) {
                        index++;
                        if(k >= i){
                            if(index % ((insts.size())/100) == 0){
                                pct++;
                                System.out.println("\tnonconformity " + pct + "% ...[" + i + "/" + iUpper + "]");
                            }
                        }
                        if(k < i){
                            if(insts.get(k).classValue() == 0.0d){
                                iNormal++;
                            }else{
                                iAttack++;
                            }
                        }else{
                            if(insts.get(k).classValue() == 0.0d){
                                nonConformityMeasures[0][iNormal] = conformalEvaluatorClassifier.computeNonConformityForClass(insts.get(k), 0.0d);
                                iNormal++;
                            }else{
                                nonConformityMeasures[1][iAttack] = conformalEvaluatorClassifier.computeNonConformityForClass(insts.get(k), 1.0d);
                                iAttack++;
                            }
                        }
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        }

        ArrayList<Thread> threads = new ArrayList<>();
        int jump = insts.size() / 20;
        int start = 0;
        for(int nThreads = 0; nThreads < 20; nThreads++){
            if(nThreads + 1 == 20){
                Thread t = new Thread(new TestClass(start, insts.size(), insts));
                t.start();
                threads.add(t);
                start += jump;
            }else {
                Thread t = new Thread(new TestClass(start, start + jump, insts));
                t.start();
                threads.add(t);
                start += jump;
            }
        }

        for(Thread t: threads) {
            t.join();
        }

    }

    public double getPValueForAttack(Instance inst) throws Exception{
        double nonConformity = this.conformalEvaluatorClassifier.computeNonConformityForClass(inst, 1.0d);
        int nInstancesHigher = 0;
        for(int j = 0; j < this.nonConformityMeasures[1].length; j++){
            if(this.nonConformityMeasures[1][j] >= nonConformity){
                nInstancesHigher++;
            }
        }
        return nInstancesHigher / (double) this.nonConformityMeasures[1].length;
    }

    public double getPValueForNormal(Instance inst) throws Exception{
        double nonConformity = this.conformalEvaluatorClassifier.computeNonConformityForClass(inst, 0.0d);
        int nInstancesHigher = 0;
        for(int j = 0; j < this.nonConformityMeasures[0].length; j++){
            if(this.nonConformityMeasures[0][j] >= nonConformity){
                nInstancesHigher++;
            }
        }
        return nInstancesHigher / (double) this.nonConformityMeasures[0].length;
    }

    public double getNonConformity(Instance inst, double givenClass) throws Exception{
        return this.conformalEvaluatorClassifier.computeNonConformityForClass(inst, givenClass);
    }

}