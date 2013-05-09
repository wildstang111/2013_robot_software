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
public class WsAutonomousStepSetDriveDistancePidSetpoint extends WsAutonomousStep {

    double value;

    public WsAutonomousStepSetDriveDistancePidSetpoint(double value) {
        this.value = value;
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(value);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Set the drive distance PID setpoint to " + value;
    }
}
