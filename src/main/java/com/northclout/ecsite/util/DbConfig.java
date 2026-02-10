package com.northclout.ecsite.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DbConfig {
  private static final String PROPERTIES_FILE = "db.properties";
  private static final Properties PROPERTIES = new Properties();
  private static boolean loaded = false;

  private DbConfig() {
  }

  public static synchronized void load() {
    if (loaded) {
      return;
    }
    try (InputStream in = DbConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
      if (in == null) {
        throw new IllegalStateException("Missing " + PROPERTIES_FILE + " in classpath");
      }
      PROPERTIES.load(in);
      loaded = true;
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load " + PROPERTIES_FILE, e);
    }
  }

  public static String get(String key) {
    if (!loaded) {
      load();
    }
    String value = PROPERTIES.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Missing config: " + key);
    }
    return value;
  }
}
