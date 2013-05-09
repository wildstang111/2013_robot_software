/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.joystick;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import edu.wpi.first.wpilibj.Joystick;
import java.util.Arrays;

/**
 *
 * @author chadschmidt
 */
public class WsHardwareJoystick implements IHardwareJoystick {

    private HIDDeviceInfo hIDJoystickInfo = null; 
    private HIDDevice hIDJoystick = null; 
    private int startOfAxisDataIndex; 
    private int halfByteButtonIndex; 
    private int wholeByteButtonIndex; 
            
    static int[] connectedDeviceProductIDs = {0,0}; 
    static String[] connectedDevicePaths = {"",""}; 
    static int connectedJoysticks = 0 ; 
    
    private final double[] m_axis_values_from_usb;
    private int m_button_values_from_usb; 
    
    private final byte[] m_axes_mapping;
    private final byte[] m_buttons_mapping;

    public WsHardwareJoystick() {
        m_axis_values_from_usb = new double[6];
        m_axes_mapping = new byte[6];
        m_buttons_mapping = new byte[2];
        
        m_axes_mapping[AxisType.kX.value] = kDefaultXAxis;
        m_axes_mapping[AxisType.kY.value] = kDefaultYAxis;
        m_axes_mapping[AxisType.kZ.value] = kDefaultZAxis;
        m_axes_mapping[AxisType.kTwist.value] = kDefaultTwistAxis;
        m_axes_mapping[AxisType.kThrottle.value] = kDefaultThrottleAxis;

        m_buttons_mapping[ButtonType.kTrigger.value] = kDefaultTriggerButton;
        m_buttons_mapping[ButtonType.kTop.value] = kDefaultTopButton;

        
    }
    
    public boolean initializeJoystick(){
        
        //See if any joysticks are connected
        ClassPathLibraryLoader.loadNativeHIDLibrary();
        try {
            HIDDeviceInfo[] devices = HIDManager.getInstance().listDevices();
            for (HIDDeviceInfo hIDDeviceInfo : devices) {
                int usage = hIDDeviceInfo.getUsage(); 
                if ((usage == 4 ) || (usage == 5)){
                    
                    if (connectedJoysticks !=0){
                        System.out.println("Joystick found with productID " + hIDDeviceInfo.getProduct_id());
                        boolean alreadyUsed = false; 
                        for (int i = 0; i < connectedJoysticks; i++) {
                            if (connectedDeviceProductIDs[i] == hIDDeviceInfo.getProduct_id()){
                                if (connectedDevicePaths[i].equalsIgnoreCase(hIDDeviceInfo.getPath())){
                                    alreadyUsed = true; 
                                }
                                break;
                            }
                        }
                        if (alreadyUsed){ 
                            //Use a different device
                            hIDJoystickInfo = hIDDeviceInfo; 
//                    System.out.println("Joystick found with usage " + usage);
//                    System.out.println("Joystick found with device info " + hIDJoystickInfo);
                    
                            continue;
                        }
                    }
                    hIDJoystickInfo = hIDDeviceInfo; 
//                    System.out.println("Joystick found with usage " + usage);
//                    System.out.println("Joystick found with device info " + hIDJoystickInfo);
                    hIDJoystick = hIDJoystickInfo.open();
                    hIDJoystick.disableBlocking();
                    connectedDeviceProductIDs[connectedJoysticks] = hIDDeviceInfo.getProduct_id();
                    connectedDevicePaths[connectedJoysticks]= hIDDeviceInfo.getPath();
                    connectedJoysticks++; 
                    switch (hIDJoystickInfo.getUsage()) {
                        case 4:
                            startOfAxisDataIndex= 0;
                            halfByteButtonIndex = 4; 
                            wholeByteButtonIndex = 5; 
                            break;
                        case 5:
                            startOfAxisDataIndex= 2; 
                            halfByteButtonIndex = 1; 
                            wholeByteButtonIndex = 0; 
                            break;
                        default:
                            throw new AssertionError();
                    }
                    
                    break; 
                }
            }
        } catch (Exception e) {
            hIDJoystickInfo = null; 
            hIDJoystick = null; 
        }
        
        if (hIDJoystick == null){
            return false; 
        }else { 
            return true; 
        }
        
    }
    
    
    public void pullData() {
        //Move data from usb to variables
        

        //Get input reports
        byte[] inbuf = new byte[8]; 
        Arrays.fill(inbuf, (byte)0);
        try {
            hIDJoystick.read(inbuf);
            
        } catch (Exception e) {
            System.out.println("Read didn't return ok: " + inbuf[0] + e);
            
        }
        boolean dataRefreshed = false; 
        for (int i = 0; i < inbuf.length; i++) {
            if(inbuf[i] != 0){
                dataRefreshed = true; 
                break; 
            }
        }
        if (dataRefreshed){ 
            for (int i = 0; i < m_axis_values_from_usb.length; i++) {
                m_axis_values_from_usb[i] = getJoystickValueFromUSB(inbuf[i+startOfAxisDataIndex]);
                //Reverse the signs on the vertical axis
                if (i%2 == 1){
                   m_axis_values_from_usb[i] *= -1;  
                }
            }
//            System.out.println("USBRead: " + Arrays.toString(inbuf) + " Axis: " + Arrays.toString(m_axis_values_from_usb)); 
            m_button_values_from_usb = getButtonValueFromUSB(inbuf);
//            
//            do {                
//                
//            Arrays.fill(inbuf, (byte)0);
//            try {
//                hIDJoystick.read(inbuf);
//
//            } catch (Exception e) {
//                System.out.println("Read didn't return ok: " + inbuf[0] + e);
//
//            }
//            dataRefreshed = false; 
//            for (int i = 0; i < inbuf.length; i++) {
//                if(inbuf[i] != 0){
//                    dataRefreshed = true; 
//                    break; 
//                }
//            }
//            } while (dataRefreshed);
        }
    }
    
