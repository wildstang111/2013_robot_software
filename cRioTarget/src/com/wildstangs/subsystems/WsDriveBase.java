/*
 * Example drive base for the simulation env.
 */
package com.wildstangs.subsystems;

import com.wildstangs.autonomous.parameters.AutonomousBooleanConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousDoubleConfigFileParameter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.logger.Logger;
import com.wildstangs.motionprofile.ContinuousAccelFilter;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.pid.controller.base.WsPidController;
import com.wildstangs.pid.controller.base.WsPidStateType;
import com.wildstangs.pid.controller.base.WsSpeedPidController;
import com.wildstangs.pid.inputs.WsDriveBaseDistancePidInput;
import com.wildstangs.pid.inputs.WsDriveBaseHeadingPidInput;
import com.wildstangs.pid.inputs.WsDriveBaseSpeedPidInput;
import com.wildstangs.pid.outputs.WsDriveBaseDistancePidOutput;
import com.wildstangs.pid.outputs.WsDriveBaseHeadingPidOutput;
import com.wildstangs.pid.outputs.WsDriveBaseSpeedPidOutput;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Smitty
 */
public class WsDriveBase extends WsSubsystem implements IObserver {

    private static final double MAX_INPUT_THROTTLE_VALUE = 1.0;
    private static final double MAX_NEG_INPUT_THROTTLE_VALUE = -1.0;
    private static final double MAX_INPUT_HEADING_VALUE = 1.0;
    private static final double MAX_NEG_INPUT_HEADING_VALUE = -1.0;
    private static final double HEADING_SENSITIVITY = 1.8;
    private static final double MAX_MOTOR_OUTPUT = 1.0;
    private static final double NEG_MAX_MOTOR_OUTPUT = -1.0;
    private static final double ANTI_TURBO_MAX_DEFLECTION = 0.500;
    private static double THROTTLE_LOW_GEAR_ACCEL_FACTOR = 0.250;
    private static double HEADING_LOW_GEAR_ACCEL_FACTOR = 0.500;
    private static double THROTTLE_HIGH_GEAR_ACCEL_FACTOR = 0.125;
    private static double HEADING_HIGH_GEAR_ACCEL_FACTOR = 0.250;
    private static double TICKS_PER_ROTATION = 360.0;
    private static double WHEEL_DIAMETER = 6;
    private static double MAX_HIGH_GEAR_PERCENT = 0.80;
    private static double ENCODER_GEAR_RATIO = 7.5;
    private static double DEADBAND = 0.05;
    private static double SLOW_TURN_FORWARD_SPEED;
    private static double SLOW_TURN_BACKWARD_SPEED;
    private static double MAX_ACCELERATION_DRIVE_PROFILE = 600.0;
    private static double STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR = 10.0;
    private static double DRIVE_OFFSET = 1.0;
    private static boolean USE_LEFT_SIDE_FOR_OFFSET = true;
    private static double QUICK_TURN_CAP;
    private static double QUICK_TURN_ANTITURBO;
    private static double driveBaseThrottleValue = 0.0;
    private static double driveBaseHeadingValue = 0.0;
    private static double pidThrottleValue = 0.0;
    private static double pidHeadingValue = 0.0;
    private static double pidSpeedValue = 0.0;
    private static boolean antiTurboFlag = false;
    private static boolean slowTurnLeftFlag = false;
    private static boolean slowTurnRightFlag = false;
    private static boolean turboFlag = false;
    private static DoubleSolenoid.Value shifterFlag = DoubleSolenoid.Value.kForward; //Default to low gear
    private static boolean quickTurnFlag = false;
    private static Encoder leftDriveEncoder;
    private static Encoder rightDriveEncoder;
    private static Gyro driveHeadingGyro;
    private static WsPidController driveHeadingPid;
    private static WsDriveBaseHeadingPidInput driveHeadingPidInput;
    private static WsDriveBaseHeadingPidOutput driveHeadingPidOutput;
    private static boolean driveHeadingPidEnabled = false;
    
