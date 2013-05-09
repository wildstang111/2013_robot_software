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
public class WsAutonomousStepWaitForHopperUp extends WsAutonomousStep {

    public WsAutonomousStepWaitForHopperUp() {
    }

    public void initialize() {
    }

    public void update() {
        boolean upSwitchState = ((BooleanSubject) WsInputManager.getInstance().getSensorInput(WsInputManager.HOPPER_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null)).getValue();
        if (true == upSwitchState) {
            finished = true;
        }

    }

    public String toString() {
        return "Wait For Hopper Up Limit Switch";
    }
}
