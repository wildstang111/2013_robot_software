/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;

/**
 *
 * @author Joey
 */
public class WsAutonomousStepWaitForAccumulatorLeftAndRightLimitSwitches extends WsAutonomousStep {

    public WsAutonomousStepWaitForAccumulatorLeftAndRightLimitSwitches() {
    }

    public void initialize() {
    }

    public void update() {
        if (((BooleanSubject) WsInputManager.getInstance().
                getSensorInput(WsInputManager.LEFT_ACCUMULATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null)).
                getValue() == true && ((BooleanSubject) WsInputManager.getInstance().
                getSensorInput(WsInputManager.RIGHT_ACCUMULATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null)).
                getValue() == true) {
            finished = true;
        }
    }

    public String toString() {
        return "Waiting for accumulator left and right limit switches";
    }
}
