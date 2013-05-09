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

public class WsAutonomousProgramShootSevenSensor extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstDrive;
    private DoubleConfigFileParameter secondDrive;
    private DoubleConfigFileParameter thirdDrive;
    private IntegerConfigFileParameter firstEnterWheelSetPoint;
    private IntegerConfigFileParameter firstExitWheelSetPoint;
    private BooleanConfigFileParameter firstShooterAngle;
    private IntegerConfigFileParameter secondEnterWheelSetPoint;
    private IntegerConfigFileParameter secondExitWheelSetPoint;
    private BooleanConfigFileParameter secondShooterAngle;
    private IntegerConfigFileParameter thirdFrisbeeDelay;
    private IntegerConfigFileParameter accumulatorDelay;

    private WsShooter.Preset startPreset, secondShooterPreset;

    private void defineConfigValues() {
        firstDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.FirstDrive", 27);
        secondDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.SecondDrive", 109);
        thirdDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDrive", -58);
        firstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        firstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        firstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        secondEnterWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.EnterWheelSetPoint", 2100);
        secondExitWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.ExitWheelSetPoint", 2750);
        secondShooterAngle = new AutonomousBooleanConfigFileParameter("FrontPyramid.ShooterAngle", true);
        thirdFrisbeeDelay = new AutonomousIntegerConfigFileParameter("ThirdFrisbeeDelay", 700);
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

    public WsAutonomousProgramShootSevenSensor() {
        super(24);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Set shooter, drop accumulator");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
            pg1.addStep(new WsAutonomousStepWaitForShooter());
        WsAutonomousSerialStepContainer waitAndShoot = new WsAutonomousSerialStepContainer();
        programSteps[1] = waitAndShoot;
            waitAndShoot.addStep(new WsAutonomousStepWaitForShooter());
            waitAndShoot.addStep(new WsAutonomousStepMultikick(2));
        programSteps[2] = new WsAutonomousStepDelay(thirdFrisbeeDelay.getValue());
        programSteps[3] = new WsAutonomousStepMultikick(1);
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Drop hopper and turn on accum");
        programSteps[4] = pg2;
            pg2.addStep(new WsAutonomousStepLowerHopper());
            pg2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            pg2.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(firstDrive.getValue(), 0.0));
        programSteps[5] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[6] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[7] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[8] = pg3;
            pg3.addStep(new WsAutonomousStepRaiseAccumulator());
            pg3.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        programSteps[9] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
        programSteps[10] = pgIntake;
            pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
            pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
       
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[11] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            WsAutonomousSerialStepContainer pssWaitandThenDrive = new WsAutonomousSerialStepContainer("Wait a bit for accumulator and then Drive");
            pg4.addStep(pssWaitandThenDrive); 
                pssWaitandThenDrive.addStep(new WsAutonomousStepDelay(accumulatorDelay.getValue()));
                pssWaitandThenDrive.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(secondDrive.getValue(), 0.0));
        programSteps[12] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[13] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[14] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[15] = pg5;
            pg5.addStep(new WsAutonomousStepRaiseAccumulator());
            pg5.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[16] = pg7;
            pg7.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(thirdDrive.getValue(), 0.0));
            pg7.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg7.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelStepGroup pgIntakeDrive = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pgIntake2 = new WsAutonomousParallelStepGroup("Wait for intake");
        WsAutonomousSerialStepContainer pssDrive = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        WsAutonomousSerialStepContainer pssIntakeThenRaise = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        programSteps[17] = pgIntakeDrive;
            pgIntakeDrive.addStep(pssIntakeThenRaise);
                    pssIntakeThenRaise.addStep(pgIntake2);
                        pgIntake2.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
                        pgIntake2.addStep(new WsAutonomousStepDelay(1000)); //Min delay since it is not "finished on any"
                    pssIntakeThenRaise.addStep(new WsAutonomousStepIntakeMotorStop());
                    pssIntakeThenRaise.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
                
            pgIntakeDrive.addStep(pssDrive);
                pssDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile());    
                pssDrive.addStep( new WsAutonomousStepStopDriveUsingMotionProfile());   
       

        programSteps[18] = new WsAutonomousStepRaiseHopper();
        programSteps[19] = new WsAutonomousStepWaitForHopperUp();
        programSteps[20] = new WsAutonomousStepWaitForShooter(); 
        programSteps[21] = new WsAutonomousStepDelay(200);
        programSteps[22] = new WsAutonomousStepMultikick(4);
        programSteps[23] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shooting Seven Frisbees sensor only";
    }
}
