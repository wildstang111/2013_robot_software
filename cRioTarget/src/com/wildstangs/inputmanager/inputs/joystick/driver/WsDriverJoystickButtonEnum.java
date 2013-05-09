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
public class WsDriverJoystickButtonEnum implements IInputEnum {

    private int index;
    private String name;

    private WsDriverJoystickButtonEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    public static final WsDriverJoystickButtonEnum BUTTON1 = new WsDriverJoystickButtonEnum(0, "D_BUTTON1");
    public static final WsDriverJoystickButtonEnum BUTTON2 = new WsDriverJoystickButtonEnum(1, "D_BUTTON2");
    public static final WsDriverJoystickButtonEnum BUTTON3 = new WsDriverJoystickButtonEnum(2, "D_BUTTON3");
    public static final WsDriverJoystickButtonEnum BUTTON4 = new WsDriverJoystickButtonEnum(3, "D_BUTTON4");
    public static final WsDriverJoystickButtonEnum BUTTON5 = new WsDriverJoystickButtonEnum(4, "D_BUTTON5");
    public static final WsDriverJoystickButtonEnum BUTTON6 = new WsDriverJoystickButtonEnum(5, "D_BUTTON6");
    public static final WsDriverJoystickButtonEnum BUTTON7 = new WsDriverJoystickButtonEnum(6, "D_BUTTON7");
    public static final WsDriverJoystickButtonEnum BUTTON8 = new WsDriverJoystickButtonEnum(7, "D_BUTTON8");
    public static final WsDriverJoystickButtonEnum BUTTON9 = new WsDriverJoystickButtonEnum(8, "D_BUTTON9");
    public static final WsDriverJoystickButtonEnum BUTTON10 = new WsDriverJoystickButtonEnum(9, "D_BUTTON10");
    public static final WsDriverJoystickButtonEnum BUTTON11 = new WsDriverJoystickButtonEnum(10, "D_BUTTON11");
    public static final WsDriverJoystickButtonEnum BUTTON12 = new WsDriverJoystickButtonEnum(11, "D_BUTTON12");

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }

    static public WsDriverJoystickButtonEnum getEnumFromIndex(int index) {
        switch (index) {
            case 0:
                return WsDriverJoystickButtonEnum.BUTTON1;
            case 1:
                return WsDriverJoystickButtonEnum.BUTTON2;
            case 2:
                return WsDriverJoystickButtonEnum.BUTTON3;
            case 3:
                return WsDriverJoystickButtonEnum.BUTTON4;
            case 4:
                return WsDriverJoystickButtonEnum.BUTTON5;
            case 5:
                return WsDriverJoystickButtonEnum.BUTTON6;
            case 6:
                return WsDriverJoystickButtonEnum.BUTTON7;
            case 7:
                return WsDriverJoystickButtonEnum.BUTTON8;
            case 8:
                return WsDriverJoystickButtonEnum.BUTTON9;
            case 9:
                return WsDriverJoystickButtonEnum.BUTTON10;
            case 10:
                return WsDriverJoystickButtonEnum.BUTTON11;
            case 11:
                return WsDriverJoystickButtonEnum.BUTTON12;
            default:
                return null;
        }
    }
}
