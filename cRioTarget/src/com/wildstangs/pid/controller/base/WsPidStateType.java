package com.wildstangs.pid.controller.base;

/**
 *
 * @author Nathan
 */
public class WsPidStateType {

    private String title;

    public WsPidStateType(String name) {
        this.title = title;
    }
    public static WsPidStateType WS_PID_DISABLED_STATE = new WsPidStateType("WS_PID_DISABLED_STATE");
    public static WsPidStateType WS_PID_INITIALIZE_STATE = new WsPidStateType("WS_PID_INITIALIZE_STATE");
    public static WsPidStateType WS_PID_BELOW_TARGET_STATE = new WsPidStateType("WS_PID_BELOW_TARGET_STATE");
    public static WsPidStateType WS_PID_ON_TARGET_STATE = new WsPidStateType("WS_PID_ON_TARGET_STATE");
    public static WsPidStateType WS_PID_STABILIZED_STATE = new WsPidStateType("WS_PID_STABILIZED_STATE");
    public static WsPidStateType WS_PID_ABOVE_TARGET_STATE = new WsPidStateType("WS_PID_ABOVE_TARGET_STATE");
}
