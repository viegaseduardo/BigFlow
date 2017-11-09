/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.method;

import fcul.viegas.topologies.machinelearning.MachineLearningModelBuilders;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

/**
 *
 * @author viegas
 */
public class EvaluateClassifierMapFunctionWithConformalCascadeStatic extends RichMapFunction<String[], String>{
    
    private MachineLearningModelBuilders mlModelBuilder;
    private WekaMoaClassifierWrapper wekaWrapper;
    
    public EvaluateClassifierMapFunctionWithConformalCascadeStatic(MachineLearningModelBuilders mlModelBuilder){
        this.mlModelBuilder = mlModelBuilder;
    }
    
    @Override
    public void open(Configuration cfg) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(Topologies_EVALUATION_DISTRIBUTED_STATIC_CASCADE_WithConformal.PathToModel));
            this.wekaWrapper = (WekaMoaClassifierWrapper) ois.readObject();
            ois.close();
            System.out.println("EvaluateClassifierMapFunctionWithConformalCascadeStatic - Just loaded model: "
                    + getRuntimeContext().getIndexOfThisSubtask());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String map(String[] in) throws Exception {
        return mlModelBuilder.evaluateClassifierWithRejectionThroughConformalAndCascade(in, wekaWrapper);
    }
    
}