    private static WsSpeedPidController driveSpeedPid;
    private static WsDriveBaseSpeedPidInput driveSpeedPidInput;
    private static WsDriveBaseSpeedPidOutput driveSpeedPidOutput;
    private static ContinuousAccelFilter continuousAccelerationFilter;
    //Set low gear top speed to 8.5 ft/ second = 102 inches / second = 2.04 inches/ 20 ms 
    private static double MAX_SPEED_INCHES_LOWGEAR = 90.0; 
    private double goal_velocity = 0.0; 
    private double distance_to_move = 0.0; 
    private double distance_moved = 0.0; 
    private double distance_remaining  = 0.0; 
    private boolean motionProfileActive = false; 
    private double currentProfileX =0.0; 
    private double currentProfileV =0.0; 
    private double currentProfileA =0.0; 
    private static double FEED_FORWARD_VELOCITY_CONSTANT = 1.00; 
    private static double FEED_FORWARD_ACCELERATION_CONSTANT = 0.00018; 
    private double totalPosition = 0.0; 
    private double previousPositionSinceLastReset = 0.0; 
    private double previousRightPositionSinceLastReset = 0.0; 
    private double previousLeftPositionSinceLastReset = 0.0; 
    private double deltaPosition = 0.0; 
    private double deltaTime = 0.0; 
    private double deltaPosError = 0.0; 
    private double deltaProfilePosition = 0.0; 
    private double previousTime = 0.0; 
    private double previousVelocity = 0.0; 
    private double currentVelocity = 0.0; 
    private double currentAcceleration = 0.0; 
    private double DECELERATION_VELOCITY_THRESHOLD = 48; //Velocity in in/sec
    private double DECELERATION_MOTOR_SPEED = 0.3;
    
    private static WsPidController driveDistancePid;
    private static WsDriveBaseDistancePidInput driveDistancePidInput;
    private static WsDriveBaseDistancePidOutput driveDistancePidOutput;
    private static boolean driveDistancePidEnabled = false;
    private static DoubleConfigFileParameter WHEEL_DIAMETER_config;
    private static DoubleConfigFileParameter TICKS_PER_ROTATION_config;
    private static DoubleConfigFileParameter THROTTLE_LOW_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter HEADING_LOW_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter HEADING_HIGH_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter MAX_HIGH_GEAR_PERCENT_config;
    private static DoubleConfigFileParameter ENCODER_GEAR_RATIO_config;
    private static DoubleConfigFileParameter DEADBAND_config;
    private static DoubleConfigFileParameter SLOW_TURN_FORWARD_SPEED_config;
    private static DoubleConfigFileParameter SLOW_TURN_BACKWARD_SPEED_config;
    private static DoubleConfigFileParameter FEED_FORWARD_VELOCITY_CONSTANT_config;
    private static DoubleConfigFileParameter FEED_FORWARD_ACCELERATION_CONSTANT_config;
    private static DoubleConfigFileParameter MAX_ACCELERATION_DRIVE_PROFILE_config;
    private static DoubleConfigFileParameter MAX_SPEED_INCHES_LOWGEAR_config;
    private static DoubleConfigFileParameter DECELERATION_VELOCITY_THRESHOLD_config;
    private static DoubleConfigFileParameter DECELERATION_MOTOR_SPEED_config;
    private static DoubleConfigFileParameter STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config;
    private static DoubleConfigFileParameter DRIVE_OFFSET_config;
    private static DoubleConfigFileParameter QUICK_TURN_CAP_config;
    private static DoubleConfigFileParameter QUICK_TURN_ANTITURBO_config;
    private static BooleanConfigFileParameter USE_LEFT_SIDE_FOR_OFFSET_config;

