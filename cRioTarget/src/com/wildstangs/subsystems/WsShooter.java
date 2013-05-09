package com.wildstangs.subsystems;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickEnum;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.outputmanager.outputs.WsVictor;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables2.util.List;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WsShooter extends WsSubsystem implements IObserver {

    public static class Preset {

        public final int ENTER_WHEEL_SET_POINT;
        public final int EXIT_WHEEL_SET_POINT;
        public final DoubleSolenoid.Value ANGLE;

        public Preset(int enterWheelSetPoint, int exitWheelSetPoint, DoubleSolenoid.Value angle) {
            this.ENTER_WHEEL_SET_POINT = enterWheelSetPoint;
            this.EXIT_WHEEL_SET_POINT = exitWheelSetPoint;
            this.ANGLE = angle;
        }
    }
    public static class ShotSnapshot{
        public final int enterWheelSpeed;
        public final double dShotSec;
        public final int exitWheelSpeed;
        public final int enterVictorValue;
        public final int exitVictorValue;
        public final boolean atSpeed;
        public ShotSnapshot(int enterWheelSpeed, int exitWheelSpeed, int enterVictorValue, int exitVictorValue, boolean atSpeed, double dShotSec){
            this.enterWheelSpeed = enterWheelSpeed; 
            this.exitWheelSpeed = exitWheelSpeed; 
            this.enterVictorValue = enterVictorValue; 
            this.exitVictorValue = exitVictorValue; 
            this.atSpeed = atSpeed; 
            this.dShotSec = dShotSec; 
        }        
    }
    private IntegerConfigFileParameter PresetTowerShooterEnterSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetTowerShooterEnterSpeed", 1600);
    private IntegerConfigFileParameter PresetTowerShooterExitSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetTowerShooterExitSpeed", 2200);
    private BooleanConfigFileParameter PresetTowerShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "PresetTowerShooterAngle", true);
    private IntegerConfigFileParameter PresetLongLowEnterSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetLongLowEnterSpeed", 4000);
    private IntegerConfigFileParameter PresetLongLowExitSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetLongLowExitSpeed", 4500);
    private BooleanConfigFileParameter PresetLongLowAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "PresetLongLowAngle", false);
    private IntegerConfigFileParameter PresetShortHighEnterSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetShortHighEnterSpeed", 1700);
    private IntegerConfigFileParameter PresetShortHighExitSpeed = new IntegerConfigFileParameter(
            this.getClass().getName(), "PresetShortHighExitSpeed", 2300);
    private BooleanConfigFileParameter PresetShortHighAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "PresetShortHighAngle", true);
    private Preset PresetTowerShooterStation = new Preset(PresetTowerShooterEnterSpeed.getValue(),
            PresetTowerShooterExitSpeed.getValue(), translatePresetConfigAngle(PresetTowerShooterAngle.getValue()));
    private Preset PresetLongLow = new Preset(PresetLongLowEnterSpeed.getValue(),
            PresetLongLowExitSpeed.getValue(), translatePresetConfigAngle(PresetLongLowAngle.getValue()));
    private Preset PresetShortHigh = new Preset(PresetShortHighEnterSpeed.getValue(),
            PresetShortHighExitSpeed.getValue(), translatePresetConfigAngle(PresetShortHighAngle.getValue()));
    private Preset PresetOffLow = new Preset(0, 0, DoubleSolenoid.Value.kReverse);
    private Counter counterEnter, counterExit;
    private DoubleConfigFileParameter lowerWheelSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelSpeed", 0);
    private DoubleConfigFileParameter lowerVictorSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerVictorSpeed", 0);
    private DoubleConfigFileParameter lowerWheelEnterTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelEnterTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelEnterTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "UpperWheelEnterTestSpeed", 9000);
    private DoubleConfigFileParameter LowerWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelExitTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "UpperWheelExitTestSpeed", 9000);
    private DoubleConfigFileParameter atSpeedToleranceAboveSetpointConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AtSpeedToleranceAboveSetpoint", 200);
    private DoubleConfigFileParameter atSpeedToleranceUnderSetpointConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AtSpeedToleranceUnderSetpoint", 25);
    private DoubleConfigFileParameter atSpeedToleranceUnderSetpointNoGoConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AtSpeedToleranceUnderSetpointNoGo", 10);
    private DoubleConfigFileParameter ENTER_GEAR_RATIO_config = new DoubleConfigFileParameter(
            this.getClass().getName(), "enter_gear_ratio", 3.0);
    private DoubleConfigFileParameter EXIT_GEAR_RATIO_config = new DoubleConfigFileParameter(
            this.getClass().getName(), "exit_gear_ratio", 3.2);
    private DoubleConfigFileParameter minimumSafeFlywheelSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "MinimumSafeFlywheelSpeed", 1200.0);
    private double ENTER_GEAR_RATIO = 3.0;
    private double EXIT_GEAR_RATIO = 3.2;
    private double wheelEnterSetPoint = 0;
    private double wheelExitSetPoint = 0;
    private double previousTime = 0;
    private double lowWheelSpeed, lowVictorSpeed;
    private double lowWheelEnterTestSpeed, highWheelEnterTestSpeed, lowWheelExitTestSpeed, highWheelExitTestSpeed;
    private double testingEnterKnob = 0.0;
    private double testingExitKnob = 0.0;
    private double safeFlywheelSpeed = 0.0;
    private boolean testingCalled = false;
    private boolean atSpeed = false;
    private boolean atSafeSpeed = false;
    private boolean presetUnlock = false;
    private double aboveSetpointTolerance = 0.0;
    private double underSetpointTolerance = 0.0;
    private double underSetpointNoGoTolerance = 0.0;
    private DoubleSolenoid.Value angleFlag = DoubleSolenoid.Value.kReverse;
    final List shotSnapshots = new List();
    private int snapshotWaitCounter = 0 ; 
    private double initTime = 0.0; 
    private double victorEnterValue = 0.0 ; 
    private double victorExitValue = 0.0; 
    private double speedEnter = 0.0; 
    private double speedExit = 0.0;
    
    public WsShooter(String name) {
        super(name);
        BooleanConfigFileParameter outputsFor2012 = new BooleanConfigFileParameter(WsOutputManager.getInstance().getClass().getName(), "2012_Robot", false);
        if (outputsFor2012.getValue()) {
            counterEnter = new Counter(9);
            counterExit = new Counter(10);
        } else {
            counterEnter = new Counter(10);
            counterExit = new Counter(11);
        }


        //Implement this later for testing
        //Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.SHOOTER_SPEED_INPUT).getSubject(null);
        //subject.attach(this);
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON2);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON4);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickEnum.D_PAD_UP_DOWN);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.ENTER_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum) null);
        subject.attach(this);
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.EXIT_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum) null);
        subject.attach(this);

        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();

        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();

        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();

         underSetpointTolerance = atSpeedToleranceUnderSetpointConfig.getValue();
         underSetpointNoGoTolerance = atSpeedToleranceUnderSetpointNoGoConfig.getValue();
         aboveSetpointTolerance = atSpeedToleranceAboveSetpointConfig.getValue();
        
        safeFlywheelSpeed = minimumSafeFlywheelSpeed.getValue();
        init(); 
    }

    public DoubleSolenoid.Value translatePresetConfigAngle(boolean configVal) {
        if (configVal == true) {
            return DoubleSolenoid.Value.kForward;
        } else {
            return DoubleSolenoid.Value.kReverse;
        }
    }
    
    public String angleToString (DoubleSolenoid.Value angle) {
        if (angle == DoubleSolenoid.Value.kForward) {
            return "Up";       
        } else if (angle == DoubleSolenoid.Value.kReverse) {
            return "Down";
        } else {
            return "Unknown";
        }
    } 
    
    public void init() {
        resetEnterCounter();
        resetExitCounter();
        wheelEnterSetPoint = 0;
        wheelExitSetPoint = 0;
        angleFlag = DoubleSolenoid.Value.kReverse;
        presetUnlock = false;
        WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_ANGLE).set(null, new Integer(angleFlag.value));
        snapshotWaitCounter = 0 ; 
        initTime = Timer.getFPGATimestamp();
    }

    public void update() {
        double enterKnobValue = ((DoubleSubject) WsInputManager.getInstance()
                .getOiInput(WsInputManager.ENTER_WHEEL_SHOOTER_SPEED_INPUT)
                .getSubject((ISubjectEnum) null)).getValue();

        if (enterKnobValue > 3.3) {
            enterKnobValue = 3.3;
        }
        //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
        double calcKnobEnterSetPoint = ((enterKnobValue * (highWheelEnterTestSpeed - lowWheelEnterTestSpeed)) / 3.3)
                + lowWheelEnterTestSpeed;
        int knobEnterSetPoint = ((int)(calcKnobEnterSetPoint/25))*25;

        double exitKnobValue = ((DoubleSubject) WsInputManager.getInstance()
                .getOiInput(WsInputManager.EXIT_WHEEL_SHOOTER_SPEED_INPUT)
                .getSubject((ISubjectEnum) null)).getValue();

        if (exitKnobValue > 3.3) {
            exitKnobValue = 3.3;
        }
        //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
        double  calcKnobExitSetPoint = ((exitKnobValue * (highWheelExitTestSpeed - lowWheelExitTestSpeed)) / 3.3)
                + lowWheelExitTestSpeed;
        int knobExitSetPoint = ((int)(calcKnobExitSetPoint/25))*25;
        
        if (((BooleanSubject) WsInputManager.getInstance()
                .getOiInput(WsInputManager.SHOOTER_WHEEL_SPEED_OVERRIDE)
                .getSubject((ISubjectEnum) null)).getValue()) {
            wheelEnterSetPoint = knobEnterSetPoint;

            wheelExitSetPoint = knobExitSetPoint;
        }
        int enterCounterCount = counterEnter.get();
        int exitCounterCount = counterExit.get();
        double newTime = Timer.getFPGATimestamp();
        speedEnter = (60.0 / (128 * ENTER_GEAR_RATIO)) * counterEnter.get() / (newTime - previousTime);
        speedExit = (60.0 / (128 * EXIT_GEAR_RATIO)) * counterExit.get() / (newTime - previousTime);

        this.resetEnterCounter();
        this.resetExitCounter();

        previousTime = newTime;

        WsVictor victorEnter = (WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_VICTOR_ENTER);
        WsVictor victorExit = (WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_VICTOR_EXIT);

        if (((speedEnter < lowWheelSpeed) && (speedEnter < wheelEnterSetPoint))) {
            victorEnterValue = lowVictorSpeed;
        } else if (speedEnter < wheelEnterSetPoint) {
            victorEnterValue= 1.0; 
        } else {
            victorEnterValue= 0.0; 
        }
        victorEnter.set(null, Double.valueOf(victorEnterValue));

        if (((speedExit < lowWheelSpeed) && (speedExit < wheelExitSetPoint))) {
            victorExitValue = lowVictorSpeed;
        } else if (speedExit < wheelExitSetPoint) {
            victorExitValue= 1.0; 
        } else {
            victorExitValue= 0.0;
        }
        victorExit.set(null, Double.valueOf(victorExitValue));
//        if (((WsHopper) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER))
//                .getKickerValue() == true) {
//            victorExit.set(null, Double.valueOf(1.0));
//            victorEnter.set(null, Double.valueOf(1.0));
//        }
        //This is commented out because we want to keep the flywheels at speed no matter what
        /*if (((WsHopper) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER))
                .isHopperUp() == false) {
            victorExit.set(null, Double.valueOf(0.0));
            victorEnter.set(null, Double.valueOf(0.0));
        }*/
        //Pause flywheels when quick-turning
        if (((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getQuickTurnFlag() == true) {
            victorEnter.set(null, Double.valueOf(0.0));
            victorExit.set(null, Double.valueOf(0.0));
        }
        boolean lastAtSpeed = atSpeed;
        if (speedExit > wheelExitSetPoint - underSetpointTolerance &&
            speedExit < wheelExitSetPoint + aboveSetpointTolerance &&
            speedEnter > wheelEnterSetPoint - underSetpointTolerance &&
            speedEnter < wheelEnterSetPoint + aboveSetpointTolerance ) 
//            ((speedEnter < (wheelEnterSetPoint - underSetpointNoGoTolerance))|| (speedEnter > (wheelEnterSetPoint + underSetpointNoGoTolerance))) &&
//            ((speedExit  < (wheelExitSetPoint  - underSetpointNoGoTolerance))|| (speedExit  > (wheelExitSetPoint  + underSetpointNoGoTolerance))))
        {
            atSpeed = true;
        } 
        else 
        {
            atSpeed = false;
        }
        
        atSafeSpeed = ((speedExit > safeFlywheelSpeed) && (speedEnter > safeFlywheelSpeed));

        SmartDashboard.putNumber("EnterWheelSpeed", speedEnter);
        SmartDashboard.putNumber("ExitWheelSpeed", speedExit);
        SmartDashboard.putNumber("EnterCounter", enterCounterCount);
        SmartDashboard.putNumber("ExitCounter", exitCounterCount);
        SmartDashboard.putNumber("ExitWheelVictor", victorExitValue);
        SmartDashboard.putNumber("EnterWheelVictor", victorEnterValue);
        SmartDashboard.putNumber("EnterWheelSetPoint", wheelEnterSetPoint);
        SmartDashboard.putNumber("ExitWheelSetPoint", wheelExitSetPoint);
        SmartDashboard.putNumber("KnobEnterSetPoint", knobEnterSetPoint);
        SmartDashboard.putNumber("KnobExitSetPoint", knobExitSetPoint);
        SmartDashboard.putBoolean("Flywheels At Speed", atSpeed);
        
        /*if (shotSnapshots.size() > 0 ){ 
            snapshotWaitCounter++; 
            if (snapshotWaitCounter > 50){
                snapshotWaitCounter = 0 ; 
                ShotSnapshot ss = (ShotSnapshot)shotSnapshots.get(0); 
                String snapshot =  "ShotSec: " + ss.dShotSec +  " AtSpeed:" + ss.atSpeed + " Enter: " + ss.enterWheelSpeed + " Exit: " + ss.exitWheelSpeed + " Victors: " +  ss.enterVictorValue +  ss.exitVictorValue ; 
                Logger.getLogger().info(this.getClass().getName(), "Flywheel Snapshot", snapshot);
                FileLogger.getFileLogger().logData(snapshot); 
                shotSnapshots.remove(0);
            }
        }*/
        //Output snapshot at transitions to figure out range
//        if ((lastAtSpeed == true) && (atSpeed == false))
//        {
//            Logger.getLogger().debug(this.getClass().getName(), "Flywheel Not AtSpeed", " Enter: " + (int)speedEnter + " " + (int)wheelEnterSetPoint + " " +(int)(speedEnter-wheelEnterSetPoint)  + " Exit: " + (int)speedExit + " " + (int)wheelExitSetPoint + " " +(int)(speedExit-wheelExitSetPoint) + " Victors: " +  (int)victorEnterValue +  (int)victorExitValue );
//        }
        //set shooter angle
        WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_ANGLE).set(null, new Integer(angleFlag.value));
    }

    public void notifyConfigChange() {
        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();

        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();

        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();
        
        underSetpointTolerance = atSpeedToleranceUnderSetpointConfig.getValue();
        underSetpointNoGoTolerance = atSpeedToleranceUnderSetpointNoGoConfig.getValue();
        aboveSetpointTolerance = atSpeedToleranceAboveSetpointConfig.getValue();

        safeFlywheelSpeed = minimumSafeFlywheelSpeed.getValue();
        
        ENTER_GEAR_RATIO = ENTER_GEAR_RATIO_config.getValue();
        EXIT_GEAR_RATIO = EXIT_GEAR_RATIO_config.getValue();
        
        PresetTowerShooterStation = new Preset(PresetTowerShooterEnterSpeed.getValue(),
            PresetTowerShooterExitSpeed.getValue(), translatePresetConfigAngle(PresetTowerShooterAngle.getValue()));
        PresetLongLow = new Preset(PresetLongLowEnterSpeed.getValue(),
            PresetLongLowExitSpeed.getValue(), translatePresetConfigAngle(PresetLongLowAngle.getValue()));
        PresetShortHigh = new Preset(PresetShortHighEnterSpeed.getValue(),
            PresetShortHighExitSpeed.getValue(), translatePresetConfigAngle(PresetShortHighAngle.getValue()));
    }

    public void setWheelEnterSetPoint(int setPoint) {
        wheelEnterSetPoint = setPoint;
    }

    public void setWheelExitSetPoint(int setPoint) {
        wheelExitSetPoint = setPoint;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        double dpadVal;
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON2) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                if (angleFlag == DoubleSolenoid.Value.kReverse) {
                    angleFlag = DoubleSolenoid.Value.kForward;
                } else {
                    angleFlag = DoubleSolenoid.Value.kReverse;
                }
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON4) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            presetUnlock = button.getValue();
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickEnum.D_PAD_UP_DOWN) {
            dpadVal = ((DoubleSubject) subjectThatCaused).getValue();
            if (dpadVal == -1 && presetUnlock) {
                Logger.getLogger().debug(this.getClass().getName(), "acceptNotification",
                        "Set Long and Low Preset");
                angleFlag = PresetLongLow.ANGLE;
                wheelEnterSetPoint = PresetLongLow.ENTER_WHEEL_SET_POINT;
                wheelExitSetPoint = PresetLongLow.EXIT_WHEEL_SET_POINT;
                SmartDashboard.putString("Shooter Preset", "Enter: " + wheelEnterSetPoint + " Exit: " + wheelExitSetPoint + " Angle: " + angleToString(angleFlag));
            } else if (dpadVal == 1 && presetUnlock) {
                Logger.getLogger().debug(this.getClass().getName(), "acceptNotification",
                        "Set Short and High Preset");

                angleFlag = PresetShortHigh.ANGLE;
                wheelEnterSetPoint = PresetShortHigh.ENTER_WHEEL_SET_POINT;
                wheelExitSetPoint = PresetShortHigh.EXIT_WHEEL_SET_POINT;
                SmartDashboard.putString("Shooter Preset", "Enter: " + wheelEnterSetPoint + " Exit: " + wheelExitSetPoint + " Angle: " + angleToString(angleFlag));
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT) {
            dpadVal = ((DoubleSubject) subjectThatCaused).getValue();
            if (dpadVal == -1 && presetUnlock) {
                Logger.getLogger().debug(this.getClass().getName(), "acceptNotification",
                        "Set Tower Shooter Station Preset");
                angleFlag = PresetTowerShooterStation.ANGLE;
                wheelEnterSetPoint = PresetTowerShooterStation.ENTER_WHEEL_SET_POINT;
                wheelExitSetPoint = PresetTowerShooterStation.EXIT_WHEEL_SET_POINT;
                SmartDashboard.putString("Shooter Preset", "Enter: " + wheelEnterSetPoint + " Exit: " + wheelExitSetPoint + " Angle: " + angleToString(angleFlag));
            }
            if (dpadVal == 1 && presetUnlock) {
                Logger.getLogger().debug(this.getClass().getName(), "acceptNotification",
                        "Set Off Preset");
                angleFlag = PresetOffLow.ANGLE;
                wheelEnterSetPoint = PresetOffLow.ENTER_WHEEL_SET_POINT;
                wheelExitSetPoint = PresetOffLow.EXIT_WHEEL_SET_POINT;
                SmartDashboard.putString("Shooter Preset", "Enter: " + wheelEnterSetPoint + " Exit: " + wheelExitSetPoint + " Angle: Low");

            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT) {
            double currentValue = ((DoubleSubject) subjectThatCaused).getValue();
            double previousValue = ((DoubleSubject) subjectThatCaused).getPreviousValue();
            if (previousValue < 0.25 && currentValue > 0.25) {
                //We transitioned from below to above the positive threshold, increase the speed by by 100
                wheelEnterSetPoint += 50;
            } else if (previousValue > -0.25 && currentValue < -0.25) {
                //We transitioned from above to below the negative threshold, decrease the speed by 100
                wheelEnterSetPoint -= 50;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT) {
            double currentValue = ((DoubleSubject) subjectThatCaused).getValue();
            double previousValue = ((DoubleSubject) subjectThatCaused).getPreviousValue();
            if (previousValue < 0.25 && currentValue > 0.25) {
                //We transitioned from below to above the positive threshold, increase the speed by by 100
                wheelExitSetPoint += 50;
            } else if (previousValue > -0.25 && currentValue < -0.25) {
                //We transitioned from above to below the negative threshold, decrease the speed by 100
                wheelExitSetPoint -= 50;
            }
        }
    }

    public Counter getEnterCounter() {
        return counterEnter;
    }

    public Counter getExitCounter() {
        return counterExit;
    }

    public Counter getEnterCounterValue() {
        return counterEnter;
    }

    public Counter getExitCounterValue() {
        return counterExit;
    }

    public void resetEnterCounter() {
        counterEnter.reset();
        counterEnter.start();
    }

    public void resetExitCounter() {
        counterExit.reset();
        counterExit.start();
    }

    public double getKnobEnterValue() {
        return testingEnterKnob;
    }

    public double getKnobExitValue() {
        return testingExitKnob;
    }

    public void setPresetState(Preset preset) {
        this.wheelEnterSetPoint = preset.ENTER_WHEEL_SET_POINT;
        this.wheelExitSetPoint = preset.EXIT_WHEEL_SET_POINT;
        this.angleFlag = preset.ANGLE;
    }

    public boolean isAtSpeed() {
        return atSpeed;
    }
    
    public boolean isFlywheelAtSafeSpeed()
    {
        return atSafeSpeed;
    }
    
    public void cacheFlywheelSnapshot(){
            /*double time_since_init = Timer.getFPGATimestamp() - initTime; 
            int shotMs = (int)(time_since_init * 1000); 
            double dShotMs = shotMs / 1000.0; 
            shotSnapshots.add(new ShotSnapshot((int)speedEnter, (int)speedExit, (int)victorEnterValue, (int)victorExitValue, atSpeed, dShotMs));*/
    }
}
