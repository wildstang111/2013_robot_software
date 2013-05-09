package com.wildstangs.outputmanager.base;

/**
 *
 * @author Nathan
 */
public interface IOutput {

    /**
     * Method to set the value of the object for the key provided.
     *
     * @param key the key of the value to set.
     * @param value the value to set.
     */
    abstract void set(IOutputEnum key, Object value);

    /**
     * Retrieves the value represented by the key.
     *
     * @param key the key of the value to grab.
     * @return the value for the key.
     */
    abstract Object get(IOutputEnum key);

    /**
     * Method to force the output element to update. Used by the output facade
     * to update all elements.
     */
    abstract void update();

    /**
     * Method to notify the output element that config values have been updated
     * and should be re-read. Used by the output facade to notify all output
     * elements.
     */
    abstract void notifyConfigChange();
}
