package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsFloorPickup;
import com.wildstangs.subsystems.WsIntake;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Liam
 */
public class WsAutonomousStepRaiseAccumulator extends WsAutonomousStep {

    public void initialize() {
        WsFloorPickup subsystem = (WsFloorPickup) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON5);
        BooleanSubject button = (BooleanSubject) subject;
        if (!subsystem.isUp()) {
            button.setValue(false);
        }
        //Latch how many discs are in the accumulator
        ((WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE)).latchNumDiscs();
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Raise the Accumulator";
    }
}
