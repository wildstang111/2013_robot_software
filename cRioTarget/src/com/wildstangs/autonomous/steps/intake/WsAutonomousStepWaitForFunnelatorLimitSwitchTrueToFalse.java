/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Joey
 */
public class WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse extends WsAutonomousStep implements IObserver {

    public WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse() {
    }

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getSensorInput(WsInputManager.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
    }

    public void update() {
    }

    public String toString() {
        return "Waiting for funnelator limit switch to go from true to false";
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (((BooleanSubject) subjectThatCaused).getValue() == false) {
            finished = true;
        }
    }
}
