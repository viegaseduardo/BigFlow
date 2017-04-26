/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import java.util.HashSet;

/**
 *
 * @author viegas
 */
public class Features_ORUNADA_A_Middleware_DTO {
    
    private HashSet<Integer> uniqueDestinationIPs;
    private HashSet<Integer> uniqueDestinationPorts;
    
    public Features_ORUNADA_A_Middleware_DTO(){
        this.uniqueDestinationIPs = new HashSet<>();
        this.uniqueDestinationPorts = new HashSet<>();
    }

    public HashSet<Integer> getUniqueDestinationIPs() {
        return uniqueDestinationIPs;
    }

    public HashSet<Integer> getUniqueDestinationPorts() {
        return uniqueDestinationPorts;
    }
  
}
