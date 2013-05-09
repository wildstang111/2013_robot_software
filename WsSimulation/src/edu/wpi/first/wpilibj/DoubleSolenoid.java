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
 * Solenoid class for running high voltage Digital Output (9472 module).
 *
 * The Solenoid class is typically used for pneumatics solenoids, but could be used
 * for any device within the current spec of the 9472 module.
 */
public class DoubleSolenoid {
/**
     * Possible values for a DoubleSolenoid
     */
    public static class Value {

        public final int value;
        public static final int kOff_val = 0;
        public static final int kForward_val = 1;
        public static final int kReverse_val = 2;
        public static final Value kOff = new Value(kOff_val);
        public static final Value kForward = new Value(kForward_val);
        public static final Value kReverse = new Value(kReverse_val);

        private Value(int value) {
            this.value = value;
        }
	/**
	* NOTE This is an adder for the stub, do not use externally 
	*/
	public String toString() {
		if (value == 0) {
			return "Off";
		} else if (value == 1) {
			return "Forward";
		} else if (value == 2) {
			return "Reverse";
		} else {
			return "Unknown";
		}
	}
		
    }
    private int mForwardChannel;
    private int mReverseChannel;
    private Value solenoidState;
    
    private JFrame frame;
    private JLabel outputLabel;
/**
     * Common function to implement constructor behavior.
     */
    private void initSolenoid() {
	 solenoidState = Value.kOff;
        frame = new JFrame("DoubleSolenoid: Forward: " + mForwardChannel + " Reverse: " + mReverseChannel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 100));
	frame.setLayout(new BorderLayout());
		
	outputLabel = new JLabel("Double Solenoid State: Off");
	frame.add(outputLabel, BorderLayout.NORTH);
		
	frame.pack();
	frame.setVisible(true);
    }

    /**
     * Constructor.
     *
     * @param forwardChannel The forward channel on the module to control.
     * @param reverseChannel The reverse channel on the module to control.
     */
    public DoubleSolenoid(final int forwardChannel, final int reverseChannel) {
	mForwardChannel = forwardChannel;
	mReverseChannel = reverseChannel;
        initSolenoid();
    }

    /**
     * Constructor.
     *
     * @param moduleNumber The module number of the solenoid module to use.
     * @param forwardChannel The forward channel on the module to control.
     * @param reverseChannel The reverse channel on the module to control.
     */
    public DoubleSolenoid(final int moduleNumber, final int forwardChannel, final int reverseChannel) {
	mForwardChannel = forwardChannel;
	mReverseChannel = reverseChannel;
        initSolenoid();
    }

    /**
     * Destructor.
     */
    public void free() {}

    /**
     * Set the value of a solenoid.
     *
     * @param value Move the solenoid to forward, reverse, or don't move it.
     */
    public void set(final Value value) {
	solenoidState = value;
	    
	outputLabel.setText("Double Solenoid State: " + value.toString());
    }

    /**
     * Read the current value of the solenoid.
     *
     * @return The current value of the solenoid.
     */
    public Value get() {
        return solenoidState;
    }

    }