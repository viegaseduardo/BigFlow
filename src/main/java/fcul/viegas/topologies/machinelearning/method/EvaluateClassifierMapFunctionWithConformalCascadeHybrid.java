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
public class EvaluateClassifierMapFunctionWithConformalCascadeHybrid extends RichMapFunction<String[], String> {
    
    private MachineLearningModelBuilders mlModelBuilder;
    private WekaMoaClassifierWrapper wekaWrapper;
    private String path;
    
    public EvaluateClassifierMapFunctionWithConformalCascadeHybrid(MachineLearningModelBuilders mlModelBuilder, String path){
        this.mlModelBuilder = mlModelBuilder;
        this.path = path;
    }
    
    @Override
    public void open(Configuration cfg) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(this.path));
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
        return mlModelBuilder.evaluateClassifierWithRejectionThroughConformalHybrid(in, wekaWrapper);
    }
    
}
