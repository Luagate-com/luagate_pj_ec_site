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
    // ログインガード（このブロックはそのまま残す）
    HttpSession session = req.getSession();
    Object userId = session.getAttribute("userId");
    if (userId == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }

    // TODO Ch7-7: マイページ画面の表示処理を実装する
    // ヒント:
    //   1. UserDAO の findById((Long) userId) で Optional<UserDTO> を取得する
    //   2. user が存在すれば req.setAttribute("user", value) で JSP に渡す
    //      （JSP 側で ${user.lastName} ${user.email} などとして表示される）
    //   3. session に "mypageError" が残っていれば req に詰め替えて
    //      session からは removeAttribute で消す（PRG パターンの後始末）
    //   4. 最後に /WEB-INF/jsp/mypage.jsp へ forward する
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-7: 会員情報の更新処理を実装する
    // ヒント:
    //   1. hidden の _method パラメータを取り、"PUT" でなければ /mypage に redirect して return
    //      （HTML フォームは GET/POST しか送れないので、擬似的に PUT を表現している）
    //   2. ログインガード（session.getAttribute("userId") == null なら /login へ）
    //   3. req.getParameter で lastName / firstName / address / cardBrand /
    //      cardLast4 / cardExpMonth / cardExpYear / cardName を受け取る
    //   4. UserDTO を組み立てる
    //      - id には (Long) userId をセット
    //      - 氏名は空文字に正規化（null や " " は ""）
    //      - カード関連は空ならば null にする
    //      - cardExpMonth / cardExpYear は ValidationUtil.isPositiveInt で範囲チェックしてから
    //        Integer.parseInt して setCardExpMonth / setCardExpYear（年は 2000 + yy）
    //   5. UserDAO.updateUser(user) で更新
    //   6. PRG パターンで /mypage に redirect する
  }
}
