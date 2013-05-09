/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepStopAutonomous extends WsAutonomousStep {

    public WsAutonomousStepStopAutonomous() {
        // Do nothing. This step does nothing, and never finishes, effectively halting autonomous operations.
        // Note: If included in a parallel step group, it only halts operations after all other steps in the group finish.
    }

    public void initialize() {
        // Do nothing.
    }

    public void update() {
        // Do nothing.
    }

//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepStop)
//        {
//            return true;
//        }
//        return false;
//    }
    public String toString() {
        return "Stop auto-op";
    }
//    public int hashCode()
//    {
//        int hash = 7;
//        return hash;
//    }
}
