package com.wildstangs.pid.controller.base;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.pid.outputs.base.IPidOutput;
import com.wildstangs.timer.WsTimer;

/**
 *
 * @author Nathan
 */
public class WsPidController implements IPidController {

    private double p;   // P coefficient
    private double i;   // I coefficient
    private double d;   // D coefficient
    private double currentError;  //Current error
    private double previousError; //Previous error
    private double setPoint; //The set point
    private double errorSum; // Sum of previous errors (for I calculation)
    private double errorIncrement; // Max increment to error sum each call
    private double errorEpsilon; // Allowable error in determining when done
    private double staticEpsilon; // Allowable error in steady state.  Used to account for small acceptable motor backdrive
    private double maxIntegral;
    private double integralErrorThresh;
    private double differentiatorBandLimit;
    private double maxOutput; // Ceiling on calculation output
    private double minOutput;
    private double maxInput; // Ceiling on calculation input
    private double minInput;
    private WsPidStateType currentState; //State of the pid for calculating I
    private double minOnTargetTime; // Minimum number of cycles in epsilon range to be done
    private boolean allowStaticEpsilon;
    private WsTimer stabilizationTimer;
    private IPidInput pidSource;
    private IPidOutput pidOutput;
    private String controllerName;
    DoubleConfigFileParameter p_config;
    DoubleConfigFileParameter i_config;
    DoubleConfigFileParameter d_config;
    DoubleConfigFileParameter errorIncrement_config;
    DoubleConfigFileParameter errorEpsilon_config;
    DoubleConfigFileParameter staticEpsilon_config;
    DoubleConfigFileParameter maxIntegral_config;
    DoubleConfigFileParameter integralErrorThresh_config;
    DoubleConfigFileParameter differentiatorBandLimit_config;
    DoubleConfigFileParameter maxOutput_config;
    DoubleConfigFileParameter minOutput_config;
    DoubleConfigFileParameter maxInput_config;
    DoubleConfigFileParameter minInput_config;
    DoubleConfigFileParameter minOnTargetTime_config;

