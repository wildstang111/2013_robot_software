/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

/**
 *
 * @author coder65535
 */
public interface IStepContainer {

    public WsAutonomousStep getCurrentStep();

    public WsAutonomousStep getNextStep();

    public void setNextStep(WsAutonomousStep newStep);

    public void setStep(WsAutonomousStep newStep, int stepNumber);

    public boolean lastStepHadError();
}
