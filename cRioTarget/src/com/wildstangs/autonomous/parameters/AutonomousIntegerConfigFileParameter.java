/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.parameters;

import com.wildstangs.config.IntegerConfigFileParameter;

/**
 *
 * @author chadschmidt
 */
public class AutonomousIntegerConfigFileParameter extends IntegerConfigFileParameter{

    public AutonomousIntegerConfigFileParameter(String pName, int defValue) {
        super("com.wildstangs.autonomous.parameters", pName, defValue);
    }
    
}
