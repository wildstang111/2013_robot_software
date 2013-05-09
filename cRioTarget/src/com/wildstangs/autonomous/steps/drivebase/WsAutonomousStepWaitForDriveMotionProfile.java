/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.pid.controller.base.WsPidStateType;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepWaitForDriveMotionProfile extends WsAutonomousStep {

    public WsAutonomousStepWaitForDriveMotionProfile() {
    }

    public void initialize() {
    }

    public void update() {
        double distanceRemaining = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getDistanceRemaining();
        double velocity = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getVelocity();
        if ((distanceRemaining < 0.01) &&
            (distanceRemaining > -0.01))  {
            finished = true;
        } 
        if ((distanceRemaining < 12.0) &&
            (distanceRemaining > -12.0) &&
                (velocity < 0.10) &&
                (velocity > -0.10))  {
            finished = true; 
    
        }
    }

    public String toString() {
        return "Wait for the motion profile to finish moving to target";
    }
}
