package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GoodDAO {
  private static final String SELECT_ALL =
      "SELECT id, code, name, description, price, category, image_url FROM goods ORDER BY id";
  private static final String SELECT_BY_CATEGORY =
      "SELECT id, code, name, description, price, category, image_url FROM goods WHERE category = ? ORDER BY id";

  public List<GoodDTO> findAll() {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
         ResultSet rs = stmt.executeQuery()) {
      return mapList(rs);
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load goods", e);
    }
  }

  public List<GoodDTO> findByCategory(String category) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CATEGORY)) {
      stmt.setString(1, category);
      try (ResultSet rs = stmt.executeQuery()) {
        return mapList(rs);
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load goods by category", e);
    }
  }

  private List<GoodDTO> mapList(ResultSet rs) throws SQLException {
    List<GoodDTO> goods = new ArrayList<>();
    while (rs.next()) {
      GoodDTO dto = new GoodDTO();
      dto.setId(rs.getLong("id"));
      dto.setCode(rs.getString("code"));
      dto.setName(rs.getString("name"));
      dto.setDescription(rs.getString("description"));
      dto.setPrice(rs.getInt("price"));
      dto.setCategory(rs.getString("category"));
      dto.setImageUrl(rs.getString("image_url"));
      goods.add(dto);
    }
    return goods;
  }
}
