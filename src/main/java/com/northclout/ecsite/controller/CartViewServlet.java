package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dto.CartItemDTO;
import com.northclout.ecsite.dto.CartItemViewDTO;
import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.util.ValidationUtil;

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
import java.util.stream.Collectors;

@WebServlet("/cart")
public class CartViewServlet extends HttpServlet {
  private static final String CART_SESSION_KEY = "cart";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-4:
    //   1. session から getCart(session) でカート（List<CartItemDTO>）を取り出す
    //   2. カート内の goodId をまとめて GoodDAO.findByIds で引き、Map<Long, GoodDTO> を作る
    //      （カートはセッションにIDと数量しか持っていないので、表示用の名前・価格・画像はここでDBから補完する）
    //   3. CartItemDTO と GoodDTO を組み合わせて CartItemViewDTO のリストを作り、合計金額 total を計算する
    //   4. session に "cartError" があれば取り出して req にセットし、セッションからは消す（Flashメッセージ的な扱い）
    //   5. req に "items" と "total" をセットして /WEB-INF/jsp/cart.jsp に forward する
    req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // HTMLフォームからPUT/DELETEを扱うため、隠しフィールド _method で疑似メソッドを受け取る。
    // _method の値で add / update / remove を振り分ける。
    String override = req.getParameter("_method");
    if ("PUT".equalsIgnoreCase(override) || "PATCH".equalsIgnoreCase(override)) {
      handleUpdate(req, resp);
      return;
    }
    if ("DELETE".equalsIgnoreCase(override)) {
      handleRemove(req, resp);
      return;
    }
    handleAdd(req, resp);
  }

  private void handleAdd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // TODO Ch7-4 (add):
    //   - リクエストパラメータ goodId / quantity を取り出す
    //   - ValidationUtil で goodId が long、quantity が 1〜99 の正整数か検証する
    //   - 不正なら session に "cartError" をセットして /cart にリダイレクト
    //   - existsGood(goodId) で商品の存在も確認する
    //   - getCart(session) で取り出したリストに、同じ goodId があれば数量を加算（最大99）、
    //     なければ新しい CartItemDTO を add する
    //   - session に CART_SESSION_KEY で保存して /cart にリダイレクト
    resp.sendRedirect(req.getContextPath() + "/cart");
  }

  private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // TODO Ch7-4 (update):
    //   - goodId / quantity を取り出してバリデーション（add と同じルール）
    //   - getCart(session) のリストから一致する goodId の CartItemDTO を見つけて、数量を上書きする
    //   - session に保存して /cart にリダイレクト
    resp.sendRedirect(req.getContextPath() + "/cart");
  }

  private void handleRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // TODO Ch7-4 (remove):
    //   - goodId を取り出して long として妥当か検証
    //   - existsGood(goodId) も確認する
    //   - getCart(session) のリストから該当する CartItemDTO を removeIf で削除
    //   - session に保存して /cart にリダイレクト
    resp.sendRedirect(req.getContextPath() + "/cart");
  }

  @SuppressWarnings("unchecked")
  private List<CartItemDTO> getCart(HttpSession session) {
    Object stored = session.getAttribute(CART_SESSION_KEY);
    if (stored instanceof List) {
      return (List<CartItemDTO>) stored;
    }
    // セッション初回アクセス時は空リストを返して呼び出し側でそのまま追加できるようにする。
    return new ArrayList<>();
  }

  private boolean existsGood(long goodId) {
    // goodId は goods に存在する値のみ受け付ける。
    GoodDAO dao = new GoodDAO();
    return dao.findById(goodId).isPresent();
  }
}
