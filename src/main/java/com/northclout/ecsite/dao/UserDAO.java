package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.UserDTO;
import com.northclout.ecsite.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UserDAO {
  private static final String SELECT_BY_ID =
      "SELECT id, email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name "
          + "FROM users WHERE id = ?";
  private static final String SELECT_BY_EMAIL =
      "SELECT id, email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name "
          + "FROM users WHERE email = ?";
  private static final String INSERT_USER =
      "INSERT INTO users (email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name, created_at, updated_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
  private static final String UPDATE_USER =
      "UPDATE users SET last_name = ?, first_name = ?, address = ?, "
          + "card_number_last4 = ?, card_brand = ?, card_exp_month = ?, card_exp_year = ?, card_name = ?, "
          + "updated_at = NOW() WHERE id = ?";
  private static final String UPDATE_PASSWORD =
      "UPDATE users SET password_hash = ?, updated_at = NOW() WHERE id = ?";

  public Optional<UserDTO> findById(long id) {
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
      throw new IllegalStateException("Failed to load user", e);
    }
  }

  public Optional<UserDTO> findByEmail(String email) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL)) {
      stmt.setString(1, email);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapRow(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load user by email", e);
    }
  }

  public long insertUser(UserDTO user) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getPasswordHash());
      stmt.setString(3, user.getLastName());
      stmt.setString(4, user.getFirstName());
      stmt.setString(5, user.getAddress());
      stmt.setString(6, user.getCardNumberLast4());
      stmt.setString(7, user.getCardBrand());
      if (user.getCardExpMonth() == null) {
        stmt.setNull(8, java.sql.Types.TINYINT);
      } else {
        stmt.setInt(8, user.getCardExpMonth());
      }
      if (user.getCardExpYear() == null) {
        stmt.setNull(9, java.sql.Types.SMALLINT);
      } else {
        stmt.setInt(9, user.getCardExpYear());
      }
      stmt.setString(10, user.getCardName());
      stmt.executeUpdate();
      try (var rs = stmt.getGeneratedKeys()) {
        if (rs.next()) {
          return rs.getLong(1);
        }
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to insert user", e);
    }
    throw new IllegalStateException("Failed to insert user");
  }

  public int updateUser(UserDTO user) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
      stmt.setString(1, user.getLastName());
      stmt.setString(2, user.getFirstName());
      stmt.setString(3, user.getAddress());
      stmt.setString(4, user.getCardNumberLast4());
      stmt.setString(5, user.getCardBrand());
      if (user.getCardExpMonth() == null) {
        stmt.setNull(6, java.sql.Types.TINYINT);
      } else {
        stmt.setInt(6, user.getCardExpMonth());
      }
      if (user.getCardExpYear() == null) {
        stmt.setNull(7, java.sql.Types.SMALLINT);
      } else {
        stmt.setInt(7, user.getCardExpYear());
      }
      stmt.setString(8, user.getCardName());
      stmt.setLong(9, user.getId());
      return stmt.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to update user", e);
    }
  }

  public int updatePassword(long userId, String passwordHash) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_PASSWORD)) {
      stmt.setString(1, passwordHash);
      stmt.setLong(2, userId);
      return stmt.executeUpdate();
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to update password", e);
    }
  }

  private UserDTO mapRow(ResultSet rs) throws SQLException {
    UserDTO dto = new UserDTO();
    dto.setId(rs.getLong("id"));
    dto.setEmail(rs.getString("email"));
    dto.setPasswordHash(rs.getString("password_hash"));
    dto.setLastName(rs.getString("last_name"));
    dto.setFirstName(rs.getString("first_name"));
    dto.setAddress(rs.getString("address"));
    dto.setCardNumberLast4(rs.getString("card_number_last4"));
    dto.setCardBrand(rs.getString("card_brand"));
    int month = rs.getInt("card_exp_month");
    if (!rs.wasNull()) {
      dto.setCardExpMonth(month);
    }
    int year = rs.getInt("card_exp_year");
    if (!rs.wasNull()) {
      dto.setCardExpYear(year);
    }
    dto.setCardName(rs.getString("card_name"));
    return dto;
  }
}
