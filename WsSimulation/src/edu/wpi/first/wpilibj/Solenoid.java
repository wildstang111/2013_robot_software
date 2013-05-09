/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import com.wildstangs.simulation.solenoids.WsSolenoidContainer;
import com.wildstangs.simulation.*;


     /**
 * Solenoid class for running high voltage Digital Output (9472 module).
 *
 * The Solenoid class is typically used for pneumatics solenoids, but could be used
 * for any device within the current spec of the 9472 module.
 */
public class Solenoid {

    private boolean outputState;
    
/**
     * Common function to implement constructor behavior.
     */
    private void initSolenoid() {
        outputState = false;
    }

    /**
     * Constructor.
     *
     * @param moduleNumber The module number of the solenoid module to use.
     * @param channel The channel on the module to control.
     */
    public Solenoid(final int moduleNumber, final int channel) {
        WsSolenoidContainer.getInstance().add(this, moduleNumber, channel);
        initSolenoid();
    }

    /**
     * Constructor.
     *
     * @param channel The channel on the module to control.
     */
    public Solenoid(final int channel) {
        this(1,channel); 
    }

    /**
     * Destructor.
     */
    public void free() {}

    /**
     * Set the value of a solenoid.
     *
     * @param on Turn the solenoid output off or on.
     */
    public void set(boolean value) {
        outputState = value;
    }

    /**
     * Read the current value of the solenoid.
     *
     * @return The current value of the solenoid.
     */
    public boolean get() {
        return outputState;
    }

    }
