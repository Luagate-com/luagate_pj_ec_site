package com.northclout.ecsite.service;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dao.OrderDAO;
import com.northclout.ecsite.dao.StockDAO;
import com.northclout.ecsite.dto.CartItemDTO;
import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.dto.OrderResult;
import com.northclout.ecsite.dto.StockDTO;
import com.northclout.ecsite.util.TransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 注文確定サービス。
 *
 * Ch7-6 のメインクラス。「在庫チェック → 注文ヘッダ作成 → 明細作成 → 在庫減算」を
 * 1つのトランザクションでまとめて実行する責務を持つ。
 *
 * なぜトランザクションが必要か（授業で必ず思い出すこと）
 *   - 注文ヘッダだけ INSERT されて明細が無い、明細はあるが在庫が減ってない、
 *     という「中途半端な状態」を絶対に残さないため。
 *   - 同じ商品を別ユーザーが同時に買おうとした時に、在庫の二重引きを防ぐため。
 */
public class OrderService {

  /**
   * カートの内容を確定して注文を作る。
   *
   * @param userId    注文するユーザーID（ログイン済み前提）
   * @param cartItems セッションに入っているカート（goodId と quantity のリスト）
   * @return 成功なら orderId 入りの success、失敗なら理由メッセージ入りの failure
   */
  public OrderResult completeOrder(long userId, List<CartItemDTO> cartItems) {
    if (cartItems == null || cartItems.isEmpty()) {
      return OrderResult.failure("カートが空です。");
    }

    // TODO Ch7-6: ここから下を、TransactionManager.execute(conn -> { ... }) で囲んで実装する。
    //
    // ▼ 実装の流れ（順番が大事）
    //   1. GoodDAO / StockDAO / OrderDAO のインスタンスを用意する
    //   2. cartItems から goodId のリストを作り、GoodDAO.findByIds で商品マスタを引く
    //      （注文時点の単価を order_items に保存したいので、ここで価格を確定させる）
    //   3. cartItems を1件ずつループしながら StockDAO.findByGoodIdForUpdate(conn, goodId) を呼ぶ
    //      → これが SELECT ... FOR UPDATE。行ロックを取って他トランザクションを待たせる
    //      → 在庫が無い／不足している場合は OrderResult.failure(...) を return
    //         （TransactionManager が SQLException 以外で return された値をそのまま返してくれる想定）
    //   4. 合計金額を計算（good.getPrice() * item.getQuantity() の総和）
    //   5. OrderDAO.insertOrder(conn, userId, LocalDateTime.now(), total) で orders を作成し、
    //      自動採番された orderId を受け取る
    //   6. cartItems をもう一度ループして
    //        - OrderDAO.insertOrderItem(conn, orderId, goodId, unitPrice, quantity) で明細を1行ずつINSERT
    //        - StockDAO.updateQuantity(conn, goodId, 新しい在庫数) で在庫を減らす
    //   7. 全部成功したら OrderResult.success(orderId) を return
    //
    // ▼ ハマりやすいポイント
    //   - TransactionManager.execute は、ラムダ内で例外が出たら自動で rollback してくれる。
    //     逆に、return で OrderResult.failure を返すだけだと commit されてしまう実装もあり得る。
    //     今回の TransactionManager は「SQLException が出た時だけ rollback」なので、
    //     在庫不足のような業務エラーで return failure する分には commit されてOK
    //     （ヘッダも明細もまだ INSERT されていない時点で抜けるから副作用ゼロ）。
    //   - 5 と 6 の順序を逆にすると、order_items に入れる order_id が無くて FK 違反になる。
    //   - SELECT FOR UPDATE は同じトランザクション（同じ Connection）内で実行しないと意味がない。
    //     必ず TransactionManager.execute のラムダから受け取った conn を使うこと。
    //   - 在庫減算を UPDATE stocks SET quantity = quantity - ? にせず、
    //     「読んだ値 - 数量」で UPDATE しているのは、上で行ロックを取っているから安全。
    //     行ロックを取らずに同じ書き方をすると、有名な「在庫マイナス事故」が起きる。

    return OrderResult.failure("未実装");
  }
}
