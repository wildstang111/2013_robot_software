package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Liam
 */
public class WsAutonomousStepOverrideFunnelatorUpButtonOn extends WsAutonomousStep {

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON9);
        BooleanSubject backButton = (BooleanSubject) subject;

        backButton.setValue(true);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Turn off the funnelator gate";
    }
}
