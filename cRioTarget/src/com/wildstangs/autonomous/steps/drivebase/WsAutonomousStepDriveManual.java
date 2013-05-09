/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickEnum;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepDriveManual extends WsAutonomousStep {

    private double throttle, heading;
    public static final double KEEP_PREVIOUS_STATE = 2.0;

    public WsAutonomousStepDriveManual(double throttle, double heading) {
        this.throttle = throttle;
        this.heading = heading;
    }

    public void initialize() {
        finished = true;
        if (throttle != KEEP_PREVIOUS_STATE) {
            WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.THROTTLE, new Double(Math.max(Math.min(throttle, 1.0), -1.0)));
        }
        if (heading != KEEP_PREVIOUS_STATE) {
            WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(Math.max(Math.min(heading, 1.0), -1.0)));
        }
    }

    public void update() {
    }

//    public int hashCode()
//    {
//        int hash = 5;
//        hash = 67 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepSetThrottle)
//        {
//            WsAutonomousStepSetThrottle obj = (WsAutonomousStepSetThrottle)o;
//            return obj.value == this.value;
//        }
//        return false;
//    }
    public String toString() {
        return "Set throttle to " + throttle + " and heading to " + heading;
    }
}
