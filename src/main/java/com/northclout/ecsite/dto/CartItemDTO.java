package com.northclout.ecsite.dto;

public class CartItemDTO {
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
