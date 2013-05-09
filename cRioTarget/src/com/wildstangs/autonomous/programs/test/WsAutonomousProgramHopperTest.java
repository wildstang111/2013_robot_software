/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepKick;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepLowerHopper;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepRaiseHopper;

/**
 *
 * @author Batman
 */
public class WsAutonomousProgramHopperTest extends WsAutonomousProgram {

    public WsAutonomousProgramHopperTest() {
        super(5);
    }

    protected void defineSteps() {
        programSteps[0] = new WsAutonomousStepRaiseHopper();
        programSteps[1] = new WsAutonomousStepDelay(50);
        programSteps[2] = new WsAutonomousStepKick();
        programSteps[3] = new WsAutonomousStepDelay(50);
        programSteps[4] = new WsAutonomousStepLowerHopper();
    }

    public String toString() {
        return "Raise the lift, kick the kicker, and lower the lift.";
    }
}
