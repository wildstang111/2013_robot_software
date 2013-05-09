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
 * @author User
 */
public class WsAutonomousStepKick extends WsAutonomousStep {

    private boolean waitForKickerFalseToTrue;
    private boolean waitForKickerTrueToFalse;

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        WsHopper subsystem = (WsHopper) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
        //Check if the kicker is already kicking and set up wait
        if (subsystem.getKickerValue() == true) {
            waitForKickerTrueToFalse = true;
            waitForKickerFalseToTrue = false;
        } else {
            subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
            BooleanSubject button = (BooleanSubject) subject;
            button.setValue(true);
            waitForKickerFalseToTrue = true;
            waitForKickerTrueToFalse = false;
        }
    }

    public void update() {
        WsHopper subsystem = (WsHopper) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
        if (waitForKickerTrueToFalse) {
            if (subsystem.getKickerValue() == false) {
                Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
                BooleanSubject button = (BooleanSubject) subject;
                button.setValue(true);
                waitForKickerFalseToTrue = true;
                waitForKickerTrueToFalse = false;
            }
        } else if (waitForKickerFalseToTrue) {
            Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
            BooleanSubject button = (BooleanSubject) subject;
            if (subsystem.getKickerValue() == true) {
                button.setValue(false);
                finished = true;
            } else if (button.getValue() == true) {
                button.setValue(false);
            } else {
                button.setValue(true);
            }
        }
    }

    public String toString() {
        return "Start the kicker cycle";
    }
}
