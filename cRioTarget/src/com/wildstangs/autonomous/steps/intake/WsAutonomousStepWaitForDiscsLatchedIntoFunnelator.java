package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsIntake;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepWaitForDiscsLatchedIntoFunnelator extends WsAutonomousStep implements IObserver {
    
    int numLatchedDiscs = 0;
    int numDiscsCollected = 0;
    boolean trueToFalse = false;

    public WsAutonomousStepWaitForDiscsLatchedIntoFunnelator() {
        
    }

    public void initialize() {
        Subject subject = WsInputManager.getInstance().getSensorInput(WsInputManager.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        numLatchedDiscs = ((WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE)).getNumLatchedDiscs();
        if (numLatchedDiscs == 0) {
            //No discs were collected, no need to intake
            finished = true;
//            numLatchedDiscs = 1 ; 
        }
        trueToFalse = false;
    }

    public void update() {
        if(numDiscsCollected >= numLatchedDiscs || trueToFalse) {
            //We have collected all the discs in the accumulator, we're done here
            numDiscsCollected = 0;
            finished = true;
        }
    }

    public String toString() {
        return "Waits for the number of discs latched in the accumulator to intake";
    }

    public void acceptNotification(Subject subjectThatCaused) {
        boolean currentValue = ((BooleanSubject)subjectThatCaused).getValue();
        boolean previousValue = ((BooleanSubject)subjectThatCaused).getPreviousValue();
        if (currentValue == false && previousValue == true) {
            numDiscsCollected++;
        }
        if (currentValue == true && previousValue == false  && (numLatchedDiscs - numDiscsCollected) <= 1) {
            trueToFalse = true;
        }
    }
}
