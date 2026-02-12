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
    String name = req.getParameter("name");
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String passwordConfirm = req.getParameter("passwordConfirm");
    String postal = req.getParameter("postal");
    String prefecture = req.getParameter("prefecture");
    String city = req.getParameter("city");
    String building = req.getParameter("building");
    String agree = req.getParameter("agree");

    if (ValidationUtil.isBlank(name)
        || !ValidationUtil.isEmail(email)
        || ValidationUtil.isBlank(password)
        || ValidationUtil.isBlank(passwordConfirm)
        || !password.equals(passwordConfirm)
        || ValidationUtil.isBlank(postal)
        || ValidationUtil.isBlank(prefecture)
        || ValidationUtil.isBlank(city)
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

    String[] nameParts = splitName(name);
    String address = String.join(" ",
        "〒" + postal,
        prefecture,
        city,
        building == null ? "" : building
    ).trim();

    UserDTO user = new UserDTO();
    user.setEmail(email);
    user.setPasswordHash(PasswordUtil.hash(password));
    user.setLastName(nameParts[0]);
    user.setFirstName(nameParts[1]);
    user.setAddress(address);

    long userId = dao.insertUser(user);

    HttpSession session = req.getSession();
    session.setAttribute("userId", userId);
    resp.sendRedirect(req.getContextPath() + "/goods");
  }

  // 氏名が「姓 名」形式でない場合は、姓のみ保存し名は空にする。
  private String[] splitName(String name) {
    String trimmed = name.trim();
    int spaceIndex = trimmed.indexOf(' ');
    if (spaceIndex < 0) {
      return new String[]{trimmed, ""};
    }
    String last = trimmed.substring(0, spaceIndex).trim();
    String first = trimmed.substring(spaceIndex + 1).trim();
    return new String[]{last, first};
  }
}
