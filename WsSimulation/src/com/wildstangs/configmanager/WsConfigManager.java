/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configmanager;


import com.wildstangs.configmanager.WsConfigManagerException;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nathan
 */
public class WsConfigManager {
    private static WsConfigManager instance = null;
    private static String configFileName = "/ws_config.txt";
    private static Hashtable config = new Hashtable();

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

    protected WsConfigManager() {}

    /**
     * Sets the filename to parse. Overrides the default /ws_config.txt. The
     * filename will only be set if it exists and is readable.
     *
     * @param filename The new filename to use for reading.
     * @throws WsConfigFacadeException
     */
    public void setFileName(String filename) throws WsConfigManagerException {
        String path = System.getProperty("user.dir"); 
        path += filename; 
        System.out.println("Path " + path);
        File testFile = new File(path);
        try {
            testFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(WsConfigManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (testFile.exists()) {
            if (testFile.canRead()) {
                configFileName = path;
            } else {
                throw new WsConfigManagerException("File " + path + " is not readable.");
            }
        } else {
            throw new WsConfigManagerException("File " + path + " does not exist.");
        }
    }

    /**
     * Reads the config file /ws_config.txt unless setFileName has been used to
     * change the filename.  Supports comment lines using // delineator.
     * Comments can be by themselves on a line or at the end of the line.
     *
     * The config file should be on the format key=value Example:
     * com.wildstangs.WsInputManager.WsDriverJoystick.trim=0.1
     *
     * @throws WsConfigFacadeException
     */
    public void readConfig() throws WsConfigManagerException {
        File configFile = new File(configFileName);
        BufferedReader br = null;
        String line;
        StringTokenizer st;
        String value;
        String key;
        String configLine;

        if (configFile.exists()) {
            if (configFile.canRead()) {
                try {
                    br = new BufferedReader(new FileReader(configFile));
                    while ((line = br.readLine()) != null) {
                        if (!line.trim().startsWith("//") && !line.trim().isEmpty()) {
                            // This is not a comment line
                            if (line.contains("//")) {
                                //There is comment in the line
                                st = new StringTokenizer(line.trim(), "//");
                                if (st.countTokens() >= 2) {
                                    configLine = st.nextToken();
                                } else {
                                    configLine = line.substring(0, (line.lastIndexOf("//") - 1));
                                }
                            } else {
                                configLine = line;
                            }
                            
                            st = new StringTokenizer(configLine.trim(), "=");
                            if (st.countTokens() >= 2) {
                                key = st.nextToken();
                                value = st.nextToken();
                                config.put(key, value);
                            } else {
                                throw new WsConfigManagerException("Bad line in config file " + line);
                            }
                        }
                    }

                    }  catch (IOException ioe) {
                    throw new WsConfigManagerException(ioe.toString());
                }
            }
        } else {
            throw new WsConfigManagerException("File " + configFileName + " does not exist");
        }
        if (br != null) {
            try {
                br.close();
            } catch (IOException ioe) {
                throw new WsConfigManagerException("Error closing file.");
            }
        }
        
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
     * @throws WsConfigFacadeException if the key cannot be found.
     */
    public String getConfigParamByName(String name) throws WsConfigManagerException {
        String o = null;  
        if (config.get(name) != null){ 
            o = (config.get(name)).toString();
        }
        if (o == null) {
            throw new WsConfigManagerException("Config Param " + name + " not found");
        }
        return o;
    }

    public String dumpConfigData() {
        for (Enumeration e = config.keys(); e.hasMoreElements();) {
            Object name = e.nextElement();
            System.out.println((String)name + "=" + (String)config.get(name));
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
        StringTokenizer st;
        String configName = null;
        if (configItem != null) {
            st = new StringTokenizer(configItem.trim(), ".");
            while (st.hasMoreTokens()) {
                configName = st.nextToken();
            }
        }
        return configName;
    }
}
