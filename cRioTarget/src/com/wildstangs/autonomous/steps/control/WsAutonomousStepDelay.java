/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepDelay extends WsAutonomousStep /* This step delays testing for the specified number of cycles.
 * Note: If used in a parallel step group, it insures that the group waits for at least the specified number of cycles, instead.
 */ {

    private int count;
    protected final int msDelay;
    private static final int MS_PER_FRAME = 20;

    public WsAutonomousStepDelay(int msDelay) {
        int delay = (int) Math.ceil((double) msDelay / (double) MS_PER_FRAME);
        count = delay - 1;
        this.msDelay = msDelay;
        if (delay < 0) {
            pass = false;
            errorInfo = "Negative delay";
        }
    }

    public WsAutonomousStepDelay() {
        this(1000);
    }

    public void initialize() // Do nothing, as the variables have been initialised in the constructor.
    {
    }

    public void update() {
        if (count-- <= 0) //Preventing stupid errors that could occur by passing a negative value into the constructor.
        {
            finished = true;
        }
    }

    public String toString() {
        return "Delay for " + msDelay + "  ms";
    }
//    public int hashCode()
//    {
//        int hash = 5;
//        hash = 53 * hash + this.originalCount;
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepDelay)
//        {
//            WsAutonomousStepDelay step = (WsAutonomousStepDelay) o;
//            if (step.originalCount == this.originalCount)
//            {
//                return true;
//            }
//        }
//        return false;
//    }
}
