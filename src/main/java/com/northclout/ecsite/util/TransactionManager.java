package com.northclout.ecsite.util;

import java.sql.Connection;
import java.sql.SQLException;

public final class TransactionManager {
  private TransactionManager() {
  }

  public static <T> T execute(TransactionCallback<T> callback) {
    try (Connection conn = DbConnection.getConnection()) {
      // ここで明示的にトランザクションを開始する。
      conn.setAutoCommit(false);
      try {
        T result = callback.doInTransaction(conn);
        // コールバック処理が最後まで成功した場合のみ確定する。
        conn.commit();
        return result;
      } catch (Exception e) {
        // 途中で失敗した更新は全て取り消す。
        rollbackQuietly(conn);
        throw new IllegalStateException("Transaction failed", e);
      } finally {
        try {
          // 他処理への影響を避けるため、接続状態を元に戻す。
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
