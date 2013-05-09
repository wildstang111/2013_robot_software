package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousBooleanStartPositionConfigFileParameter;
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

public class WsAutonomousProgramShootFiveFromMiddleLine extends WsAutonomousProgram {

    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter SecondEnterWheelSetPoint;
    private IntegerConfigFileParameter SecondExitWheelSetPoint;
    private BooleanConfigFileParameter SecondShooterAngle;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset firstShooterPreset, secondShooterPreset;
    //Middle line drive config
    private DoubleConfigFileParameter turnToFrisbeesAngle, turnToShootAngle, firstFrisbeeDriveDistance, firstFrisbeeDriveHeading,
            secondFrisbeeDriveDistance,firstBackToShootDriveDistance, firstBackToShootDriveHeading,
            secondBackToShootDriveDistance;

    private void defineConfigValues() {

        FirstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2500);
        SecondExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3250);
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
        //Middle Line drive
        turnToFrisbeesAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".TurnToFrisbeesAngle", -45);
        turnToShootAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".TurnToShootAngle", -45);
        firstFrisbeeDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstFrisbeeDriveDistance", -80);
        firstFrisbeeDriveHeading = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstFrisbeeDriveHeading", 0.5);
        secondFrisbeeDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondFrisbeeDriveDistance", 20);
        firstBackToShootDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstBackToShootDriveDistance", -80);
        firstBackToShootDriveHeading = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstBackToShootDriveHeading", 0.5);
        secondBackToShootDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondBackToShootDriveDistance", 29);
    }

    public WsAutonomousProgramShootFiveFromMiddleLine() {
        super(22);
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

        programSteps[4] = new WsAutonomousStepStartDriveUsingMotionProfileAndHeading(firstFrisbeeDriveDistance.getValue(), 0.0, firstFrisbeeDriveHeading.getValue());
        programSteps[5] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[6] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[7] = new WsAutonomousStepQuickTurn(turnToFrisbeesAngle.getValue()); 
        programSteps[8] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[9] = new WsAutonomousStepStartDriveUsingMotionProfile(secondFrisbeeDriveDistance.getValue(), 0.0);
        programSteps[10] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[11] = new WsAutonomousStepStopDriveUsingMotionProfile();
        
        programSteps[12] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[13] = pg2;
            pg2.addStep(new WsAutonomousStepRaiseAccumulator());
            pg2.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        
        WsAutonomousSerialStepContainer pssDrive = new WsAutonomousSerialStepContainer("Do all the drive stuff back to the shooting position");
            pssDrive.addStep(new WsAutonomousStepStartDriveUsingMotionProfileAndHeading(firstBackToShootDriveDistance.getValue(), 0.0, firstBackToShootDriveHeading.getValue()));
            pssDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile()); 
            pssDrive.addStep(new WsAutonomousStepStopDriveUsingMotionProfile());
            pssDrive.addStep(new WsAutonomousStepQuickTurn(turnToShootAngle.getValue())); 
            pssDrive.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
            pssDrive.addStep( new WsAutonomousStepStartDriveUsingMotionProfile(secondBackToShootDriveDistance.getValue(), 0.0));
            pssDrive.addStep( new WsAutonomousStepWaitForDriveMotionProfile()); 
            pssDrive.addStep( new WsAutonomousStepStopDriveUsingMotionProfile());
        WsAutonomousSerialStepContainer pssIntake = new WsAutonomousSerialStepContainer("Intake the frisbees");
            pssIntake.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
                WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
                pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
                pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
            pssIntake.addStep(pgIntake);
        WsAutonomousParallelStepGroup pgPrepForShot = new WsAutonomousParallelStepGroup("Get back to take shots");
            pgPrepForShot.addStep(pssDrive);
            pgPrepForShot.addStep(pssIntake);
        programSteps[14] = pgPrepForShot;
        programSteps[15] = new WsAutonomousStepRaiseHopper();
        programSteps[16] = new WsAutonomousStepWaitForHopperUp();
        programSteps[17] = new WsAutonomousStepWaitForShooter();
        programSteps[18] = new WsAutonomousStepMultikick(1);
        programSteps[19] = new WsAutonomousStepDelay(700);
        programSteps[20] = new WsAutonomousStepMultikick(1);
        programSteps[21] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
        //Drive to the feeder station
//        programSteps[20] = new WsAutonomousStepStartDriveUsingMotionProfile(firstFrisbeeDriveDistance.getValue(), firstFrisbeeDriveHeading.getValue());
//        programSteps[21] = new WsAutonomousStepWaitForDriveMotionProfile();
//        programSteps[22] = new WsAutonomousStepStopDriveUsingMotionProfile(); 
//        programSteps[23] = new WsAutonomousStepQuickTurn(turnToFrisbeesAngle.getValue());
//        programSteps[24] = new WsAutonomousStepStartDriveUsingMotionProfile(secondFrisbeeDriveDistance.getValue(), secondFrisbeeDriveHeading.getValue());
//        programSteps[25] = new WsAutonomousStepWaitForDriveMotionProfile();
//        programSteps[26] = new WsAutonomousStepStopDriveUsingMotionProfile();
//        programSteps[27] = new WsAutonomousStepQuickTurn(turnToShootAngle.getValue());
    }

    public String toString() {
        return "Shoot Five Frisbees - From Side to Center Line Discs ";
    }
}
