/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepLowerHopper;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramTestPickupDelay extends WsAutonomousProgram
{
    public WsAutonomousProgramTestPickupDelay()
    {
        super(22);
    }
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepLowerAccumulator();
        programSteps[1] = new WsAutonomousStepLowerHopper();
        programSteps[2] = new WsAutonomousStepDelay();
        programSteps[3] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[4] = new WsAutonomousStepDelay(3000);
        programSteps[5] = new WsAutonomousStepIntakeMotorStop();
        programSteps[6] = new WsAutonomousStepRaiseAccumulator();
        programSteps[7] = new WsAutonomousStepWaitForAccumulatorUp();
        programSteps[8] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[9] = new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator();
        programSteps[10] = new WsAutonomousStepIntakeMotorStop();
        programSteps[11] = new WsAutonomousStepLowerAccumulator();
        programSteps[12] = new WsAutonomousStepLowerHopper();
        programSteps[13] = new WsAutonomousStepDelay();
        programSteps[14] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[15] = new WsAutonomousStepDelay(3000);
        programSteps[16] = new WsAutonomousStepIntakeMotorStop();
        programSteps[17] = new WsAutonomousStepRaiseAccumulator();
        programSteps[18] = new WsAutonomousStepWaitForAccumulatorUp();
        programSteps[19] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[20] = new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator();
        programSteps[21] = new WsAutonomousStepIntakeMotorStop();
        
    }
    
    public String toString()
    {
        return "TEST WaitForDiscsLatched TWICE";
    }
}

