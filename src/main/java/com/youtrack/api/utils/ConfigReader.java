package com.youtrack.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;
    private static final String CONFIG_FILE = "config.properties";

    static {
        properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + CONFIG_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getApiBasePath() {
        return properties.getProperty("api.base.path");
    }

    public static String getAuthToken() {
        return properties.getProperty("auth.token");
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("timeout.seconds", "30"));
    }

    public static String getTestProjectId() {
        return properties.getProperty("test.project.id");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
