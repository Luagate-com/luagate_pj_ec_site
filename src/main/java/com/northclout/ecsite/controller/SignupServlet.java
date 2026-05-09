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
    // TODO Ch7-5: 新規会員登録処理を実装する
    //
    //   実装の流れ:
    //   1) フォーム入力値の取得
    //      - lastName, firstName, email, password, passwordConfirm, agree (利用規約同意のチェックボックス)
    //      - すべて req.getParameter("...") で取得
    //
    //   2) バリデーション (1つでも NG なら入力エラー)
    //      - lastName / firstName: ValidationUtil.isBlank が true なら NG
    //      - email: ValidationUtil.isEmail が false なら NG
    //      - password / passwordConfirm: isBlank で必須チェック + password.equals(passwordConfirm)
    //      - agree: チェック未入力だと req.getParameter は null になる → null なら NG
    //      - エラー時:
    //          req.setAttribute("signupError", "入力内容を確認してください。");
    //          req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
    //          return;
    //
    //   3) メールアドレス重複チェック
    //      - new UserDAO().findByEmail(email) で既存ユーザーを検索
    //      - isPresent() なら既に登録済み
    //          req.setAttribute("signupError", "このメールアドレスは既に登録されています。");
    //          を設定して signup.jsp へ forward
    //
    //   4) ユーザー作成
    //      - UserDTO user = new UserDTO();
    //      - user.setEmail(email);
    //      - user.setPasswordHash(PasswordUtil.hash(password));   // ★平文を保存しない
    //      - user.setLastName(lastName.trim());
    //      - user.setFirstName(firstName.trim());
    //      - user.setAddress(null);   // 住所は本章では扱わないので null
    //      - long userId = dao.insertUser(user);
    //
    //   5) 自動ログイン → 商品一覧へリダイレクト
    //      - HttpSession session = req.getSession();
    //      - session.setAttribute("userId", userId);
    //      - resp.sendRedirect(req.getContextPath() + "/goods");
    //
    //   参考:
    //   - PasswordUtil.hash の実装 (SHA-256 等) を読み、平文保存の危険性を理解しておくこと
    //   - 重複チェックは「DB の UNIQUE 制約」と「アプリ側の事前チェック」の二段構えが基本

    // 仮実装: 受講生が実装するまで登録は失敗扱いにする
    req.setAttribute("signupError", "会員登録処理は未実装です (Ch7-5)");
    req.getRequestDispatcher("/WEB-INF/jsp/signup.jsp").forward(req, resp);
  }

}
