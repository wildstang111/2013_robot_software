/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepStartDriveUsingMotionProfile extends WsAutonomousStep {

    double distance;
    double goal_velocity;

    public WsAutonomousStepStartDriveUsingMotionProfile(double distance , double goal_velocity) {
        this.distance = distance;
        this.goal_velocity = goal_velocity;
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).startStraightMoveWithMotionProfile(distance , goal_velocity);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Start the drive using motion profile for " + distance + " inches and reach going " + goal_velocity + " inches/second ";
    }
}
