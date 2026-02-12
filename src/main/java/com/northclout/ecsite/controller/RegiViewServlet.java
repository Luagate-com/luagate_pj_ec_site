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

@WebServlet("/regi")
public class RegiViewServlet extends HttpServlet {
  private static final String CART_SESSION_KEY = "cart";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      // 未ログインの場合はログイン画面へ戻す。
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    List<CartItemDTO> cart = getCart(session);
    if (cart.isEmpty()) {
      // カートが空の場合はカート画面に戻す。
      session.setAttribute("cartError", "カートに商品がありません。");
      resp.sendRedirect(req.getContextPath() + "/cart");
      return;
    }

    List<Long> ids = cart.stream()
        .map(CartItemDTO::getGoodId)
        .collect(Collectors.toList());

    Map<Long, GoodDTO> goodsMap = new HashMap<>();
    GoodDAO goodDAO = new GoodDAO();
    for (GoodDTO good : goodDAO.findByIds(ids)) {
      goodsMap.put(good.getId(), good);
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

    UserDAO userDAO = new UserDAO();
    Optional<UserDTO> user = userDAO.findById((Long) userId);
    user.ifPresent(value -> req.setAttribute("user", value));

    Object regiError = session.getAttribute("regiError");
    if (regiError != null) {
      req.setAttribute("regiError", regiError);
      session.removeAttribute("regiError");
    }

    req.setAttribute("items", viewItems);
    req.setAttribute("total", total);

    req.getRequestDispatcher("/WEB-INF/jsp/regi.jsp").forward(req, resp);
  }

  @SuppressWarnings("unchecked")
  private List<CartItemDTO> getCart(HttpSession session) {
    Object stored = session.getAttribute(CART_SESSION_KEY);
    if (stored instanceof List) {
      return (List<CartItemDTO>) stored;
    }
    return new ArrayList<>();
  }
}
