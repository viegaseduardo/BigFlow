/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.flinkdistributed;

import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import weka.classifiers.Classifier;

/**
 * @author viegas
 */
public class EvaluateClassifierMapFunctionWithRejection extends RichMapFunction<String, String> {

    private Classifier classifier;
    private MachineLearningModelBuilders mlModelBuilder;
    private float normalRejectThreshold;
    private float attackRejectThreshold;

    public EvaluateClassifierMapFunctionWithRejection(MachineLearningModelBuilders mlModelBuilder, float normalRejectThreshold, float attackRejectThreshold) {
        this.mlModelBuilder = mlModelBuilder;
        this.normalRejectThreshold = normalRejectThreshold;
        this.attackRejectThreshold = attackRejectThreshold;
    }

    @Override
    public void open(Configuration cfg) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate.PathToModel));
            this.classifier = (Classifier) ois.readObject();
            ois.close();
            System.out.println("EvaluateClassifierMapFunctionWithRejection - Just loaded model: " + 
                    getRuntimeContext().getIndexOfThisSubtask());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String map(String path) throws Exception {
        return mlModelBuilder.evaluateClassifierWithRejection(path, classifier, this.normalRejectThreshold, this.attackRejectThreshold);
    }

}
