/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.math;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author viegas
 */
public class MathUtilsArray {

    private ArrayList<Integer> values;
    private Integer[] vec;
    private boolean isSorted;
    private MathUtils mathUtils;

    public MathUtilsArray() {
        this.values = new ArrayList();
        this.isSorted = false;
        this.mathUtils = new MathUtils();
    }

    public void addNumber(Integer number) {
        this.values.add(number);
        this.mathUtils.addNumber(number);
        this.isSorted = false;
    }

    private void sortArray() {
        this.vec = (Integer[]) this.values.toArray();
        Arrays.sort(this.vec);
        this.isSorted = true;
    }

    public Integer getQuartile(double lowerPercent) {
        if (!this.isSorted) {
            this.sortArray();
        }
        if (this.vec.length > 0) {
            return this.vec[(int) Math.round(this.vec.length * lowerPercent / 100)];
        }
        return 0;
    }

    public Integer getMedian() {
        if (!this.isSorted) {
            this.sortArray();
        }
        if (this.vec.length > 0) {
            if (this.vec.length % 2 == 0) {
                return (this.vec[this.vec.length / 2] +  this.vec[(this.vec.length / 2) - 1])/2;
            } else {
                return this.vec[this.vec.length / 2];
            }
        }
        return 0;
    }
    
    public Float getAverage(){
        return this.mathUtils.getAverage();
    }
    
    public double getVariance() {
        return this.mathUtils.getVariance();
    }
    
    public double getStandardDeviation(){
        return this.mathUtils.getStandardDeviation();
    }

}
