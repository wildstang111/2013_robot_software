package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepIntakeMotorPullFrisbeesIn;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepIntakeMotorPushFrisbeesOut;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepIntakeMotorStop;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepLowerAccumulator;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepRaiseAccumulator;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepLowerHopper;

/**
 *
 * @author Liam
 */
public class WsAutonomousProgramFloorPickup extends WsAutonomousProgram {

    public WsAutonomousProgramFloorPickup() {
        super(10);
    }

    protected void defineSteps() {
        programSteps[0] = new WsAutonomousStepLowerAccumulator();
        programSteps[1] = new WsAutonomousStepLowerHopper();
        programSteps[2] = new WsAutonomousStepIntakeMotorPushFrisbeesOut();
        programSteps[3] = new WsAutonomousStepDelay(2000);
        programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[5] = new WsAutonomousStepIntakeMotorStop();
        programSteps[6] = new WsAutonomousStepDelay(2000);
        programSteps[7] = new WsAutonomousStepRaiseAccumulator();
        programSteps[8] = new WsAutonomousStepDelay(2000);
        programSteps[9] = new WsAutonomousStepLowerAccumulator();
    }

    public String toString() {
        return "Raise Accumulator, Lower Accumulator & Hopper, Move motor forward then backward, Raise Accumulator, and lower it";
    }
}