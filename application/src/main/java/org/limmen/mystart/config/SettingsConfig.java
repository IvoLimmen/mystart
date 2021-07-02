package org.limmen.mystart.config;

import java.util.Properties;

import jakarta.inject.Singleton;

@Singleton
public class SettingsConfig {

    Properties properties = new Properties();

    public SettingsConfig() {
    }

    public Properties getProperties() {
        return properties;
    }
}
