/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepIntakeIfFunnelatorTripped;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSevenManualFinger extends WsAutonomousProgram {

    private DoubleConfigFileParameter StartDrive;
    private DoubleConfigFileParameter AngleTurn;
    private DoubleConfigFileParameter SecondDrive;
    private DoubleConfigFileParameter ThirdDrive;
    private DoubleConfigFileParameter FourthDrive;
    private DoubleConfigFileParameter FifthDrive;
    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter SecondEnterWheelSetPoint;
    private IntegerConfigFileParameter SecondExitWheelSetPoint;
    private BooleanConfigFileParameter SecondShooterAngle;
    private IntegerConfigFileParameter FunnelatorLoadDelay;
    private IntegerConfigFileParameter AccumulatorDelay;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset startPreset, secondShooterPreset;

    private void defineConfigValues() {
        StartDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".StartDrive", 60.5);
        AngleTurn = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".AngleTurn", 90);
        SecondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 60.5);
        ThirdDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdDrive", 60.5);
        FourthDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FourthDrive", 60.5);
        FifthDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FifthDrive", 60.5);
        FirstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.EnterWheelSetPoint", 2100);
        SecondExitWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.ExitWheelSetPoint", 2750);
        SecondShooterAngle = new AutonomousBooleanConfigFileParameter("FrontPyramid.ShooterAngle", true);
        ThirdFrisbeeDelay = new AutonomousIntegerConfigFileParameter("ThirdFrisbeeDelay", 700);
        AccumulatorDelay = new AutonomousIntegerConfigFileParameter("LowerAccumulatorDelay", 300);
        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootSevenManualFinger() {
        super(29);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Drive and Set shooter");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(StartDrive.getValue()));
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
        programSteps[1] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Wait for shooter and drive");
        programSteps[2] = pg2;
            pg2.addStep(new WsAutonomousStepWaitForShooter());
            pg2.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[3] = new WsAutonomousStepSetDriveDistancePidSetpoint(SecondDrive.getValue());
        programSteps[4] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[5] = new WsAutonomousStepWaitForDriveDistancePid();
        WsAutonomousSerialStepContainer ssc0 = new WsAutonomousSerialStepContainer("Kick twice, delay, then kick again");
        programSteps[6] = ssc0;
            ssc0.addStep(new WsAutonomousStepMultikick(2));
            ssc0.addStep(new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue()));
            ssc0.addStep(new WsAutonomousStepMultikick(1));
        programSteps[7] = new WsAutonomousStepLowerHopper();
        programSteps[8] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[9] = new WsAutonomousStepStartDriveUsingMotionProfile(ThirdDrive.getValue(), 0.0);
        programSteps[10] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[11] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[12] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[13] = pg7;
            pg7.addStep(new WsAutonomousStepRaiseAccumulator());
            pg7.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer sscIntake1 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[14] = sscIntake1;
            sscIntake1.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfa1 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
        WsAutonomousSerialStepContainer sscIntake2 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            sscIntake2.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOn());    
            sscIntake2.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            sscIntake2.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            sscIntake2.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            sscIntake2.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            sscIntake2.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
        WsAutonomousSerialStepContainer sscfunnel = new WsAutonomousSerialStepContainer("Funnel Delay");
            sscfunnel.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            sscfunnel.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
                pfa1.addStep(sscfunnel);
                pfa1.addStep(sscIntake2);
            sscIntake1.addStep(pfa1);
            sscIntake1.addStep(new WsAutonomousStepDelay(500));
            sscIntake1.addStep(new WsAutonomousStepIntakeIfFunnelatorTripped());
            sscIntake1.addStep(new WsAutonomousStepIntakeMotorStop());
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[15] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepDelay(AccumulatorDelay.getValue()));
        programSteps[16] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[17] = new WsAutonomousStepStartDriveUsingMotionProfile(FourthDrive.getValue(), 0.0);
        programSteps[18] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[19] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[20] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg8 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[21] = pg8;
            pg8.addStep(new WsAutonomousStepRaiseAccumulator());
            pg8.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer sscIntake3 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[22] = sscIntake3;
            sscIntake3.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfaIntake2 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
        WsAutonomousSerialStepContainer sscIntake4 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            sscIntake4.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOn());    
            sscIntake4.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            sscIntake4.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            sscIntake4.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            sscIntake4.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            sscIntake4.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
        WsAutonomousSerialStepContainer sscfunnel1 = new WsAutonomousSerialStepContainer("Funnel Delay");
            sscfunnel1.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel1.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            sscfunnel1.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            sscfunnel1.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel1.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
                pfaIntake2.addStep(sscfunnel);
                pfaIntake2.addStep(sscIntake4);
            sscIntake3.addStep(pfaIntake2);
            sscIntake3.addStep(new WsAutonomousStepDelay(500));
            sscIntake3.addStep(new WsAutonomousStepIntakeIfFunnelatorTripped());
            sscIntake3.addStep(new WsAutonomousStepIntakeMotorStop());
        programSteps[23] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[24] = pg5;
            pg5.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(FifthDrive.getValue(), 0.0));
            pg5.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[25] = pg6;
            pg6.addStep(new WsAutonomousStepWaitForShooter());
            pg6.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        programSteps[26] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[27] = new WsAutonomousStepMultikick(4);
        programSteps[28] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shooting Seven Frisbees With Manual Finger";
    }
}
