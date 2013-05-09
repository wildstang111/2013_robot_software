/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Class to write digital outputs.
 * This class will wrtie digital outputs. Other devices
 * that are implemented elsewhere will automatically
 * allocate digital inputs and outputs as required.
 */
public class DigitalOutput {

    private int mChannel;
    private boolean pulsing;
    private boolean outputState;
    
    private JFrame frame;
    private JLabel outputLabel;
    private JLabel pulseLabel;
    
    private void initDigitalOutput(int moduleNumber, int channel) {
        mChannel = channel;
        outputState = false;
        pulsing = false;
        frame = new JFrame("Digital Output: " + channel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 100));
	frame.setLayout(new BorderLayout());
		
	outputLabel = new JLabel("Output State: " + outputState);
        pulseLabel = new JLabel("Pulsing State: " + pulsing);
	frame.add(outputLabel, BorderLayout.NORTH);
        frame.add(pulseLabel, BorderLayout.SOUTH);
		
	frame.pack();
	frame.setVisible(true);
    }

    /**
     * Create an instance of a digital output.
     * Create an instance of a digital output given a module number and channel.
     * @param moduleNumber The number of the digital module to use
     * @param channel the port to use for the digital output
     */
    public DigitalOutput(int moduleNumber, int channel) {
        initDigitalOutput(moduleNumber, channel);
    }

    /**
     * Create an instance of a digital output.
     * Create a digital output given a channel. The default module is used.
     * @param channel the port to use for the digital output
     */
    public DigitalOutput(int channel) {
        initDigitalOutput(1, channel);
    }

    /**
     * Free the resources associated with a digital output.
     */
    public void free() {}

    /**
     * Set the value of a digital output.
     * @param value true is on, off is false
     */
    public void set(boolean value) {
        outputState = value;
	outputLabel.setText("Output State: " + outputState);
        pulseLabel.setText("Pulsing State: " + pulsing);
    }

    /**
     * @return The GPIO channel number that this object represents.
     */
    public int getChannel() {
	return mChannel;
    }

    /**
     * Output a single pulse on the digital output line.
     * Send a single pulse on the digital output line where the pulse diration is specified in seconds.
     * Maximum pulse length is 0.0016 seconds.
     * @param length The pulselength in seconds
     */
    public void pulse(double length) {
        if (length == 0) {
            pulsing = false;
        } else {
            pulsing = true;
        }
	outputLabel.setText("Output State: " + outputState);
        pulseLabel.setText("Pulsing State: " + pulsing);
        
    }

    /**
     * Determine if the pulse is still going.
     * Determine if a previously started pulse is still going.
     * @return true if pulsing
     */
    public boolean isPulsing() {
        return pulsing;
    }

    /**
     * Change the PWM frequency of the PWM output on a Digital Output line.
     *
     * The valid range is from 0.6 Hz to 19 kHz.  The frequency resolution is logarithmic.
     *
     * There is only one PWM frequency per digital module.
     *
     * @param rate The frequency to output all digital output PWM signals on this module.
     */
    public void setPWMRate(double rate){}

    /**
     * Enable a PWM Output on this line.
     *
     * Allocate one of the 4 DO PWM generator resources from this module.
     *
     * Supply the initial duty-cycle to output so as to avoid a glitch when first starting.
     *
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or less)
     * but is reduced the higher the frequency of the PWM signal is.
     *
     * @param initialDutyCycle The duty-cycle to start generating. [0..1]
     */
    public void enablePWM(double initialDutyCycle) {}

    /**
     * Change this line from a PWM output back to a static Digital Output line.
     *
     * Free up one of the 4 DO PWM generator resources that were in use.
     */
    public void disablePWM() {}

    /**
     * Change the duty-cycle that is being generated on the line.
     *
     * The resolution of the duty cycle is 8-bit for low frequencies (1kHz or less)
     * but is reduced the higher the frequency of the PWM signal is.
     *
     * @param dutyCycle The duty-cycle to change to. [0..1]
     */
    public void updateDutyCycle(double dutyCycle) {}

    /**
     * @return The value to be written to the channel field of a routing mux.
     */
    public int getChannelForRouting() {
	return getChannel() - 1;
    }

    /**
     * @return The value to be written to the module field of a routing mux.
     */
    public int getModuleForRouting() {
	return 1;
    }

    /**
     * @return The value to be written to the analog trigger field of a routing mux.
     */
    public boolean getAnalogTriggerForRouting() {
	return false;
    }
}
