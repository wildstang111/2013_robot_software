/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveHeadingPid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveHeadingPidRelativeSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveHeadingPidSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveHeadingPid;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveHeading extends WsAutonomousProgram {

    private DoubleConfigFileParameter angle;

    public WsAutonomousProgramDriveHeading() {
        super(3);
        angle = new DoubleConfigFileParameter(this.getClass().getName(), "angle", 30);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepSetDriveHeadingPidRelativeSetpoint(angle.getValue());
        programSteps[1] = new WsAutonomousStepEnableDriveHeadingPid();
        programSteps[2] = new WsAutonomousStepWaitForDriveHeadingPid();
    }

    public String toString() {
        return "TEST Relative heading test using PID control";
    }
}
