package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.UserDAO;
import com.northclout.ecsite.dto.UserDTO;
import com.northclout.ecsite.util.PasswordUtil;
import com.northclout.ecsite.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    if (!ValidationUtil.isEmail(email) || ValidationUtil.isBlank(password)) {
      req.setAttribute("loginError", "メールアドレスまたはパスワードが正しくありません。");
      req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
      return;
    }

    UserDAO dao = new UserDAO();
    Optional<UserDTO> userOpt = dao.findByEmail(email);
    if (userOpt.isEmpty()) {
      req.setAttribute("loginError", "メールアドレスまたはパスワードが正しくありません。");
      req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
      return;
    }

    UserDTO user = userOpt.get();
    String hashed = PasswordUtil.hash(password);
    if (!hashed.equals(user.getPasswordHash())) {
      req.setAttribute("loginError", "メールアドレスまたはパスワードが正しくありません。");
      req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
      return;
    }

    HttpSession session = req.getSession();
    session.setAttribute("userId", user.getId());
    resp.sendRedirect(req.getContextPath() + "/goods");
  }
}
