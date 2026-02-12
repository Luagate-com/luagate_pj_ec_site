package com.northclout.ecsite.service;

import com.northclout.ecsite.dao.GoodDAO;
import com.northclout.ecsite.dao.OrderDAO;
import com.northclout.ecsite.dao.StockDAO;
import com.northclout.ecsite.dto.CartItemDTO;
import com.northclout.ecsite.dto.GoodDTO;
import com.northclout.ecsite.dto.OrderResult;
import com.northclout.ecsite.dto.StockDTO;
import com.northclout.ecsite.util.TransactionManager;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {
  public OrderResult completeOrder(long userId, List<CartItemDTO> cartItems) {
    if (cartItems == null || cartItems.isEmpty()) {
      return OrderResult.failure("カートが空です。");
    }

    return TransactionManager.execute(conn -> {
      GoodDAO goodDAO = new GoodDAO();
      StockDAO stockDAO = new StockDAO();
      OrderDAO orderDAO = new OrderDAO();

      List<Long> ids = cartItems.stream()
          .map(CartItemDTO::getGoodId)
          .collect(Collectors.toList());
      // 商品情報は注文時点の単価を保存するため必須。
      Map<Long, GoodDTO> goodsMap = new HashMap<>();
      for (GoodDTO good : goodDAO.findByIds(ids)) {
        goodsMap.put(good.getId(), good);
      }

      // 在庫チェック（FOR UPDATE で行ロックを取得）
      Map<Long, StockDTO> stockMap = new HashMap<>();
      for (CartItemDTO item : cartItems) {
        Optional<StockDTO> stockOpt = stockDAO.findByGoodIdForUpdate(conn, item.getGoodId());
        if (stockOpt.isEmpty()) {
          return OrderResult.failure("在庫が不足しています。");
        }
        StockDTO stock = stockOpt.get();
        if (stock.getQuantity() < item.getQuantity()) {
          return OrderResult.failure("在庫が不足しています。");
        }
        stockMap.put(item.getGoodId(), stock);
      }

      int total = 0;
      for (CartItemDTO item : cartItems) {
        GoodDTO good = goodsMap.get(item.getGoodId());
        if (good == null) {
          return OrderResult.failure("商品情報の取得に失敗しました。");
        }
        total += good.getPrice() * item.getQuantity();
      }

      long orderId = orderDAO.insertOrder(conn, userId, LocalDateTime.now(), total);

      for (CartItemDTO item : cartItems) {
        GoodDTO good = goodsMap.get(item.getGoodId());
        orderDAO.insertOrderItem(conn, orderId, item.getGoodId(), good.getPrice(), item.getQuantity());
        StockDTO stock = stockMap.get(item.getGoodId());
        int newQty = stock.getQuantity() - item.getQuantity();
        stockDAO.updateQuantity(conn, item.getGoodId(), newQty);
      }

      return OrderResult.success(orderId);
    });
  }
}
