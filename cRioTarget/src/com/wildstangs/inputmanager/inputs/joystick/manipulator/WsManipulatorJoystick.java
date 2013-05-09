package com.wildstangs.inputmanager.inputs.joystick.manipulator;

import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.inputmanager.inputs.joystick.IHardwareJoystick;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystick implements IInput {

    DoubleSubject enterFlywheelAdjustment;
    DoubleSubject exitFlywheelAdjustment;
    DoubleSubject dPadUpDown;
    DoubleSubject dPadLeftRight;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick manipulatorJoystick = null;

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum == WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT) {
            return enterFlywheelAdjustment;
        } else if (subjectEnum == WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT) {
            return exitFlywheelAdjustment;
        } else if (subjectEnum == WsManipulatorJoystickEnum.D_PAD_UP_DOWN) {
            return dPadUpDown;
        } else if (subjectEnum == WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT) {
            return dPadLeftRight;
        } else if (subjectEnum instanceof WsManipulatorJoystickButtonEnum) {
            return buttons[((WsManipulatorJoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public WsManipulatorJoystick() {
        enterFlywheelAdjustment = new DoubleSubject(WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT);
        exitFlywheelAdjustment = new DoubleSubject(WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT);
        dPadUpDown = new DoubleSubject(WsManipulatorJoystickEnum.D_PAD_UP_DOWN);
        dPadLeftRight = new DoubleSubject(WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT);
        manipulatorJoystick = (Joystick) new Joystick(2);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kX, 1);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kY, 2);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kZ, 4);
//        manipulatorJoystick.setAxisChannel(Joystick.AxisType.k, 4);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kTwist, 5);
        manipulatorJoystick.setAxisChannel(Joystick.AxisType.kThrottle, 6);

        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(WsManipulatorJoystickButtonEnum.getEnumFromIndex(i));
        }
    }

    public void set(IInputEnum key, Object value) {
        if (key == WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT) {
            enterFlywheelAdjustment.setValue(value);
        } else if (key == WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT) {
            exitFlywheelAdjustment.setValue(value);
        } else if (key == WsManipulatorJoystickEnum.D_PAD_UP_DOWN) {
            dPadUpDown.setValue(value);
        } else if (key == WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT) {
            dPadLeftRight.setValue(value);
        } else if (key instanceof WsManipulatorJoystickButtonEnum) {
            buttons[((WsManipulatorJoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }
    }

    public Object get(IInputEnum key) {
        if (key == WsManipulatorJoystickEnum.ENTER_FLYWHEEL_ADJUSTMENT) {
            return enterFlywheelAdjustment.getValueAsObject();
        } else if (key == WsManipulatorJoystickEnum.EXIT_FLYWHEEL_ADJUSTMENT) {
            return exitFlywheelAdjustment.getValueAsObject();
        } else if (key == WsManipulatorJoystickEnum.D_PAD_UP_DOWN) {
            return dPadUpDown.getValueAsObject();
        } else if (key == WsManipulatorJoystickEnum.D_PAD_LEFT_RIGHT) {
            return dPadLeftRight.getValueAsObject();
        } else if (key instanceof WsManipulatorJoystickButtonEnum) {
            return buttons[((WsManipulatorJoystickButtonEnum) key).toValue()].getValueAsObject();
        } else {
            return new Double(-100);
        }
    }

    public void update() {
        enterFlywheelAdjustment.updateValue();
        exitFlywheelAdjustment.updateValue();
        dPadUpDown.updateValue();
        dPadLeftRight.updateValue();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].updateValue();
        }
    }

    public void pullData() {
        if (manipulatorJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) manipulatorJoystick).pullData();
        }
        enterFlywheelAdjustment.setValue(manipulatorJoystick.getY() * -1);
        exitFlywheelAdjustment.setValue(manipulatorJoystick.getZ() * -1);
        //Get data from the D-pad
        //We invert the values so up & left are 1, down & right are -1
        dPadUpDown.setValue(manipulatorJoystick.getThrottle() * -1);
        dPadLeftRight.setValue(manipulatorJoystick.getTwist() * -1);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(manipulatorJoystick.getRawButton(i + 1));
        }

        //System.out.println("X: "+  manipulatorJoystick.getX() + " Y: " + manipulatorJoystick.getY() + " Z:" + manipulatorJoystick.getZ() + " TH: " + manipulatorJoystick.getThrottle()+ " TW: " + manipulatorJoystick.getTwist());
    }

    public void notifyConfigChange() {
    }
}
