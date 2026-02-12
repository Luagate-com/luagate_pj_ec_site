package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.UserDTO;
import com.northclout.ecsite.util.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAO {
  private static final String SELECT_BY_ID =
      "SELECT id, email, last_name, first_name, address, card_number_last4, card_brand, "
          + "card_exp_month, card_exp_year, card_name FROM users WHERE id = ?";

  public Optional<UserDTO> findById(long id) {
    try (Connection conn = DbConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
      stmt.setLong(1, id);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          UserDTO dto = new UserDTO();
          dto.setId(rs.getLong("id"));
          dto.setEmail(rs.getString("email"));
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
          return Optional.of(dto);
        }
        return Optional.empty();
      }
    } catch (SQLException e) {
      throw new IllegalStateException("Failed to load user", e);
    }
  }
}
