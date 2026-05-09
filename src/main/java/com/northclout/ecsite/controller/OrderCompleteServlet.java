package com.northclout.ecsite.controller;

import com.northclout.ecsite.dto.CartItemDTO;
import com.northclout.ecsite.dto.OrderResult;
import com.northclout.ecsite.service.OrderService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * /order/complete - 注文確定処理 + 完了画面表示。
 *
 * Ch7-6 のクライマックス。
 *   - doPost: 「注文を確定」ボタンが押された時の処理。OrderService を呼んで実際にDBへ書き込む。
 *   - doGet : 完了画面の描画。PRG（Post-Redirect-Get）パターンで、
 *             doPost 成功後に sendRedirect でこちらに飛ばしてから表示する。
 *
 * doGet は教材として読みやすいよう、あえて完成形を残してある。
 * doPost を埋めるのが課題。
 */
@WebServlet("/order/complete")
public class OrderCompleteServlet extends HttpServlet {
  private static final String CART_SESSION_KEY = "cart";

  /**
   * 注文完了画面の表示。PRG の Get 側。
   *
   * doPost 成功時に session.setAttribute("orderNumber", ...) しておき、
   * リダイレクト後の doGet でそれを 1回だけ取り出して表示する。
   * （リロードしても重複注文が起きないようにするための定番パターン）
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object orderNumber = session.getAttribute("orderNumber");
    if (orderNumber != null) {
      // PRGパターンで遷移するため、完了画面表示後にセッション値は消す。
      req.setAttribute("orderNumber", orderNumber.toString());
      session.removeAttribute("orderNumber");
    } else {
      // 直接アクセス時の表示崩れを防ぐためのフォールバック値。
      req.setAttribute("orderNumber", "ORD-240130-001");
    }
    req.getRequestDispatcher("/WEB-INF/jsp/order_complete.jsp").forward(req, resp);
  }

  /**
   * 「注文を確定」ボタンの POST を受けて、注文を確定させる。
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-6: 注文確定処理を実装する。
    //
    // ▼ やること（順番通り）
    //   1. HttpSession session = req.getSession();
    //   2. ログインチェック
    //        - session.getAttribute("userId") が null なら /login へ sendRedirect → return
    //   3. カート取得: List<CartItemDTO> cart = getCart(session);
    //        - 空ならエラーメッセージを session.setAttribute("regiError", "カートが空です。") して
    //          /regi へ sendRedirect → return
    //   4. OrderService を生成して completeOrder((Long) userId, cart) を呼ぶ
    //   5. 結果（OrderResult）を見て分岐
    //        ▽ 成功 (result.isSuccess()) の場合
    //            - session からカートを消す: session.removeAttribute(CART_SESSION_KEY)
    //            - 注文番号を session に積む: session.setAttribute("orderNumber", "ORD-" + result.getOrderId())
    //            - resp.sendRedirect(req.getContextPath() + "/order/complete")
    //              ← PRG の R 部分。これが無くリロードされると同じ POST が再実行されてしまう
    //            - return
    //        ▽ 失敗の場合
    //            - session.setAttribute("regiError", result.getMessage())
    //            - resp.sendRedirect(req.getContextPath() + "/regi") でレジ画面に戻す
    //
    // ▼ ポイント
    //   - 確定失敗の代表例は「在庫不足」。これは業務エラーなので例外ではなく失敗メッセージで戻すのが鉄則。
    //   - 成功時に必ずリダイレクト（forward じゃなくて sendRedirect）すること。
    //     forward だと URL が /order/complete に変わらず、リロードで二重注文の可能性が出てしまう。

    resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "OrderCompleteServlet#doPost is not implemented yet (Ch7-6)");
  }

  @SuppressWarnings("unchecked")
  private List<CartItemDTO> getCart(HttpSession session) {
    Object stored = session.getAttribute(CART_SESSION_KEY);
    if (stored instanceof List) {
      return (List<CartItemDTO>) stored;
    }
    // セッションに未作成の場合は空カートとして扱う。
    return new ArrayList<>();
  }
}
