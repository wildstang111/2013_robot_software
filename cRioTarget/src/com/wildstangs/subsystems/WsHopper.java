package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.outputmanager.outputs.WsServo;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam
 */
public class WsHopper extends WsSubsystem implements IObserver {

    private static final boolean KICKER_DEFAULT_VALUE = false;
    private static final DoubleSolenoid.Value LIFT_DEFAULT_VALUE = DoubleSolenoid.Value.kForward;
    private IntegerConfigFileParameter forwardCycleConfig = new IntegerConfigFileParameter(this.getClass().getName(), "forwardCycles", 15);
    private IntegerConfigFileParameter backwardCycleConfig = new IntegerConfigFileParameter(this.getClass().getName(), "backwardCycles", 15);
    private int backwardCycles;
    private int forwardCycles;
    private int cycle;
    private boolean goingForward = false, goingBack = false;
    private boolean prepareFiringSolution = false; 
    private boolean upLimitSwitchValue = false, downLimitSwitchValue = false;
    private boolean kickerButtonPressed = false;
    private boolean kickerValue;
    private DoubleSolenoid.Value liftValue;
    private int disks = 0;
    private boolean timeRecovery = false;
    private double startTime = 0;
    private DoubleConfigFileParameter tomahawkDownConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleDown", 75);
    private DoubleConfigFileParameter tomahawkUpConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleUp", 0);
    private boolean tomahawkUp = true;
    private double tomahawkUpValue;
    private double tomahawkDownValue;

