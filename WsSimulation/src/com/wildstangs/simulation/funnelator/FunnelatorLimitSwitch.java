/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.funnelator;

import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.simulation.digitalInputs.WsDigitalInputContainer;
import com.wildstangs.subsystems.WsShooter;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author chadschmidt
 */
public class FunnelatorLimitSwitch {

    private static int FUNNELATOR_LIMIT_SWITCH_CHANNEL = 9;
    public FunnelatorLimitSwitch() {
    }
    
    public void update(){
        
        //Use the lift value in case you need to test a jammed true funnelator switch
//        Integer angle = (Integer)WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_ANGLE).get(null); 
//        
//        if (angle.intValue() == 1){
//            //Set the limit switches based on that value
            WsDigitalInputContainer.getInstance().inputs[FUNNELATOR_LIMIT_SWITCH_CHANNEL].set(false);
//            
//        } else { 
//            //Set the limit switches based on that value
//            WsDigitalInputContainer.getInstance().inputs[FUNNELATOR_LIMIT_SWITCH_CHANNEL].set(false);
//            
//        }
        
    }
}
