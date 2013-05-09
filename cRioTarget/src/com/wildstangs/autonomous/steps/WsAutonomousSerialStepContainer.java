package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author coder65535
 */
public class WsAutonomousSerialStepContainer extends WsAutonomousStep {
    //Parallel groups execute all contained steps sequentially

    final List steps = new List();
    int currentStep = 0;
    boolean initialized = false;
    String name = "";
    private boolean finishedPreviousStep;

    public WsAutonomousSerialStepContainer() {
        name = "";
    }

    public WsAutonomousSerialStepContainer(String name) {
        this.name = name;
    }

    public void initialize() {
        finishedPreviousStep = false;
        currentStep = 0;
        if (!steps.isEmpty()) {
            ((WsAutonomousStep) steps.get(0)).initialize();
        }
        initialized = true;
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedPreviousStep) {
            finishedPreviousStep = false;
            currentStep++;
            if (currentStep >= steps.size()) {
                //We have reached the end of our list of steps, we're finished
                finished = true;
                return;
            } else {
                ((WsAutonomousStep) steps.get(currentStep)).initialize();
            }
        }
        WsAutonomousStep step = (WsAutonomousStep) steps.get(currentStep);
        step.update();
        if (step.isFinished()) {
            finishedPreviousStep = true;
            if (!step.isPassed()) {
                failedStep(step, currentStep);
            } else {
                finishedPreviousStep = true;
            }
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
        Logger.getLogger().error("Substep " + i + "(" + step.toString() + ") of serial autonomous step group.", "Auto Step", step.errorInfo);
    }

    public void addStep(WsAutonomousStep step) {
        if (!initialized) {
            steps.add(step);
        }
    }

    public String toString() {
        return "Serial step group: " + name;
    }
}
