/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.base;

/**
 *
 * @author Nathan
 */
public class WsSubsystem {

    static String subSystemName;

    /**
     * Constructor for a subsystem.
     *
     * @param name The name of the subsystem.
     */
    public WsSubsystem(String name) {
        subSystemName = name;
    }

    public void init() {
    }

    /**
     * Gets the name of the subsystem.
     *
     * @return The name of the subsystem.
     */
    public String getName() {
        return subSystemName;
    }

    /**
     * Method to allow the subsystem to update.
     *
     * Must be overridden when extending the base class.
     */
    public void update() {
        //Override when extending base class.
    }

    /**
     * Method to notify the subsystem of a config change.
     *
     * Override this method when extending the base class, if config params are
     * required.
     */
    public void notifyConfigChange() {
        //Override when extending base class if config is needed.
    }
}
