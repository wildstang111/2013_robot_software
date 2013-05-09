package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousBooleanConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousBooleanStartPositionConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerStartPositionConfigFileParameter;
import com.wildstangs.autonomous.steps.WsAutonomousParallelFinishedOnAnyStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepContainer;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootFiveTwist extends WsAutonomousProgram {

    private DoubleConfigFileParameter FirstDrive;
    private DoubleConfigFileParameter SecondDrive;
    private DoubleConfigFileParameter ThirdDrive;
    private DoubleConfigFileParameter TwistAngle;
    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter SecondEnterWheelSetPoint;
    private IntegerConfigFileParameter SecondExitWheelSetPoint;
    private BooleanConfigFileParameter SecondShooterAngle;
    private IntegerConfigFileParameter FunnelatorLoadDelay;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset firstShooterPreset, secondShooterPreset;

    private void defineConfigValues() {
        TwistAngle = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".TwistAngle", 30);
        FirstDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDrive", 60.5);
        SecondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 60.5);
        ThirdDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdDrive", 60.5);

        FirstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.EnterWheelSetPoint", 2100);
        SecondExitWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.ExitWheelSetPoint", 2750);
        SecondShooterAngle = new AutonomousBooleanConfigFileParameter("FrontPyramid.ShooterAngle", true);
        ThirdFrisbeeDelay = new AutonomousIntegerConfigFileParameter("ThirdFrisbeeDelay", 700);
        FunnelatorLoadDelay = new AutonomousIntegerConfigFileParameter("FunnelatorLoadDelay", 300);

        
        firstShooterPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootFiveTwist() {
        super(25);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Drive and Set shooter");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(firstShooterPreset.ENTER_WHEEL_SET_POINT, firstShooterPreset.EXIT_WHEEL_SET_POINT, firstShooterPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
        programSteps[1] = new WsAutonomousStepWaitForShooter();
        WsAutonomousSerialStepContainer ssc1 = new WsAutonomousSerialStepContainer("Kick twice, delay, then kick again");
        programSteps[2] = ssc1;
            ssc1.addStep(new WsAutonomousStepMultikick(2));
            ssc1.addStep(new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue()));
            ssc1.addStep(new WsAutonomousStepMultikick(1));
            
        WsAutonomousSerialStepContainer sstwist = new WsAutonomousSerialStepContainer("Twist back to square up to frisbees");
        programSteps[3] = sstwist;
            sstwist.addStep(new WsAutonomousStepSetDriveHeadingPidRelativeSetpoint(TwistAngle.getValue()));
            sstwist.addStep(new WsAutonomousStepEnableDriveHeadingPid());
            sstwist.addStep(new WsAutonomousStepWaitForDriveHeadingPid());
            
            sstwist.addStep(new WsAutonomousStepLowerHopper());
        
        programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[5] = new WsAutonomousStepStartDriveUsingMotionProfile(FirstDrive.getValue(), 0.0);
        programSteps[6] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[7] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[8] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[9] = pg2;
            pg2.addStep(new WsAutonomousStepRaiseAccumulator());
            pg2.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer ssc2 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[10] = ssc2;
            ssc2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfa1 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
                pfa1.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()));
                pfa1.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());
            ssc2.addStep(pfa1);
            ssc2.addStep(new WsAutonomousStepIntakeMotorStop());
        programSteps[11] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[12] = pg3;
            pg3.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(SecondDrive.getValue(), 0.0));
            pg3.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[13] = pg4;
            pg4.addStep(new WsAutonomousStepWaitForShooter());
            pg4.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        programSteps[14] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[15] = new WsAutonomousStepMultikick(2);
        WsAutonomousSerialStepContainer ssLower = new WsAutonomousSerialStepContainer("Lower hopper and accumulator and then shooter angle");
        programSteps[16] = ssLower;
            ssLower.addStep(new WsAutonomousStepLowerAccumulator());
            ssLower.addStep(new WsAutonomousStepLowerHopper());
            ssLower.addStep(new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse));  //Put the angle down
        programSteps[17] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[18] = new WsAutonomousStepStartDriveUsingMotionProfile(ThirdDrive.getValue(), 0.0);
        programSteps[19] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[20] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[21] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[22] = pg5;
            pg5.addStep(new WsAutonomousStepRaiseAccumulator());
            pg5.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer ssc4 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[23] = ssc4;
            ssc4.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfa2 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
        WsAutonomousSerialStepContainer ssc5 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            ssc5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            ssc5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
                pfa2.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()));
                pfa2.addStep(ssc5);
            ssc4.addStep(pfa2);
            ssc4.addStep(new WsAutonomousStepIntakeMotorStop()); 
        programSteps[24] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shoot Five Frisbees TWIST And Pick Up Two More";
    }
}
