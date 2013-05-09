/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepGroupIntakeTwoFrisbees extends WsAutonomousSerialStepGroup {

    int delay;

    public WsAutonomousStepGroupIntakeTwoFrisbees(int delay) {
        super(6);
        this.delay = delay;
        this.defineSteps();
    }

    public void defineSteps() {
        steps[0] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        steps[1] = new WsAutonomousStepDelay(delay);
        steps[2] = new WsAutonomousStepOverrideFunnelatorButtonOn();
        steps[3] = new WsAutonomousStepDelay(delay);
        steps[4] = new WsAutonomousStepOverrideFunnelatorButtonOff();
        steps[5] = new WsAutonomousStepIntakeMotorStop();

    }

    public String toString() {
        return "Taking in two frisbees";
    }
}
