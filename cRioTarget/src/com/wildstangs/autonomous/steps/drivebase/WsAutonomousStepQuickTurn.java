/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Joey
 */
public class WsAutonomousStepQuickTurn extends WsAutonomousStep {

    private double value, angle;
    private boolean shouldFinish = false;

    public WsAutonomousStepQuickTurn(double relativeAngle) {
        this.value = relativeAngle;
    }

    public void initialize() {

        angle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle() + value;
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setThrottleValue(0);
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).overrideHeadingValue(value < 0 ? 0.6 : -0.6);

        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.THROTTLE, new Double(0.0));
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(value < 0 ? 0.6 : -0.6));

    }

    public void update() {
        if (shouldFinish) {
            finished = true;
            ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).overrideHeadingValue(0.0);
            return;
        }
        double gyroAngle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle();
        if (value < 0) {
            if (angle > gyroAngle) {
                ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).overrideHeadingValue(0.0);
                WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(0.0));
                shouldFinish = true;
            }
        } else {
            if (angle < gyroAngle) {
                ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).overrideHeadingValue(0.0);
                WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(0.0));
                shouldFinish = true;
            }
        }
    }

    public String toString() {
        return "Turning using quickturn with a relative angle";
    }
}
