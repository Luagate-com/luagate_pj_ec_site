package com.northclout.ecsite.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * ログイン必須ページ (/regi, /mypage, /mypage/password) のアクセスを一元的にチェックする Filter。
 *
 * Servlet が呼ばれる前に doFilter が走る → 未ログインなら /login にリダイレクト → 各 Servlet では userId が必ず入っている前提で書ける、というのが Filter パターンの強み。
 */
@WebFilter(urlPatterns = {"/regi", "/mypage", "/mypage/password"})
public class AuthFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    // TODO Ch7-5 Step 10: ログイン状態をチェックして未ログインなら /login にリダイレクトする
    //  ヒント:
    //   1. request を HttpServletRequest にキャスト、response を HttpServletResponse にキャスト
    //   2. req.getSession(false) でセッションを取得 (false にするのは「無ければ作らない」ため)
    //   3. session が null か session.getAttribute("userId") が null なら未ログイン
    //   4. 未ログインなら resp.sendRedirect(req.getContextPath() + "/login") して return
    //   5. ログイン済みなら chain.doFilter(request, response) で次のフィルタ / Servlet に処理を渡す
    chain.doFilter(request, response);
  }
}
