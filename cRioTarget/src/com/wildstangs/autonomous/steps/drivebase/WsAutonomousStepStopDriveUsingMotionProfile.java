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
public class WsAutonomousStepStopDriveUsingMotionProfile extends WsAutonomousStep {

    public WsAutonomousStepStopDriveUsingMotionProfile() {
    }

    public void initialize() {
    }

    public void update() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).stopStraightMoveWithMotionProfile();
        finished = true;
    }

    public String toString() {
        return "Stop the drive using motion profile" ; 
    }
}
