package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joshua
 */
public class WsClimber extends WsSubsystem implements IObserver {

    private static final boolean CLIMB_DEFAULT_VALUE = false;
    private boolean climbState;

    public WsClimber(String name) {
        super(name);
        WsInputManager.getInstance().attachJoystickButton(WsDriverJoystickButtonEnum.BUTTON2 , this); 
        
//        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON2);
//        subject.attach(this);
    }

    public void init() {
        climbState = CLIMB_DEFAULT_VALUE;
    }

    public void update() {
        WsOutputManager.getInstance().getOutput(WsOutputManager.CLIMBER).set((IOutputEnum) null, new Boolean(climbState));

        SmartDashboard.putBoolean("Climb State", climbState);
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON2) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            if (true == button.getValue() && false == button.getPreviousValue()) {
                climbState = !climbState;
            }
        }
    }

    public boolean getClimbState() {
        return climbState;
    }
}