package com.wildstangs.profiling;

import com.wildstangs.logger.Logger;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WsProfilingTimer {

    double startingTime;
    double endingTime;
    double totalTime;
    double minTime;
    double maxTime;
    int runs;
    int iterations;
    String name;

    public WsProfilingTimer(String name, int iterations) {
        reset();
        this.iterations = iterations;
        this.name = name;
    }

    public void startTimingSection() {
        startingTime = Timer.getFPGATimestamp();
    }

    public double endTimingSection() {
        double spentTime = 0;
        endingTime = Timer.getFPGATimestamp();
        spentTime = endingTime - startingTime;

        runs++;
        totalTime += spentTime;

        if (spentTime > maxTime) {
            maxTime = spentTime;
        }

        if (spentTime < minTime || (minTime == 0)) {
            minTime = spentTime;
        }

//        Logger.getLogger().debug(name, "Cycle Time", Double.toString(spentTime));
        if (runs >= iterations) {
            String reportText = " Avg Time: " + Double.toString(totalTime / (double) runs);
            reportText += " Max Time: " + Double.toString(maxTime);
            reportText += " Min Time: " + Double.toString(minTime);
            Logger.getLogger().debug(name, "Profile", reportText);
            reset();
        }
        SmartDashboard.putNumber(name, spentTime);
        return spentTime;
    }

    private void reset() {
        startingTime = 0;
        endingTime = 0;
        totalTime = 0;
        minTime = 0;
        maxTime = 0;
        runs = 0;
    }
}