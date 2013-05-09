package com.wildstangs.simulation.digitalInputs;

import com.wildstangs.inputmanager.base.IInputEnum;

/**
 *  Enum types used for the Driver Joystick class.  
 * 
 * @author chadschmidt
 */
public class WsDigitalInputEnum implements IInputEnum {
    


  private int index;
  private String name;
 
private WsDigitalInputEnum(int index, String desc){
    this.index = index; 
    this.name = desc; 
    
} 

public static final WsDigitalInputEnum INPUT1 = new WsDigitalInputEnum(1, "INPUT1"); 
public static final WsDigitalInputEnum INPUT2 = new WsDigitalInputEnum(2, "INPUT2"); 
public static final WsDigitalInputEnum INPUT3 = new WsDigitalInputEnum(3, "INPUT3"); 
public static final WsDigitalInputEnum INPUT4 = new WsDigitalInputEnum(4, "INPUT4"); 
public static final WsDigitalInputEnum INPUT5 = new WsDigitalInputEnum(5, "INPUT5"); 
public static final WsDigitalInputEnum INPUT6 = new WsDigitalInputEnum(6, "INPUT6"); 
public static final WsDigitalInputEnum INPUT7 = new WsDigitalInputEnum(7, "INPUT7"); 
public static final WsDigitalInputEnum INPUT8 = new WsDigitalInputEnum(8, "INPUT8"); 
public static final WsDigitalInputEnum INPUT9 = new WsDigitalInputEnum(9, "INPUT9"); 
public static final WsDigitalInputEnum INPUT10 = new WsDigitalInputEnum(0, "INPUT10"); 
public static final WsDigitalInputEnum INPUT11 = new WsDigitalInputEnum(10, "INPUT11"); 
public static final WsDigitalInputEnum INPUT12 = new WsDigitalInputEnum(11, "INPUT12"); 
public static final WsDigitalInputEnum INPUT13 = new WsDigitalInputEnum(12, "INPUT13"); 
public static final WsDigitalInputEnum INPUT14 = new WsDigitalInputEnum(13, "INPUT14"); 
public static final WsDigitalInputEnum INPUT15 = new WsDigitalInputEnum(14, "INPUT15"); 
public static final WsDigitalInputEnum INPUT16 = new WsDigitalInputEnum(15, "INPUT16"); 

public static WsDigitalInputEnum getEnumFromChannel(int channel)
{
    switch (channel)
    {
        case 1:
            return INPUT1;
        case 2:
            return INPUT2;
        case 3:
            return INPUT3;
        case 4:
            return INPUT4;
        case 5:
            return INPUT5;
        case 6:
            return INPUT6;
        case 7:
            return INPUT7;
        case 8:
            return INPUT8;
        case 9:
            return INPUT9;
        case 10:
            return INPUT10;
        case 11:
            return INPUT11;
        case 12:
            return INPUT12;
        case 13:
            return INPUT13;
        case 14:
            return INPUT14;
        case 15:
            return INPUT15;
        case 16:
            return INPUT16;
        default:
            return null;
    }
}
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
