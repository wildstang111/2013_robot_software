/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepContainer;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDriveManual;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepIntakeMotorPullFrisbeesIn;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepIntakeMotorStop;

/**
 *
 * @author Nathan
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramTestSerial extends WsAutonomousProgram {

    public WsAutonomousProgramTestSerial() {
        super(2);
    }

    public void defineSteps() {
        System.out.println("Define steps called");
        WsAutonomousSerialStepContainer parallelGroup = new WsAutonomousSerialStepContainer("Test serial step container.");
            parallelGroup.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            parallelGroup.addStep(new WsAutonomousStepDelay(250));
            parallelGroup.addStep(new WsAutonomousStepIntakeMotorStop());
        programSteps[0] = parallelGroup;
        programSteps[1] = new WsAutonomousStepDriveManual(0.0, 0.0);

    }

    public String toString() {
        return "Test Serial Groups";
    }
}
