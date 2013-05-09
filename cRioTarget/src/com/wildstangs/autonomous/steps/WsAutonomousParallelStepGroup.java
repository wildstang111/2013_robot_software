/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author coder65535
 */
public class WsAutonomousParallelStepGroup extends WsAutonomousStep {
    //Parallel groups execute all contained steps in the same frame. Be careful!
    //Note: a finished step is immediately removed from the list. update() is not called on any step that finishes.

    final List steps = new List();
    boolean initialized = false;
    String name = "";

    public WsAutonomousParallelStepGroup() {
        name = "";
    }

    public WsAutonomousParallelStepGroup(String name) {
        this.name = name;
    }

    public void initialize() {
        initialized = true;
        for (int i = 0; i < steps.size(); i++) {
            ((WsAutonomousStep) steps.get(i)).initialize();
        }
    }

    public void update() {
        for (int i = 0; i < steps.size(); i++) {
            WsAutonomousStep step = (WsAutonomousStep) steps.get(i);
            step.update();
            if (step.isFinished()) {
                if (!step.isPassed()) {
                    failedStep(step, i);
                }
                steps.remove(step);
            }
        }
        if (steps.isEmpty()) {
            finished = true;
        }
    }

    protected final void failedStep(WsAutonomousStep step, int i) {
        if (step.isFatal()) {
            finished = true;
            fatal = true;
        }
        handleError(step, i);
    }

    protected void handleError(WsAutonomousStep step, int i) //Separate method for easy overrides.
    {
        pass = false;
        errorInfo = "";
        Logger.getLogger().error("Substep " + i + "(" + step.toString() + ") of parallel autonomous step group.", "Auto Step", step.errorInfo);
    }

    public void addStep(WsAutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

//    public int hashCode()
//    {
//        int hash = 5;
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        return false;
//    }
    public String toString() {
        return "Parallel step group: " + name;
    }
}