    public WsPidController(IPidInput source,
            IPidOutput output,
            String pidControllerName) {
        p = 1.0;
        i = 0.0;
        d = 0.0;
        currentError = 0.0;
        previousError = 0.0;
        setPoint = 0.0;
        errorSum = 0.0;
        errorIncrement = 1.0;
        errorEpsilon = 1.0;
        staticEpsilon = 0.0;
        maxIntegral = 1.0;
        integralErrorThresh = -1.0;
        differentiatorBandLimit = 1.0;
        maxOutput = 1.0;
        minOutput = -1.0;
        maxInput = 1000.0;
        minInput = -1.0;
        currentState = WsPidStateType.WS_PID_INITIALIZE_STATE;
        minOnTargetTime = 0.2;
        allowStaticEpsilon = false;
        stabilizationTimer = new WsTimer();
        pidSource = source;
        pidOutput = output;
        controllerName = pidControllerName;

        p_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "p", 0.0);
        i_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "i", 0.0);
        d_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "d", 0.0);
        errorIncrement_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "errorIncrement", 0.0);
        errorEpsilon_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "errorEpsilon", 0.0);
        staticEpsilon_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "staticEpsilon", 0.0);
        maxIntegral_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "maxIntegral", 0.0);
        integralErrorThresh_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "integralErrorThresh", 0.0);
        differentiatorBandLimit_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "differentiatorBandLimit", 0.0);
        maxOutput_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "maxOutput", 0.0);
        minOutput_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "minOutput", 0.0);
        maxInput_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "maxInput", 0.0);
        minInput_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "minInput", 0.0);
        minOnTargetTime_config = new DoubleConfigFileParameter(this.getClass().getName() + "." + pidControllerName, "minOnTargetTime", 0.0);

        this.setErrorIncrementPercentage(errorIncrement);
    }

    public double getP() {
        return p;
    }

    public double getI() {
        return i;
    }

    public double getD() {
        return d;
    }

    public double getEpsilon() {
        return errorEpsilon;
    }

    public double getStaticEpsilon() {
        return staticEpsilon;
    }

    public void setConstants(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public void setStaticEpsilon(double epsilon) {
        this.staticEpsilon = epsilon;
    }

    public void setErrorEpsilon(double epsilon) {
        this.errorEpsilon = epsilon;
    }

    public void setErrorIncrementPercentage(double inc) {
        errorIncrement = ((maxOutput - minOutput) * (inc / 100.0f));
    }

    public void setMinMaxOutput(double min, double max) {
        if (min < max) {
            minOutput = min;
            maxOutput = max;
        }
    }

    public void setMinMaxInput(double min, double max) {
        if (min < max) {
            minInput = min;
            maxInput = max;
        }
    }

    public void setMaxIntegral(double max) {
        maxIntegral = Math.abs(max);
    }

    public void setIntegralErrorThresh(double thresh) {
        integralErrorThresh = Math.abs(thresh);
    }

    public double getIntegralErrorThresh() {
        return integralErrorThresh;
    }

    public void setMinStabilizationTime(double time) {
        minOnTargetTime = time;
    }

    public void setSetPoint(double set_point) {
        this.setPoint = set_point;
    }

    public double getSetPoint() {
        return setPoint;
    }

    public void setDifferentiatorBandLimit(double band_limit) {
        differentiatorBandLimit = band_limit;
    }

    public double getDifferentiatorBandLimit() {
        return differentiatorBandLimit;
    }

    public void resetErrorSum() {
        errorSum = 0.0;
    }

    public void calcPid() {
        WsPidStateType new_state = currentState;

        // Read the value of the process variable under control and limit it
        double current_pv = pidSource.pidRead();
        current_pv = this.limitInput(current_pv);
//        System.out.println(this.controllerName + " pid source value: " + current_pv);

        // Calculate the current error term
        currentError = setPoint - current_pv;
//        System.out.println(this.controllerName + " error: " + currentError);
//        System.out.println(this.controllerName + " errorSum: " + errorSum);

        //
        // Adjust our metrics depending on where the process variable is as compared
        //  to the set point.
        //
        if (currentState == WsPidStateType.WS_PID_DISABLED_STATE) {
//            System.out.println(this.controllerName + " is DISABLED");
            // PID controller is disabled, nothing to do here...

            // reset everything now.
            this.reset();
            return;
        } else if (currentState == WsPidStateType.WS_PID_INITIALIZE_STATE) {
//            System.out.println(this.controllerName + " is INITIALIZE");
            // Don't look at the D-term when we're just starting up
            previousError = currentError;

            //
            // Find the next state
            //
            if (currentError <= -errorEpsilon) {
                //
                // Error is negative and outside the epsilon band.
                // Negative errors mean we are above our setpoint
                //
                new_state = WsPidStateType.WS_PID_ABOVE_TARGET_STATE;
            } else if (currentError >= errorEpsilon) {
                //
                // Positive Errors mean we are below our setpoint
                //
                new_state = WsPidStateType.WS_PID_BELOW_TARGET_STATE;
            } else if ((currentError >= (-1 * errorEpsilon))
                    && (currentError <= errorEpsilon)) {
                new_state = WsPidStateType.WS_PID_ON_TARGET_STATE;
                stabilizationTimer.reset();
                stabilizationTimer.start();
            } else {
                // You had better hope this does not happen
            }

        } else if (currentState == WsPidStateType.WS_PID_BELOW_TARGET_STATE) {
//            System.out.println(this.controllerName + " is BELOW_TARGET");
            //
            // In this case, we were above and we switched to below
            //
            if (errorSum < 0) {
                // If we are fighting away from the point, reset the error.
                errorSum = 0;
            }

            if (currentError < errorIncrement) {
                // If the error is smaller than the max increment amount, add it.
                errorSum += currentError;
            } else {
                // Otherwise, add the maximum increment per cycle.
                errorSum += errorIncrement;
            }
            if (currentError >= staticEpsilon) {
                allowStaticEpsilon = false;
            }

            // Find the next state
            // Error is inside the epsilon band.
            if ((currentError >= (-1 * errorEpsilon))
                    && (currentError <= errorEpsilon)) {
                new_state = WsPidStateType.WS_PID_ON_TARGET_STATE;
                stabilizationTimer.reset();
                stabilizationTimer.start();
            } // Error is negative and outside the epsilon band.
            else if (currentError <= -errorEpsilon) {
                new_state = WsPidStateType.WS_PID_ABOVE_TARGET_STATE;

                //Reset the error sum
                errorSum = 0.0;
            } else {
                //Stay here.
                new_state = WsPidStateType.WS_PID_BELOW_TARGET_STATE;
            }

        } else if (currentState == WsPidStateType.WS_PID_ON_TARGET_STATE) {
//            System.out.println(this.controllerName + " is ON TARGET");
            errorSum = 0.0;
            allowStaticEpsilon = true;

            //Find the next state
            // Error is positive and outside the epsilon band.
            if (currentError >= errorEpsilon) {
                new_state = WsPidStateType.WS_PID_BELOW_TARGET_STATE;
                stabilizationTimer.stop();
                stabilizationTimer.reset();
            } else if (currentError <= -errorEpsilon) {
                new_state = WsPidStateType.WS_PID_ABOVE_TARGET_STATE;
                stabilizationTimer.stop();
                stabilizationTimer.reset();
            } else if (true == (stabilizationTimer.get() > minOnTargetTime)) {
                new_state = WsPidStateType.WS_PID_STABILIZED_STATE;
                stabilizationTimer.stop();
                stabilizationTimer.reset();
            } else {
                // Stay right here, we are on target, but not long enough yet...
                new_state = WsPidStateType.WS_PID_ON_TARGET_STATE;
            }
        } else if (currentState == WsPidStateType.WS_PID_STABILIZED_STATE) {
//            System.out.println(this.controllerName + " is STABILIZED");
            errorSum = 0.0;
            allowStaticEpsilon = true;

            // Find the next state
            // Error is positive and outside the epsilon band.
            if (currentError >= errorEpsilon) {
                new_state = WsPidStateType.WS_PID_BELOW_TARGET_STATE;
            } else if (currentError <= -errorEpsilon) {
                new_state = WsPidStateType.WS_PID_ABOVE_TARGET_STATE;
            } else {
                new_state = WsPidStateType.WS_PID_STABILIZED_STATE;
            }

        } else if (currentState == WsPidStateType.WS_PID_ABOVE_TARGET_STATE) {
//            System.out.println(this.controllerName + " is ABOVE TARGET");
            //
            // In this case, we were below and we just switched to above
            //
            if (errorSum > 0) {
                // If we are fighting away from the point, reset the error.
                errorSum = 0;
            }
            if (currentError > -errorIncrement) {
                // If the error is smaller than the max increment amount, add it.
                errorSum += currentError;
            } else {
                // Otherwise, subtract the maximum increment per cycle.
                errorSum += -errorIncrement;
            }

            if (currentError <= -staticEpsilon) {
                //Check to see if we are outside the static epsilon region
                allowStaticEpsilon = false;
            }

            //Find the next state
            // Error is inside the epsilon band.
            if ((currentError >= (-1 * errorEpsilon))
                    && (currentError <= errorEpsilon)) {
                new_state = WsPidStateType.WS_PID_ON_TARGET_STATE;
                stabilizationTimer.reset();
                stabilizationTimer.start();
            } // Error is positive and outside the epsilon band.
            else if (currentError >= errorEpsilon) {
                new_state = WsPidStateType.WS_PID_BELOW_TARGET_STATE;
                //Reset the error sum

                errorSum = 0.0;
            } else {
                new_state = WsPidStateType.WS_PID_ABOVE_TARGET_STATE;
            }

        } else {
            // Invalid state

            new_state = WsPidStateType.WS_PID_DISABLED_STATE;

        }

        currentState = new_state;

        // Finally, calculate the PID output
        double output = this.calcProportionalTerm()
                + this.calcIntegralTerm()
                + this.calcDerivativeTerm();
//        System.out.println(this.controllerName + " p-term,: " + this.calcProportionalTerm());
//        System.out.println(this.controllerName + " i-term: " + this.calcIntegralTerm());
//        System.out.println(this.controllerName + " d-term: " + this.calcDerivativeTerm());
//        System.out.println(this.controllerName + " output: " + output);

        // Handle Static Epsilon
        if ((allowStaticEpsilon == true)
                && Math.abs(currentError) < staticEpsilon) {
            output = 0;
            errorSum = 0;
        }

        // Clip the output to the allowable region
        output = this.limitOutput(output);

        // Write to the PidOutput object
        pidOutput.pidWrite(output);

        // Save the current error for next cycle's D calculation.
        previousError = currentError;
    }

    public boolean isOnTarget() {
        boolean on_target = ((WsPidStateType.WS_PID_ON_TARGET_STATE == currentState)
                || (WsPidStateType.WS_PID_STABILIZED_STATE == currentState));
        return on_target;
    }

    public boolean isStabilized() {
        boolean stabilized = (WsPidStateType.WS_PID_STABILIZED_STATE == currentState);
        return stabilized;
    }

    public boolean isEnabled() {
        return (WsPidStateType.WS_PID_DISABLED_STATE != currentState);
    }

    public void enable() {
        if (currentState == WsPidStateType.WS_PID_DISABLED_STATE) {
            currentState = WsPidStateType.WS_PID_INITIALIZE_STATE;
        }
    }

    public void disable() {
        this.reset();
        currentState = WsPidStateType.WS_PID_DISABLED_STATE;
    }

    public void reset() {
        errorSum = 0.0;
        currentError = 0.0;
        previousError = 0.0;
        allowStaticEpsilon = true;
    }

    public double getError() {
        return currentError;
    }
    public double getPreviousError() {
        return previousError;
    }

    private double calcProportionalTerm() {
        double p_term = p * currentError;
        return p_term;
    }

    private double calcIntegralTerm() {
        // Prevent Integral Wind Up.
        if (integralErrorThresh != -1) {
            if (currentError > integralErrorThresh || currentError < -integralErrorThresh) {
                this.resetErrorSum();
            }
        }
        double i_term = i * errorSum;
        if (i_term > maxIntegral) {
            i_term = maxIntegral;
        } else if (i_term < -maxIntegral) {
            i_term = -maxIntegral;
        } else {
            // Leave i-term alone
        }

        return i_term;
    }

    protected double calcDerivativeTerm() {
        double d_term = d * (currentError - previousError);

        // Band-limit the differential term
        if (Math.abs(d_term) > differentiatorBandLimit) {
            d_term = (d_term > 0.0f)
                    ? differentiatorBandLimit
                    : -differentiatorBandLimit;
        }
        return d_term;
    }

    private double limitOutput(double output) {
        double clipped_output = 0.0;
        if (output > maxOutput) {
            clipped_output = maxOutput;
        } else if (output < minOutput) {
            clipped_output = minOutput;
        } else {
            // Output is already in range, just pass it along
            clipped_output = output;
        }

        return clipped_output;
    }

    private double limitInput(double input) {
        double clipped_input = 0.0;
        if (input > maxInput) {
            clipped_input = maxInput;
        } else if (input < minInput) {
            clipped_input = minInput;
        } else {
            // Input is already in range, just pass it along
            clipped_input = input;
        }

        return clipped_input;
    }

    public WsPidStateType getState() {
        return this.currentState;
    }

    public void notifyConfigChange() {
        p = p_config.getValue();
        i = i_config.getValue();
        d = d_config.getValue();
        errorIncrement = errorIncrement_config.getValue();
        errorEpsilon = errorEpsilon_config.getValue();
        staticEpsilon = staticEpsilon_config.getValue();
        maxIntegral = maxIntegral_config.getValue();
        integralErrorThresh = integralErrorThresh_config.getValue();
        differentiatorBandLimit = differentiatorBandLimit_config.getValue();
        maxOutput = maxOutput_config.getValue();
        minOutput = minOutput_config.getValue();
        maxInput = maxInput_config.getValue();
        minInput = minInput_config.getValue();
//        System.out.println("WsPidController " + p);
    }
}