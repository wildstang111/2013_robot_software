/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.config;

import com.wildstangs.configmanager.WsConfigManager;
import com.wildstangs.logger.Logger;

/**
 *
 * @author Nathan
 */
public class StringConfigFileParameter extends ConfigFileParameter {

    String defaultValue;

    /**
     * Create a String config file parameter.
     *
     * @param cName The class name to get
     * @param pName The parameter name get
     * @param defValue A default value for the parameter.
     */
    public StringConfigFileParameter(String cName, String pName,
            String defValue) {
        super(cName, pName);
        defaultValue = defValue;
    }

    /**
     * Retrieve the config file parameter.
     *
     * @return the config file parameter value.
     */
    public String getValue() {
        String fullName = getFullParamName();
        try {
            return WsConfigManager.getInstance().getConfigParamByName(fullName);
        } catch (Throwable e) {
            Logger.getLogger().error(this.getClass().getName(), "getValue", fullName + " parameter not found. Using default value.");
            return defaultValue;
        }
    }
}
