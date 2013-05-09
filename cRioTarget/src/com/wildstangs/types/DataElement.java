package com.wildstangs.types;

/**
 *
 * @author Nathan
 */
public class DataElement {

    private String dkey;
    private Object dvalue;

    /**
     * A generic dataElement
     *
     * @param key The key.
     * @param value The value
     */
    public DataElement(String key, Object value) {
        dkey = key;
        dvalue = value;
    }

    /**
     * Retrieve the key
     *
     * @return the key
     */
    public String getKey() {
        return dkey;
    }

    /**
     * Retrieve the value Object.
     *
     * @return the Object stored as value
     */
    public Object getValue() {
        return dvalue;
    }
}