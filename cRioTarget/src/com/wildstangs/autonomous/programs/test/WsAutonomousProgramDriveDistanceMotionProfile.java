/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStartDriveUsingMotionProfileAndHeading;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveDistanceMotionProfile extends WsAutonomousProgram {

    private DoubleConfigFileParameter distance;
    private DoubleConfigFileParameter heading;

    public WsAutonomousProgramDriveDistanceMotionProfile() {
        super(3);
    }

    public void defineSteps() {
        distance = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".distance", 10.0);
        heading = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".heading", 0.0);
        programSteps[0] = new WsAutonomousStepStartDriveUsingMotionProfileAndHeading(distance.getValue(), 0.0, heading.getValue());
        programSteps[1] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[2] = new WsAutonomousStepStopDriveUsingMotionProfile();

//        programSteps[3] = new WsAutonomousStepEnableDriveDistancePid();
//        programSteps[4] = new WsAutonomousStepSetDriveDistancePidSetpoint(distance.getValue());
//        programSteps[5] = new WsAutonomousStepWaitForDriveDistancePid();
//        programSteps[6] = new WsAutonomousStepStartDriveUsingMotionProfile(distance.getValue(), 0.0);
//        programSteps[7] = new WsAutonomousStepWaitForDriveMotionProfile(); 
//        programSteps[8] = new WsAutonomousStepStopDriveUsingMotionProfile();
    }

    public String toString() {
        return "TEST Motion profile distance";
    }
}
