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

@WebServlet("/mypage/password")
public class MypagePasswordServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    Object error = session.getAttribute("passwordError");
    if (error != null) {
      req.setAttribute("passwordError", error);
      session.removeAttribute("passwordError");
    }

    req.getRequestDispatcher("/WEB-INF/jsp/mypage_password.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    String currentPassword = req.getParameter("currentPassword");
    String newPassword = req.getParameter("newPassword");
    String newPasswordConfirm = req.getParameter("newPasswordConfirm");

    if (ValidationUtil.isBlank(currentPassword)
        || ValidationUtil.isBlank(newPassword)
        || ValidationUtil.isBlank(newPasswordConfirm)
        || !newPassword.equals(newPasswordConfirm)) {
      session.setAttribute("passwordError", "入力内容を確認してください。");
      resp.sendRedirect(req.getContextPath() + "/mypage/password");
      return;
    }

    UserDAO dao = new UserDAO();
    Optional<UserDTO> userOpt = dao.findById((Long) userId);
    if (userOpt.isEmpty()) {
      session.setAttribute("passwordError", "ユーザー情報が取得できません。");
      resp.sendRedirect(req.getContextPath() + "/mypage/password");
      return;
    }

    UserDTO user = userOpt.get();
    if (!PasswordUtil.hash(currentPassword).equals(user.getPasswordHash())) {
      session.setAttribute("passwordError", "現在のパスワードが正しくありません。");
      resp.sendRedirect(req.getContextPath() + "/mypage/password");
      return;
    }

    dao.updatePassword(user.getId(), PasswordUtil.hash(newPassword));
    resp.sendRedirect(req.getContextPath() + "/mypage");
  }
}
