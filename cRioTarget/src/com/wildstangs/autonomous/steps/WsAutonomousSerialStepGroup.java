/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.IStepContainer;
import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousSerialStepGroup extends WsAutonomousStep implements IStepContainer {
    // A serial step group functions as a subprogram, so use it to group together related steps.

    protected final WsAutonomousStep[] steps;
    protected int currentStep, errorCount;
    protected boolean finishedStep, lastStepError;

    public WsAutonomousSerialStepGroup(int stepCount) {
        steps = new WsAutonomousStep[stepCount];
        defineSteps();
    }

    protected abstract void defineSteps();// Use this just like in WsAutonomousProgram, except the array is called "steps".

    public void initialize() {
        finishedStep = false;
        currentStep = 0;
        errorCount = 0;
        steps[0].initialize();
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedStep) {
            finishedStep = false;
            currentStep++;
            if (currentStep >= steps.length) {
                finished = true;
                cleanup();
                return;
            } else {
                steps[currentStep].initialize();
            }
        }
        WsAutonomousStep step = steps[currentStep]; //Prevent errors caused by mistyping.
        step.update();
        if (step.isFinished()) {
            finishedStep = true;
            if (!step.isPassed()) {
                lastStepError = true;
                failedStep(step);
            } else {
                lastStepError = false;
            }
        }
    }

    public WsAutonomousStep getCurrentStep() {
        return steps[currentStep];
    }

    public WsAutonomousStep getNextStep() {
        if (currentStep + 1 < steps.length) {
            return steps[currentStep + 1];
        } else {
            return null;
        }
    }

    public void setNextStep(WsAutonomousStep newStep) {
        if (steps[currentStep] instanceof WsAutonomousSerialStepGroup) {
            if (((WsAutonomousSerialStepGroup) steps[currentStep]).getNextStep() != null) {
                ((WsAutonomousSerialStepGroup) steps[currentStep]).setNextStep(newStep);
            } else {
                steps[currentStep + 1] = newStep;
            }
        } else if (currentStep + 1 < steps.length) {
            steps[currentStep + 1] = newStep;
        }
    }

    public void setStep(WsAutonomousStep newStep, int stepNumber) {
        if (currentStep != stepNumber && stepNumber >= 0 && stepNumber < steps.length) {
            steps[stepNumber] = newStep;
        }
    }

    protected void cleanup() {
        for (int i = 0; i < steps.length; i++) {
            steps[i] = null;
        }
    }

    public void finishGroup() {
        finished = true;
    }

    protected final void failedStep(WsAutonomousStep step) {
        if (step.isFatal()) {
            finished = true;
            fatal = true;
            fatalError(step);
        } else {
            handleError(step);
        }
    }

    protected void fatalError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        handleError(step);
    }

    protected void handleError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        pass = false;
        errorInfo = "";
        Logger.getLogger().error("Substep " + currentStep + "(" + step.toString() + ") of serial autonomous step group " + this.toString(), "Auto Step", step.errorInfo);
    }

    public boolean lastStepHadError() {
        if (steps[currentStep] instanceof WsAutonomousSerialStepGroup) {
            return ((WsAutonomousSerialStepGroup) steps[currentStep]).lastStepHadError();
        }
        return lastStepError;
    }
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = 59 * hash + Arrays.deepHashCode(this.steps);
//        return hash;
//    }
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepGroup)
//        {
//            WsAutonomousStepGroup obj = (WsAutonomousStepGroup)o;
//            return Arrays.deepEquals(obj.steps, steps);
//        }
//        return false;
//    }
}
