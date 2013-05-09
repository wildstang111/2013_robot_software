/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousStep {

    protected boolean finished, pass, fatal;
    public String errorInfo;

    public WsAutonomousStep() {
        //initialize variables
        finished = false; //A step can't finish before it starts.
        pass = true; //Most steps pass automatically, as they issue a command or check a value.
        fatal = false; //This is only used to "fail safe". Most steps don't need to interrupt the autonomous program.
        errorInfo = "Passed";
    }

    public abstract void initialize();//This method is called once, when the step is first run. Use this method to set up anything that is necessary for the step.

    public abstract void update();//This method is called on the active step, once per call to RobotTemplate.autonomousPeriodic().
    //Steps will continue to have this method called until they set finished to true.
    //Note: this method is first called right after initialize(), with no delay in between.

    public boolean isFinished() {
        return finished;
    }

    public boolean isPassed() {
        return pass;
    }

    public boolean isFatal() {
        if (isPassed()) //No test can both return a fatal error and pass, this is a catch to prevent stupid errors.
        {
            return false;
        } else {
            return fatal;
        }
    }

    public abstract String toString(); //Please use future tense (NOT present tense!) when naming steps.
//    public abstract int hashCode();
//    public abstract boolean equals(Object o);
}
