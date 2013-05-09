/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.joystick.manipulator;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystickEnum implements IInputEnum {

    private int index;
    private String name;

    private WsManipulatorJoystickEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    public static final WsManipulatorJoystickEnum ENTER_FLYWHEEL_ADJUSTMENT = new WsManipulatorJoystickEnum(0, "ENTER_FLYWHEEL_ADJUSTMENT");
    public static final WsManipulatorJoystickEnum EXIT_FLYWHEEL_ADJUSTMENT = new WsManipulatorJoystickEnum(1, "EXIT_FLYWHEEL_ADJUSTMENT");
    public static final WsManipulatorJoystickEnum D_PAD_UP_DOWN = new WsManipulatorJoystickEnum(2, "D_PAD_UP_DOWN");
    public static final WsManipulatorJoystickEnum D_PAD_LEFT_RIGHT = new WsManipulatorJoystickEnum(3, "D_PAD_LEFT_RIGHT");

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }
}
