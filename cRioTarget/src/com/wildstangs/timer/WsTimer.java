package com.wildstangs.timer;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Nathan
 */
public class WsTimer {
    //For sake of brevity, we will use a Timer object from WPILib

    private Timer timer;

    // Create a new timer object and reset the time to zero. The timer is initially not running and
    // must be started.
    public WsTimer() {
        timer = new Timer();
        timer.reset();
    }

    public double getFPGATimestamp() {
        return Timer.getFPGATimestamp();
    }

    public synchronized double get() {
        return timer.get();
    }

    public synchronized void reset() {
        timer.reset();
    }

    public synchronized void start() {
        timer.start();
    }

    public synchronized void stop() {
        timer.stop();
    }

    // Returns true if the given period, in seconds, has elapsed
    public synchronized boolean hasPeriodPassed(double period) {
        if (timer.get() < period * 1000000) {
            return true;
        } else {
            return false;
        }
    }
}
