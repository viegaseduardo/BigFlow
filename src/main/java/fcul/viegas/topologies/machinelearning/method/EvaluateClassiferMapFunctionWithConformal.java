/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.method;

import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import fcul.viegas.topologies.machinelearning.relatedWorks.Transcend_ConformalPredictor;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import weka.classifiers.Classifier;

/**
 *
 * @author viegas
 */
public class EvaluateClassiferMapFunctionWithConformal extends RichMapFunction<String, String> {

    private Classifier classifier;
    private MachineLearningModelBuilders mlModelBuilder;
    private Transcend_ConformalPredictor transcendConformal;
    private float normalThreshold;
    private float attackThreshold;
    private String featureSet;

    public EvaluateClassiferMapFunctionWithConformal(
            MachineLearningModelBuilders mlModelBuilder,
            Transcend_ConformalPredictor transcendConformal,
            float normalThreshold,
            float attackThreshold,
            String featureSet) {
        this.mlModelBuilder = mlModelBuilder;
        this.transcendConformal = transcendConformal;
        this.normalThreshold = normalThreshold;
        this.attackThreshold = attackThreshold;
        this.featureSet = featureSet;
    }

    @Override
    public void open(Configuration cfg) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(Topologies_EVALUATION_DISTRIBUTED_WithConformalEvaluator.PathToModel));
            this.classifier = (Classifier) ois.readObject();
            ois.close();
            System.out.println("EvaluateClassiferMapFunctionWithConformal - Just loaded model: "
                    + getRuntimeContext().getIndexOfThisSubtask());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String map(String path) throws Exception {
        return mlModelBuilder.evaluateClassifierWithRejectionThroughConformal(
                path,
                classifier,
                transcendConformal,
                this.normalThreshold,
                this.attackThreshold,
                this.featureSet);
    }

}