    public WsHopper(String name) {
        super(name);
        init();

        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);

        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);

        subject = WsInputManager.getInstance().getSensorInput(WsInputManager.HOPPER_DOWN_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);

        subject = WsInputManager.getInstance().getSensorInput(WsInputManager.HOPPER_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);

        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON1);
        subject.attach(this);

        //Needed for tomahawk control
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
    }

    public void init() {
        forwardCycles = forwardCycleConfig.getValue();
        backwardCycles = backwardCycleConfig.getValue();
        kickerValue = KICKER_DEFAULT_VALUE;
        liftValue = LIFT_DEFAULT_VALUE;
        cycle = 0;
        kickerButtonPressed = false;
        disks = 0;
        timeRecovery = false;
        startTime = 0;
        tomahawkDownValue = tomahawkDownConfig.getValue();
        tomahawkUpValue = tomahawkUpConfig.getValue();
        tomahawkUp = true;
        prepareFiringSolution = false; 
    }

    public void update() {
        if (goingForward) {
            cycle++;
            if (cycle >= forwardCycles) {
                goingForward = false;
                goingBack = true;
                kickerValue = false;
                cycle = 0;
                //Measure time till the flywheel gets back to speed
                timeRecovery = true;
                startTime = Timer.getFPGATimestamp();
            }
        } else if (goingBack) {
            cycle++;
            if (cycle >= backwardCycles) {
                goingBack = false;
                cycle = 0;
                WsShooter shooter = (WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER);
                WsIntake intake = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
                if (kickerButtonPressed && ((this.isUpLimitSwitchTriggered() && shooter.isFlywheelAtSafeSpeed())
                        || intake.getFingerDownOverrideButtonState())) {
                    prepareFiringSolution = true; 
                }
            }
        }

        if (timeRecovery) {
            WsShooter shooter = (WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER);
            if (shooter.isAtSpeed()) {
                double endTime = Timer.getFPGATimestamp();
                double diffTime = (endTime - startTime);
                SmartDashboard.putNumber("Recovery Time", diffTime);
                //Logger.getLogger().debug(this.getClass().getName(), "Recovery Time", "Flywheel up to speed in: " + (endTime - startTime));
                timeRecovery = false;
            }
        }
        //This allows the software to choose when the kicker actually kicks. However, software hasn't found a proper solution yet
        if (prepareFiringSolution){
            WsShooter shooter = (WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER);
            WsIntake intake = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
            if (((false == this.isUpLimitSwitchTriggered()) || (false ==shooter.isFlywheelAtSafeSpeed()) ) && (false == intake.getFingerDownOverrideButtonState())){
                //Something happened between staging and firing to where it is not safe to fire!
                prepareFiringSolution =false; 
                
            }else {
                prepareFiringSolution =false; 
                goingForward = true;
                cycle = 0;
                kickerValue = true;
                if (disks > 0) {
                    disks--;
                }
                shooter.cacheFlywheelSnapshot();
            }
        }


        WsOutputManager.getInstance().getOutput(WsOutputManager.KICKER).set((IOutputEnum) null, new Boolean(kickerValue));
        ((WsServo) WsOutputManager.getInstance().getOutput(WsOutputManager.TOMAHAWK_SERVO))
                .setAngle((IOutputEnum) null, new Double(tomahawkUp ? tomahawkUpValue : tomahawkDownValue));
        WsOutputManager.getInstance().getOutput(WsOutputManager.LIFT).set((IOutputEnum) null, new Integer(liftValue.value));

        SmartDashboard.putBoolean("Kicker value", kickerValue);
        SmartDashboard.putNumber("Lift Value", liftValue.value);
        SmartDashboard.putBoolean("KickerReady", goingForward || goingBack);
        SmartDashboard.putBoolean("Up limit switch", upLimitSwitchValue);
        SmartDashboard.putBoolean("Down limit switch", downLimitSwitchValue);
        SmartDashboard.putBoolean("Frisbee Servo Position", tomahawkUp);
        SmartDashboard.putNumber("Num disks", disks);
    }

    public void notifyConfigChange() {
    }

    public boolean getLiftValueEquals(DoubleSolenoid.Value value) {
        return liftValue.equals(value);
    }

    public boolean getKickerValue() {
        return kickerValue;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;

        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) {
            kickerButtonPressed = button.getValue();
            if (button.getValue()) {
                WsShooter shooter = (WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER);
                WsIntake intake = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
                if (!goingForward && !goingBack && ((this.isUpLimitSwitchTriggered()
                        && shooter.isFlywheelAtSafeSpeed()) || intake.getFingerDownOverrideButtonState())) {
                    
                    prepareFiringSolution = true; 
                    

                }
            }
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON8) {
            if (button.getValue() == true && (button.getPreviousValue() == false)) {
                if (liftValue == DoubleSolenoid.Value.kReverse) {
                    WsIntake intakeSubsystem = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
                    boolean funnelatorLimitSwitch = intakeSubsystem.getFunnelatorLimitSwitch();
                    //Only allow the hopper to go up if the funnelator switch is NOT tripped (prevent jam-ups in autonomous)
                    if (false == funnelatorLimitSwitch) {
                        liftValue = DoubleSolenoid.Value.kForward;
                        //Also lift the tomahawk
                        tomahawkUp = true;
                    }

                } else {
                    liftValue = DoubleSolenoid.Value.kReverse;
                }
            }
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().
                getSensorInput(WsInputManager.HOPPER_DOWN_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            downLimitSwitchValue = ((BooleanSubject) WsInputManager.getInstance()
                    .getSensorInput(WsInputManager.HOPPER_DOWN_LIMIT_SWITCH)
                    .getSubject(((ISubjectEnum) null))).getValue();
        } else if (subjectThatCaused.equals(WsInputManager.getInstance().
                getSensorInput(WsInputManager.HOPPER_UP_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            upLimitSwitchValue = ((BooleanSubject) WsInputManager.getInstance()
                    .getSensorInput(WsInputManager.HOPPER_UP_LIMIT_SWITCH)
                    .getSubject(((ISubjectEnum) null))).getValue();
        } else if (subjectThatCaused == WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).
                getSubject(WsManipulatorJoystickButtonEnum.BUTTON1)) {
            if (button.getValue() == true && (button.getPreviousValue() == false)) {
                if (liftValue == DoubleSolenoid.Value.kForward) {
                    tomahawkUp = true;
                } else {
                    tomahawkUp = !tomahawkUp;
                }
            }
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) {
            if (true == button.getValue()) {
                if (liftValue == DoubleSolenoid.Value.kReverse) {
                    tomahawkUp = true;
                }
            }
        }
    }

    public DoubleSolenoid.Value getLiftState() {
        return liftValue;
    }

    public boolean isDownLimitSwitchTriggered() {
        return downLimitSwitchValue;
    }

    public boolean isUpLimitSwitchTriggered() {
        return upLimitSwitchValue;
    }

    public boolean isHopperUp() {
        return (liftValue == DoubleSolenoid.Value.kForward);
    }

    public boolean isTomahawkUp() {
        return tomahawkUp;
    }

    public void addDisk() {
        disks++;
    }

    public int getDisks() {
        return disks;
    }
}