    //This converts from 0 -255 unsigned scale or really weird signed 0to127 and -128to-1 
    private double getJoystickValueFromUSB(byte signedByte){
        double axisValue; 
        if (signedByte >= 0 ){
            axisValue = -1.0 + (signedByte/128.0);
        }else {
            axisValue = ((signedByte+128.0)/127.0);
        }
        return axisValue;
    }
    
    //This converts from the report from USB to the ordered buttons based on usage
    private short getButtonValueFromUSB(byte[] inbuf ){
        short buttonValues; 
        short topByte ; 
        short bottomByte; 
        switch (hIDJoystickInfo.getUsage()) {
                        case 4:
                            //Mapping is bottom 4 bits is in half byte and top 8 bits are in whole bit
                            topByte = (short)((inbuf[wholeByteButtonIndex] * 0x10) & 0x0FF0);
                            bottomByte = (short)((inbuf[halfByteButtonIndex] >>> 4) & 0x000F) ;
                            buttonValues =  ((short)(topByte | bottomByte)) ;
                            System.out.println(String.format("Buttons: %X %X to %02X", inbuf[halfByteButtonIndex], inbuf[wholeByteButtonIndex] , buttonValues ));
                            break;
                        case 5:
                            //Mapping is top 4 bits is in half byte and bottom 8 bits are in whole bit
                            topByte = (short)((inbuf[halfByteButtonIndex] * 0x0100) & 0x0F00);
                            bottomByte = (short)(inbuf[wholeByteButtonIndex] & 0xFF);
                            buttonValues =  ((short)(topByte | bottomByte)) ; 
                            System.out.println(String.format("Buttons: %X %X to %02X", inbuf[halfByteButtonIndex], inbuf[wholeByteButtonIndex] , buttonValues ));
                            break;
                        default:
                            buttonValues= 0 ; 
                            throw new AssertionError();
        }
        return buttonValues; 
    
    }

    static final byte kDefaultXAxis = 1;
    static final byte kDefaultYAxis = 2;
    static final byte kDefaultZAxis = 3;
    static final byte kDefaultTwistAxis = 3;
    static final byte kDefaultThrottleAxis = 4;
    static final int kDefaultTriggerButton = 1;
    static final int kDefaultTopButton = 2;

    /**
     * Represents an analog axis on a joystick.
     */
    public static class AxisType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kX_val = 0;
        static final int kY_val = 1;
        static final int kZ_val = 2;
        static final int kTwist_val = 3;
        static final int kThrottle_val = 4;
        static final int kNumAxis_val = 5;
        /**
         * axis: x-axis
         */
        public static final AxisType kX = new AxisType(kX_val);
        /**
         * axis: y-axis
         */
        public static final AxisType kY = new AxisType(kY_val);
        /**
         * axis: z-axis
         */
        public static final AxisType kZ = new AxisType(kZ_val);
        /**
         * axis: twist
         */
        public static final AxisType kTwist = new AxisType(kTwist_val);
        /**
         * axis: throttle
         */
        public static final AxisType kThrottle = new AxisType(kThrottle_val);
        /**
         * axis: number of axis
         */
        public static final AxisType kNumAxis = new AxisType(kNumAxis_val);

