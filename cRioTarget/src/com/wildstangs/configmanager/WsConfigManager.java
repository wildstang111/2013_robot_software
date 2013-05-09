/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configmanager;

import com.wildstangs.configmanager.impl.WsConfigFacadeImpl;
import com.wildstangs.configmanager.impl.WsConfigFacadeImplException;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public class WsConfigManager {

    static String myClassName = "WsConfigFacade";
    private static WsConfigManager instance = null;
    private static String configFileName = "/ws_config.txt";
    private static List config = new List();

    /**
     * Gets the instance of the WsConfigManager Singleton.
     *
     * @return The instance of the WsConfigManager.
     */
    public static WsConfigManager getInstance() {
        if (instance == null) {
            instance = new WsConfigManager();
        }
        return instance;
    }

    protected WsConfigManager() {
    }

    /**
     * Sets the filename to parse. Overrides the default /ws_config.txt. The
     * filename will only be set if it exists and is readable.
     *
     * @param filename The new filename to use for reading.
     * @throws WsConfigManagerException
     */
    public void setFileName(String filename) throws WsConfigManagerException {
        if (WsConfigFacadeImpl.checkCreateFile(filename)) {
            configFileName = filename;
        } else {
            throw new WsConfigManagerException("Problem setting config file name");
        }

    }

    /**
     * Reads the config file /ws_config.txt unless setFileName has been used to
     * change the filename. Supports comment lines using // delineator. Comments
     * can be by themselves on a line or at the end of the line.
     *
     * The config file should be on the format key=value Example:
     * com.wildstangs.WsInputManager.WsDriverJoystick.trim=0.1
     *
     * @throws WsConfigManagerException
     */
    public void readConfig() throws WsConfigManagerException {
        try {
            config = (List) WsConfigFacadeImpl.readConfig(configFileName);
        } catch (WsConfigFacadeImplException e) {
            throw new WsConfigManagerException(e.toString());
        }
        Logger.getLogger().always(this.getClass().getName(), "readConfig", "Read config File: " + configFileName);
        //Update all the facades
        WsInputManager.getInstance().notifyConfigChange();
        WsOutputManager.getInstance().notifyConfigChange();
        WsSubsystemContainer.getInstance().notifyConfigChange();

    }

    /**
     * Return a Config value the matches the key
     *
     * @param name The key value to search for.
     * @return An Object that contains the value.
     * @throws WsConfigManagerException if the key cannot be found.
     */
    public String getConfigParamByName(String name) throws WsConfigManagerException {
        for (int i = 0; i < config.size(); i++) {
            if ((((String) ((DataElement) config.get(i)).getKey())).equals(name)) {
                return (String) ((DataElement) config.get(i)).getValue();
            }
        }
        throw new WsConfigManagerException("Config Param " + name + " not found");

    }

    public String dumpConfigData() {
        System.out.println("Dumping config data...");
        for (int i = 0; i < config.size(); i++) {
            String name = ((String) ((DataElement) config.get(i)).getKey());
            String value = ((String) ((DataElement) config.get(i)).getValue().toString());
            System.out.println(name + "=" + value);
        }
        return null;
    }

    /**
     * Config Item name parser
     *
     * Example: com.wildstangs.WsInputManager.WsDriverJoystick.trim will return
     * trim.
     *
     * @returns The config Item name or null if the string is unparsable
     * @param configItem A String representing the config item to parse
     */
    public String getConfigItemName(String configItem) {
        return WsConfigFacadeImpl.getConfigItemName(configItem);
    }
}
