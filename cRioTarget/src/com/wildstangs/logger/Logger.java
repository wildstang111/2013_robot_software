package com.wildstangs.logger;

import com.wildstangs.types.Level;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.StringConfigFileParameter;
import com.wildstangs.logger.impl.LoggerImpl;

/**
 *
 * @author Nathan
 */
public class Logger {

    LoggerImpl logimpl = null;
    private static Logger logger = null;
    private static int logLevel = Level.ERROR_INT;
    private static long numEventsPosted = 0;
    private static final int MAX_MSG_LENGTH = 1472;
    boolean remoteLoggingEnabled;
    boolean stdoutLoggingEnabled;
    double startupTime = System.currentTimeMillis();
    BooleanConfigFileParameter logToStdout = new BooleanConfigFileParameter(
            this.getClass().getName(), "logToStdout", true);
    BooleanConfigFileParameter logToServer = new BooleanConfigFileParameter(
            this.getClass().getName(), "logToServer", false);
    StringConfigFileParameter configLogLevel = new StringConfigFileParameter(
            this.getClass().getName(), "logLevel", "OFF");
    StringConfigFileParameter logIp = new StringConfigFileParameter(
            this.getClass().getName(), "logIp", "10.1.11.22");
    StringConfigFileParameter logPort = new StringConfigFileParameter(
            this.getClass().getName(), "port", "17654");

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
        logimpl = new LoggerImpl();

        remoteLoggingEnabled = logToServer.getValue();
        stdoutLoggingEnabled = logToStdout.getValue();
        if (remoteLoggingEnabled) {
            if (!logimpl.openConnection(logIp.getValue(), logPort.getValue())) {
                System.out.println("Unable to open Logger connection");
            }
        }

        logLevel = (Level.toLevel(configLogLevel.getValue(), Level.toLevel(logLevel))).toInt();
    }

    /**
     * Shuts down the remote logging socket.
     */
    public void shutdownRemoteLogging() {
        if (logimpl != null) {
            logimpl.closeConnection();
        }
    }

    /**
     * Sets the current log level.
     *
     * @param l The level to set logging to.
     */
    public void setLogLevel(Level l) {
        logLevel = l.toInt();
    }

    public void readConfig() {
        remoteLoggingEnabled = logToServer.getValue();
        if (remoteLoggingEnabled) {
            if (logimpl.updateConfig(logIp.getValue(), logPort.getValue())) {
                System.out.println("Unable to update Logger config.");
            }
        }
        stdoutLoggingEnabled = logToStdout.getValue();
        logLevel = (Level.toLevel(configLogLevel.getValue(), Level.toLevel(logLevel))).toInt();
    }

    /**
     * Logs a trace message.
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     * Logs a fatal message. Note fatal messages will stop the execution.
     *
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
     * Always logs a message, regardless of the current log level, even if the
     * logging is off
     *
     * @param c The name of the class logging the message.
     * @param id A unique identifier per class for easy debugging.
     * @param message The message to log.
     */
    public void always(String c, String id, Object message) {
        logMessage(Level.ALWAYS, c, id, message);
    }

    /**
     * Performs the real work of of logging a message.
     *
     * @param l The log level of the message
     * @param c The name of the class that is logging the message
     * @param id A unique identifier per class that allows easier debugging.
     * @param message The actual message. Most likely a string.
     */
    protected void logMessage(Level l, String c, String id, Object message) {
        String logString;
        String builtString;
        int builtStringLen;
        //subtract the startup time to get a sane number here and divide by 1000 to get seconds.
        double currentTime = ((System.currentTimeMillis() - startupTime) / 1000);
        builtString = ++numEventsPosted + "|" + currentTime + "|" + l.toString() + "|" + c + "|" + id + "|" + message.toString();

        builtStringLen = builtString.length();

        if (builtStringLen > MAX_MSG_LENGTH) {
            logString = builtString.substring(0, MAX_MSG_LENGTH - 1);
        } else {
            logString = builtString;
        }

        if (stdoutLoggingEnabled == true) {
            System.out.println(logString);
        }
        if (remoteLoggingEnabled == true) {
            //Fire and forget the packet.
            logimpl.sendPacket(logString);

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