        private AxisType(int value) {
            this.value = value;
        }
    }

    /**
     * Represents a digital button on the JoyStick
     */
    public static class ButtonType {

        /**
         * The integer value representing this enumeration
         */
        public final int value;
        static final int kTrigger_val = 0;
        static final int kTop_val = 1;
        static final int kNumButton_val = 2;
        /**
         * button: trigger
         */
        public static final ButtonType kTrigger = new ButtonType((kTrigger_val));
        /**
         * button: top button
         */
        public static final ButtonType kTop = new ButtonType(kTop_val);
        /**
         * button: num button types
         */
        public static final ButtonType kNumButton = new ButtonType((kNumButton_val));

        private ButtonType(int value) {
            this.value = value;
        }
    }
    
    /**
     * Construct an instance of a joystick.
     * The joystick index is the usb port on the drivers station.
     *
     * @param port The port on the driver station that the joystick is plugged into.
     */


    /**
     * Get the X value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @param hand Unused
     * @return The X value of the joystick.
     */
    public double getX() {
        return getRawAxis(m_axes_mapping[AxisType.kX.value]);
    }

    /**
     * Get the Y value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @param hand Unused
     * @return The Y value of the joystick.
     */
    public double getY() {
        return getRawAxis(m_axes_mapping[AxisType.kY.value]);
    }

    /**
     * Get the Z value of the joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @param hand Unused
     * @return The Z value of the joystick.
     */
    public double getZ() {
        return getRawAxis(m_axes_mapping[AxisType.kZ.value]);
    }

    /**
     * Get the twist value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @return The Twist value of the joystick.
     */
    public double getTwist() {
        return getRawAxis(m_axes_mapping[AxisType.kTwist.value]);
    }

    /**
     * Get the throttle value of the current joystick.
     * This depends on the mapping of the joystick connected to the current port.
     *
     * @return The Throttle value of the joystick.
     */
    public double getThrottle() {
        return getRawAxis(m_axes_mapping[AxisType.kThrottle.value]);
    }

    /**
     * Get the value of the axis.
     *
     * @param axis The axis to read [1-6].
     * @return The value of the axis.
     */
    public double getRawAxis(final int axis) {
        return m_axis_values_from_usb[axis-1]; 
    }

    /**
     * For the current joystick, return the axis determined by the argument.
     *
     * This is for cases where the joystick axis is returned programatically, otherwise one of the
     * previous functions would be preferable (for example getX()).
     *
     * @param axis The axis to read.
     * @return The value of the axis.
     */
    public double getAxis(final AxisType axis) {
        switch (axis.value) {
            case AxisType.kX_val:
                return getX();
            case AxisType.kY_val:
                return getY();
            case AxisType.kZ_val:
                return getZ();
            case AxisType.kTwist_val:
                return getTwist();
            case AxisType.kThrottle_val:
                return getThrottle();
            default:
                return 0.0;
        }
    }

    /**
     * Read the state of the trigger on the joystick.
     *
     * Look up which button has been assigned to the trigger and read its state.
     *
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the trigger.
     */
    public boolean getTrigger() {
        return getRawButton(m_buttons_mapping[ButtonType.kTrigger.value]);
    }

    /**
     * Read the state of the top button on the joystick.
     *
     * Look up which button has been assigned to the top and read its state.
     *
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the top button.
     */
    public boolean getTop() {
        return getRawButton(m_buttons_mapping[ButtonType.kTop.value]);
    }

    /**
     * This is not supported for the Joystick.
     * This method is only here to complete the GenericHID interface.
     *
     * @param hand This parameter is ignored for the Joystick class and is only here to complete the GenericHID interface.
     * @return The state of the bumper (always false)
     */
    public boolean getBumper() {
        return false;
    }

    /**
     * Get the button value for buttons 1 through 12.
     *
     * The buttons are returned in a single 16 bit value with one bit representing the state
     * of each button. The appropriate button is returned as a boolean value.
     *
     * @param button The button number to be read.
     * @return The state of the button.
     */
    public boolean getRawButton(final int button) {
        return ((0x1 << (button - 1)) & m_button_values_from_usb) != 0;
    }

    /**
     * Get buttons based on an enumerated type.
     *
     * The button type will be looked up in the list of buttons and then read.
     *
     * @param button The type of button to read.
     * @return The state of the button.
     */
    public boolean getButton(ButtonType button) {
        switch (button.value) {
            case ButtonType.kTrigger_val:
                return getTrigger();
            case ButtonType.kTop_val:
                return getTop();
            default:
                return false;
        }
    }

    /**
     * Get the magnitude of the direction vector formed by the joystick's
     * current position relative to its origin
     *
     * @return The magnitude of the direction vector
     */
    public double getMagnitude() {
        return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in radians
     *
     * @return The direction of the vector in radians
     */
    public double getDirectionRadians() {
        return Math.atan2(getX(), -getY());
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in degrees
     *
     * uses acos(-1) to represent Pi due to absence of readily accessable Pi
     * constant in C++
     *
     * @return The direction of the vector in degrees
     */
    public double getDirectionDegrees() {
        return Math.toDegrees(getDirectionRadians());
    }

    /**
     * Get the channel currently associated with the specified axis.
     *
     * @param axis The axis to look up the channel for.
     * @return The channel fr the axis.
     */
    public int getAxisChannel(Joystick.AxisType axis) {
        return m_axes_mapping[axis.value];
    }

    /**
     * Set the channel associated with a specified axis.
     *
     * @param axis The axis to set the channel for.
     * @param channel The channel to set the axis to.
     */
    public void setAxisChannel(Joystick.AxisType axis, int channel) {
        m_axes_mapping[axis.value] = (byte) channel;
    }
    
    
}
