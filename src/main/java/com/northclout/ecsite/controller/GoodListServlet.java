package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dto.GoodDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/goods")
public class GoodListServlet extends HttpServlet {
  private static final List<String> CATEGORIES = Arrays.asList(
      "インテリア",
      "キッチン雑貨",
      "ファブリック",
      "文具",
      "ファッション",
      "食器"
  );

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-3: 商品一覧画面の表示処理
    //  ヒント:
    //   1. req.getParameter("category") でカテゴリ絞り込みパラメータを取得する
    //   2. GoodDAO を new して、category が CATEGORIES に含まれていれば findByCategory、
    //      そうでなければ findAll で List<GoodDTO> を取得する
    //   3. req.setAttribute("categories", CATEGORIES) / "goods" / "selectedCategory" / "totalCount" を JSP に渡す
    //   4. 最後に good_list.jsp に forward する（forward 呼び出しは既に下に書いてある）

    req.getRequestDispatcher("/WEB-INF/jsp/good_list.jsp").forward(req, resp);
  }
}
