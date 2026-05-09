package com.northclout.ecsite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

/**
 * 注文ヘッダ（orders）と注文明細（order_items）のINSERT専用DAO。
 *
 * Ch7-6 の注文確定トランザクションから呼ばれる。
 * Connection は引数で受け取る形（DAO側で getConnection しない）にしているのは、
 * Service 側のトランザクションに「相乗り」させるため。
 */
public class OrderDAO {
  // 注文ヘッダ（orders）作成SQL。
  // total_amount は注文時点の合計金額のスナップショット。後で商品価格が変わっても変えない。
  private static final String INSERT_ORDER =
      "INSERT INTO orders (user_id, ordered_at, total_amount, created_at) VALUES (?, ?, ?, NOW())";

  // 注文明細（order_items）作成SQL。
  // unit_price はその時点で買った単価。後から商品マスタの price が変わっても、注文書としては不変。
  private static final String INSERT_ITEM =
      "INSERT INTO order_items (order_id, good_id, unit_price, quantity, created_at) VALUES (?, ?, ?, ?, NOW())";

  /**
   * 注文ヘッダを1行 INSERT して、自動採番された order_id を返す。
   *
   * @param conn        Service 側のトランザクションで開いた Connection（DAOではcloseしない）
   * @param userId      注文者
   * @param orderedAt   注文日時（基本は LocalDateTime.now()）
   * @param totalAmount 合計金額
   * @return 生成された orders.id
   */
  public long insertOrder(Connection conn, long userId, LocalDateTime orderedAt, int totalAmount) throws SQLException {
    // TODO Ch7-6: orders に1行 INSERT して、AUTO_INCREMENT で発行された id を取り出して返す。
    //
    // ▼ 実装手順
    //   1. conn.prepareStatement(INSERT_ORDER, Statement.RETURN_GENERATED_KEYS) で PreparedStatement を作る
    //      （第2引数を指定しないと getGeneratedKeys() で id が取れない！ ここがハマりポイント）
    //   2. setLong / setObject / setInt でプレースホルダを埋める
    //        - 1番目: userId
    //        - 2番目: orderedAt（setObject でOK）
    //        - 3番目: totalAmount
    //   3. executeUpdate() を呼ぶ
    //   4. stmt.getGeneratedKeys() で ResultSet を取り、rs.next() / rs.getLong(1) で id を取る
    //   5. PreparedStatement / ResultSet は try-with-resources で必ず close する（Connection はしない）
    //
    // ▼ id が取れなかった時は new SQLException("Failed to insert order") を投げる
    //   → そうすれば TransactionManager が拾って rollback してくれる

    throw new SQLException("Not implemented yet (Ch7-6)");
  }

  /**
   * 注文明細を1行 INSERT する。
   *
   * @param conn      Service 側トランザクションの Connection
   * @param orderId   先に insertOrder で作った orders.id
   * @param goodId    商品ID
   * @param unitPrice 注文時点の単価
   * @param quantity  注文数量
   * @return INSERTされた行数（基本 1）
   */
  public int insertOrderItem(Connection conn, long orderId, long goodId, int unitPrice, int quantity) throws SQLException {
    // TODO Ch7-6: order_items に1行 INSERT する。
    //
    // ▼ 実装手順
    //   1. conn.prepareStatement(INSERT_ITEM) で PreparedStatement を作る
    //      （こちらは generated keys は不要）
    //   2. プレースホルダを埋める: orderId / goodId / unitPrice / quantity
    //   3. executeUpdate() の戻り値をそのまま return
    //   4. PreparedStatement は try-with-resources で close

    throw new SQLException("Not implemented yet (Ch7-6)");
  }
}
