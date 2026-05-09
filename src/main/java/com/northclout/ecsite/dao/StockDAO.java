package com.northclout.ecsite.dao;

import com.northclout.ecsite.dto.StockDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 在庫テーブル（stocks）のロック取得＆更新DAO。
 *
 * Ch7-6 の主役。SELECT ... FOR UPDATE で「行ロック」を取るのが最大のポイント。
 *
 * なぜ FOR UPDATE が必要か（必ず説明できるようになること）
 *   - 普通の SELECT は他のトランザクションを止めない。
 *     A さんと B さんが同時に「在庫1個」の商品を買おうとしたら、
 *     両方とも「残り1個ある」と読んでしまい、両方注文成立 → 在庫が -1 になる。
 *   - SELECT ... FOR UPDATE をすると、選んだ行に排他ロックがかかり、
 *     後から来たトランザクションは「先のトランザクションが COMMIT/ROLLBACK するまで」待つ。
 *   - これによって「読み込み → 判定 → 更新」が原子的になる。
 */
public class StockDAO {
  // 注文確定時の在庫確認で行ロックを取得するSQL。
  // FOR UPDATE が肝。ここを外すと、同時アクセス時に在庫マイナスが起きる可能性がある。
  private static final String SELECT_FOR_UPDATE =
      "SELECT good_id, quantity FROM stocks WHERE good_id = ? FOR UPDATE";

  // 注文確定後の在庫数更新SQL。
  // 上で行ロックを取っているので、ここは安全に「新しい数量」を直接書ける。
  private static final String UPDATE_QUANTITY =
      "UPDATE stocks SET quantity = ?, updated_at = NOW() WHERE good_id = ?";

  /**
   * 指定商品の在庫行に行ロックを取りながら現在在庫数を返す。
   *
   * @param conn   Service 側トランザクションの Connection（必ず同じ conn を使うこと！）
   * @param goodId 在庫を見たい商品ID
   * @return 在庫行があれば StockDTO、無ければ Optional.empty()
   */
  public Optional<StockDTO> findByGoodIdForUpdate(Connection conn, long goodId) throws SQLException {
    // TODO Ch7-6: SELECT ... FOR UPDATE を実行して在庫行を取得する。
    //
    // ▼ 実装手順
    //   1. conn.prepareStatement(SELECT_FOR_UPDATE) で PreparedStatement を作る
    //   2. setLong(1, goodId) でプレースホルダを埋める
    //   3. executeQuery() で ResultSet を取得
    //   4. rs.next() が true なら StockDTO に詰めて Optional.of(...) で返す
    //        - dto.setGoodId(rs.getLong("good_id"))
    //        - dto.setQuantity(rs.getInt("quantity"))
    //   5. rs.next() が false なら Optional.empty() を返す
    //   6. PreparedStatement / ResultSet は try-with-resources で close する
    //      （Connection は閉じない！ Service 側のトランザクションがまだ生きているので）
    //
    // ▼ 重要: ここで取った行ロックは「この conn の commit / rollback まで」継続する。
    //   なので Service 側で TransactionManager.execute のラムダが終わった瞬間にロックが解放される。

    throw new SQLException("Not implemented yet (Ch7-6)");
  }

  /**
   * 在庫数を新しい値で上書きする。
   *
   * @param conn        Service 側トランザクションの Connection
   * @param goodId      対象商品
   * @param newQuantity 新しい在庫数（呼び出し側で 現在数 - 注文数 を計算してから渡す）
   * @return 更新行数
   */
  public int updateQuantity(Connection conn, long goodId, int newQuantity) throws SQLException {
    // TODO Ch7-6: stocks の quantity を更新する。
    //
    // ▼ 実装手順
    //   1. conn.prepareStatement(UPDATE_QUANTITY) で PreparedStatement を作る
    //   2. プレースホルダを埋める: 1番目に newQuantity、2番目に goodId
    //   3. executeUpdate() の戻り値をそのまま return
    //   4. PreparedStatement は try-with-resources で close
    //
    // ▼ なぜ "SET quantity = quantity - ?" にしないのか？
    //   - findByGoodIdForUpdate で行ロック済みなので、他のトランザクションは
    //     この行を更新できない＝読んだ時点の値が信頼できる。
    //   - 行ロックを取っていない設計だと "quantity = quantity - ?" にする必要がある。

    throw new SQLException("Not implemented yet (Ch7-6)");
  }
}
