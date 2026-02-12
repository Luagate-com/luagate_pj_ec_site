package com.northclout.ecsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class OrderDAO {
  private static final String INSERT_ORDER =
      "INSERT INTO orders (user_id, ordered_at, total_amount, created_at) VALUES (?, ?, ?, NOW())";
  private static final String INSERT_ITEM =
      "INSERT INTO order_items (order_id, good_id, unit_price, quantity, created_at) VALUES (?, ?, ?, ?, NOW())";

  public long insertOrder(Connection conn, long userId, LocalDateTime orderedAt, int totalAmount) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setLong(1, userId);
      stmt.setObject(2, orderedAt);
      stmt.setInt(3, totalAmount);
      stmt.executeUpdate();
      try (var rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
      }
    }
    throw new SQLException("Failed to insert order");
  }

  public int insertOrderItem(Connection conn, long orderId, long goodId, int unitPrice, int quantity) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(INSERT_ITEM)) {
      stmt.setLong(1, orderId);
      stmt.setLong(2, goodId);
      stmt.setInt(3, unitPrice);
      stmt.setInt(4, quantity);
      return stmt.executeUpdate();
    }
  }
}
