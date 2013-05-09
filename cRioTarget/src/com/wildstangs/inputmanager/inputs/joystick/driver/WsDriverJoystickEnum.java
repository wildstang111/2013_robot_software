/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.joystick.driver;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsDriverJoystickEnum implements IInputEnum {

    private int index;
    private String name;

    private WsDriverJoystickEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    /**
     * Throttle enum type.
     */
    public static final WsDriverJoystickEnum THROTTLE = new WsDriverJoystickEnum(0, "THROTTLE");
    /**
     * Heading enum type.
     */
    public static final WsDriverJoystickEnum HEADING = new WsDriverJoystickEnum(1, "HEADING");

    /**
     * D-Pad enum types
     */
    public static final WsDriverJoystickEnum D_PAD_UP_DOWN = new WsDriverJoystickEnum(2, "D_PAD_UP_DOWN"); 
    
    /**
     * Converts the enum type to a String.
     *
     * @return A string representing the enum.
     */
    public String toString() {
        return name;
    }

    /**
     * Converts the enum type to a numeric value.
     *
     * @return An integer representing the enum.
     */
    public int toValue() {
        return index;
    }
}
