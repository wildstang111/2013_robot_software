/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.parameters;

import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousDoubleConfigFileParameter extends DoubleConfigFileParameter{

    public AutonomousDoubleConfigFileParameter(String pName, double defValue) {
        super("com.wildstangs.autonomous.parameters", pName, defValue);
    }
    
}
