package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

/**
 *
 * @author John
 */
public class WsLED extends WsSubsystem implements IObserver {

    MessageHandler messageSender;
    //button states.
    boolean kickerButtonPressed = false;
    boolean climbButtonPressed = false;
    boolean intakeChanged = false;
    boolean intakeButtonPreviousState = false;
    // Sent states
    boolean autoDataSent = false;
    boolean disableDataSent = false;
    boolean kickSent = false;
    boolean climbSent = false;
    boolean intakeSent = false;
    //Send message control
    boolean sendData = false;

    public WsLED(String name) {
        super(name);
        //Fire up the message sender thread.
        Thread t = new Thread(messageSender = new MessageHandler());
        //This is safe because there is only one instance of the subsystem in the subsystem container.
        t.start();
        
        //Kicker
        Subject subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);
        //Intake
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        //Climb
        subject = WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON2);
        subject.attach(this);
    }

    public void init() {
        autoDataSent = false;
        disableDataSent = false;
        intakeButtonPreviousState = false;
        sendData = false;
    }

    public void update() {
        byte[] dataBytes = new byte[5];
        byte commandByte = 0x00;
        byte payloadByteOne = 0x00;
        byte payloadByteTwo = 0x00;

        // Get all inputs relevant to the LEDs
        boolean isRobotEnabled = DriverStation.getInstance().isEnabled();
        boolean isRobotTeleop = DriverStation.getInstance().isOperatorControl();
        boolean isRobotAuton = DriverStation.getInstance().isAutonomous();
        DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        int station_location = DriverStation.getInstance().getLocation();

        /**
         * --------------------------------------------------------- 
         * | Function      | Cmd  | PL 1 | PL 2                    |
         * --------------------------------------------------------- 
         * | Shoot         | 0x05 | 0x13 | 0x14                    | 
         * | Climb         | 0x06 | 0x11 | 0x12                    | 
         * | Intake on     | 0x07 | 0x11 | 0x12 (same as off)      | 
         * | Intake off    | 0x07 | 0x11 | 0x12(same as on)        |
         * | Red Alliance  | 0x04 | 0x52 | station id(0x01 - 0x03) |
         * | Blue Alliance | 0x04 | 0x47 | station id(0x01 - 0x03) |
         * ---------------------------------------------------------
         *
         * Send sequence once, no spamming the Arduino.
         */
        if (isRobotEnabled) {
            if (isRobotTeleop) {
                //--------------------------------------------------------------
                // Handle TeleOp signalling here
                //--------------------------------------------------------------

                if (kickerButtonPressed) {
                    commandByte = 0x05;
                    payloadByteOne = 0x13;
                    payloadByteTwo = 0x14;
                    if (!kickSent) {
                        sendData = true;
                        kickSent = true;
                    }
                } else if (climbButtonPressed) {
                    commandByte = 0x06;
                    payloadByteOne = 0x11;
                    payloadByteTwo = 0x12;
                    if (!climbSent) {
                        sendData = true;
                        climbSent = true;
                    }
                } else if (intakeChanged) {
                    commandByte = 0x07;
                    payloadByteOne = 0x11;
                    payloadByteTwo = 0x12;
                    if (!intakeSent) {
                        sendData = true;
                        intakeSent = true;
                        intakeChanged = false;
                    }
                } else {
                    kickSent = false;
                    climbSent = false;
                    intakeSent = false;
                    //Make sure we don't send anything on this run through.
                    sendData = false;
                    intakeChanged = false;
                }

            } else if (isRobotAuton) {
                //--------------------------------------------------------------
                //  Handle Autonomous signalling here
                //--------------------------------------------------------------
                //One send and one send only. 
                //Don't take time in auto sending LED cmds.
                commandByte = 0x02;
                payloadByteOne = 0x11;
                payloadByteTwo = 0x12;

                if (!autoDataSent) {
                    sendData = true;
                    autoDataSent = true;
                }
            }
        } else {
            //------------------------------------------------------------------
            // Handle Disabled signalling here
            //------------------------------------------------------------------
            switch (alliance.value) {
                case DriverStation.Alliance.kRed_val: {
                    commandByte = 0x04;
                    payloadByteOne = 0x52;
                    payloadByteTwo = ((byte) station_location);
                }
                break;

                case DriverStation.Alliance.kBlue_val: {
                    commandByte = 0x04;
                    payloadByteOne = 0x47;
                    payloadByteTwo = ((byte) station_location);
                }
                break;

                default: {
                    disableDataSent = false;
                }
                break;
            }

            if ((station_location < 1)
                    || (station_location > 3)) {
                disableDataSent = false;
            }

            if (!disableDataSent) {
                sendData = true;
                disableDataSent = true;
            }
        }
        if (sendData) {
            dataBytes[0] = commandByte;
            dataBytes[1] = payloadByteOne;
            dataBytes[2] = payloadByteTwo;
            dataBytes[3] = 0;
            dataBytes[4] = 0;
            synchronized (messageSender) {
                messageSender.setSendData(dataBytes, dataBytes.length);
                messageSender.notify();
            }
            sendData = false;
        }
    }

    public void acceptNotification(Subject subjectThatCaused) {
        boolean buttonState = ((BooleanSubject) subjectThatCaused).getValue();

        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) {
            kickerButtonPressed = buttonState;
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) {
            if (intakeButtonPreviousState == false) {
                if ((intakeButtonPreviousState = buttonState) == true) {
                    intakeChanged = true;
                }
            } else if (intakeButtonPreviousState != buttonState) {
                intakeButtonPreviousState = buttonState;
                intakeChanged = true;
            } else {
                intakeChanged = false;
            }
        }
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON2) {
            climbButtonPressed = buttonState;
        }

    }

    private static class MessageHandler implements Runnable {
        //Designed to only have one single threaded controller. (WsLED)
        //Offload to a thread avoid blocking main thread with LED sends.

        static byte[] rcvBytes;
        byte[] sendData;
        int sendSize = 0;
        I2C i2c;
        boolean running = true;
        boolean dataToSend = false;

        public MessageHandler() {
            //Get ourselves an i2c instance to send out some data.
            i2c = new I2C(DigitalModule.getInstance(1), 0x52 << 1);
        }

        public void run() {
            while (running) {
                synchronized (this) {
                    try {
                        //blocking sleep until someone calls notify.
                        this.wait();
                        //Need at least 5 bytes and someone has to have called setSendData.
                        if (sendSize >= 5 && dataToSend) {
                            // Extremely fast and cheap data confirmation algorithm
                            sendData[3] = (byte) (~sendData[1]);
                            sendData[4] = (byte) (~sendData[2]);
                            //byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize
                            // set receive size to 0 to avoid sending an i2c read request.
                            //System.out.println("Cmd: " + Integer.toHexString(sendData[0]));
                            i2c.transaction(sendData, sendSize, rcvBytes, 0);
                            dataToSend = false;
                        }
                    } catch (InterruptedException e) {}   
                }
            }
        }

        public void setSendData(byte[] data, int size) {
            sendData = data;
            sendSize = size;
            dataToSend = true;
        }

        public void stop() {
            running = false;
        }
    }
}
