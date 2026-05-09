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
    // ログインガード（このブロックはそのまま残す）
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // session に残った passwordError を req に詰め替える（PRG パターンの後始末）
    Object error = session.getAttribute("passwordError");
    if (error != null) {
      req.setAttribute("passwordError", error);
      session.removeAttribute("passwordError");
    }

    // パスワード変更フォーム表示
    req.getRequestDispatcher("/WEB-INF/jsp/mypage_password.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // ログインガード（このブロックはそのまま残す）
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // TODO Ch7-7: パスワード変更処理を実装する
    // ヒント:
    //   1. req.getParameter で currentPassword / newPassword / newPasswordConfirm を取得
    //   2. バリデーション（どれかが空 or newPassword と newPasswordConfirm が一致しない場合）
    //      → session.setAttribute("passwordError", "入力内容を確認してください。")
    //         して /mypage/password に redirect & return
    //      ※ ValidationUtil.isBlank が使える
    //   3. UserDAO.findById((Long) userId) でユーザー取得
    //      取れなかったら同様に passwordError をセットして redirect
    //   4. 現在のパスワード照合
    //      PasswordUtil.hash(currentPassword) と user.getPasswordHash() を equals 比較する
    //      （平文同士ではなく「ハッシュ同士」で比較するのがポイント）
    //      不一致なら "現在のパスワードが正しくありません。" でエラー
    //   5. 全部 OK なら UserDAO.updatePassword(userId, PasswordUtil.hash(newPassword))
    //   6. /mypage に redirect（PRG パターン）
  }
}
