/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.outputmanager.outputs;

import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Servo;

/**
 *
 * @author Rick a.k.a. Batman
 */
public class WsServo implements IOutput {

    Servo servo;
    DoubleSubject position;
    private boolean angleSet;

    public WsServo(String name, int channel) {
        this.position = new DoubleSubject(name);
        this.servo = new Servo(channel);
        angleSet = false;
    }

    public void set(IOutputEnum key, Object value) {
        position.setValue(((Double) value).doubleValue());
        angleSet = false;
    }

    public void setAngle(IOutputEnum key, Object value) {
        position.setValue(((Double) value).doubleValue());
        angleSet = true;
    }

    public Object get(IOutputEnum key) {
        return this.position.getValueAsObject();
    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return this.position;
    }

    public void update() {
        this.position.updateValue();
        if (angleSet) {
            this.servo.setAngle(this.position.getValue());
        } else {
            this.servo.set(this.position.getValue());
        }
    }

    public void notifyConfigChange() {
    }
}
