/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousBooleanConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousBooleanStartPositionConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousDoubleConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerStartPositionConfigFileParameter;
import com.wildstangs.autonomous.steps.WsAutonomousParallelFinishedOnAnyStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepContainer;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForAccumulatorLeftAndRightLimitSwitches;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedIntoFunnelator;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSevenActiveAccumulator extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstDrive;
    private DoubleConfigFileParameter secondDrive;
    private DoubleConfigFileParameter thirdDrive;
    private DoubleConfigFileParameter thirdDriveHeading;
    private IntegerConfigFileParameter firstEnterWheelSetPoint;
    private IntegerConfigFileParameter firstExitWheelSetPoint;
    private BooleanConfigFileParameter firstShooterAngle;
    private IntegerConfigFileParameter secondEnterWheelSetPoint;
    private IntegerConfigFileParameter secondExitWheelSetPoint;
    private BooleanConfigFileParameter secondShooterAngle;
    private IntegerConfigFileParameter accumulatorDelay;

    private WsShooter.Preset startPreset, secondShooterPreset;

    private void defineConfigValues() {
        firstDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.FirstDrive", 27);
        secondDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.SecondDrive", 109);
        thirdDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDrive", -58);
        thirdDriveHeading = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDriveHeading", 0.22);
        firstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        firstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        firstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        secondEnterWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.EnterWheelSetPoint", 2100);
        secondExitWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.ExitWheelSetPoint", 2750);
        secondShooterAngle = new AutonomousBooleanConfigFileParameter("FrontPyramid.ShooterAngle", true);
        accumulatorDelay = new AutonomousIntegerConfigFileParameter("LowerAccumulatorDelay", 300);
        
        startPreset = new WsShooter.Preset(firstEnterWheelSetPoint.getValue(),
                firstExitWheelSetPoint.getValue(),
                firstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(secondEnterWheelSetPoint.getValue(),
                secondExitWheelSetPoint.getValue(),
                secondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootSevenActiveAccumulator() {
        super(17);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Set shooter, drop accumulator");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
        WsAutonomousSerialStepContainer waitAndKick = new WsAutonomousSerialStepContainer();
        programSteps[1] = waitAndKick;
            waitAndKick.addStep(new WsAutonomousStepWaitForShooter());
            waitAndKick.addStep(new WsAutonomousStepMultikick(3));
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Drop hopper and turn on accum");
        programSteps[2] = pg2;
            pg2.addStep(new WsAutonomousStepLowerHopper());
            pg2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            pg2.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(firstDrive.getValue(), 0.0)); 
        WsAutonomousParallelStepGroup stopRaiseAccum = new WsAutonomousParallelStepGroup("Stop Drive, raise accumulator");
        WsAutonomousSerialStepContainer stopMotion = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        WsAutonomousSerialStepContainer raiseAccum = new WsAutonomousSerialStepContainer("Raise Accumulator");
        WsAutonomousParallelFinishedOnAnyStepGroup toLrTrip = new WsAutonomousParallelFinishedOnAnyStepGroup("Timeout or left and right trip");
            stopMotion.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
            stopMotion.addStep(new WsAutonomousStepStopDriveUsingMotionProfile());
            
            toLrTrip.addStep(new WsAutonomousStepWaitForAccumulatorLeftAndRightLimitSwitches());
            toLrTrip.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
            
            raiseAccum.addStep(toLrTrip);
            raiseAccum.addStep(new WsAutonomousStepIntakeMotorStop());
            raiseAccum.addStep(new WsAutonomousStepRaiseAccumulator());
            raiseAccum.addStep(new WsAutonomousStepWaitForAccumulatorUp());
            
            stopRaiseAccum.addStep(stopMotion);
            stopRaiseAccum.addStep(raiseAccum);
        programSteps[3] = stopRaiseAccum; 
        programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
        programSteps[5] = pgIntake;
            pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedIntoFunnelator());            
            pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
       
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[6] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            WsAutonomousSerialStepContainer pssWaitandThenDrive = new WsAutonomousSerialStepContainer("Wait a bit for accumulator and then Drive");
            pg4.addStep(pssWaitandThenDrive); 
                pssWaitandThenDrive.addStep(new WsAutonomousStepDelay(accumulatorDelay.getValue()));
                pssWaitandThenDrive.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(secondDrive.getValue(), 0.0));
        WsAutonomousParallelStepGroup stopRaiseAccum1 = new WsAutonomousParallelStepGroup("Stop Drive, raise accumulator 1");
        WsAutonomousSerialStepContainer stopMotion1 = new WsAutonomousSerialStepContainer("Wait for Drive and stop 1");
        WsAutonomousSerialStepContainer raiseAccum1 = new WsAutonomousSerialStepContainer("Raise Accumulator 1");
        WsAutonomousParallelFinishedOnAnyStepGroup toLrTrip1 = new WsAutonomousParallelFinishedOnAnyStepGroup("Timeout or left and right trip 1");
            stopMotion1.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
            stopMotion1.addStep(new WsAutonomousStepStopDriveUsingMotionProfile());
            
            toLrTrip1.addStep(new WsAutonomousStepWaitForAccumulatorLeftAndRightLimitSwitches());
            toLrTrip1.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
            
            raiseAccum1.addStep(toLrTrip1);
            raiseAccum1.addStep(new WsAutonomousStepIntakeMotorStop());
            raiseAccum1.addStep(new WsAutonomousStepRaiseAccumulator());
            raiseAccum1.addStep(new WsAutonomousStepWaitForAccumulatorUp());
            
            stopRaiseAccum1.addStep(stopMotion1);
            stopRaiseAccum1.addStep(raiseAccum1);
        programSteps[7] = stopRaiseAccum1; 
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[8] = pg7;
            pg7.addStep(new WsAutonomousStepStartDriveUsingMotionProfileAndHeading(thirdDrive.getValue(), 0.0, thirdDriveHeading.getValue()));
            pg7.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg7.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelStepGroup pgIntakeDrive = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pgIntake2 = new WsAutonomousParallelStepGroup("Wait for intake");
        WsAutonomousSerialStepContainer pssDrive = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        WsAutonomousSerialStepContainer pssIntakeThenRaise = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        programSteps[9] = pgIntakeDrive;
            pgIntakeDrive.addStep(pssIntakeThenRaise);
                    pssIntakeThenRaise.addStep(pgIntake2);
                        pgIntake2.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
                        pgIntake2.addStep(new WsAutonomousStepDelay(1000)); //Min delay since it is not "finished on any"
                    pssIntakeThenRaise.addStep(new WsAutonomousStepIntakeMotorStop());
                    pssIntakeThenRaise.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
                
            pgIntakeDrive.addStep(pssDrive);
                pssDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile());    
                pssDrive.addStep( new WsAutonomousStepStopDriveUsingMotionProfile());   
       

        programSteps[10] = new WsAutonomousStepRaiseHopper();
        programSteps[11] = new WsAutonomousStepWaitForHopperUp();
        programSteps[12] = new WsAutonomousStepWaitForShooter(); 
        //Wait for frisbees to settle.
        programSteps[13] = new WsAutonomousStepDelay(200);
        programSteps[14] = new WsAutonomousStepMultikick(5);
        programSteps[15] = new WsAutonomousStepDelay(300);
        programSteps[16] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shooting Seven Frisbees with active accumulator optimizations";
    }
}
