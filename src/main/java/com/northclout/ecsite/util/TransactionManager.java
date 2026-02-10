package com.northclout.ecsite.util;

import java.sql.Connection;
import java.sql.SQLException;

public final class TransactionManager {
  private TransactionManager() {
  }

  public static <T> T execute(TransactionCallback<T> callback) {
    try (Connection conn = DbConnection.getConnection()) {
      conn.setAutoCommit(false);
      try {
        T result = callback.doInTransaction(conn);
        conn.commit();
        return result;
      } catch (Exception e) {
        rollbackQuietly(conn);
        throw new IllegalStateException("Transaction failed", e);
      } finally {
        try {
          conn.setAutoCommit(true);
        } catch (SQLException ignored) {
        }
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to open DB connection", e);
    }
  }

  private static void rollbackQuietly(Connection conn) {
    try {
      conn.rollback();
    } catch (SQLException ignored) {
    }
  }
}
