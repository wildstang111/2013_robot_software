/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepSetDriveHeadingPidRelativeSetpoint extends WsAutonomousStep {

    double value;

    public WsAutonomousStepSetDriveHeadingPidRelativeSetpoint(double value) {
        this.value = value;
    }

    public void initialize() {
        double angle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle() + value;
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveHeadingPidSetpoint(angle);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Set the drive heading PID ssetpoint to " + value;
    }
}
