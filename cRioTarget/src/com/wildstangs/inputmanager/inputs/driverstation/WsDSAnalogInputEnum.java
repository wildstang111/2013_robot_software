/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputmanager.inputs.driverstation;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsDSAnalogInputEnum implements IInputEnum {

    private int index;
    private String name;

    private WsDSAnalogInputEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    /**
     * The 4 analog inputs.
     */
    public static final WsDSAnalogInputEnum INPUT1 = new WsDSAnalogInputEnum(1, "Input1");
    public static final WsDSAnalogInputEnum INPUT2 = new WsDSAnalogInputEnum(2, "Input2");
    public static final WsDSAnalogInputEnum INPUT3 = new WsDSAnalogInputEnum(3, "Input3");
    public static final WsDSAnalogInputEnum INPUT4 = new WsDSAnalogInputEnum(4, "Input4");

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

    public static WsDSAnalogInputEnum getEnumFromValue(int i) {
        switch (i) {
            case 1:
                return WsDSAnalogInputEnum.INPUT1;
            case 2:
                return WsDSAnalogInputEnum.INPUT2;
            case 3:
                return WsDSAnalogInputEnum.INPUT3;
            case 4:
                return WsDSAnalogInputEnum.INPUT4;
            default:
                return null;
        }
    }
}
