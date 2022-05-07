package org.limmen.mystart.config;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

import ch.qos.logback.classic.util.ContextInitializer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SettingsProvider {

  private final String workingDirectory = System.getProperty("user.dir");
  private final Properties properties = new Properties();

  @Inject
  public SettingsProvider(ApplicationContext applicationContext) {
    Environment environment = applicationContext.getEnvironment();
    String profile = environment.getActiveNames().iterator().next();
    String configurationDir = environment.get("config.dir", String.class).orElse("unknown");

    System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY,
        pathToConfigFile(configurationDir, profile, "logback", "xml").toString());

    loadProperties(configurationDir, profile);
  }

  private void loadProperties(String configurationDir, String profile) {
    Path propertyFile = pathToConfigFile(configurationDir, profile, "application", "properties");
    Properties profileProperties = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream(propertyFile.toFile())) {
      profileProperties.load(fileInputStream);
    } catch (Exception e) {
      throw new RuntimeException(String.format("Failed to load properties from %s", propertyFile), e);
    }
    properties.putAll(profileProperties);
  }

  private Path pathToConfigFile(String configurationDir, String profile, String fileName, String ext) {
    return Path.of(workingDirectory, configurationDir, getFileName(fileName, ext, profile));
  }

  private String getFileName(String fileName, String ext, String profile) {
    return String.format("%s-%s.%s", fileName, profile, ext);
  }

  public Properties getProperties() {
    return properties;
  }

  public boolean bool(String key) {
    return "true".equals(getProperties().getProperty(key).toLowerCase());
  }

  public String string(String key) {
    return getProperties().getProperty(key);
  }

  public Integer integer(String key) {
    try {
      return Integer.parseInt(getProperties().getProperty(key));
    } catch (NumberFormatException nfe) {
      return null;
    }
  }
}
