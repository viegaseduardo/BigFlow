/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.relatedWorks;

import java.io.Serializable;
import java.util.Arrays;
import weka.clusterers.SimpleKMeans;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.TestInstances;

/*
 * @author viegas
 */
public class Transcend_ConformalPredictor implements Serializable {

    private double[] centroidAttack;
    private double[] centroidNormal;
    private double[] pvaluesAttack;
    private double[] pvaluesNormal;

    public Transcend_ConformalPredictor() {
    }

    public Double getPValueForAttack(Instance inst) {
        int i = this.pvaluesAttack.length - 1;
        double distance = this.distance(inst.toDoubleArray(), this.centroidAttack);
        for (; i > 0; i--) {
            if (distance > this.pvaluesAttack[i]) {
                break;
            }
        }
        double ret = (this.pvaluesAttack.length - i) / (double) this.pvaluesAttack.length;
        return ret;
    }

    public Double getPValueForNormal(Instance inst) {
        int i = this.pvaluesNormal.length - 1;
        double distance = this.distance(inst.toDoubleArray(), this.centroidNormal);
        for (; i > 0; i--) {
            if (distance > this.pvaluesNormal[i]) {
                break;
            }
        }
        double ret = (this.pvaluesNormal.length - i) / (double) this.pvaluesNormal.length;
        return ret;
    }

    public void setDataset(Instances data) throws Exception {
        SimpleKMeans kmeansNormal = new SimpleKMeans();
        SimpleKMeans kmeansAttack = new SimpleKMeans();

        Instances dataNormal = new Instances(data, 0, 1);
        Instances dataAttack = new Instances(data, 0, 1);

        System.out.println(data.size());

        for (Instance inst : data) {
            if (inst.classValue() == 0.0d) {
                dataNormal.add(inst);
            } else {
                dataAttack.add(inst);
            }
        }

        System.out.println("attack size " + dataAttack.size());
        System.out.println("normal size " + dataNormal.size());

        this.centroidNormal = new double[dataNormal.get(0).toDoubleArray().length];
        this.centroidAttack = new double[dataNormal.get(0).toDoubleArray().length];
        for(int i = 0; i < this.centroidNormal.length; i++){
            this.centroidNormal[i] = 0.0d;
            this.centroidAttack[i] = 0.0d;
        }

        for (Instance inst : dataNormal) {
            for(int i = 0; i < inst.toDoubleArray().length; i++) {
                this.centroidNormal[i] += (inst.toDoubleArray()[i] / (double) dataNormal.size());
            }
        }
        for (Instance inst : dataAttack) {
            for(int i = 0; i < inst.toDoubleArray().length; i++) {
                this.centroidAttack[i] += (inst.toDoubleArray()[i] / (double) dataAttack.size());
            }
        }

        this.pvaluesNormal = new double[dataNormal.size()];
        this.pvaluesAttack = new double[dataAttack.size()];

        int i = 0;
        for (Instance inst : dataNormal) {
            this.pvaluesNormal[i++] = this.distance(inst.toDoubleArray(), this.centroidNormal);
        }

        i = 0;
        for (Instance inst : dataAttack) {
            this.pvaluesAttack[i++] = this.distance(inst.toDoubleArray(), this.centroidAttack);
        }

        Arrays.sort(this.pvaluesNormal);
        Arrays.sort(this.pvaluesAttack);

        System.out.println("NORMAL - Primeiro p-value: " + this.pvaluesNormal[0]);
        System.out.println("NORMAL - Ultimo p-value: " + this.pvaluesNormal[this.pvaluesNormal.length - 1]);

        System.out.println("ATTACK - Primeiro p-value: " + this.pvaluesAttack[0]);
        System.out.println("ATTACK - Ultimo p-value: " + this.pvaluesAttack[this.pvaluesAttack.length - 1]);

    }

    private double distance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < (a.length  - 1) && i < (b.length - 1); i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }
}
