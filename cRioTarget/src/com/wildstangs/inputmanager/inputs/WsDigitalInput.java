package com.wildstangs.inputmanager.inputs;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author Nathan
 */
public class WsDigitalInput implements IInput {

    BooleanSubject digitalValue;
    DigitalInput input;
    BooleanConfigFileParameter startState = new BooleanConfigFileParameter(
            this.getClass().getName(), "startState", false);

    //By giving the input number in the constructor we can make this generic for all digital inputs
    public WsDigitalInput(int channel) {
        this.digitalValue = new BooleanSubject("DigitalInput" + channel);
        this.input = new DigitalInput(channel);

        digitalValue.setValue(startState.getValue());
    }

    public void set(IInputEnum key, Object value) {
        digitalValue.setValue(((Boolean) value).booleanValue());

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return digitalValue;
    }

    public Object get(IInputEnum key) {
        return digitalValue.getValueAsObject();
    }

    public void update() {
        digitalValue.updateValue();
    }

    public void pullData() {
        digitalValue.setValue(input.get());
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
