package com.wildstangs.outputmanager.outputs;

import com.wildstangs.outputmanager.base.IOutput;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.IntegerSubject;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Nathan
 */
public class WsDoubleSolenoid implements IOutput {

    IntegerSubject subject;
    DoubleSolenoid solenoid;

    public WsDoubleSolenoid(String name, int module, int channel1, int channel2) {
        this.subject = new IntegerSubject(name);
        subject.setValue(DoubleSolenoid.Value.kForward_val);
        solenoid = new DoubleSolenoid(module, channel1, channel2);
        solenoid.set(DoubleSolenoid.Value.kForward);
    }

    public WsDoubleSolenoid(String name, int channel1, int channel2) {
        this(name, 1, channel1, channel2);
    }

    public void set(IOutputEnum key, Object value) {
        subject.setValue(((Integer) value).intValue());

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return subject;
    }

    public Object get(IOutputEnum key) {
        return subject.getValueAsObject();
    }

    public void update() {
        subject.updateValue();
        DoubleSolenoid.Value solValue;
        switch (subject.getValue()) {
            case DoubleSolenoid.Value.kForward_val:
                solValue = DoubleSolenoid.Value.kForward;
                break;
            case DoubleSolenoid.Value.kReverse_val:
                solValue = DoubleSolenoid.Value.kReverse;
                break;
            case DoubleSolenoid.Value.kOff_val:
            default:
                solValue = DoubleSolenoid.Value.kOff;
                break;
        }
        solenoid.set(solValue);
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
