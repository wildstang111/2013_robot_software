/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Batman
 */
public class WsAutonomousStepRaiseHopper extends WsAutonomousStep {

    private boolean wait = false;

    public void initialize() {
        wait = false;
        WsHopper subsystem = (WsHopper) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        BooleanSubject button = (BooleanSubject) subject;

        if (!subsystem.isHopperUp()) {
            button.setValue(true);
            wait = true;
        }
    }

    public void update() {
        if (!wait) {
            WsHopper subsystem = (WsHopper) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
            Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
            BooleanSubject button = (BooleanSubject) subject;
            if (button.getValue()) {
                button.setValue(false);
            }
            finished = true;
        } else {
            wait = false;
        }
    }

    public String toString() {
        return "Raise the hopper to use the kicker";
    }
}
