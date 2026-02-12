package com.northclout.ecsite.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/order/complete")
public class OrderCompleteViewServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object orderNumber = session.getAttribute("orderNumber");
    if (orderNumber != null) {
      req.setAttribute("orderNumber", orderNumber.toString());
      session.removeAttribute("orderNumber");
    } else {
      req.setAttribute("orderNumber", "#ORD-240130-001");
    }
    req.getRequestDispatcher("/WEB-INF/jsp/order_complete.jsp").forward(req, resp);
  }
}
