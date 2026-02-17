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
    HttpSession session = req.getSession();
    List<CartItemDTO> cart = getCart(session);

    List<Long> ids = cart.stream()
        .map(CartItemDTO::getGoodId)
        .collect(Collectors.toList());

    Map<Long, GoodDTO> goodsMap = new HashMap<>();
    if (!ids.isEmpty()) {
      GoodDAO dao = new GoodDAO();
      List<GoodDTO> goods = dao.findByIds(ids);
      for (GoodDTO good : goods) {
        goodsMap.put(good.getId(), good);
      }
    }

    List<CartItemViewDTO> viewItems = new ArrayList<>();
    int total = 0;
    for (CartItemDTO item : cart) {
      GoodDTO good = goodsMap.get(item.getGoodId());
      if (good == null) {
        continue;
      }
      int subtotal = good.getPrice() * item.getQuantity();
      total += subtotal;
      viewItems.add(new CartItemViewDTO(good, item.getQuantity(), subtotal));
    }

    Object error = session.getAttribute("cartError");
    if (error != null) {
      req.setAttribute("cartError", error);
      session.removeAttribute("cartError");
    }

    req.setAttribute("items", viewItems);
    req.setAttribute("total", total);

    req.getRequestDispatcher("/WEB-INF/jsp/cart.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // HTMLフォームからPUT/DELETEを扱うため、隠しフィールドで疑似メソッドを受け取る。
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
    HttpSession session = req.getSession();
    String goodIdParam = req.getParameter("goodId");
    String quantityParam = req.getParameter("quantity");

    // 数量と商品IDの基本バリデーションはここで統一する。
    if (!ValidationUtil.isLong(goodIdParam) || !ValidationUtil.isPositiveInt(quantityParam, 1, 99)) {
      session.setAttribute("cartError", "数量は1〜99で入力してください。");
      resp.sendRedirect(req.getContextPath() + "/cart");
      return;
    }

    long goodId = Long.parseLong(goodIdParam);
    int quantity = Integer.parseInt(quantityParam);

    List<CartItemDTO> cart = getCart(session);
    boolean updated = false;
    for (CartItemDTO item : cart) {
      if (item.getGoodId() == goodId) {
        int nextQuantity = item.getQuantity() + quantity;
        item.setQuantity(Math.min(nextQuantity, 99));
        updated = true;
        break;
      }
    }
    if (!updated) {
      cart.add(new CartItemDTO(goodId, quantity));
    }

    session.setAttribute(CART_SESSION_KEY, cart);
    resp.sendRedirect(req.getContextPath() + "/cart");
  }

  private void handleUpdate(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession();
    String goodIdParam = req.getParameter("goodId");
    String quantityParam = req.getParameter("quantity");

    // 更新時も同じルールで数値チェックを行う。
    if (!ValidationUtil.isLong(goodIdParam) || !ValidationUtil.isPositiveInt(quantityParam, 1, 99)) {
      session.setAttribute("cartError", "数量は1〜99で入力してください。");
      resp.sendRedirect(req.getContextPath() + "/cart");
      return;
    }

    long goodId = Long.parseLong(goodIdParam);
    int quantity = Integer.parseInt(quantityParam);

    List<CartItemDTO> cart = getCart(session);
    for (CartItemDTO item : cart) {
      if (item.getGoodId() == goodId) {
        item.setQuantity(quantity);
        break;
      }
    }
    session.setAttribute(CART_SESSION_KEY, cart);
    resp.sendRedirect(req.getContextPath() + "/cart");
  }

  private void handleRemove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    HttpSession session = req.getSession();
    String goodIdParam = req.getParameter("goodId");

    if (!ValidationUtil.isLong(goodIdParam)) {
      resp.sendRedirect(req.getContextPath() + "/cart");
      return;
    }

    long goodId = Long.parseLong(goodIdParam);
    List<CartItemDTO> cart = getCart(session);
    cart.removeIf(item -> item.getGoodId() == goodId);
    session.setAttribute(CART_SESSION_KEY, cart);

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
}
