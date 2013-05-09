/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.*;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepSkipNextStepIfError extends WsAutonomousStep {
    //Note: This step silently passes on the error status of the previous step.
    //(That is, if the previous step had an error, this step doesn't log an error, but WsAutonomousManager.getInstance().getRunningProgram().lastStepHadError() will remain true.
    //Also, it doesn't work in parallel step groups.

    public WsAutonomousStepSkipNextStepIfError() {
        //Nothing to set.
    }

    public void initialize() {
        //All the work is done in update().
    }

    public void update() {
        finished = true;
        if (WsAutonomousManager.getInstance().getRunningProgram().lastStepHadError()) {
            errorInfo = "";
            pass = false;
            WsAutonomousManager.getInstance().getRunningProgram().setNextStep(new WsAutonomousStepNoOp());
        }
    }

//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepSkipNextStepIfError)
//        {
//            return true;
//        }
//        return false;
//    }
    public String toString() {
        return "Skips next step if last step had error";
    }
//    public int hashCode()
//    {
//        int hash = 3;
//        return hash;
//    }
}
