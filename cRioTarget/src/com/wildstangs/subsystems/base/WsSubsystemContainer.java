package com.wildstangs.subsystems.base;

import com.wildstangs.subsystems.WsClimber;
import com.wildstangs.subsystems.WsCompressor;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.WsFloorPickup;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.WsIntake;
import com.wildstangs.subsystems.WsLED;
import com.wildstangs.subsystems.WsLoadingRamp;
import com.wildstangs.subsystems.WsShooter;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;
import com.wildstangs.subsystems.WsLightCannon;

/**
 *
 * @author Nathan
 */
public class WsSubsystemContainer {

    private static WsSubsystemContainer instance = null;
    private static List subsystem = new List();

    public static WsSubsystemContainer getInstance() {
        if (WsSubsystemContainer.instance == null) {
            WsSubsystemContainer.instance = new WsSubsystemContainer();
        }
        return WsSubsystemContainer.instance;
    }

    public void init() {
        for (int i = 0; i < subsystem.size(); i++) {
            ((WsSubsystem) (((DataElement) subsystem.get(i)).getValue())).init();
        }
    }

    /**
     * Retrieves a subsystem based on a key value.
     *
     * @param key The key representing the subsystem.
     * @return A subsystem.
     */
    public WsSubsystem getSubsystem(String key) {
        for (int i = 0; i < subsystem.size(); i++) {
            if ((((DataElement) subsystem.get(i)).getKey()).equals(key)) {
                return (WsSubsystem) (((DataElement) subsystem.get(i)).getValue());
            }
        }
        return (WsSubsystem) null;
    }

    /**
     * Triggers all subsystems to be updated.
     */
    public void update() {
        for (int i = 0; i < subsystem.size(); i++) {
            ((WsSubsystem) (((DataElement) subsystem.get(i)).getValue())).update();
        }
    }

    /**
     * Notifies all subsystems a config change has occurred and config params
     * should be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < subsystem.size(); i++) {
            ((WsSubsystem) (((DataElement) subsystem.get(i)).getValue())).notifyConfigChange();
        }
    }
    //Subsystem keys - must add a new key for each subsystem.
    public static final String WS_DRIVE_BASE = "WsDriveBase";
    public static final String WS_COMPRESSOR = "WsCompressor";
    public static final String WS_INTAKE = "WsIntake";
    public static final String WS_FLOOR_PICKUP = "WsFloorPickup";
    public static final String WS_HOPPER = "WsHopper";
    public static final String WS_SHOOTER = "WsShooter";
    public static final String WS_LOADING_RAMP = "WsLoadingRamp";
    public static final String WS_CLIMBER = "WsClimber";
    public static final String WS_LED = "WsLED";
    public static final String WS_LIGHT_CANNON = "WsLightCannon";

    /**
     * Constructor for the subsystem container.
     *
     * Each new subsystem must be added here. This is where they are
     * instantiated as well as placed in the subsystem container.
     */
    protected WsSubsystemContainer() {
        subsystem.add(new DataElement(WS_DRIVE_BASE, new WsDriveBase(WS_DRIVE_BASE)));
        subsystem.add(new DataElement(WS_COMPRESSOR, new WsCompressor(WS_COMPRESSOR, 1, 1, 1, 1)));
        subsystem.add(new DataElement(WS_INTAKE, new WsIntake(WS_INTAKE)));
        subsystem.add(new DataElement(WS_FLOOR_PICKUP, new WsFloorPickup(WS_FLOOR_PICKUP)));
        subsystem.add(new DataElement(WS_SHOOTER, new WsShooter(WS_SHOOTER)));
        subsystem.add(new DataElement(WS_HOPPER, new WsHopper(WS_HOPPER)));
        subsystem.add(new DataElement(WS_LOADING_RAMP, new WsLoadingRamp(WS_LOADING_RAMP)));
        subsystem.add(new DataElement(WS_CLIMBER, new WsClimber(WS_CLIMBER)));
        subsystem.add(new DataElement(WS_LED, new WsLED(WS_LED)));
        subsystem.add(new DataElement(WS_LIGHT_CANNON, new WsLightCannon(WS_LIGHT_CANNON)));

    }
}
