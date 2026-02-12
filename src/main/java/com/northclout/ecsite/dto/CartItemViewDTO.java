package com.northclout.ecsite.dto;

public class CartItemViewDTO {
  private GoodDTO good;
  private int quantity;
  private int subtotal;

  public CartItemViewDTO(GoodDTO good, int quantity, int subtotal) {
    this.good = good;
    this.quantity = quantity;
    this.subtotal = subtotal;
  }

  public GoodDTO getGood() {
    return good;
  }

  public int getQuantity() {
    return quantity;
  }

  public int getSubtotal() {
    return subtotal;
  }
}
