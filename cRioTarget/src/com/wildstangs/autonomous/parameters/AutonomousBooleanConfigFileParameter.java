/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.parameters;

import com.wildstangs.config.BooleanConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousBooleanConfigFileParameter extends BooleanConfigFileParameter{

    public AutonomousBooleanConfigFileParameter(String pName, boolean defValue) {
        super("com.wildstangs.autonomous.parameters", pName, defValue);
    }
    
}
