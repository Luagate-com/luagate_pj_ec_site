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
    // TODO Ch7-3: 全商品を取得して List<GoodDTO> で返す
    //  ヒント:
    //   1. try-with-resources で DbConnection.getConnection() / PreparedStatement / ResultSet を確保する
    //   2. SQL は SELECT_ALL 定数を使う（パラメータなし）
    //   3. mapList(rs) で ResultSet → List<GoodDTO> に詰める
    //   4. SQLException は catch して new IllegalStateException("Failed to load goods", e) を投げる
    return new ArrayList<>();
  }

  public List<GoodDTO> findByCategory(String category) {
    // TODO Ch7-3: 指定カテゴリの商品を取得して List<GoodDTO> で返す
    //  ヒント:
    //   1. try-with-resources で Connection / PreparedStatement を確保する
    //   2. SQL は SELECT_BY_CATEGORY を使い、stmt.setString(1, category) でバインドする
    //   3. executeQuery() で取得した ResultSet も try-with-resources で閉じる
    //   4. mapList(rs) で List 化して返す
    return new ArrayList<>();
  }

  public Optional<GoodDTO> findById(long id) {
    // TODO Ch7-3: id 指定で商品 1 件を取得して Optional<GoodDTO> で返す
    //  ヒント:
    //   1. SQL は SELECT_BY_ID を使い、stmt.setLong(1, id) でバインドする
    //   2. rs.next() が true なら mapRow(rs) を Optional.of でラップして返す
    //   3. 見つからなければ Optional.empty() を返す
    return Optional.empty();
  }

  public List<GoodDTO> findByIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return new ArrayList<>();
    }
    // TODO Ch7-3: 複数 id の商品を一括取得して List<GoodDTO> で返す（カート表示などで利用）
    //  ヒント:
    //   1. ids.size() 個の "?" を StringJoiner で "?,?,?" のように連結する
    //   2. "SELECT ... FROM goods WHERE id IN (" + joiner + ") ORDER BY id" の SQL を組み立てる
    //   3. for ループで stmt.setLong(i + 1, ids.get(i)) のように 1-based でパラメータをバインドする
    //   4. mapList(rs) で List<GoodDTO> に詰めて返す
    return new ArrayList<>();
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
