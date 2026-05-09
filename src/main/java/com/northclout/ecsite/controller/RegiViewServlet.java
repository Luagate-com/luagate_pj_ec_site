package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dao.UserDAO;
import com.northclout.ecsite.dto.CartItemDTO;
import com.northclout.ecsite.dto.CartItemViewDTO;
import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * /regi - レジ画面（注文確認画面）の表示用サーブレット。
 *
 * Ch7-6 の入口。注文確定ボタンを押す前の「最終確認画面」を作る役割。
 * 実際の注文確定（DB書き込み）は OrderCompleteServlet#doPost が担当する。
 */
@WebServlet("/regi")
public class RegiViewServlet extends HttpServlet {
  private static final String CART_SESSION_KEY = "cart";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-6: レジ画面を表示するための前処理を実装する。
    //
    // ▼ やること（順番通り）
    //   1. セッションを取り出す: HttpSession session = req.getSession();
    //   2. ログインチェック
    //        - session.getAttribute("userId") が null なら未ログイン
    //        - その場合は resp.sendRedirect(req.getContextPath() + "/login") で /login に飛ばして return
    //   3. カート取得: List<CartItemDTO> cart = getCart(session);
    //        - 空なら "cartError" メッセージを session に積んで /cart へ sendRedirect → return
    //   4. cart から goodId のリストを作り、GoodDAO.findByIds(ids) で商品マスタを引く
    //        - id をキーに引ける Map<Long, GoodDTO> goodsMap を作っておくと後段が楽
    //   5. cart をループして CartItemViewDTO のリストを作りつつ合計金額 total を集計
    //        - subtotal = good.getPrice() * item.getQuantity()
    //        - new CartItemViewDTO(good, item.getQuantity(), subtotal)
    //   6. UserDAO.findById((Long) userId) でユーザー情報を取得し req.setAttribute("user", value)
    //   7. session に "regiError"（前回の確定失敗メッセージ）が残っていたら
    //        - req.setAttribute("regiError", ...) に詰め替えて
    //        - session.removeAttribute("regiError") で消す（PRG的に1回限りの表示）
    //   8. req.setAttribute("items", viewItems) と req.setAttribute("total", total)
    //   9. req.getRequestDispatcher("/WEB-INF/jsp/regi.jsp").forward(req, resp);
    //
    // ▼ ポイント
    //   - JSPで参照している EL: ${user.*} ${items} ${total} ${regiError} ${item.good.*} ${item.quantity}
    //     これらが全部 setAttribute されていれば画面が出る。
    //   - "userId" は Long で session に入っている前提。キャストは (Long) userId。

    // 暫定: 何もせず空画面を出すと NullPointerException になるので、エラー表示にしておく。
    resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "RegiViewServlet#doGet is not implemented yet (Ch7-6)");
  }

  @SuppressWarnings("unchecked")
  private List<CartItemDTO> getCart(HttpSession session) {
    Object stored = session.getAttribute(CART_SESSION_KEY);
    if (stored instanceof List) {
      return (List<CartItemDTO>) stored;
    }
    // セッションにカートが無い場合は空として扱う。
    return new ArrayList<>();
  }
}
