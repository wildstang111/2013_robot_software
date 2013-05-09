package com.wildstangs.config;

/**
 *
 * @author Nathan
 */
public class ConfigFileParameter {

    String className;
    String paramName;

    /**
     * A generic config file parameter
     *
     * @param cName The class name.
     * @param pName The parameter name.
     */
    protected ConfigFileParameter(String cName, String pName) {
        className = cName;
        paramName = pName;
    }

    /**
     * Retrieve the class name for this parameter type.
     *
     * @return the class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Retrieve the parameter name for this parameter.
     *
     * @return the parameter name.
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * Retrieve the full parameter name with the class and parameter name
     * concatenated.
     *
     * @return the full parameter name.
     */
    public String getFullParamName() {
        return className + "." + paramName;
    }
}