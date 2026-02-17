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

@WebServlet("/order/complete")
public class OrderCompleteServlet extends HttpServlet {
  private static final String CART_SESSION_KEY = "cart";

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

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    List<CartItemDTO> cart = getCart(session);
    if (cart.isEmpty()) {
      session.setAttribute("regiError", "カートが空です。");
      resp.sendRedirect(req.getContextPath() + "/regi");
      return;
    }

    OrderService service = new OrderService();
    OrderResult result = service.completeOrder((Long) userId, cart);

    if (result.isSuccess()) {
      session.removeAttribute(CART_SESSION_KEY);
      session.setAttribute("orderNumber", "ORD-" + result.getOrderId());
      resp.sendRedirect(req.getContextPath() + "/order/complete");
      return;
    }

    session.setAttribute("regiError", result.getMessage());
    resp.sendRedirect(req.getContextPath() + "/regi");
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
