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
      "キッチン雑貨",
      "ファブリック",
      "食器",
      "インテリア",
      "収納・小物"
  );

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String category = req.getParameter("category");
    GoodDAO dao = new GoodDAO();
    List<GoodDTO> goods;

    if (category != null && !category.isBlank() && CATEGORIES.contains(category)) {
      goods = dao.findByCategory(category);
      req.setAttribute("selectedCategory", category);
    } else {
      goods = dao.findAll();
      req.setAttribute("selectedCategory", "");
    }

    req.setAttribute("categories", CATEGORIES);
    req.setAttribute("goods", goods);
    req.setAttribute("totalCount", goods.size());

    req.getRequestDispatcher("/WEB-INF/jsp/good_list.jsp").forward(req, resp);
  }
}
