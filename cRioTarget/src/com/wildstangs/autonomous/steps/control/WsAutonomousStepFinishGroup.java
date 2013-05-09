/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.*;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepFinishGroup extends WsAutonomousStep {
    //Note: only works in serial step groups.

    public WsAutonomousStepFinishGroup() {
        //Do nothing, nothing to set up.
    }

    public void initialize() {
        // Do nothing, effects occur in update().
    }

    public void update() {
        boolean bottomOfTree = false;
        WsAutonomousSerialStepGroup group = null;
        IStepContainer container = WsAutonomousManager.getInstance().getRunningProgram();
        while (!bottomOfTree) {
            WsAutonomousStep currStep = container.getCurrentStep();
            if (currStep instanceof WsAutonomousSerialStepGroup) {
                group = (WsAutonomousSerialStepGroup) currStep;
                container = group;
            } else {
                bottomOfTree = true;
            }
        }
        if (group == null) {
            errorInfo = "This step must be run inside a step group";
            finished = true;
            pass = false;
        } else {
            group.finishGroup();
        }
    }

//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepFinishGroup)
//        {
//            return true;
//        }
//        return false;
//    }
    public String toString() {
        return "Interrupt current step group";
    }
//    public int hashCode()
//    {
//        int hash = 5;
//        return hash;
//    }
}
