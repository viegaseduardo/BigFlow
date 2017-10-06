/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.relatedWorks;

import java.util.Arrays;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.TestInstances;

/**
 *
 * @author viegas
 */
public class Transcend_ConformalPredictor {

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
        return (this.pvaluesAttack.length - i) / (double) this.pvaluesAttack.length;
    }

    public Double getPValueForNormal(Instance inst) {
        int i = this.pvaluesNormal.length - 1;
        double distance = this.distance(inst.toDoubleArray(), this.centroidNormal);
        for (; i > 0; i--) {
            if (distance > this.pvaluesNormal[i]) {
                break;
            }
        }
        return (this.pvaluesNormal.length - i) / (double) this.pvaluesNormal.length;
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
            }else{
                dataAttack.add(inst);
            }
        }
        
        System.out.println("attack size " + dataAttack.size());
        System.out.println("normal size " + dataNormal.size());

        dataNormal.setClassIndex(TestInstances.NO_CLASS);
        dataAttack.setClassIndex(TestInstances.NO_CLASS);


        System.out.println("building normal cluster");
        kmeansNormal.setNumClusters(1);
        kmeansNormal.setNumExecutionSlots(4);
        kmeansNormal.setMaxIterations(1000);
        kmeansNormal.buildClusterer(dataNormal);

        System.out.println("building attack cluster");
        kmeansAttack.setNumClusters(1);
        kmeansAttack.setNumExecutionSlots(4);
        kmeansAttack.setMaxIterations(1000);
        kmeansAttack.buildClusterer(dataAttack);

        this.centroidNormal = kmeansNormal.getClusterCentroids().get(0).toDoubleArray();
        this.centroidAttack = kmeansAttack.getClusterCentroids().get(0).toDoubleArray();

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
    }

    private double distance(double[] a, double[] b) {
        double diff_square_sum = 0.0;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff_square_sum += (a[i] - b[i]) * (a[i] - b[i]);
        }
        return Math.sqrt(diff_square_sum);
    }
}
