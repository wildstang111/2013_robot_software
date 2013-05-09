/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousStepStub extends WsAutonomousStep {

    public WsAutonomousStepStub() {
    }

    public final boolean isFinished() {
        errorInfo = "This step (" + this.toString() + ") is a STUB!";
        return true;
    }

    public final boolean isPassed() {
        errorInfo = "This step (" + this.toString() + ") is a STUB!"; //Repeatedly resetting errorInfo to ensure isn't changed accidentally.
        return false;
    }

    public final boolean isFatal() {
        errorInfo = "This step (" + this.toString() + ") is a STUB!"; //Repeatedly resetting errorInfo to ensure isn't changed accidentally.
        return true;
    }
}
