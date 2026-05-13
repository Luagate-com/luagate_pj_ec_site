package com.northclout.ecsite.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

public final class DbConfig {
  private static final String PROPERTIES_FILE = "db.properties";
  private static final Properties PROPERTIES = new Properties();
  private static boolean loaded = false;

  // DATABASE_URL をパースした結果をキャッシュする。
  private static String parsedUrl;
  private static String parsedUser;
  private static String parsedPassword;
  private static boolean databaseUrlParsed = false;

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
    // 優先順位: DATABASE_URL > DB_URL/DB_USER/DB_PASSWORD > db.properties
    String value = getFromEnv(key);
    if (value == null || value.isBlank()) {
      value = PROPERTIES.getProperty(key);
    }
    if (value == null || value.isBlank()) {
      throw new IllegalStateException("Missing config: " + key);
    }
    return value;
  }

  private static String getFromEnv(String key) {
    // DATABASE_URL が設定されていればパースして url / user / password を取り出す。
    parseDatabaseUrl();
    if ("db.url".equals(key)) {
      if (parsedUrl != null) {
        return parsedUrl;
      }
      return System.getenv("DB_URL");
    }
    if ("db.user".equals(key)) {
      if (parsedUser != null) {
        return parsedUser;
      }
      return System.getenv("DB_USER");
    }
    if ("db.password".equals(key)) {
      if (parsedPassword != null) {
        return parsedPassword;
      }
      return System.getenv("DB_PASSWORD");
    }
    return null;
  }

  // postgresql://user:password@host/dbname?sslmode=require
  // → jdbc:postgresql://host/dbname?sslmode=require, user, password
  private static synchronized void parseDatabaseUrl() {
    if (databaseUrlParsed) {
      return;
    }
    databaseUrlParsed = true;
    String databaseUrl = System.getenv("DATABASE_URL");
    if (databaseUrl == null || databaseUrl.isBlank()) {
      return;
    }
    try {
      URI uri = new URI(databaseUrl);
      String host = uri.getHost();
      int port = uri.getPort();
      String path = uri.getPath();
      String query = uri.getRawQuery();
      String userInfo = uri.getUserInfo();

      StringBuilder jdbcUrl = new StringBuilder("jdbc:postgresql://");
      jdbcUrl.append(host);
      if (port > 0) {
        jdbcUrl.append(":").append(port);
      }
      jdbcUrl.append(path);
      if (query != null && !query.isEmpty()) {
        jdbcUrl.append("?").append(query);
      }
      parsedUrl = jdbcUrl.toString();

      if (userInfo != null && userInfo.contains(":")) {
        String[] parts = userInfo.split(":", 2);
        parsedUser = parts[0];
        parsedPassword = parts[1];
      }
    } catch (Exception e) {
      // パース失敗時は DATABASE_URL を無視して従来のフォールバックに任せる。
    }
  }
}
