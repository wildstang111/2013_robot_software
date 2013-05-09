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
public class WsAutonomousStepWaitForDriveDistancePid extends WsAutonomousStep {

    public WsAutonomousStepWaitForDriveDistancePid() {
    }

    public void initialize() {
    }

    public void update() {
        WsPidStateType pidState = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getDistancePidState();
        if (pidState == WsPidStateType.WS_PID_STABILIZED_STATE) {
            ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).disableDistancePidControl();
            finished = true;
        }
    }

    public String toString() {
        return "Wait for the drive distance PID to stabilize";
    }
}
