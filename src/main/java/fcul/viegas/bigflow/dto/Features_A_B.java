/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

/**
 *
 * @author viegas
 */
public class Features_A_B {

    private Boolean firstTime;
    private Long sourceAddressHash;
    private Long destinationAddressHash;
    
    public Features_A_B(){
        this.firstTime = true;
    }

    public Long getSourceAddressHash() {
        return sourceAddressHash;
    }

    public void setSourceAddressHash(Long sourceAddressHash) {
        this.sourceAddressHash = sourceAddressHash;
    }

    public Long getDestinationAddressHash() {
        return destinationAddressHash;
    }

    public void setDestinationAddressHash(Long destinationAddressHash) {
        this.destinationAddressHash = destinationAddressHash;
    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

}
