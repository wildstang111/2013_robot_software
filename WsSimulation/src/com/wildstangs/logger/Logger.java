package com.wildstangs.logger;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.config.StringConfigFileParameter;
import edu.wpi.first.wpilibj.Timer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Nathan
 */
public class Logger {
    private static Logger logger = null;
    private static int logLevel = Level.ERROR_INT;
    private static long numEventsPosted = 0;
    private static final int MAX_MSG_LENGTH = 1472;
    DatagramSocket sock = null;
    InetAddress host = null;
    int port = 17654;
    boolean remoteLoggingEnabled;
    boolean stdoutLoggingEnabled;
    double initTime = 0.0; 
    BooleanConfigFileParameter logToStdout = new BooleanConfigFileParameter(
            this.getClass().getName(), "logToStdout", false);
    BooleanConfigFileParameter logToServer = new BooleanConfigFileParameter(
            this.getClass().getName(), "logToServer", true);
    StringConfigFileParameter configLogLevel = new StringConfigFileParameter(
            this.getClass().getName(), "logLevel", "OFF");
    StringConfigFileParameter logIp = new StringConfigFileParameter(
            this.getClass().getName(), "logIp", "224.1.11.106");
    IntegerConfigFileParameter logPort = new IntegerConfigFileParameter(
            this.getClass().getName(), "port", 17654);
    
    
    /**
      * Gets the instance of the Logger Singleton.
     *
     * @return The instance of the Logger.
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = new Logger();
        }
        return logger;
    }
    
    protected Logger() {
        try{
            sock = new DatagramSocket();
            host = InetAddress.getByName(logIp.getValue());
            port = logPort.getValue();
            remoteLoggingEnabled = logToServer.getValue();
            stdoutLoggingEnabled = logToStdout.getValue();
            logLevel = (Level.toLevel(configLogLevel.getValue(), Level.toLevel(logLevel))).toInt();
            initTime = Timer.getFPGATimestamp(); 
        } 
        catch(IOException e) {
            System.out.println("Unable to open Socket");
        }
    }
    /**
     * Shuts down the remote logging socket.
     */
    public void shutdownRemoteLogging() {
        if (sock != null) {
            sock.close();
        }
    }
    /**
     * Sets the current log level.
     * @param l The level to set logging to.
     */
    public void setLogLevel(Level l) {
        logLevel = l.toInt();
    }
    
    public void readConfig() {
        try {
            host = InetAddress.getByName(logIp.getValue());
            port = logPort.getValue();
            remoteLoggingEnabled = logToServer.getValue();
            stdoutLoggingEnabled = logToStdout.getValue();
            logLevel = (Level.toLevel(configLogLevel.getValue(), Level.toLevel(logLevel))).toInt();
        }
        catch (IOException e) {
            System.out.println("Unable to set host IP config during config read.");
        }
    }
    
    /**
     * Logs a trace message.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void trace(String c, String id, Object message) {
        if (logLevel > Level.TRACE_INT) {
            return;
        }
        
        logMessage(Level.TRACE, c, id, message);
    }
    
    /**
     * Logs a debug message.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void debug(String c, String id, Object message) {
        if (logLevel > Level.DEBUG_INT) {
            return;
        }
        
        logMessage(Level.DEBUG, c, id, message);
    }
    
    /**
     * Logs an info message
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void info(String c, String id, Object message) {
        if (logLevel > Level.INFO_INT) {
            return;
        }
        
        logMessage(Level.INFO, c, id, message);
    }
    
    /**
     * Logs a notice message.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void notice(String c, String id, Object message) {
        if (logLevel > Level.NOTICE_INT) {
            return;
        }
        
        logMessage(Level.NOTICE, c, id, message);
    }
    
    /**
     * Logs a warning message.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void warning(String c, String id, Object message) {
        if (logLevel > Level.WARNING_INT) {
            return;
        }
        
        logMessage(Level.WARNING, c, id, message);
    }
    
    /**
     * Logs an error message.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void error(String c, String id, Object message) {
        if (logLevel > Level.ERROR_INT) {
            return;
        }
        
        logMessage(Level.ERROR, c, id, message);
    }
    
    /**
     * Logs a fatal message.   Note fatal messages will stop the execution.
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void fatal(String c, String id, Object message) {
        if (logLevel > Level.FATAL_INT) {
            return;
        }
        
        logMessage(Level.FATAL, c, id, message);
    }
    
    /**
     * Always logs a message, regardless of the current log level, 
     * even it the logging is off
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void always(String c, String id, Object message) {
        logMessage(Level.ALWAYS, c, id, message);
    }
    
    
    /**
     * Performs the real work of of logging a message.
     * @param l The log level of the message
     * @param c The name of the class that is logging the message
     * @param id A unique identifier per class that allows easier debugging.
     * @param message The actual message.  Most likely a string.
     */
    protected void logMessage(Level l, String c, String id, Object message) {
        String logString;
        String builtString;
        int builtStringLen;
        double timeSinceInit = Timer.getFPGATimestamp() - initTime ;
        //need to construct an object array for java 1.3.
        Object [] sArgs = new Object[]{Long.valueOf(++numEventsPosted), Double.valueOf(timeSinceInit), l.toString(), c, id, message.toString()};
        builtString = String.format("0x%04x|%017.6f|%s|%s|1|%s|%s", sArgs);
                
       
        builtStringLen = builtString.length();
        
        if (builtStringLen > MAX_MSG_LENGTH) {
            logString = builtString.substring(0, MAX_MSG_LENGTH-1);
        }
        else {
            logString = builtString;
        }
        
        if (stdoutLoggingEnabled == true) {
            System.out.println(logString);
        }
        if (remoteLoggingEnabled == true) {    
            logString += '\0';
            //Fire and forget the packet.
            byte[] bString = logString.getBytes();  
            DatagramPacket packet = new DatagramPacket(bString, bString.length, host, port);
            try {
                sock.send(packet);
            }
                catch (Exception e) {
                    //The shutdown method could close the socket, so catch all exceptions.
                    System.out.println("Unable to send log packet");
            }
        }
        if (l.toInt() == Level.FATAL_INT) {
            //we hit a fatal error.
            System.out.println("Fatal Error encountered");
            //TODO:  put reset code in here.  Not sure how to handle this in Java.
            // Is there an exception to throw, or some function to call to assert
            // some value.
        }
        
    }
}
