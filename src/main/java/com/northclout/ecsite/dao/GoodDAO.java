package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class GoodDAO {
  // 商品一覧（全件）取得用SQL。
  private static final String SELECT_ALL =
      "SELECT id, code, name, description, price, category, image_url FROM goods ORDER BY id";
  // カテゴリで絞り込んだ商品一覧取得用SQL。
  private static final String SELECT_BY_CATEGORY =
      "SELECT id, code, name, description, price, category, image_url FROM goods WHERE category = ? ORDER BY id";
  // 商品詳細（1件）取得用SQL。
  private static final String SELECT_BY_ID =
      "SELECT id, code, name, description, price, category, image_url FROM goods WHERE id = ?";

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

  public Optional<GoodDTO> findById(long id) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRow(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load good by id", e);
    }
  }

  public List<GoodDTO> findByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return new ArrayList<>();
    }
    StringJoiner joiner = new StringJoiner(",");
    for (int i = 0; i < ids.size(); i++) {
      joiner.add("?");
    }
    // カート表示など、複数商品を一括取得するための可変長IN句。
    String sql = "SELECT id, code, name, description, price, category, image_url FROM goods WHERE id IN ("
        + joiner + ") ORDER BY id";
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
      for (int i = 0; i < ids.size(); i++) {
        stmt.setLong(i + 1, ids.get(i));
      }
      try (ResultSet rs = stmt.executeQuery()) {
        return mapList(rs);
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load goods by ids", e);
    }
  }

  private List<GoodDTO> mapList(ResultSet rs) throws SQLException {
    List<GoodDTO> goods = new ArrayList<>();
    while (rs.next()) {
      goods.add(mapRow(rs));
    }
    return goods;
  }

  private GoodDTO mapRow(ResultSet rs) throws SQLException {
    GoodDTO dto = new GoodDTO();
    dto.setId(rs.getLong("id"));
    dto.setCode(rs.getString("code"));
    dto.setName(rs.getString("name"));
    dto.setDescription(rs.getString("description"));
    dto.setPrice(rs.getInt("price"));
    dto.setCategory(rs.getString("category"));
    dto.setImageUrl(rs.getString("image_url"));
    return dto;
  }
}
