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
  // ユーザーIDで会員情報を取得するSQL。
  private static final String SELECT_BY_ID =
      "SELECT id, email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name "
          + "FROM users WHERE id = ?";
  // ログイン認証で使うメールアドレス検索SQL。
  private static final String SELECT_BY_EMAIL =
      "SELECT id, email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name "
          + "FROM users WHERE email = ?";
  // 新規会員登録SQL。
  private static final String INSERT_USER =
      "INSERT INTO users (email, password_hash, last_name, first_name, address, "
          + "card_number_last4, card_brand, card_exp_month, card_exp_year, card_name, created_at, updated_at) "
          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
  // マイページ編集SQL。
  private static final String UPDATE_USER =
      "UPDATE users SET last_name = ?, first_name = ?, address = ?, "
          + "card_number_last4 = ?, card_brand = ?, card_exp_month = ?, card_exp_year = ?, card_name = ?, "
          + "updated_at = NOW() WHERE id = ?";
  // パスワード変更SQL。
  private static final String UPDATE_PASSWORD =
      "UPDATE users SET password_hash = ?, updated_at = NOW() WHERE id = ?";

  public Optional<UserDTO> findById(long id) {
    // TODO Ch7-5: ユーザーIDで会員情報を1件取得し Optional<UserDTO> で返す
    //   ヒント:
    //   1. DbConnection.getConnection() で Connection を取得 (try-with-resources で自動クローズ)
    //   2. conn.prepareStatement(SELECT_BY_ID) で PreparedStatement を作成
    //   3. stmt.setLong(1, id) で ? に値をバインド
    //   4. stmt.executeQuery() で ResultSet を取得
    //   5. rs.next() が true なら mapRow(rs) を Optional.of でラップして返す
    //   6. レコードが無ければ Optional.empty() を返す
    //   7. SQLException は IllegalStateException("Failed to load user", e) に変換して throw
    //   参考: https://docs.oracle.com/javase/jp/21/docs/api/java.sql/java/sql/PreparedStatement.html
    return Optional.empty();
  }

  public Optional<UserDTO> findByEmail(String email) {
    // TODO Ch7-5: メールアドレスでユーザーを検索する (ログイン認証で使用)
    //   ヒント:
    //   - findById とほぼ同じ流れ。SQL は SELECT_BY_EMAIL を使う
    //   - ? には stmt.setString(1, email) で文字列をバインド
    //   - ヒットしなければ Optional.empty() を返す (ログイン側で「ユーザー無し」を判定)
    return Optional.empty();
  }

  public long insertUser(UserDTO user) {
    // TODO Ch7-5: 新規ユーザーを INSERT し、自動採番された ID を返す
    //   ヒント:
    //   1. prepareStatement の第2引数に Statement.RETURN_GENERATED_KEYS を渡すと
    //      AUTO_INCREMENT で発行された ID を後から取得できる
    //   2. INSERT_USER の ? に user の各フィールドを順番にバインドする
    //      - 1: email, 2: passwordHash, 3: lastName, 4: firstName, 5: address
    //      - 6: cardNumberLast4, 7: cardBrand
    //      - 8: cardExpMonth (Integer なので null チェックが必要 → setNull(8, Types.SMALLINT))
    //      - 9: cardExpYear (同上)
    //      - 10: cardName
    //   3. stmt.executeUpdate() で実行
    //   4. stmt.getGeneratedKeys() で ResultSet を取り、rs.next() なら rs.getLong(1) を return
    //   5. 失敗時は IllegalStateException("Failed to insert user", e) を throw
    //   参考: Statement.RETURN_GENERATED_KEYS と getGeneratedKeys() の使い方
    throw new IllegalStateException("Failed to insert user");
  }

  public int updateUser(UserDTO user) {
    // TODO Ch7-7: マイページから会員情報を更新する (Ch7-5 段階では空のままにしておく)
    //   ヒント:
    //   - UPDATE_USER の ? に lastName, firstName, address, cardNumberLast4, cardBrand,
    //     cardExpMonth, cardExpYear, cardName, id の順にバインド
    //   - cardExpMonth / cardExpYear は Integer なので null の場合 setNull(idx, Types.SMALLINT)
    //   - stmt.executeUpdate() の戻り値 (更新件数) をそのまま return する
    return 0;
  }

  public int updatePassword(long userId, String passwordHash) {
    // TODO Ch7-7: パスワード変更時に password_hash カラムだけを更新する (Ch7-5 段階では空のままにしておく)
    //   ヒント:
    //   - UPDATE_PASSWORD を使う
    //   - ? は 1: passwordHash, 2: userId
    //   - 戻り値は executeUpdate() の更新件数 (通常 1)
    return 0;
  }

  private UserDTO mapRow(ResultSet rs) throws SQLException {
    // DBの1レコードをDTOへ詰め替える共通マッパー。
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
