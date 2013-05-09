package com.wildstangs.outputmanager.base;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.outputmanager.outputs.WsDoubleSolenoid;
import com.wildstangs.outputmanager.outputs.WsDriveSpeed;
import com.wildstangs.outputmanager.outputs.WsRelay;
import com.wildstangs.outputmanager.outputs.WsServo;
import com.wildstangs.outputmanager.outputs.WsSolenoid;
import com.wildstangs.outputmanager.outputs.WsVictor;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public class WsOutputManager {

    private static WsOutputManager instance = null;
    private static List outputs = new List();

    /**
     * Method to obtain the instance of the WsOutputManager singleton.
     *
     * @return the instance of the WsOutputManager.
     */
    public static WsOutputManager getInstance() {
        if (WsOutputManager.instance == null) {
            WsOutputManager.instance = new WsOutputManager();
        }
        return WsOutputManager.instance;
    }

    /**
     * Method to cause all output elements to update.
     */
    public void init() {
    }

    public void update() {
        for (int i = 0; i < outputs.size(); i++) {
            ((IOutput) (((DataElement) outputs.get(i)).getValue())).update();
        }
    }

    /**
     * Method to notify all output elements that a config change has occurred
     * and config values need to be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < outputs.size(); i++) {
            ((IOutput) (((DataElement) outputs.get(i)).getValue())).notifyConfigChange();
        }
    }

    /**
     * Gets an output element based on a key.
     *
     * @param key A string representation of the output element.
     *
     * @return The output element.
     */
    public IOutput getOutput(String key) {
        for (int i = 0; i < outputs.size(); i++) {
            if ((((DataElement) outputs.get(i)).getKey()).equals(key)) {
                return (IOutput) (((DataElement) outputs.get(i)).getValue());
            }
        }
        return (IOutput) null;
    }
    //Key Values - Need to update for each new output element.
    public static final String RIGHT_DRIVE_SPEED = "RightDriveSpeed";
    public static final String LEFT_DRIVE_SPEED = "LeftDriveSpeed";
    public static final String SHIFTER = "Shifter";
    public static final String LIFT = "Lift";
    public static final String KICKER = "Kicker";
    public static final String SHOOTER_VICTOR_ENTER = "ShooterVictorEnter";
    public static final String SHOOTER_VICTOR_EXIT = "ShooterVictorExit";
    public static final String SHOOTER_ANGLE = "ShooterAngle";
    public static final String FRISBIE_CONTROL = "FrisbieControl";
    public static final String ACCUMULATOR_SOLENOID = "AccumulatorSolenoid";
    public static final String ACCUMULATOR_SECONDARY_SOLENOID = "AccumulatorSecondarySolenoid";
    public static final String ACCUMULATOR_VICTOR = "AccumulatorVictor";
    public static final String FUNNELATOR_ROLLER = "FunnelatorRoller";
    public static final String LOADING_RAMP = "LoadingRamp";
    public static final String CLIMBER = "Climber";
    public static final String TOMAHAWK_SERVO = "TomahawkServo";
    public static final String LIGHT_CANNON_RELAY = "LightCannonRelay";
    /**
     * Constructor for WsOutputManager.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    protected WsOutputManager() {
        //Add the facade data elements
        outputs.add(new DataElement(RIGHT_DRIVE_SPEED, new WsDriveSpeed(RIGHT_DRIVE_SPEED, 1, 2)));
        outputs.add(new DataElement(LEFT_DRIVE_SPEED, new WsDriveSpeed(LEFT_DRIVE_SPEED, 3, 4)));
        BooleanConfigFileParameter outputsFor2012 = new BooleanConfigFileParameter(this.getClass().getName(), "2012_Robot", false);
        if (outputsFor2012.getValue()) {
            //Shifter is actually a single solenoid on 4 but 2 is unused for faking it as a double
            outputs.add(new DataElement(SHIFTER, new WsDoubleSolenoid(SHIFTER, 1, 2, 4)));
            outputs.add(new DataElement(FRISBIE_CONTROL, new WsSolenoid(FRISBIE_CONTROL, 1, 5)));

            outputs.add(new DataElement(KICKER, new WsSolenoid(KICKER, 1, 1)));
            outputs.add(new DataElement(ACCUMULATOR_SOLENOID, new WsSolenoid(ACCUMULATOR_SOLENOID, 2, 7)));
            outputs.add(new DataElement(LIFT, new WsDoubleSolenoid(LIFT, 2, 3, 4)));
            outputs.add(new DataElement(SHOOTER_ANGLE, new WsDoubleSolenoid(SHOOTER_ANGLE, 2, 5, 6)));
            outputs.add(new DataElement(SHOOTER_VICTOR_EXIT, new WsVictor(SHOOTER_VICTOR_EXIT, 10)));
            outputs.add(new DataElement(LOADING_RAMP, new WsServo(LOADING_RAMP, 6)));
            outputs.add(new DataElement(FUNNELATOR_ROLLER, new WsVictor(FUNNELATOR_ROLLER, 8)));
            
            outputs.add(new DataElement(CLIMBER, new WsSolenoid(CLIMBER, 2, 1)));

        } else {
            outputs.add(new DataElement(KICKER, new WsSolenoid(KICKER, 1, 1)));
            outputs.add(new DataElement(ACCUMULATOR_SOLENOID, new WsSolenoid(ACCUMULATOR_SOLENOID, 1, 2)));
            outputs.add(new DataElement(ACCUMULATOR_SECONDARY_SOLENOID, new WsSolenoid(ACCUMULATOR_SECONDARY_SOLENOID, 1, 5)));
            outputs.add(new DataElement(FRISBIE_CONTROL, new WsSolenoid(FRISBIE_CONTROL, 1, 3)));
            outputs.add(new DataElement(CLIMBER, new WsSolenoid(CLIMBER, 1, 4)));
            outputs.add(new DataElement(SHIFTER, new WsDoubleSolenoid(SHIFTER, 2, 1, 2)));
            //put the frisbee holder above the lift, so it updates first.
            outputs.add(new DataElement(TOMAHAWK_SERVO, new WsServo(TOMAHAWK_SERVO, 8)));
            outputs.add(new DataElement(LIFT, new WsDoubleSolenoid(LIFT, 2, 3, 4)));
            outputs.add(new DataElement(SHOOTER_ANGLE, new WsDoubleSolenoid(SHOOTER_ANGLE, 2, 5, 6)));
            outputs.add(new DataElement(SHOOTER_VICTOR_EXIT, new WsVictor(SHOOTER_VICTOR_EXIT, 6)));
            outputs.add(new DataElement(LOADING_RAMP, new WsServo(LOADING_RAMP, 7)));
            outputs.add(new DataElement(FUNNELATOR_ROLLER, new WsVictor(FUNNELATOR_ROLLER, 10)));
        }
        outputs.add(new DataElement(SHOOTER_VICTOR_ENTER, new WsVictor(SHOOTER_VICTOR_ENTER, 5)));
        outputs.add(new DataElement(ACCUMULATOR_VICTOR, new WsVictor(ACCUMULATOR_VICTOR, 9)));
        outputs.add(new DataElement(LIGHT_CANNON_RELAY, new WsRelay(1, 2, Relay.Direction.kForward)));
    }
}
