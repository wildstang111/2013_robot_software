package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousBooleanStartPositionConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousDoubleConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerStartPositionConfigFileParameter;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepContainer;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootFiveFeederStation extends WsAutonomousProgram {

    private DoubleConfigFileParameter FirstDrive;
    private DoubleConfigFileParameter SecondDrive;
    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter SecondEnterWheelSetPoint;
    private IntegerConfigFileParameter SecondExitWheelSetPoint;
    private BooleanConfigFileParameter SecondShooterAngle;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset firstShooterPreset, secondShooterPreset;
    //Feeder station drive config
    private DoubleConfigFileParameter firstFeederAngle, secondFeederAngle, firstFeederDriveDistance, firstFeederDriveVelocity,
            secondFeederDriveDistance, secondFeederDriveVelocity;

    private void defineConfigValues() {
        FirstDrive = new AutonomousDoubleConfigFileParameter("ShootFive.FirstDrive", 50);
        SecondDrive = new AutonomousDoubleConfigFileParameter("ShootFive.SecondDrive", -50);
        FirstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        SecondExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        SecondShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        ThirdFrisbeeDelay = new AutonomousIntegerConfigFileParameter("ThirdFrisbeeDelay", 700);
 
        firstShooterPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        //Feeder station drive
        firstFeederAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstFeederRelativeAngle", 90);
        secondFeederAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondFeederRelativeAngle", 90);
        firstFeederDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstFeederDriveDistance", -80);
        firstFeederDriveVelocity = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstFeederDriveVelocity", 0.0);
        secondFeederDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondFeederDriveDistance", 120);
        secondFeederDriveVelocity = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondFeederDriveVelocity", 0.0);
    }

    public WsAutonomousProgramShootFiveFeederStation() {
        super(28);
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
            ssc1.addStep(new WsAutonomousStepDelay(100));
            ssc1.addStep(new WsAutonomousStepMultikick(1));
            ssc1.addStep(new WsAutonomousStepDelay(700));
            ssc1.addStep(new WsAutonomousStepMultikick(1));
            ssc1.addStep(new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue()));
            ssc1.addStep(new WsAutonomousStepMultikick(1));
        programSteps[3] = new WsAutonomousStepLowerHopper();
        programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[5] = new WsAutonomousStepStartDriveUsingMotionProfile(FirstDrive.getValue(), 0.0);
        programSteps[6] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[7] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[8] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[9] = pg2;
            pg2.addStep(new WsAutonomousStepRaiseAccumulator());
            pg2.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        programSteps[10] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
        programSteps[11] = pgIntake;
            pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
            pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
        programSteps[12] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[13] = pg3;
            pg3.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(SecondDrive.getValue(), 0.0));
            pg3.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[14] = pg4;
            pg4.addStep(new WsAutonomousStepWaitForShooter());
            pg4.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        programSteps[15] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[16] = new WsAutonomousStepMultikick(1);
        programSteps[17] = new WsAutonomousStepDelay(700);
        programSteps[18] = new WsAutonomousStepMultikick(1);
        programSteps[19] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
        //Drive to the feeder station
        programSteps[20] = new WsAutonomousStepStartDriveUsingMotionProfile(firstFeederDriveDistance.getValue(), firstFeederDriveVelocity.getValue());
        programSteps[21] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[22] = new WsAutonomousStepStopDriveUsingMotionProfile(); 
        programSteps[23] = new WsAutonomousStepQuickTurn(firstFeederAngle.getValue());
        programSteps[24] = new WsAutonomousStepStartDriveUsingMotionProfile(secondFeederDriveDistance.getValue(), secondFeederDriveVelocity.getValue());
        programSteps[25] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[26] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[27] = new WsAutonomousStepQuickTurn(secondFeederAngle.getValue());
    }

    public String toString() {
        return "Shoot Five Frisbees, drive to protected feeder station ";
    }
}
