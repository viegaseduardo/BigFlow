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
public class Features_A {
    
    private Boolean firstTime;
    private Integer addressHash;
    private String address;

    private Features_ORUNADA_A_Middleware_DTO features_ORUNADA_A_Middleware;
    
    public Features_A(){
        this.firstTime = true;
        this.features_ORUNADA_A_Middleware = new Features_ORUNADA_A_Middleware_DTO();
    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

    public Integer getAddressHash() {
        return addressHash;
    }

    public void setAddressHash(Integer addressHash) {
        this.addressHash = addressHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Features_ORUNADA_A_Middleware_DTO getFeatures_ORUNADA_A_Middleware() {
        return features_ORUNADA_A_Middleware;
    }

    public void setFeatures_ORUNADA_A_Middleware(Features_ORUNADA_A_Middleware_DTO features_ORUNADA_A_Middleware) {
        this.features_ORUNADA_A_Middleware = features_ORUNADA_A_Middleware;
    }
    
    
}
