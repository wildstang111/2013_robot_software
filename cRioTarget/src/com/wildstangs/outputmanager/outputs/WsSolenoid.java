package com.wildstangs.outputmanager.outputs;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author Nathan
 */
public class WsSolenoid implements IOutput {

    BooleanSubject subject;
    Solenoid solenoid;

    public WsSolenoid(String name, int module, int channel1) {
        this.subject = new BooleanSubject(name);
        subject.setValue(false);
        solenoid = new Solenoid(module, channel1);
        solenoid.set(false);


    }

    public WsSolenoid(String name, int channel1) {
        this(name, 1, channel1);
    }

    public void set(IOutputEnum key, Object value) {
        subject.setValue(((Boolean) value).booleanValue());

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return subject;
    }

    public Object get(IOutputEnum key) {
        return subject.getValueAsObject();
    }

    public void update() {
        subject.updateValue();
        solenoid.set(subject.getValue());
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
