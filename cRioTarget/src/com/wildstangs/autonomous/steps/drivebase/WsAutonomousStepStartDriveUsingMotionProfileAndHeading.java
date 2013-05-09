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
public class WsAutonomousStepStartDriveUsingMotionProfileAndHeading extends WsAutonomousStep {

    double distance;
    double goal_velocity;
    double heading;

    public WsAutonomousStepStartDriveUsingMotionProfileAndHeading(double distance , double goal_velocity, double heading) {
        this.distance = distance;
        this.goal_velocity = goal_velocity;
        this.heading = heading;
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).startMoveWithHeadingAndMotionProfile(distance, goal_velocity, heading);
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Start the drive using motion profile for " + distance + " inches and reach going " + goal_velocity + " inches/second at a heading of " + heading;
    }
}
