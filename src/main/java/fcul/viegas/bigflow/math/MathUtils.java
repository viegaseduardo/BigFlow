/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.math;

/**
 *
 * @author viegas
 */
public class MathUtils {
    
    private int count;
    private Long sum;
    
    public MathUtils(){
        this.count = 0;
        this.sum = 0l;
    }
    
    public void addNumber(int number){
        this.count += 1;
        this.sum += number;
    }
    
    public Float getAverage(){
        if(this.count == 0){
            return 0.0f;
        }
        return ((float)this.sum/this.count);
    }

    public int getCount() {
        return count;
    }

    public Long getSum() {
        return sum;
    }

}
