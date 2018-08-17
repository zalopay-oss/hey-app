package com.hey.util;

import com.hey.Main;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtils {

    private static final Logger LOGGER = LogManager.getLogger(PropertiesUtils.class);

    private static final String PROP_FILE_NAME = "application.properties";
    private static PropertiesUtils instance;
    private static String env;
    private Properties properties;

    private PropertiesUtils() {
        env = System.getenv("env");
        try {
            properties = new Properties();
            properties.load(FileUtils.openInputStream(new File(Main.RESOURCE_PATH + env + "." + PROP_FILE_NAME)));
        } catch (IOException ioe) {
            LOGGER.error("Error reading config properties: ", ioe);
            throw new RuntimeException(ioe);
        }
    }

    public static PropertiesUtils getInstance() {
        if (instance == null)
            instance = new PropertiesUtils();
        return instance;
    }

    public String getValue(String key) {
        String value = null;
        value = properties.getProperty(key);
        return value;
    }

    public Integer getIntValue(String key) {
        Integer value = null;
        try {
            value = Integer.valueOf(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return null;
        }
        return value;
    }
}
