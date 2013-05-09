/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.shooter;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsShooter;
import com.wildstangs.subsystems.WsShooter.Preset;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepSetShooterPreset extends WsAutonomousStep {

    private int enterWheelSetpoint;
    private int exitWheelSetpoint;
    private DoubleSolenoid.Value angle;

    public WsAutonomousStepSetShooterPreset(int enterWheelSetpoint, int exitWheelSetpoint, DoubleSolenoid.Value angle) {
        this.enterWheelSetpoint = enterWheelSetpoint;
        this.exitWheelSetpoint = exitWheelSetpoint;
        this.angle = angle;
    }

    public void initialize() {
        Preset preset = new Preset(enterWheelSetpoint, exitWheelSetpoint, angle);
        ((WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER)).setPresetState(preset);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Set the Shooter to a given preset with a front and back flywheel setpoint and a shooter angle";
    }
}