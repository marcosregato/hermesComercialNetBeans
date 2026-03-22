
package com.br.hermescomercialnetbeans.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

    private static final Logger logger = LogManager.getLogger(ConfigProperties.class);
    private static final Properties properties = new Properties();

    static {
        String path = System.getProperty("user.dir") + "/config.properties";
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (Exception e) {
            logger.error("Error loading config.properties file from path: " + path, e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value.trim();
        } else {
            logger.warn("Property '" + key + "' not found in config.properties");
            return null; // Return null to indicate the property was not found
        }
    }
}
