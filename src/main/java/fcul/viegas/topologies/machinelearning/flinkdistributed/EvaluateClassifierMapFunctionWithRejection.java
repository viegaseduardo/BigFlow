/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.flinkdistributed;

import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import weka.classifiers.Classifier;

/**
 * @author viegas
 */
public class EvaluateClassifierMapFunctionWithRejection extends RichFlatMapFunction<String, String> {

    private Classifier classifier;
    private MachineLearningModelBuilders mlModelBuilder;

    public EvaluateClassifierMapFunctionWithRejection(MachineLearningModelBuilders mlModelBuilder) {
        this.mlModelBuilder = mlModelBuilder;
    }

    @Override
    public void open(Configuration cfg) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(Topologies_FLINK_DISTRIBUTED_TestWithoutUpdate.PathToModel));
            this.classifier = (Classifier) ois.readObject();
            ois.close();
            System.out.println("EvaluateClassifierMapFunctionWithRejection - Just loaded model: "
                    + getRuntimeContext().getIndexOfThisSubtask());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void flatMap(String path, Collector<String> clctr) throws Exception {
        ArrayList<String> list = mlModelBuilder.evaluateClassifierWithRejection(path, classifier);
        for(String s : list){
            clctr.collect(s);
        }
    }

}
