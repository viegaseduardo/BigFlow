/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.topologies.machinelearning.method;

import java.io.Serializable;

/*
 * @author viegas
 */
public class OperationPoints implements Serializable {

    private double normalThreshold;
    private double attackThreshold;

    public OperationPoints(double normalThreshold, double attackThreshold) {
        this.normalThreshold = normalThreshold;
        this.attackThreshold = attackThreshold;
    }

    public double getNormalThreshold() {
        return normalThreshold;
    }

    public void setNormalThreshold(double normalThreshold) {
        this.normalThreshold = normalThreshold;
    }

    public double getAttackThreshold() {
        return attackThreshold;
    }

    public void setAttackThreshold(double attackThreshold) {
        this.attackThreshold = attackThreshold;
    }

}
