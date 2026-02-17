package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.StockDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StockDAO {
  // 注文確定時の在庫確認で行ロックを取得するSQL。
  private static final String SELECT_FOR_UPDATE =
      "SELECT good_id, quantity FROM stocks WHERE good_id = ? FOR UPDATE";
  // 注文確定後の在庫数更新SQL。
  private static final String UPDATE_QUANTITY =
      "UPDATE stocks SET quantity = ?, updated_at = NOW() WHERE good_id = ?";

  public Optional<StockDTO> findByGoodIdForUpdate(Connection conn, long goodId) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(SELECT_FOR_UPDATE)) {
      stmt.setLong(1, goodId);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          StockDTO dto = new StockDTO();
          dto.setGoodId(rs.getLong("good_id"));
          dto.setQuantity(rs.getInt("quantity"));
          return Optional.of(dto);
        }
        return Optional.empty();
      }
    }
  }

  public int updateQuantity(Connection conn, long goodId, int newQuantity) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(UPDATE_QUANTITY)) {
      stmt.setInt(1, newQuantity);
      stmt.setLong(2, goodId);
      return stmt.executeUpdate();
    }
  }
}
