package com.wildstangs.inputmanager.inputs;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.AnalogChannel;

/**
 *
 * @author Nathan
 */
public class WsAnalogInput implements IInput {

    DoubleSubject analogValue;
    AnalogChannel input;
    DoubleConfigFileParameter startState = new DoubleConfigFileParameter(
            this.getClass().getName(), "startState", 0.0f);

    //By giving the input number in the constructor we can make this generic for all digital inputs
    public WsAnalogInput(int channel) {
        this.analogValue = new DoubleSubject("AnalogInput" + channel);
        this.input = new AnalogChannel(channel);

        analogValue.setValue(startState.getValue());
    }

    public void set(IInputEnum key, Object value) {
        boolean b = ((Boolean) value).booleanValue();
        double d = 0;
        if (b) {
            d = 1;
        }
        analogValue.setValue(d);

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return analogValue;
    }

    public Object get(IInputEnum key) {
        return analogValue.getValueAsObject();
    }

    public void update() {
        analogValue.updateValue();
    }

    public void pullData() {
        analogValue.setValue(input.getVoltage());
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
