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
public class WsAutonomousStepEnableDriveDistancePid extends WsAutonomousStep {

    public WsAutonomousStepEnableDriveDistancePid() {
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
        Logger.getLogger().info(this.toString(), "initialize", "Drive Distance pid is enabled");
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Enable the drive distance PID";
    }
}
