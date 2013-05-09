/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDriveManual;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramTestParallel extends WsAutonomousProgram {

    public WsAutonomousProgramTestParallel() {
        super(2);
    }

    public void defineSteps() {
        WsAutonomousParallelStepGroup parallelGroup = new WsAutonomousParallelStepGroup();
            parallelGroup.addStep(new WsAutonomousStepDriveManual(WsAutonomousStepDriveManual.KEEP_PREVIOUS_STATE, 1.0));
            parallelGroup.addStep(new WsAutonomousStepDelay(250));
            parallelGroup.addStep(new WsAutonomousStepDriveManual(1.0, WsAutonomousStepDriveManual.KEEP_PREVIOUS_STATE));
        programSteps[0] = parallelGroup;
        programSteps[1] = new WsAutonomousStepDriveManual(0.0, 0.0);

    }

    public String toString() {
        return "Test Parallel Groups";
    }
}
