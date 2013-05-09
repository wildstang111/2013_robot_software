package com.wildstangs.inputmanager.inputs.joystick;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Nathan
 */
public interface IJoystick {

    /**
     * Get the button value for buttons 1 through 10, with 0 being 10.
     *
     * @return True if the provided button is being pressed, false if not.
     */
    boolean getRawButton(int but);

    /**
     * The current state of the trigger on the Joystick.
     *
     * @return True if the trigger is being pressed down, false if not.
     */
    boolean getTrigger();

    /**
     * The X value of the Joystick.
     *
     * @return The X value of the Joystick, ranges from -1.0 to +1.0.
     */
    double getX();

    /**
     * The Y value of the Joystick.
     *
     * @return The Y value of the Joystick, ranges from -1.0 to +1.0.
     */
    double getY();

    /**
     * The Z value of the Joystick.
     *
     * @return The Z value of the Joystick, ranges from -1.0 to +1.0.
     */
    double getZ();

    /**
     * The Throttle (D-pad up/down) value of the Joystick.
     *
     * @return The Throttle value of the Joystick, ranges from -1.0 to +1.0.
     * Note: -1 is up, 1 is down
     */
    double getThrottle();

    /**
     * The Twist (D-pad left/right) value of the Joystick.
     *
     * @return The Twist value of the Joystick, ranges from -1.0 to +1.0. Note:
     * -1 is left, 1 is right
     */
    double getTwist();

    public int getAxisChannel(Joystick.AxisType axis);

    /**
     * Set the channel associated with a specified axis.
     *
     * @param axis The axis to set the channel for.
     * @param channel The channel to set the axis to.
     */
    public void setAxisChannel(Joystick.AxisType axis, int channel);
}
