package com.northclout.ecsite.dto;

import java.io.Serializable;

public class CartItemDTO implements Serializable {
  private static final long serialVersionUID = 1L;

  private long goodId;
  private int quantity;

  public CartItemDTO(long goodId, int quantity) {
    this.goodId = goodId;
    this.quantity = quantity;
  }

  public long getGoodId() {
    return goodId;
  }

  public void setGoodId(long goodId) {
    this.goodId = goodId;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
