package com.wildstangs.configmanager.impl;

/**
 *
 * @author Nathan
 */
public class WsConfigFacadeImplException extends Exception {

    public WsConfigFacadeImplException(String message) {
        super(message);
        System.out.println(message);
    }
}