    public WsDriveBase(String name) {
        super(name);

        WHEEL_DIAMETER_config = new DoubleConfigFileParameter(this.getClass().getName(), "wheel_diameter", 6);
        TICKS_PER_ROTATION_config = new DoubleConfigFileParameter(this.getClass().getName(), "ticks_per_rotation", 360.0);
        THROTTLE_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_low_gear_accel_factor", 0.250);
        HEADING_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_low_gear_accel_factor", 0.500);
        THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_high_gear_accel_factor", 0.125);
        HEADING_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_high_gear_accel_factor", 0.250);
        MAX_HIGH_GEAR_PERCENT_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_high_gear_percent", 0.80);
        ENCODER_GEAR_RATIO_config = new DoubleConfigFileParameter(this.getClass().getName(), "encoder_gear_ratio", 7.5);
        DEADBAND_config = new DoubleConfigFileParameter(this.getClass().getName(), "deadband", 0.05);
        SLOW_TURN_FORWARD_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "slow_turn_forward_speed", 0.16);
        SLOW_TURN_BACKWARD_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "slow_turn_backward_speed", -0.19);
        FEED_FORWARD_VELOCITY_CONSTANT_config = new DoubleConfigFileParameter(this.getClass().getName(), "feed_forward_velocity_constant", 1.00);
        FEED_FORWARD_ACCELERATION_CONSTANT_config = new DoubleConfigFileParameter(this.getClass().getName(), "feed_forward_acceleration_constant", 0.00018);
        MAX_ACCELERATION_DRIVE_PROFILE_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_acceleration_drive_profile", 600.0);
        MAX_SPEED_INCHES_LOWGEAR_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_speed_inches_lowgear", 90.0);
        DECELERATION_VELOCITY_THRESHOLD_config = new DoubleConfigFileParameter(this.getClass().getName(), "deceleration_velocity_threshold", 48.0);
        DECELERATION_MOTOR_SPEED_config = new DoubleConfigFileParameter(this.getClass().getName(), "deceleration_motor_speed", 0.3);
        STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config = new DoubleConfigFileParameter(this.getClass().getName(), "stopping_distance_at_max_speed_lowgear", 10.0);
        DRIVE_OFFSET_config = new AutonomousDoubleConfigFileParameter("DriveOffset", 1.00);
        USE_LEFT_SIDE_FOR_OFFSET_config = new AutonomousBooleanConfigFileParameter("UseLeftDriveForOffset", true);
        QUICK_TURN_CAP_config = new DoubleConfigFileParameter(this.getClass().getName(), "quick_turn_cap", 10.0);
        QUICK_TURN_ANTITURBO_config = new DoubleConfigFileParameter(this.getClass().getName(), "quick_turn_antiturbo", 10.0);

        //Anti-Turbo button
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        //Turbo button
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        //Shifter Button
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON6);
        subject.attach(this);
        //Left/right slow turn buttons
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON1);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON3);
        subject.attach(this);

        //Initialize the drive base encoders
        leftDriveEncoder = new Encoder(2, 3, true, EncodingType.k4X);
        leftDriveEncoder.reset();
        leftDriveEncoder.start();
        rightDriveEncoder = new Encoder(4, 5, false, EncodingType.k4X);
        rightDriveEncoder.reset();
        rightDriveEncoder.start();

        //Initialize the gyro
        //@TODO: Get the correct port
        driveHeadingGyro = new Gyro(1);

        //Initialize the PIDs
        driveDistancePidInput = new WsDriveBaseDistancePidInput();
        driveDistancePidOutput = new WsDriveBaseDistancePidOutput();
        driveDistancePid = new WsPidController(driveDistancePidInput, driveDistancePidOutput, "WsDriveBaseDistancePid");

        driveHeadingPidInput = new WsDriveBaseHeadingPidInput();
        driveHeadingPidOutput = new WsDriveBaseHeadingPidOutput();
        driveHeadingPid = new WsPidController(driveHeadingPidInput, driveHeadingPidOutput, "WsDriveBaseHeadingPid");
        
        driveSpeedPidInput = new WsDriveBaseSpeedPidInput();
        driveSpeedPidOutput = new WsDriveBaseSpeedPidOutput();
        driveSpeedPid = new WsSpeedPidController(driveSpeedPidInput, driveSpeedPidOutput, "WsDriveBaseSpeedPid");
        continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0); 
        init();
    }

    public void init() {
        driveBaseThrottleValue = 0.0;
        driveBaseHeadingValue = 0.0;
        antiTurboFlag = false;
        turboFlag = false;
        shifterFlag = DoubleSolenoid.Value.kForward;
        quickTurnFlag = false;
        this.disableDistancePidControl();
        this.disableHeadingPidControl();
        motionProfileActive = false; 
        previousTime =  Timer.getFPGATimestamp(); 
        currentProfileX = 0.0; 
        continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0);
        //Zero out all motor values left over from autonomous
        (WsOutputManager.getInstance().getOutput(WsOutputManager.LEFT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(0.0));
        (WsOutputManager.getInstance().getOutput(WsOutputManager.RIGHT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(0.0));
        (WsOutputManager.getInstance().getOutput(WsOutputManager.LEFT_DRIVE_SPEED)).update();
        (WsOutputManager.getInstance().getOutput(WsOutputManager.RIGHT_DRIVE_SPEED)).update();
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.THROTTLE, new Double(0.0));
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(0.0));
        WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).update();
        //Clear encoders
        resetLeftEncoder();
        resetRightEncoder();
    }
   
    public void update() {
        updateSpeedAndAccelerationCalculations(); 
        if (true == motionProfileActive ){
            
            //Update PID using profile velocity as setpoint and measured velocity as PID input 
            enableSpeedPidControl();
            setDriveSpeedPidSetpoint(continuousAccelerationFilter.getCurrVel());
            //Update system to get feed forward terms
            deltaPosError = this.deltaPosition - (deltaProfilePosition); 
            distance_moved += this.deltaPosition; 
//            distance_remaining = this.distance_to_move - currentProfileX;
            distance_remaining = this.distance_to_move - distance_moved;
            //Logger.getLogger().debug(this.getClass().getName(), "AccelFilter", "distance_left: " + distance_remaining + " p: " + continuousAccelerationFilter.getCurrPos()+ " v: " + continuousAccelerationFilter.getCurrVel() + " a: " + continuousAccelerationFilter.getCurrAcc() );
            continuousAccelerationFilter.calculateSystem(distance_remaining , currentProfileV, goal_velocity, MAX_ACCELERATION_DRIVE_PROFILE, MAX_SPEED_INCHES_LOWGEAR, deltaTime);
            deltaProfilePosition = continuousAccelerationFilter.getCurrPos() - currentProfileX ;  
            currentProfileX = continuousAccelerationFilter.getCurrPos();
            currentProfileV = continuousAccelerationFilter.getCurrVel();
            currentProfileA = continuousAccelerationFilter.getCurrAcc();
            SmartDashboard.putNumber("Speed PID Error", driveSpeedPid.getError());
            SmartDashboard.putNumber("Speed PID Output", this.pidSpeedValue);
            SmartDashboard.putNumber("Distance Error", this.deltaPosError);
            //Update motor output with PID output and feed forward velocity and acceleration 
            double throttleValue = this.pidSpeedValue 
                                    + FEED_FORWARD_VELOCITY_CONSTANT*(continuousAccelerationFilter.getCurrVel()/ MAX_SPEED_INCHES_LOWGEAR )
                                    + FEED_FORWARD_ACCELERATION_CONSTANT*continuousAccelerationFilter.getCurrAcc(); 
            
            if (((distance_remaining < getStoppingDistanceFromDistanceToMove(distance_to_move)) && (currentProfileV > 0) && (currentProfileA < 0 )) ||
               ((distance_remaining > -getStoppingDistanceFromDistanceToMove(distance_to_move)) && (currentProfileV < 0) && (currentProfileA > 0 )))
            {
                throttleValue =0.0; 
            }
            
            //Update the throttle value outside the function so that the acceleration factor is not applied. 
            driveBaseThrottleValue = throttleValue; 
            if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
                driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
            } else if (driveBaseThrottleValue < MAX_NEG_INPUT_THROTTLE_VALUE) {
                driveBaseThrottleValue = MAX_NEG_INPUT_THROTTLE_VALUE;
            }
            SmartDashboard.putNumber("Motion Profile Throttle", driveBaseThrottleValue);
            updateDriveMotors(true);
        } else if (true == driveDistancePidEnabled) {
            //We are driving by distance under PID control
            enableDistancePidControl();
            driveDistancePid.calcPid();
            setThrottleValue(pidThrottleValue);
            setHeadingValue(0);
            updateDriveMotors(false);
            SmartDashboard.putNumber("PID Throttle Value", pidThrottleValue);
        } else if (true == driveHeadingPidEnabled) {
            //We are driving by heading under PID control
            enableHeadingPidControl();
            driveHeadingPid.calcPid();
            quickTurnFlag = true;
            setThrottleValue(0);
            setHeadingValue(pidHeadingValue);
            updateDriveMotors(false);
            SmartDashboard.putNumber("PID Heading Value", pidHeadingValue);
        } else if (true == driveDistancePidEnabled && true == driveHeadingPidEnabled) {
            //This isn't good...
            //Disable both the PIDs and tell the logger we have a problem
            disableDistancePidControl();
            disableHeadingPidControl();
            Logger.getLogger().error(this.getClass().getName(), "update", "Both PIDS are enabled. Disabling both.");
        } else if (false == driveDistancePidEnabled && false == driveHeadingPidEnabled) {
            //We are in manual control
            //Get the inputs for heading and throttle
            //Set headign and throttle values
            double throttleValue = 0.0;
            double headingValue = 0.0;

            throttleValue = ((Double) ((WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.THROTTLE)).doubleValue();
            headingValue = ((Double) ((WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.HEADING)).doubleValue();

            setThrottleValue(throttleValue);
            setHeadingValue(headingValue);

            //Use updated values to update the quickTurnFlag
            checkAutoQuickTurn();

            //Set the drive motor outputs
            updateDriveMotors(false);

            SmartDashboard.putNumber("Throttle Value", driveBaseThrottleValue);
            SmartDashboard.putNumber("Heading Value", driveBaseHeadingValue);
            SmartDashboard.putBoolean("Shifter State", shifterFlag.equals(DoubleSolenoid.Value.kReverse));
            SmartDashboard.putBoolean("Anti-Turbo Flag", antiTurboFlag);

            //Set gear shift output
            WsOutputManager.getInstance().getOutput(WsOutputManager.SHIFTER).set(null, new Integer(shifterFlag.value));
        } else {
        }
        
        
        SmartDashboard.putNumber("Left encoder count: ", this.getLeftEncoderValue());
        SmartDashboard.putNumber("Right encoder count: ", this.getRightEncoderValue());
        SmartDashboard.putNumber("Right Distance: ", this.getRightDistance());
        SmartDashboard.putNumber("Left Distance: ", this.getLeftDistance());
        SmartDashboard.putNumber("Gyro angle", this.getGyroAngle());
    }

    
    private void updateSpeedAndAccelerationCalculations() {
        double newTime = Timer.getFPGATimestamp();
        double leftDistance = this.getLeftDistance();
        double rightDistance = this.getRightDistance();
        double rightDelta = (rightDistance - previousRightPositionSinceLastReset);
        double leftDelta = (leftDistance - previousLeftPositionSinceLastReset);
        this.deltaPosition = (Math.abs(rightDelta) > Math.abs(leftDelta) ? rightDelta : leftDelta);
        this.deltaTime = (newTime - previousTime); 
        if (this.deltaTime > 0.060){ 
            this.deltaTime = 0.060; 
        }
        //Do velocity internally in in/sec
        currentVelocity= (deltaPosition / deltaTime ) ;
        currentAcceleration = ((currentVelocity - previousVelocity) / deltaTime); 
        
        //Output velocity in ft/sec
        SmartDashboard.putNumber("Velocity: ", this.currentVelocity/12.0);
        SmartDashboard.putNumber("Accel: ", this.currentAcceleration/144.0);

        totalPosition += deltaPosition; 
        if ( Math.abs(deltaPosition) > 0.005 ){
            //Logger.getLogger().debug(this.getClass().getName(), "Kinematics", "tP: "+  totalPosition + " dP: " + deltaPosition + "dpp:" + deltaProfilePosition +  " dt: " + deltaTime + " cv: " + currentVelocity + " pv: " + previousVelocity + " ca: " + currentAcceleration);
        }
        previousPositionSinceLastReset += deltaPosition;
        previousRightPositionSinceLastReset += rightDelta;
        previousLeftPositionSinceLastReset += leftDelta;
        previousTime = newTime; 
        previousVelocity = currentVelocity; 
        
        
    }
    public void resetKinematics(){ 
        previousPositionSinceLastReset = 0.0; 
        previousVelocity = 0.0; 
        currentVelocity = 0.0; 
        currentAcceleration = 0.0; 
    }
    
    public double getDistanceRemaining(){
        return this.distance_remaining; 
    }
    
    public double getAcceleration(){ 
     return currentAcceleration;    
    }
    public double getVelocity(){ 
     return currentVelocity;    
    }
    
    public void setThrottleValue(double tValue) {

        // Taking into account Anti-Turbo
        double new_throttle = tValue;
        if (true == antiTurboFlag) {
            new_throttle *= ANTI_TURBO_MAX_DEFLECTION;

            //Cap the throttle at the maximum deflection allowed for anti-turbo
            if (new_throttle > ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_throttle < -ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = -ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, see if the turbo button is pressed
            if (turboFlag == true) {
                //We are in turbo mode, don't cap the output
            } else {
                //We aren't in turbo mode, cap the output at the max percent for high gear
                if (new_throttle > MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
                    new_throttle = MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
                }
                if (new_throttle < NEG_MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
                    new_throttle = NEG_MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
                }
            }

        } else {
            //We are in low gear, don't modify the throttle
        }

        //Use the acceleration factor based on the current shifter state
        if (shifterFlag == DoubleSolenoid.Value.kForward) {
            //We are in low gear, use that acceleration factor
            driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_LOW_GEAR_ACCEL_FACTOR;
        } else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, use that acceleration factor
            driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_HIGH_GEAR_ACCEL_FACTOR;
        } else {
            //This is bad...
            //If we get here we have a problem
        }

        if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
        } else if (driveBaseThrottleValue < MAX_NEG_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_NEG_INPUT_THROTTLE_VALUE;
        }
    }

    public void setHeadingValue(double hValue) {

        // Taking into account anti-turbo
        double new_heading = hValue;
        if (true == antiTurboFlag) {
            new_heading *= ANTI_TURBO_MAX_DEFLECTION;

            //Cap the heading at the maximum deflection allowed for anti-turbo
            if (new_heading > ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_heading < -ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = -ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        //Use the acceleration factor based on the current shifter state
        if (shifterFlag == DoubleSolenoid.Value.kForward) {
            //We are in low gear, use that acceleration factor
            driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_LOW_GEAR_ACCEL_FACTOR;
        } else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, use that acceleration factor
            driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_HIGH_GEAR_ACCEL_FACTOR;
        } else {
            //This is bad...
            //If we get here we have a problem
        }

        if (driveBaseHeadingValue > MAX_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_INPUT_HEADING_VALUE;
        } else if (driveBaseHeadingValue < MAX_NEG_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_NEG_INPUT_HEADING_VALUE;
        }
    }
    
    public void overrideHeadingValue(double newHeading) {
        driveBaseHeadingValue = newHeading;
        if (driveBaseHeadingValue > MAX_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_INPUT_HEADING_VALUE;
        } else if (driveBaseHeadingValue < MAX_NEG_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_NEG_INPUT_HEADING_VALUE;
        }
    }

    public void updateDriveMotors(boolean useDriveOffset) {
        double rightMotorSpeed = 0;
        double leftMotorSpeed = 0;
        double angularPower = 0.0;
        if (Math.abs(driveBaseHeadingValue) > 0.05) {
            angularPower = Math.abs(driveBaseThrottleValue) * driveBaseHeadingValue * HEADING_SENSITIVITY;
        }

        rightMotorSpeed = driveBaseThrottleValue - angularPower;
        leftMotorSpeed = driveBaseThrottleValue + angularPower;

        if (true == quickTurnFlag) {
            rightMotorSpeed = 0.0f;
            leftMotorSpeed = 0.0f;
            driveBaseThrottleValue = 0.0f;

            // Quick turn does not take throttle into account
            leftMotorSpeed += driveBaseHeadingValue;
            rightMotorSpeed -= driveBaseHeadingValue;
            
            /*if(true == antiTurboFlag) {
                leftMotorSpeed /= ANTI_TURBO_MAX_DEFLECTION;
                leftMotorSpeed *= QUICK_TURN_ANTITURBO;
                rightMotorSpeed /= ANTI_TURBO_MAX_DEFLECTION;
                rightMotorSpeed *= QUICK_TURN_ANTITURBO;
            }
            
            if(false == turboFlag) {
                if (leftMotorSpeed > QUICK_TURN_CAP) {
                    leftMotorSpeed = QUICK_TURN_CAP;
                } else if (leftMotorSpeed < -QUICK_TURN_CAP) {
                    leftMotorSpeed = -QUICK_TURN_CAP;
                }
                if (rightMotorSpeed > QUICK_TURN_CAP) {
                    rightMotorSpeed = QUICK_TURN_CAP;
                } else if (rightMotorSpeed < -QUICK_TURN_CAP) {
                    rightMotorSpeed = -QUICK_TURN_CAP;
                }
            }*/
        } else {
            if (driveBaseThrottleValue >= 0) {
                if (rightMotorSpeed < 0) {
                    rightMotorSpeed = 0;
                }
                if (leftMotorSpeed < 0) {
                    leftMotorSpeed = 0;
                }
            } else {
                if (rightMotorSpeed >= 0) {
                    rightMotorSpeed = 0;
                }
                if (leftMotorSpeed >= 0) {
                    leftMotorSpeed = 0;
                }
            }

            if (true == slowTurnLeftFlag && false == slowTurnRightFlag) {
                rightMotorSpeed = SLOW_TURN_FORWARD_SPEED;
                leftMotorSpeed = SLOW_TURN_BACKWARD_SPEED;
            } else if (false == slowTurnLeftFlag && true == slowTurnRightFlag) {
                rightMotorSpeed = SLOW_TURN_BACKWARD_SPEED;
                leftMotorSpeed = SLOW_TURN_FORWARD_SPEED;
            } else {
                //Palm smash! Do nothing.
            }

            if (rightMotorSpeed > MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed > MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = MAX_MOTOR_OUTPUT;
            }
            if (rightMotorSpeed < NEG_MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = NEG_MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed < NEG_MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = NEG_MAX_MOTOR_OUTPUT;
            }
        }
        
        if (useDriveOffset){
            if(USE_LEFT_SIDE_FOR_OFFSET){
                leftMotorSpeed = leftMotorSpeed * DRIVE_OFFSET; 
            } else { 
                rightMotorSpeed = rightMotorSpeed * DRIVE_OFFSET; 
            }
        }
        
        //If our throttle is within the zero deadband and our velocity is above the threshold,
        //use deceleration to slow us down
        if((Math.abs(driveBaseThrottleValue) < DEADBAND) && (Math.abs(currentVelocity) > DECELERATION_VELOCITY_THRESHOLD) && (false == quickTurnFlag) && (false == slowTurnRightFlag) && (false ==slowTurnLeftFlag)) {
            //We are above the velocity threshold, apply a small inverse motor speed to decelerate
            if (currentVelocity > 0) {
                //We are moving forward, apply a negative motor value
                rightMotorSpeed = -DECELERATION_MOTOR_SPEED;
                leftMotorSpeed = -DECELERATION_MOTOR_SPEED;
            } else {
                //We are moving backward, apply a positive motor value
                rightMotorSpeed = DECELERATION_MOTOR_SPEED;
                leftMotorSpeed = DECELERATION_MOTOR_SPEED;
            }
        } else if ((Math.abs(driveBaseThrottleValue) < DEADBAND) && (Math.abs(currentVelocity) <= DECELERATION_VELOCITY_THRESHOLD) && (false == quickTurnFlag)&& (false == slowTurnRightFlag) && (false ==slowTurnLeftFlag)) {
            //We are below the velocity threshold, zero the motor values to brake
            rightMotorSpeed = 0.0;
            leftMotorSpeed = 0.0;
        }

        //If we're within the deadband, zero out the throttle and heading
        if ((leftMotorSpeed < DEADBAND && leftMotorSpeed > -DEADBAND) && (rightMotorSpeed < DEADBAND && rightMotorSpeed > -DEADBAND)) {
            this.setHeadingValue(0.0);
            this.setThrottleValue(0.0);
            rightMotorSpeed = 0.0;
            leftMotorSpeed = 0.0;
        }

        //Update Output Facade.
        (WsOutputManager.getInstance().getOutput(WsOutputManager.LEFT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(leftMotorSpeed));
        (WsOutputManager.getInstance().getOutput(WsOutputManager.RIGHT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(rightMotorSpeed));
    }

    public void checkAutoQuickTurn() {
        double throttle = driveBaseThrottleValue;
        double heading = driveBaseHeadingValue;

        throttle = Math.abs(throttle);
        heading = Math.abs(heading);

        if ((throttle < 0.1) && (heading > 0.1)) {
            quickTurnFlag = true;
        } else {
            quickTurnFlag = false;
        }
    }

    /*
     * ENCODER/GYRO STUFF
     */
    public Encoder getLeftEncoder() {
        return leftDriveEncoder;
    }

    public Encoder getRightEncoder() {
        return rightDriveEncoder;
    }

    public double getLeftEncoderValue() {
        return leftDriveEncoder.get();
    }

    public double getRightEncoderValue() {
        return rightDriveEncoder.get();
    }

    public double getLeftDistance() {
        double distance = 0.0;
        distance = (leftDriveEncoder.get() / (TICKS_PER_ROTATION * ENCODER_GEAR_RATIO)) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
        return distance;
    }

    public double getRightDistance() {
        double distance = 0.0;
        distance = (rightDriveEncoder.get() / (TICKS_PER_ROTATION * ENCODER_GEAR_RATIO)) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
        return distance;
    }

    public void setDriveDistancePidSetpoint(double distance) {
        resetLeftEncoder();
        resetRightEncoder();
        resetKinematics();
        driveDistancePid.setSetPoint(distance);
        driveDistancePid.calcPid();
    }
    public void setDriveSpeedPidSetpoint(double speed) {
        driveSpeedPid.setSetPoint(speed);
        driveSpeedPid.calcPid();
    }

    public void resetLeftEncoder() {
        leftDriveEncoder.reset();
        leftDriveEncoder.start();
    }

    public void resetRightEncoder() {
        rightDriveEncoder.reset();
        rightDriveEncoder.start();
    }

    public void enableDistancePidControl() {
        driveDistancePidEnabled = true;
        driveDistancePid.enable();
    }

    public void disableDistancePidControl() {
        driveDistancePidEnabled = false;
        driveDistancePid.disable();
        resetDistancePid();
        Logger.getLogger().debug(this.getClass().getName(), "disableDistancePidControl", "Distance PID is disabled");
    }

    public void resetDistancePid() {
        driveDistancePid.reset();
        resetLeftEncoder();
        resetRightEncoder();
        resetKinematics();
    }

    public WsPidStateType getDistancePidState() {
        return driveDistancePid.getState();
    }

    public void setPidThrottleValue(double pidThrottle) {
        pidThrottleValue = pidThrottle;
    }

    public Gyro getGyro() {
        return driveHeadingGyro;
    }

    public double getGyroAngle() {
        return driveHeadingGyro.getAngle();
    }

    public void setDriveHeadingPidSetpoint(double distance) {
        driveHeadingPid.setSetPoint(distance);
        driveHeadingPid.calcPid();
    }

    public void resetGyro() {
        driveHeadingGyro.reset();
    }

    public void enableHeadingPidControl() {
        driveHeadingPidEnabled = true;
        driveHeadingPid.enable();
    }

    public void disableHeadingPidControl() {
        driveHeadingPidEnabled = false;
        driveHeadingPid.disable();
        resetHeadingPid();
        Logger.getLogger().debug(this.getClass().getName(), "disableHeadingPidControl", "Heading PID is disabled");
    }

    public void resetHeadingPid() {
        driveHeadingPid.reset();
        resetGyro();
    }
    public void enableSpeedPidControl() {
        driveSpeedPid.enable();
    }

    public void disableSpeedPidControl() {
        driveSpeedPid.disable();
        resetSpeedPid();
        Logger.getLogger().debug(this.getClass().getName(), "disableSpeedPidControl", "Speed PID is disabled");
    }

    public void resetSpeedPid() {
        driveSpeedPid.reset();
    }
    
    public void startStraightMoveWithMotionProfile(double distance, double goal_velocity){
        startMoveWithHeadingAndMotionProfile(distance, goal_velocity, 0.0);
        
    }
    public void startMoveWithHeadingAndMotionProfile(double distance, double goal_velocity, double heading){
        this.distance_to_move = distance; 
        this.distance_moved = 0.0; 
        this.distance_remaining = distance;
        this.goal_velocity = goal_velocity; 
        motionProfileActive = true; 
        overrideHeadingValue(heading);
    }
    
    public void stopStraightMoveWithMotionProfile(){
        disableSpeedPidControl(); 
        continuousAccelerationFilter = new ContinuousAccelFilter(0, 0, 0);
        currentProfileX = 0 ; 
        this.distance_to_move = 0.0; 
        this.distance_remaining = 0.0; 
        this.goal_velocity = 0.0;
        motionProfileActive =false; 
        overrideHeadingValue(0.0);
    }

    public WsPidStateType getHeadingPidState() {
        return driveHeadingPid.getState();
    }

    public void setPidHeadingValue(double pidHeading) {
        //We have to reverse the heading to make it turn the right way
        pidHeadingValue = -pidHeading;
        Logger.getLogger().debug(this.getClass().getName(), "setPidHeadingValue", "Heading PID value set: " + pidHeading);
    }
    public void setPidSpeedValue(double pidSpeed) {
        //Add the feed forward velocity and acceleration
        
        pidSpeedValue = pidSpeed;
    }
    public double getPidSpeedValue() {
        //Add the feed forward velocity and acceleration
        
        return pidSpeedValue;
    }
    
    public boolean getQuickTurnFlag() {
        return quickTurnFlag;
    }

    public void notifyConfigChange() {
        WHEEL_DIAMETER = WHEEL_DIAMETER_config.getValue();
        TICKS_PER_ROTATION = TICKS_PER_ROTATION_config.getValue();
        THROTTLE_LOW_GEAR_ACCEL_FACTOR = THROTTLE_LOW_GEAR_ACCEL_FACTOR_config.getValue();
        HEADING_LOW_GEAR_ACCEL_FACTOR = HEADING_LOW_GEAR_ACCEL_FACTOR_config.getValue();
        THROTTLE_HIGH_GEAR_ACCEL_FACTOR = THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
        HEADING_HIGH_GEAR_ACCEL_FACTOR = HEADING_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
        MAX_HIGH_GEAR_PERCENT = MAX_HIGH_GEAR_PERCENT_config.getValue();
        ENCODER_GEAR_RATIO = ENCODER_GEAR_RATIO_config.getValue();
        SLOW_TURN_FORWARD_SPEED = SLOW_TURN_FORWARD_SPEED_config.getValue();
        SLOW_TURN_BACKWARD_SPEED = SLOW_TURN_BACKWARD_SPEED_config.getValue();
        FEED_FORWARD_VELOCITY_CONSTANT = FEED_FORWARD_VELOCITY_CONSTANT_config.getValue();
        FEED_FORWARD_ACCELERATION_CONSTANT = FEED_FORWARD_ACCELERATION_CONSTANT_config.getValue();
        MAX_ACCELERATION_DRIVE_PROFILE = MAX_ACCELERATION_DRIVE_PROFILE_config.getValue();
        MAX_SPEED_INCHES_LOWGEAR = MAX_SPEED_INCHES_LOWGEAR_config.getValue();
        DEADBAND = DEADBAND_config.getValue();
        DECELERATION_VELOCITY_THRESHOLD = DECELERATION_VELOCITY_THRESHOLD_config.getValue();
        DECELERATION_MOTOR_SPEED = DECELERATION_MOTOR_SPEED_config.getValue();
        STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR = STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR_config.getValue(); 
        DRIVE_OFFSET = DRIVE_OFFSET_config.getValue();
        USE_LEFT_SIDE_FOR_OFFSET = USE_LEFT_SIDE_FOR_OFFSET_config.getValue();
        QUICK_TURN_CAP = QUICK_TURN_CAP_config.getValue();
        QUICK_TURN_ANTITURBO = QUICK_TURN_ANTITURBO_config.getValue();
        driveDistancePid.notifyConfigChange();
        driveHeadingPid.notifyConfigChange();
        driveSpeedPid.notifyConfigChange();
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON8) {
            antiTurboFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON6) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                shifterFlag = shifterFlag.equals(DoubleSolenoid.Value.kForward)
                        ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
            }
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON7) {
            turboFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON3) {
            slowTurnLeftFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON1) {
            slowTurnRightFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
    
    public double getSpeedError (){ 
        return driveSpeedPid.getError(); 
    }
    public double getDeltaPosError (){ 
        return deltaPosError;
    }
    
    private double getStoppingDistanceFromDistanceToMove(double distance) {
        if (Math.abs(distance) > 40.0) {
            return STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR;
        } else {
            return (STOPPING_DISTANCE_AT_MAX_SPEED_LOWGEAR - ((3.0f/15.0f) * (40 - Math.abs(distance))));
        }
    }
}