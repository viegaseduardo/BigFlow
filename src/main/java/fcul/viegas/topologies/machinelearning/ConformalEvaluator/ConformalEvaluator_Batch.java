package fcul.viegas.topologies.machinelearning.ConformalEvaluator;

import weka.core.Instance;
import weka.core.Instances;

import java.util.Arrays;

public class ConformalEvaluator_Batch {

    private ConformalEvaluator_BatchClassifier conformalEvaluatorClassifier;
    private Double[][] nonConformityMeasures;
    private Double[][] pvalues;

    public ConformalEvaluator_Batch() {
        this.conformalEvaluatorClassifier = new ConformalEvaluator_BatchClassifier_RandomForest();
    }

    public void buildConformal(Instances insts) throws Exception {

        System.out.println("CONFORMAL: building classifier");
        this.conformalEvaluatorClassifier.buildClassifier(insts);
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
        int iNormal = 0;
        int iAttack = 0;
        int pct = 0;
        int index = 0;
        for (Instance inst : insts) {
            index++;
            if(index % ((nInstancesNormal+nInstancesAttack)/100) == 0){
                pct++;
                System.out.println("\tnonconformity " + pct + "%");
            }
            if(inst.classValue() == 0.0d){
                this.nonConformityMeasures[0][iNormal] = this.conformalEvaluatorClassifier.computeNonConformityForClass(inst, 0.0d);
                iNormal++;
            }else{
                this.nonConformityMeasures[1][iAttack] = this.conformalEvaluatorClassifier.computeNonConformityForClass(inst, 1.0d);
                iAttack++;
            }
        }

    /*    System.out.println("CONFORMAL: computing p-values");


        pct = 0;
        for(int i = 0; i < nInstancesNormal; i++){
            if(i % (nInstancesNormal/100) == 0){
                pct++;
                System.out.println("\tnormal p-value " + pct + "%");
            }
            int nInstancesHigher = 0;
            for(int j = 0; j < this.nonConformityMeasures[0].length; j++){
                if(j != i && this.nonConformityMeasures[0][i] >= this.nonConformityMeasures[0][j]){
                    nInstancesHigher++;
                }
            }
            this.pvalues[0][i] = nInstancesHigher / (double) this.nonConformityMeasures[0].length;
        }

        pct = 0;
        for(int i = 0; i < nInstancesAttack; i++){
            if(i % (nInstancesAttack/100) == 0){
                pct++;
                System.out.println("\tattack p-value " + pct + "%");
            }
            int nInstancesHigher = 0;
            for(int j = 0; j < this.nonConformityMeasures[1].length; j++){
                if(j != i && this.nonConformityMeasures[1][i] >= this.nonConformityMeasures[1][j]){
                    nInstancesHigher++;
                }
            }
            this.pvalues[1][i] = nInstancesHigher / (double) this.nonConformityMeasures[1].length;
        }

        Arrays.sort(this.pvalues[0]);
        Arrays.sort(this.pvalues[1]);


        for(int i = 0; i < 100; i++){
            int pingNormal = this.pvalues[0].length / 100;
            int pingAttack = this.pvalues[1].length / 100;
            System.out.println("Normal [" + this.pvalues[0][pingNormal * i] + "] Attack [" + this.pvalues[1][pingAttack * i] + "]");
        }*/

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

}