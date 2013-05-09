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
public class WsAutonomousStepWaitForDriveHeadingPid extends WsAutonomousStep {

    public WsAutonomousStepWaitForDriveHeadingPid() {
    }

    public void initialize() {
    }

    public void update() {
        WsPidStateType pidState = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getHeadingPidState();
        if (pidState == WsPidStateType.WS_PID_STABILIZED_STATE) {
            ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).disableHeadingPidControl();
            finished = true;
        }
    }

    public String toString() {
        return "Wait for the drive heading PID to stabilize";
    }
}
