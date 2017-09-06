/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning;

import org.apache.flink.api.common.functions.RichMapFunction;
import weka.classifiers.Classifier;

/**
 *
 * @author viegas
 */
public class Topologies_FLINK_MapFunction_EvaluateClassifier extends RichMapFunction<String, String> {

    private Classifier classifier;
    private MachineLearningModelBuilders mlModelBuilder;

    public Topologies_FLINK_MapFunction_EvaluateClassifier(Classifier classifier, MachineLearningModelBuilders mlModelBuilder) {
        this.classifier = classifier;
        this.mlModelBuilder = mlModelBuilder;
    }

    @Override
    public String map(String path) throws Exception {
        return mlModelBuilder.evaluateClassifier(path, classifier);
    }

}
