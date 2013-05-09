package com.wildstangs.inputmanager.base;

import com.wildstangs.subjects.base.IObservable;

/**
 *
 * @author Nathan
 */
public interface IInput extends IObservable {

    /**
     * Method to set values in the data element.
     *
     * @param key An enumerated key that to get to a specific value.
     * @param value The value to set to.
     */
    abstract void set(IInputEnum key, Object value);

    /**
     * Method to get a value from the data element.
     *
     * @param key An enumerated key that represents the value wanted.
     * @return An Object value of the element.
     */
    abstract Object get(IInputEnum key);

    /**
     * Method to force a periodic update of the data element.
     */
    abstract void update();

    /**
     * Method to pull input info for the data element.
     */
    abstract void pullData();

    /**
     * Method to notify the data element that config data has changed and needs
     * to be re-read.
     */
    abstract void notifyConfigChange();
}
