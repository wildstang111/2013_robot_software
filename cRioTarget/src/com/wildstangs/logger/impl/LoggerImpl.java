/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.logger.impl;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.OutputStreamWriter;

/**
 *
 * @author smitty
 */
public class LoggerImpl {

    StreamConnection conn = null;
    String address;
    OutputStreamWriter outStream = null;

    public LoggerImpl() {
        //nothing for the constructor at the moment.
    }

    public boolean openConnection(String destIpAddr, String destPort) {
        address = "socket://" + destIpAddr + ":" + destPort;
        try {
            conn = (StreamConnection) Connector.open(address);
            outStream = new OutputStreamWriter(conn.openDataOutputStream());
        } catch (Throwable e) {
            System.out.println("Logger connection open exception: " + e.toString() + " to: " + address);
            return false;
        }
        return true;
    }

    public boolean updateConfig(String destIpAddr, String destPort) {

        address = "socket://" + destIpAddr + ":" + destPort;
        closeConnection();
        try {
            conn = (StreamConnection) Connector.open(address);
            outStream = new OutputStreamWriter(conn.openDataOutputStream());
        } catch (Throwable e) {
            return false;
        }
        return true;
    }

    public boolean sendPacket(String logEntry) {
        try {
            if (outStream != null) {
                outStream.write(logEntry + '\n', 0, logEntry.length() + 1);
            } else {
                return false;
            }
        } catch (Throwable e) {
            System.out.println("Send Packet Exception: " + e.toString());
            //The shutdown method could close the socket, so catch all exceptions.
            return false;
        }
        return true;
    }

    public void closeConnection() {
        try {
            if (outStream != null) {
                outStream.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Throwable e) {
            System.out.println("Can not close logger connection: " + e.toString());
        }
    }
}
