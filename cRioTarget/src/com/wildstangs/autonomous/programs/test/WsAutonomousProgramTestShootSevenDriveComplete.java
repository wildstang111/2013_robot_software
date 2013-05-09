/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousDoubleConfigFileParameter;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.config.DoubleConfigFileParameter;

public class WsAutonomousProgramTestShootSevenDriveComplete extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstDrive;
    private DoubleConfigFileParameter secondDrive;
    private DoubleConfigFileParameter thirdDrive;
    private DoubleConfigFileParameter thirdDriveHeading;

    private void defineConfigValues() {
        firstDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.FirstDrive", 27);
        secondDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.SecondDrive", 109);
        thirdDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDrive", -58);
        thirdDriveHeading = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDriveHeading", 0.22);
        
    }

    public WsAutonomousProgramTestShootSevenDriveComplete() {
        super(12);
    }

    public void defineSteps() {
        defineConfigValues();
        programSteps[0] = new WsAutonomousStepStartDriveUsingMotionProfile(firstDrive.getValue(), 0.0); 
        programSteps[1] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[2] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[3] = new WsAutonomousStepDelay(2000);
        programSteps[4] = new WsAutonomousStepStartDriveUsingMotionProfile(secondDrive.getValue(), 0.0); 
        programSteps[5] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[6] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[7] = new WsAutonomousStepDelay(2000);
        programSteps[8] = new WsAutonomousStepStartDriveUsingMotionProfileAndHeading(thirdDrive.getValue(), 0.0, thirdDriveHeading.getValue()); 
        programSteps[9] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[10] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[11] = new WsAutonomousStepDelay(2000);
    }

    public String toString() {
        return "TEST COMPLETE ShootSeven Drive distances";
    }
}
