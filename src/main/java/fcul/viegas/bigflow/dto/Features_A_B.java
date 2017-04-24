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
    private Integer sourceAddressHash;
    private Integer destinationAddressHash;
    private String sourceAddress;
    private String destinationAddress;

    private Features_ORUNADA_A_B_Middleware_DTO features_ORUNADA_A_B_Middleware;

    public Features_A_B() {
        this.firstTime = true;
    }

    public Features_ORUNADA_A_B_Middleware_DTO getFeatures_ORUNADA_A_B_Middleware() {
        return features_ORUNADA_A_B_Middleware;
    }

    public void setFeatures_ORUNADA_A_B_Middleware(Features_ORUNADA_A_B_Middleware_DTO features_ORUNADA_A_B_Middleware) {
        this.features_ORUNADA_A_B_Middleware = features_ORUNADA_A_B_Middleware;
    }
    
    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Integer getSourceAddressHash() {
        return sourceAddressHash;
    }

    public void setSourceAddressHash(Integer sourceAddressHash) {
        this.sourceAddressHash = sourceAddressHash;
    }

    public Integer getDestinationAddressHash() {
        return destinationAddressHash;
    }

    public void setDestinationAddressHash(Integer destinationAddressHash) {
        this.destinationAddressHash = destinationAddressHash;
    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

}
