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
    // TODO Ch7-5: ログイン処理を実装する
    //
    //   実装の流れ:
    //   1) フォームの入力値を取得
    //      - req.getParameter("email") / req.getParameter("password")
    //
    //   2) 入力バリデーション
    //      - ValidationUtil.isEmail(email) が false、または ValidationUtil.isBlank(password) が true なら
    //        エラーとして扱う
    //      - エラー時は req.setAttribute("loginError", "メールアドレスまたはパスワードが正しくありません。")
    //        を設定し、/WEB-INF/jsp/login.jsp に forward して return
    //      - セキュリティ観点から、メール未登録 / パスワード不一致 / 形式不正は全て
    //        同じメッセージにする (ユーザー存在の有無を漏らさないため)
    //
    //   3) DB からユーザーを検索
    //      - new UserDAO().findByEmail(email) で Optional<UserDTO> を取得
    //      - 見つからない (isEmpty()) 場合は 2) と同じエラー処理
    //
    //   4) パスワード検証
    //      - PasswordUtil.hash(password) で入力値をハッシュ化
    //      - DB の user.getPasswordHash() と equals で比較
    //      - 不一致なら 2) と同じエラー処理
    //
    //   5) ログイン成功
    //      - HttpSession session = req.getSession();
    //      - session.setAttribute("userId", user.getId()); でセッションに ID を保持
    //      - resp.sendRedirect(req.getContextPath() + "/goods"); で商品一覧へ
    //
    //   参考:
    //   - HttpServletRequest#getParameter
    //   - RequestDispatcher#forward と HttpServletResponse#sendRedirect の違い
    //   - HttpSession でログイン状態を表現する一般的なパターン

    // 仮実装: 受講生が実装するまでログインは常に失敗扱いにする
    req.setAttribute("loginError", "ログイン処理は未実装です (Ch7-5)");
    req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
  }
}
