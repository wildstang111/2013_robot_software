/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.outputmanager.outputs.WsServo;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author User
 */
public class WsLoadingRamp extends WsSubsystem implements IObserver {

    private Double angle;
    private static Double ANGLE_UP = new Double(90.0);
    private static Double ANGLE_DOWN = new Double(60.0);
    private DoubleConfigFileParameter AngleUp = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleUp", 0);
    private DoubleConfigFileParameter AngleDown = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleDown", 0);

    public WsLoadingRamp(String name) {
        super(name);

        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON3);
        subject.attach(this);

        init();

        ANGLE_UP = new Double(AngleUp.getValue());
        ANGLE_DOWN = new Double(AngleDown.getValue());
    }

    public void init() {
        angle = ANGLE_UP;
    }

    public void update() {
        WsServo servo = (WsServo) (WsOutputManager.getInstance().getOutput(WsOutputManager.LOADING_RAMP));
        servo.setAngle(null, angle);
        SmartDashboard.putNumber("Loading Ramp angle:", angle.doubleValue());
    }

    public void notifyConfigChange() {
        ANGLE_UP = new Double(AngleUp.getValue());
        ANGLE_DOWN = new Double(AngleDown.getValue());
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;

        if (button.getValue()) {
            angle = ANGLE_DOWN;
            System.out.println("Angle is at " + angle);
        } else {
            angle = ANGLE_UP;
            System.out.println("Angle is at " + angle);
        }
    }
}
