/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveDistancePid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveDistancePidSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveDistancePid;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveDistance extends WsAutonomousProgram {

    private DoubleConfigFileParameter distance;

    public WsAutonomousProgramDriveDistance() {
        super(3);
        distance = new DoubleConfigFileParameter(this.getClass().getName(), "distance", 10.0);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[1] = new WsAutonomousStepSetDriveDistancePidSetpoint(distance.getValue());
        programSteps[2] = new WsAutonomousStepWaitForDriveDistancePid();
    }

    public String toString() {
        return "TEST PID Drive distance";
        
    }
}
