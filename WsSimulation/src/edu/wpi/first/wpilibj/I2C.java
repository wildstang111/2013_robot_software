/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj;

/**
 *
 * @author Nathan
 */
public class I2C {

    public I2C(DigitalModule module, int deviceAddress) {
    }

    public synchronized boolean transaction(byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize) {
        return false;
    }
}
