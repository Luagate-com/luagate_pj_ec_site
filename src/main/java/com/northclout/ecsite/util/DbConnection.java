package com.northclout.ecsite.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DbConnection {
  static {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("MySQL driver not found. Add mysql-connector-j to classpath.", e);
    }
  }

  private DbConnection() {
  }

  public static Connection getConnection() throws SQLException {
    String url = DbConfig.get("db.url");
    String user = DbConfig.get("db.user");
    String password = DbConfig.get("db.password");
    return DriverManager.getConnection(url, user, password);
  }
}
