/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.dto;

import fcul.viegas.bigflow.definitions.Definitions;
import fcul.viegas.bigflow.math.MathUtils;

/**
 *
 * @author viegas
 */
public class Features_NIGEL_A_B_Middleware_DTO {

    private Features_NIGEL_Direction_Middleware_DTO forward;
    private Features_NIGEL_Direction_Middleware_DTO backward;

    private Float protocol;

    public Features_NIGEL_A_B_Middleware_DTO() {
        this.protocol = Definitions.PROTOCOL_NONE;
        this.forward = new Features_NIGEL_Direction_Middleware_DTO();
        this.backward = new Features_NIGEL_Direction_Middleware_DTO();
    }

    public Features_NIGEL_Direction_Middleware_DTO getForward() {
        return forward;
    }

    public void setForward(Features_NIGEL_Direction_Middleware_DTO forward) {
        this.forward = forward;
    }

    public Features_NIGEL_Direction_Middleware_DTO getBackward() {
        return backward;
    }

    public void setBackward(Features_NIGEL_Direction_Middleware_DTO backward) {
        this.backward = backward;
    }

    public Float getProtocol() {
        return protocol;
    }

    public void setProtocol(Float protocol) {
        this.protocol = protocol;
    }
}
