package com.wildstangs.pid.controller.base;

/**
 *
 * @author Nathan
 */
public interface IPidController {
    // Accessor methods

    public double getP();

    public double getI();

    public double getD();

    public double getEpsilon();

    public double getStaticEpsilon();

    public void setConstants(double p, double i, double d);

    public void setStaticEpsilon(double epsilon);

    public void setErrorEpsilon(double epsilon);

    public void setErrorIncrementPercentage(double inc);

    public void setMinMaxOutput(double min, double max);

    public void setMinMaxInput(double min, double max);

    public void setMaxIntegral(double max);

    public void setIntegralErrorThresh(double thresh);

    public double getIntegralErrorThresh();

    public void setMinStabilizationTime(double time);

    public void setSetPoint(double set_point);

    public double getSetPoint();

    public void setDifferentiatorBandLimit(double band_limit);

    public double getDifferentiatorBandLimit();

    public WsPidStateType getState();

    // Behavioral methods
    public void resetErrorSum();

    public void calcPid();

    public boolean isOnTarget();

    public boolean isStabilized();

    public boolean isEnabled();

    //WPI PIDController-like functionality
    public void enable();

    public void disable();

    public void reset();

    //GetError
    public double getError();
}
