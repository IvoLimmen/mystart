package org.limmen.mystart.config;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SettingsProvider {
    
    private final static Logger log = LoggerFactory.getLogger(SettingsProvider.class);

    private final String workingDirectory = System.getProperty("user.dir");
    private final Properties properties = new Properties();

    @Inject
    public SettingsProvider(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();    
        String profile = environment.getActiveNames().iterator().next();            
        String configurationDir = environment.get("config.dir", String.class).orElse("unknown");

        loadProperties(configurationDir, profile);
    }

    private void loadProperties(String configurationDir, String profile) {
        Path propertyFile = Path.of(workingDirectory, configurationDir, getFileName(profile));
        Properties profileProperties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(propertyFile.toFile())) {
            profileProperties.load(fileInputStream);            
        } catch(Exception e) {
            log.error("Failed to load properties from {}", propertyFile, e);
        }   
        properties.putAll(profileProperties);
        log.info("Property file from profile {} loaded", profile);
    }

    private String getFileName(String profile) {
        return String.format("application-%s.properties", profile);
    }

    public Properties getProperties() {
        return properties;
    }
}
