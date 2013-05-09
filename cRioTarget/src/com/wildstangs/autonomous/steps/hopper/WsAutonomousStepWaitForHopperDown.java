/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;

/**
 *
 * @author Inspiron N5110
 */
public class WsAutonomousStepWaitForHopperDown extends WsAutonomousStep {

    public WsAutonomousStepWaitForHopperDown() {
    }

    public void initialize() {
    }

    public void update() {
        boolean downSwitchState = ((BooleanSubject) WsInputManager.getInstance().getSensorInput(WsInputManager.HOPPER_DOWN_LIMIT_SWITCH).getSubject((ISubjectEnum) null)).getValue();
        if (true == downSwitchState) {
            finished = true;
        }

    }

    public String toString() {
        return "Wait For Hopper Down Limit Switch";
    }
}
