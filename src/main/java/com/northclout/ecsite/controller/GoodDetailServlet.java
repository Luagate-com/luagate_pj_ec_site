package com.northclout.ecsite.controller;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/goods/detail")
public class GoodDetailServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Ch7-3: 商品詳細画面の表示処理
    //  ヒント:
    //   1. req.getParameter("id") で id 文字列を取得し、ValidationUtil.isLong で数値かチェックする
    //   2. Long.parseLong で long 化し、GoodDAO.findById で Optional<GoodDTO> を取得する
    //   3. Optional が空なら 404、値があれば req.setAttribute("good", ...) して
    //      "/WEB-INF/jsp/good_detail.jsp" に forward する
    //   4. 見つからない場合のサンプルとして 404 だけ書いてある（下記）。完成時はこの 1 行を置き換える
    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
  }
}
