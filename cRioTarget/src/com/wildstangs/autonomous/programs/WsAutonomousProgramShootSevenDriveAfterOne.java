/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
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

public class WsAutonomousProgramShootSevenDriveAfterOne extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstDrive;
    private DoubleConfigFileParameter secondDrive;
    private DoubleConfigFileParameter thirdDrive;
    private IntegerConfigFileParameter firstEnterWheelSetPoint;
    private IntegerConfigFileParameter firstExitWheelSetPoint;
    private BooleanConfigFileParameter firstShooterAngle;
    private IntegerConfigFileParameter secondEnterWheelSetPoint;
    private IntegerConfigFileParameter secondExitWheelSetPoint;
    private BooleanConfigFileParameter secondShooterAngle;
    private IntegerConfigFileParameter thirdEnterWheelSetPoint;
    private IntegerConfigFileParameter thirdExitWheelSetPoint;
    private BooleanConfigFileParameter thirdShooterAngle;
    private IntegerConfigFileParameter thirdFrisbeeDelay;
    private IntegerConfigFileParameter accumulatorDelay;
    private WsShooter.Preset startPreset, secondShooterPreset, thirdShooterPreset;

    private void defineConfigValues() {
        firstDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.FirstDrive", 27);
        secondDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.SecondDrive", 109);
        thirdDrive = new AutonomousDoubleConfigFileParameter("ShootSeven.ThirdDrive", -58);
        firstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        firstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        firstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        secondEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondEnterWheelSetPoint", 2200);
        secondExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondExitWheelSetPoint", 2850);
        secondShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondShooterAngle", true);
        thirdEnterWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.EnterWheelSetPoint", 2100);
        thirdExitWheelSetPoint = new AutonomousIntegerConfigFileParameter("FrontPyramid.ExitWheelSetPoint", 2750);
        thirdShooterAngle = new AutonomousBooleanConfigFileParameter("FrontPyramid.ShooterAngle", true);
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
        thirdShooterPreset = new WsShooter.Preset(thirdEnterWheelSetPoint.getValue(),
                thirdExitWheelSetPoint.getValue(),
                thirdShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        
    }

    public WsAutonomousProgramShootSevenDriveAfterOne() {
        super(19);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Set shooter, drop accumulator");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
            pg1.addStep(new WsAutonomousStepWaitForShooter());
        //Kick one and then start driving
        WsAutonomousSerialStepContainer waitAndShoot = new WsAutonomousSerialStepContainer();
        programSteps[1] = waitAndShoot;
            waitAndShoot.addStep(new WsAutonomousStepWaitForShooter());
            waitAndShoot.addStep(new WsAutonomousStepMultikick(1));
        WsAutonomousSerialStepContainer pssDriveWhileShooting = new WsAutonomousSerialStepContainer("Drive While Shooting");
        WsAutonomousSerialStepContainer pssShootWhileDriving = new WsAutonomousSerialStepContainer("Shoot while driving");
        
            pssShootWhileDriving.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
            pssShootWhileDriving.addStep(new WsAutonomousStepWaitForShooter()); 
            pssShootWhileDriving.addStep(new WsAutonomousStepMultikick(2)); 
            
            pssDriveWhileShooting.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn()); 
            pssDriveWhileShooting.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(firstDrive.getValue(), 0.0));
            pssDriveWhileShooting.addStep(new WsAutonomousStepWaitForDriveMotionProfile()); 
            pssDriveWhileShooting.addStep(new WsAutonomousStepStopDriveUsingMotionProfile()); 
            pssDriveWhileShooting.addStep(new WsAutonomousStepIntakeMotorStop()); 
        
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Drive and Shoot");
        programSteps[2] = pg2;
            pg2.addStep(pssDriveWhileShooting);
            pg2.addStep(pssShootWhileDriving);
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[3] = pg3;
            pg3.addStep(new WsAutonomousStepSetShooterPreset(0,0,startPreset.ANGLE));
            pg3.addStep(new WsAutonomousStepLowerHopper());
            pg3.addStep(new WsAutonomousStepRaiseAccumulator());
            pg3.addStep(new WsAutonomousStepWaitForAccumulatorUp());
       programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
       programSteps[5] = pgIntake;
       pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
       pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
       
       WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[6] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousSerialStepContainer pssWaitandThenDrive = new WsAutonomousSerialStepContainer("Wait a bit for accumulator and then Drive");
            pssWaitandThenDrive.addStep(new WsAutonomousStepDelay(accumulatorDelay.getValue()));
            pssWaitandThenDrive.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(secondDrive.getValue(), 0.0));
            pssWaitandThenDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
            
        programSteps[7] = pssWaitandThenDrive; 
        programSteps[8] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[9] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[10] = pg5;
            pg5.addStep(new WsAutonomousStepRaiseAccumulator());
            pg5.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[11] = pg7;
            pg7.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(thirdDrive.getValue(), 0.0));
            //Keep the angle down so that the intake can still occur
            pg7.addStep(new WsAutonomousStepSetShooterPreset(thirdShooterPreset.ENTER_WHEEL_SET_POINT, thirdShooterPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg7.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelStepGroup pgIntakeDrive = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pgIntake2 = new WsAutonomousParallelStepGroup("Wait for intake");
        WsAutonomousSerialStepContainer pssDrive = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        WsAutonomousSerialStepContainer pssIntakeThenRaise = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        programSteps[12] = pgIntakeDrive;
            pgIntakeDrive.addStep(pssIntakeThenRaise);
                    pssIntakeThenRaise.addStep(pgIntake2);
                        pgIntake2.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
                        pgIntake2.addStep(new WsAutonomousStepDelay(1000)); //Min delay since it is not "finished on any"
                    pssIntakeThenRaise.addStep(new WsAutonomousStepIntakeMotorStop());
                    pssIntakeThenRaise.addStep(new WsAutonomousStepSetShooterPreset(thirdShooterPreset.ENTER_WHEEL_SET_POINT, thirdShooterPreset.EXIT_WHEEL_SET_POINT, thirdShooterPreset.ANGLE));
                
            pgIntakeDrive.addStep(pssDrive);
                pssDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile());    
                pssDrive.addStep( new WsAutonomousStepStopDriveUsingMotionProfile());   
       

        WsAutonomousSerialStepContainer pssHopperDelay = new WsAutonomousSerialStepContainer("Wait for hopper");
            pssHopperDelay.addStep(new WsAutonomousStepDelay(accumulatorDelay.getValue()));
            pssHopperDelay.addStep(new WsAutonomousStepRaiseHopper());
        programSteps[13] = pssHopperDelay; 
        programSteps[14] = new WsAutonomousStepWaitForHopperUp();
        programSteps[15] = new WsAutonomousStepWaitForShooter();
        programSteps[16] = new WsAutonomousStepDelay(200);
        programSteps[17] = new WsAutonomousStepMultikick(4);
        programSteps[18] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shooting Seven Frisbees, drive after first frisbee shot";
    }
}
