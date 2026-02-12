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
    String idParam = req.getParameter("id");
    if (!ValidationUtil.isLong(idParam)) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    long id = Long.parseLong(idParam);
    GoodDAO dao = new GoodDAO();
    Optional<GoodDTO> goodOpt = dao.findById(id);

    if (goodOpt.isEmpty()) {
      resp.sendError(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    req.setAttribute("good", goodOpt.get());
    req.getRequestDispatcher("/WEB-INF/jsp/good_detail.jsp").forward(req, resp);
  }
}
