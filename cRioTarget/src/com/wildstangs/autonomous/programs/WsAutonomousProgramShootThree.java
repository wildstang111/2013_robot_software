/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.parameters.AutonomousBooleanStartPositionConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerConfigFileParameter;
import com.wildstangs.autonomous.parameters.AutonomousIntegerStartPositionConfigFileParameter;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootThree extends WsAutonomousProgram {

    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset startPreset;

    private void defineConfigValues() {
        FirstEnterWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new AutonomousIntegerStartPositionConfigFileParameter("FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new AutonomousBooleanStartPositionConfigFileParameter("FirstShooterAngle", false);
        ThirdFrisbeeDelay = new AutonomousIntegerConfigFileParameter("ThirdFrisbeeDelay", 700);
        
        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootThree() {
        super(8);
    }

    public void defineSteps() {
        defineConfigValues();
        programSteps[0] = new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE);
        programSteps[1] = new WsAutonomousStepWaitForShooter();
        programSteps[2] = new WsAutonomousStepMultikick(1);
        programSteps[3] = new WsAutonomousStepDelay(3000);
        programSteps[4] = new WsAutonomousStepMultikick(1);
        programSteps[5] = new WsAutonomousStepDelay(3000);
        programSteps[6] = new WsAutonomousStepMultikick(1);
        programSteps[7] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
        
    }

    public String toString() {
        return "Shooting Three Frisbees";
    }
}
