/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepDisableDriveDistancePid extends WsAutonomousStep {

    public WsAutonomousStepDisableDriveDistancePid() {
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).disableDistancePidControl();
        Logger.getLogger().info(this.toString(), "initialize", "Drive Distance pid is disabled");
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Disable the drive distance PID";
    }
}
