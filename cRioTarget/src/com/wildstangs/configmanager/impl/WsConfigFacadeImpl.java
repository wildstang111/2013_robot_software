/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configmanager.impl;

import com.sun.squawk.io.BufferedReader;
import com.sun.squawk.microedition.io.FileConnection;
import com.sun.squawk.util.StringTokenizer;
import com.wildstangs.logger.Logger;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;

/**
 *
 * @author Nathan
 */
public final class WsConfigFacadeImpl {

    static String myClassName = "WsConfigFacadeImpl";

    private WsConfigFacadeImpl() {
    }

    /**
     * Reads the config file /ws_config.txt unless setFileName has been used to
     * change the filename. Supports comment lines using // delineator. Comments
     * can be by themselves on a line or at the end of the line.
     *
     * The config file should be on the format key=value Example:
     * com.wildstangs.WsInputFacade.WsDriverJoystick.trim=0.1
     *
     * @throws WsConfigFacadeImplException
     * @returns Hashtable of the config params.
     */
    public static List readConfig(String fileName) throws WsConfigFacadeImplException {
        List config = new List();
        DataInputStream configFile;
        FileConnection fc;
        BufferedReader br = null;
        String line;
        com.sun.squawk.util.StringTokenizer st;
        String value;
        String key;
        String configLine;
        byte[] b;
        int i;
        String path = "file://";
        path += fileName;
        try {
            fc = (FileConnection) Connector.open(path, Connector.READ);
            if (fc.exists()) {
                configFile = fc.openDataInputStream();
                br = new BufferedReader(new InputStreamReader(configFile));
                while ((line = br.readLine()) != null) {
                    //initialize configLine
                    configLine = line;
                    if (!(line.trim().startsWith("//")) && (line.trim().length() != 0)) {
                        // This is not a comment line
                        b = line.trim().getBytes();
                        for (i = 0; i < (b.length - 1); i++) {
                            if (b[i] == '/') {
                                if (b[i + 1] == '/') {
                                    //We have a comment
                                    configLine = line.trim().substring(0, i - 1);
                                    break;
                                }
                            }
                        }
                        if (i == b.length - 1) {
                            //we got to the end
                            configLine = line.trim();
                        }

                        st = new StringTokenizer(configLine.trim(), "=");
                        if (st.countTokens() >= 2) {
                            config.add(new DataElement(st.nextToken(), st.nextToken()));
                        } else {
                            throw new WsConfigFacadeImplException("Bad line in config file " + line);
                        }
                    }
                }
            }

        } catch (Throwable ioe) {
            throw new WsConfigFacadeImplException(ioe.toString());
        }

        if (br != null) {
            try {
                br.close();
            } catch (Throwable ioe) {
                throw new WsConfigFacadeImplException("Error closing file.");
            }
        }
        return config;
    }

    public static boolean checkCreateFile(String filename) {

        FileConnection fc;
        String path = "file://";
        path += filename;

        try {
            fc = (FileConnection) Connector.open(path, Connector.READ_WRITE);
            if (fc.exists()) {
                return true;
            } else {
                fc.create();
                return true;
            }
        } catch (Throwable e) {
            Logger.getLogger().error(myClassName, "checkCreateFile", "Open Config File exception" + e.toString());
            return false;
        }
    }

    public static String getConfigItemName(String configItem) {
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
