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
public class WsAutonomousStepOverrideFunnelatorButtonOff extends WsAutonomousStep {

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        BooleanSubject backButton = (BooleanSubject) subject;

        backButton.setValue(false);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Turn on the funnelator presets";
    }
}
