package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsFloorPickup;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Liam
 */
public class WsAutonomousStepIntakeMotorPushFrisbeesOut extends WsAutonomousStep {

    public void initialize() {
        WsFloorPickup subsystem = (WsFloorPickup) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        BooleanSubject forwardButton = (BooleanSubject) subject;

        if (subsystem.getMotorForward() == false) {
            forwardButton.setValue(true);
        }
        finished = true;

    }

    public void update() {
    }

    public String toString() {
        return "Set motors to push frisbees out";
    }
}
