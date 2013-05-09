package com.wildstangs.inputmanager.inputs.joystick.manipulator;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystickButtonEnum implements IInputEnum {

    private int index;
    private String name;

    private WsManipulatorJoystickButtonEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    public static final WsManipulatorJoystickButtonEnum BUTTON1 = new WsManipulatorJoystickButtonEnum(0, "M_BUTTON1");
    public static final WsManipulatorJoystickButtonEnum BUTTON2 = new WsManipulatorJoystickButtonEnum(1, "M_BUTTON2");
    public static final WsManipulatorJoystickButtonEnum BUTTON3 = new WsManipulatorJoystickButtonEnum(2, "M_BUTTON3");
    public static final WsManipulatorJoystickButtonEnum BUTTON4 = new WsManipulatorJoystickButtonEnum(3, "M_BUTTON4");
    public static final WsManipulatorJoystickButtonEnum BUTTON5 = new WsManipulatorJoystickButtonEnum(4, "M_BUTTON5");
    public static final WsManipulatorJoystickButtonEnum BUTTON6 = new WsManipulatorJoystickButtonEnum(5, "M_BUTTON6");
    public static final WsManipulatorJoystickButtonEnum BUTTON7 = new WsManipulatorJoystickButtonEnum(6, "M_BUTTON7");
    public static final WsManipulatorJoystickButtonEnum BUTTON8 = new WsManipulatorJoystickButtonEnum(7, "M_BUTTON8");
    public static final WsManipulatorJoystickButtonEnum BUTTON9 = new WsManipulatorJoystickButtonEnum(8, "M_BUTTON9");
    public static final WsManipulatorJoystickButtonEnum BUTTON10 = new WsManipulatorJoystickButtonEnum(9, "M_BUTTON10");
    public static final WsManipulatorJoystickButtonEnum BUTTON11 = new WsManipulatorJoystickButtonEnum(10, "M_BUTTON11");
    public static final WsManipulatorJoystickButtonEnum BUTTON12 = new WsManipulatorJoystickButtonEnum(11, "M_BUTTON12");

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }

    static public WsManipulatorJoystickButtonEnum getEnumFromIndex(int index) {
        switch (index) {
            case 0:
                return WsManipulatorJoystickButtonEnum.BUTTON1;
            case 1:
                return WsManipulatorJoystickButtonEnum.BUTTON2;
            case 2:
                return WsManipulatorJoystickButtonEnum.BUTTON3;
            case 3:
                return WsManipulatorJoystickButtonEnum.BUTTON4;
            case 4:
                return WsManipulatorJoystickButtonEnum.BUTTON5;
            case 5:
                return WsManipulatorJoystickButtonEnum.BUTTON6;
            case 6:
                return WsManipulatorJoystickButtonEnum.BUTTON7;
            case 7:
                return WsManipulatorJoystickButtonEnum.BUTTON8;
            case 8:
                return WsManipulatorJoystickButtonEnum.BUTTON9;
            case 9:
                return WsManipulatorJoystickButtonEnum.BUTTON10;
            case 10:
                return WsManipulatorJoystickButtonEnum.BUTTON11;
            case 11:
                return WsManipulatorJoystickButtonEnum.BUTTON12;
            default:
                return null;
        }
    }
}
