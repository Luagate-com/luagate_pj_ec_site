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

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String lastName = req.getParameter("lastName");
    String firstName = req.getParameter("firstName");
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String passwordConfirm = req.getParameter("passwordConfirm");
    String agree = req.getParameter("agree");

    // 会員登録の必須入力と整合性チェックをまとめて行う。
    if (ValidationUtil.isBlank(lastName)
        || ValidationUtil.isBlank(firstName)
        || !ValidationUtil.isEmail(email)
        || ValidationUtil.isBlank(password)
        || ValidationUtil.isBlank(passwordConfirm)
        || !password.equals(passwordConfirm)
        || agree == null) {
      req.setAttribute("signupError", "入力内容を確認してください。");
      req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
      return;
    }

    UserDAO dao = new UserDAO();
    Optional<UserDTO> existing = dao.findByEmail(email);
    if (existing.isPresent()) {
      req.setAttribute("signupError", "このメールアドレスは既に登録されています。");
      req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
      return;
    }

    UserDTO user = new UserDTO();
    user.setEmail(email);
    user.setPasswordHash(PasswordUtil.hash(password));
    user.setLastName(lastName.trim());
    user.setFirstName(firstName.trim());
    // 現仕様では住所入力を扱わないためNULL保存とする。
    user.setAddress(null);

    long userId = dao.insertUser(user);

    HttpSession session = req.getSession();
    // 登録完了後は自動ログイン状態にする。
    session.setAttribute("userId", userId);
    resp.sendRedirect(req.getContextPath() + "/goods");
  }

}
