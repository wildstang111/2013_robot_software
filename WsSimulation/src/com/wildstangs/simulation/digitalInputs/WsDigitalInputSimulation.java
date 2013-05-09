package com.wildstangs.simulation.digitalInputs;



/**
 * Class to read a digital input.
 * This class will read digital inputs and return the current value on the
 * channel. Other devices
 * such as encoders, gear tooth sensors, etc. that are implemented elsewhere
 * will automatically
 * allocate digital inputs and outputs as required. This class is only for
 * devices like switches
 * etc. that aren't implemented anywhere else.
 */
public class WsDigitalInputSimulation
{

    private int mChannel;
    boolean inputState = false;

    /**
     * Create an instance of a DigitalInput.
     * Creates a digital input given a digital module number and channel. Common
     * creation routine
     * for all constructors.
     */
    private void initDigitalInput(int moduleNumber, int channel)
    {
        mChannel = channel;
    }

    /**
     * Create an instance of a Digital Input class.
     * Creates a digital input given a channel and uses the default module.
     *
     * @param channel the port for the digital input
     */
    public WsDigitalInputSimulation(int channel)
    {
        initDigitalInput(1, channel);
    }

    /**
     * Create an instance of a Digital Input class.
     * Creates a digital input given an channel and module.
     *
     * @param moduleNumber The number of the digital module to use for this
     * input
     * @param channel the port for the digital input
     */
    public WsDigitalInputSimulation(int moduleNumber, int channel)
    {
        initDigitalInput(moduleNumber, channel);
    }

    public void free()
    {
    }

    /**
     * Get the value from a digital input channel.
     * Retrieve the value of a single digital input channel from the FPGA.
     *
     * @return the stats of the digital input
     */
    public boolean get()
    {
        return inputState;
    }

    /**
     * Get the channel of the digital input
     *
     * @return The GPIO channel number that this object represents.
     */
    public int getChannel()
    {
        return mChannel;
    }

    public int getChannelForRouting()
    {
        return getChannel() - 1;
        //return DigitalModule.remapDigitalChannel(getChannel() - 1);
    }

    public int getModuleForRouting()
    {
        return 1;
    }

    public boolean getAnalogTriggerForRouting()
    {
        return false;
    }

    /**
     * Request interrupts asynchronously on this digital input.
     *
     * @param handler The address of the interrupt handler function of type
     * tInterruptHandler that
     * will be called whenever there is an interrupt on the digital input port.
     * Request interrupts in synchronous mode where the user program interrupt
     * handler will be
     * called when an interrupt occurs.
     * The default is interrupt on rising edges only.
     * @param param argument to pass to the handler
     */
    public void requestInterrupts(/*tInterruptHandler*/Object handler, Object param)
    {
    }

    /**
     * Request interrupts synchronously on this digital input.
     * Request interrupts in synchronous mode where the user program will have to
     * explicitly
     * wait for the interrupt to occur.
     * The default is interrupt on rising edges only.
     */
    public void requestInterrupts()
    {
    }

    /**
     * Set which edge to trigger interrupts on
     *
     * @param risingEdge true to interrupt on rising edge
     * @param fallingEdge true to interrupt on falling edge
     */
    public void setUpSourceEdge(boolean risingEdge, boolean fallingEdge)
    {
    }

    public void flip()
    {
        if (inputState == true)
        {
            inputState = false;
        }
        else
        {
            inputState = true;
        }
        System.out.println("Input " + mChannel + " = " + inputState);


    }
    public void set(boolean state)
    {
            inputState = state;

    }
}

