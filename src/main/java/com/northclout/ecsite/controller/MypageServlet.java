package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.UserDAO;
import com.northclout.ecsite.dto.UserDTO;
import com.northclout.ecsite.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/mypage")
public class MypageServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    UserDAO dao = new UserDAO();
    Optional<UserDTO> user = dao.findById((Long) userId);
    user.ifPresent(value -> req.setAttribute("user", value));

    Object error = session.getAttribute("mypageError");
    if (error != null) {
      req.setAttribute("mypageError", error);
      session.removeAttribute("mypageError");
    }

    req.getRequestDispatcher("/WEB-INF/jsp/mypage.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String override = req.getParameter("_method");
    if (override == null || !"PUT".equalsIgnoreCase(override)) {
      resp.sendRedirect(req.getContextPath() + "/mypage");
      return;
    }

    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    String lastName = req.getParameter("lastName");
    String firstName = req.getParameter("firstName");
    String address = req.getParameter("address");
    String cardBrand = req.getParameter("cardBrand");
    String cardLast4 = req.getParameter("cardLast4");
    String cardExpMonth = req.getParameter("cardExpMonth");
    String cardExpYear = req.getParameter("cardExpYear");
    String cardName = req.getParameter("cardName");

    UserDTO user = new UserDTO();
    user.setId((Long) userId);
    user.setLastName(normalizeToEmpty(lastName));
    user.setFirstName(normalizeToEmpty(firstName));
    user.setAddress(address);
    user.setCardBrand(blankToNull(cardBrand));
    user.setCardNumberLast4(blankToNull(cardLast4));
    user.setCardName(blankToNull(cardName));

    if (ValidationUtil.isPositiveInt(cardExpMonth, 1, 12)) {
      user.setCardExpMonth(Integer.parseInt(cardExpMonth));
    }
    if (ValidationUtil.isPositiveInt(cardExpYear, 0, 99)) {
      int yy = Integer.parseInt(cardExpYear);
      user.setCardExpYear(2000 + yy);
    }

    UserDAO dao = new UserDAO();
    dao.updateUser(user);

    resp.sendRedirect(req.getContextPath() + "/mypage");
  }

  private String blankToNull(String value) {
    return ValidationUtil.isBlank(value) ? null : value.trim();
  }

  private String normalizeToEmpty(String value) {
    return ValidationUtil.isBlank(value) ? "" : value.trim();
  }
}
