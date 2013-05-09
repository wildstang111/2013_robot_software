package com.wildstangs.inputmanager.base;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.inputmanager.inputs.WsDigitalInput;
import com.wildstangs.inputmanager.inputs.driverstation.WsDSAnalogInput;
import com.wildstangs.inputmanager.inputs.driverstation.WsDSDigitalInput;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystick;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystick;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public class WsInputManager {

    private static WsInputManager instance = null;
    private static List oiInputs = new List();
    private static List sensorInputs = new List();

    /**
     * Method to get the instance of this singleton object.
     *
     * @return The instance of WsInputManager
     */
    public static WsInputManager getInstance() {
        if (instance == null) {
            instance = new WsInputManager();
        }
        return instance;
    }

    public void init() {
    }

    /**
     * Method to trigger updates of all the sensor data input containers
     */
    public void updateSensorData() {
        IInput sIn;
        for (int i = 0; i < sensorInputs.size(); i++) {
            sIn = (IInput) (((DataElement) sensorInputs.get(i)).getValue());
            sIn.pullData();
            sIn.update();
        }
    }

    /**
     * Method to trigger updates of all the oi data input containers.
     */
    public void updateOiData() {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++) {
            oiIn = (IInput) (((DataElement) oiInputs.get(i)).getValue());
            oiIn.pullData();
            oiIn.update();
        }
    }

    public void updateOiDataAutonomous() {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++) {
            oiIn = (IInput) (((DataElement) oiInputs.get(i)).getValue());
            if (!(oiIn instanceof WsDriverJoystick || oiIn instanceof WsManipulatorJoystick)) {
                oiIn.pullData();
            }
            oiIn.update();
        }
    }

    /**
     * Method to notify all input containers that a config update occurred.
     *
     * Used by the ConfigFacade when the config is re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < sensorInputs.size(); i++) {
            ((IInput) (((DataElement) sensorInputs.get(i)).getValue())).notifyConfigChange();
        }
        for (int i = 0; i < oiInputs.size(); i++) {
            ((IInput) (((DataElement) oiInputs.get(i)).getValue())).notifyConfigChange();
        }
    }

    /**
     * Gets an OI container, based on a key.
     *
     * @param key The key that represents the OI input container
     * @return A WsInputInterface.
     */
    public IInput getOiInput(String key) {
        for (int i = 0; i < oiInputs.size(); i++) {
            if ((((DataElement) oiInputs.get(i)).getKey()).equals(key)) {
                return (IInput) (((DataElement) oiInputs.get(i)).getValue());
            }
        }
        return (IInput) null;
    }

    /**
     * Gets a sensor container, based on a key.
     *
     * @param key The key that represents the sensor input container
     * @return A WsInputInterface.
     */
    public IInput getSensorInput(String key) {
        for (int i = 0; i < sensorInputs.size(); i++) {
            if ((((DataElement) sensorInputs.get(i)).getKey()).equals(key)) {
                return (IInput) (((DataElement) sensorInputs.get(i)).getValue());
            }
        }
        return (IInput) null;
    }
    
    final public void attachJoystickButton(IInputEnum button, IObserver observer ) {
        if (button instanceof WsDriverJoystickButtonEnum){
            Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(button);
            subject.attach(observer);
        }else if (button instanceof WsManipulatorJoystickButtonEnum){
            Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(button);
            subject.attach(observer);
        }else {
            //Function was not called with proper input
        }
    }
    /**
     * Keys to represent Inputs
     */
    public static final String DRIVER_JOYSTICK = "DriverJoystick";
    public static final String MANIPULATOR_JOYSTICK = "ManipulatorJoystick";
    public static final String ENTER_WHEEL_SHOOTER_SPEED_INPUT = "EnterWheelShooterSpeedInput";
    public static final String EXIT_WHEEL_SHOOTER_SPEED_INPUT = "ExitWheelShooterSpeedInput";
    public static final String SHOOTER_WHEEL_SPEED_OVERRIDE = "ShooterWheelSpeedOverride";
    public static final String AUTO_PROGRAM_SELECTOR = "AutoProgramSelector";
    public static final String LOCK_IN_SWITCH = "LockInSwitch";
    public static final String START_POSITION_SELECTOR = "StartPositionSelector";
    public static final String LEFT_ACCUMULATOR_LIMIT_SWITCH = "LeftAccumulatorLimitSwitch";
    public static final String RIGHT_ACCUMULATOR_LIMIT_SWITCH = "RightAccumulatorLimitSwitch";
    public static final String FUNNELATOR_LIMIT_SWITCH = "FunnelatorLimitSwitch";
    public static final String HOPPER_UP_LIMIT_SWITCH = "HopperUpLimitSwitch";
    public static final String HOPPER_DOWN_LIMIT_SWITCH = "HopperDownLimitSwitch";
    public static final String ACCUMULATOR_UP_LIMIT_SWITCH = "AccumulatorUpLimitSwitch";

    /**
     * Constructor for the WsInputManager.
     *
     * Each new data element to be added to the facade must be added here and
     * have keys added above.
     */
    protected WsInputManager() {
        //Add the facade data elements
        BooleanConfigFileParameter outputsFor2012 = new BooleanConfigFileParameter(WsOutputManager.getInstance().getClass().getName(), "2012_Robot", false);
        if (outputsFor2012.getValue()) {
            sensorInputs.add(new DataElement(RIGHT_ACCUMULATOR_LIMIT_SWITCH, new WsDigitalInput(11)));
            sensorInputs.add(new DataElement(FUNNELATOR_LIMIT_SWITCH, new WsDigitalInput(7)));
        } else {
            sensorInputs.add(new DataElement(RIGHT_ACCUMULATOR_LIMIT_SWITCH, new WsDigitalInput(7)));
            sensorInputs.add(new DataElement(FUNNELATOR_LIMIT_SWITCH, new WsDigitalInput(9)));
        }
        oiInputs.add(new DataElement(DRIVER_JOYSTICK, new WsDriverJoystick()));
        oiInputs.add(new DataElement(MANIPULATOR_JOYSTICK, new WsManipulatorJoystick()));
//        oiInputs.add(new DataElement(ENTER_WHEEL_SHOOTER_SPEED_INPUT, new WsDSAnalogInput(5)));
//        oiInputs.add(new DataElement(EXIT_WHEEL_SHOOTER_SPEED_INPUT, new WsDSAnalogInput(7)));
        oiInputs.add(new DataElement(ENTER_WHEEL_SHOOTER_SPEED_INPUT, new WsDSAnalogInput(3)));
        oiInputs.add(new DataElement(EXIT_WHEEL_SHOOTER_SPEED_INPUT, new WsDSAnalogInput(4)));
        oiInputs.add(new DataElement(SHOOTER_WHEEL_SPEED_OVERRIDE, new WsDSDigitalInput(2)));
        oiInputs.add(new DataElement(AUTO_PROGRAM_SELECTOR, new WsDSAnalogInput(1)));
        oiInputs.add(new DataElement(LOCK_IN_SWITCH, new WsDSDigitalInput(1)));
        oiInputs.add(new DataElement(START_POSITION_SELECTOR, new WsDSAnalogInput(2)));
        sensorInputs.add(new DataElement(LEFT_ACCUMULATOR_LIMIT_SWITCH, new WsDigitalInput(6)));
        sensorInputs.add(new DataElement(HOPPER_DOWN_LIMIT_SWITCH, new WsDigitalInput(13)));
        sensorInputs.add(new DataElement(HOPPER_UP_LIMIT_SWITCH, new WsDigitalInput(12)));
        sensorInputs.add(new DataElement(ACCUMULATOR_UP_LIMIT_SWITCH, new WsDigitalInput(8)));
    }
}
