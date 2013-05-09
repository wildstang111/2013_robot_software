/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepMultikick extends WsAutonomousSerialStepGroup {

    private int numFrisbees;

    public WsAutonomousStepMultikick(int numFrisbees) {
        super(numFrisbees + (numFrisbees - 1));
        this.numFrisbees = numFrisbees;
        defineSteps();
    }

    public void defineSteps() {
        for (int i = 0; i < getNumSteps(numFrisbees); i++) {
            if (i % 2 == 0) {
                steps[i] = new WsAutonomousStepKick();
            } else {
                steps[i] = new WsAutonomousStepWaitForShooter();
            }
        }
    }

    public String toString() {
        return "Kick " + numFrisbees + " frisbees";
    }
    
    private int getNumSteps(int frisbees) {
        return frisbees + (frisbees - 1);
    }
}
