/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.controller.base;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.pid.outputs.base.IPidOutput;

/**
 *
 * @author chadschmidt
 */
public class WsSpeedPidController extends WsPidController{

    public WsSpeedPidController(IPidInput source, IPidOutput output, String pidControllerName) {
        super(source, output, pidControllerName);
    }

    protected double calcDerivativeTerm() {
        //This needs to take into account the goal velocity if I ever want to use a goal velocity != 0 
        double d_term = this.getD() * ((this.getError() - this.getPreviousError()));
        double differentiatorBandLimit = this.getDifferentiatorBandLimit(); 
        // Band-limit the differential term
        if (Math.abs(d_term) > differentiatorBandLimit) {
            d_term = (d_term > 0.0f)
                    ? differentiatorBandLimit
                    : -differentiatorBandLimit;
        }
        return d_term;
    }
    
    
}
