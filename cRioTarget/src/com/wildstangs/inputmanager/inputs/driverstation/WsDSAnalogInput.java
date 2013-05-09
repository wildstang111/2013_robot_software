package com.wildstangs.inputmanager.inputs.driverstation;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputmanager.base.IInput;
import com.wildstangs.inputmanager.base.IInputEnum;
import com.wildstangs.logger.Logger;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

/**
 *
 * @author Nathan
 */
public class WsDSAnalogInput implements IInput {

    DoubleSubject analogValue;
    int channel;
    DoubleConfigFileParameter startState = new DoubleConfigFileParameter(
            this.getClass().getName(), "startState", 0.0f);

    //By giving the input number in the constructor we can make this generic for all digital inputs
    public WsDSAnalogInput(int channel) {
        this.analogValue = new DoubleSubject("AnalogInput" + channel, WsDSAnalogInputEnum.getEnumFromValue(channel));
        this.channel = channel;

        analogValue.setValue(startState.getValue());
    }

    public void set(IInputEnum key, Object value) {
        double d = 0.0;
        if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            d = 0;
            if (b) {
                d = 1;
            }
        } else {
            d = ((Double) value).doubleValue();
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
        
        analogValue.setValue(DriverStation.getInstance().getAnalogIn(channel));
//        try {
//            analogValue.setValue(DriverStation.getInstance().getEnhancedIO().getAnalogIn(channel));
//        }
//        catch (EnhancedIOException e) {
//            analogValue.setValue(DriverStation.getInstance().getAnalogIn(channel));
//            Logger.getLogger().error(this.getClass().getName(), "pullData", "Enhanced IO Error(switch to non-enhanced): " + e.toString());
//        }
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
