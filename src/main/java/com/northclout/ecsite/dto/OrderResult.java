package com.northclout.ecsite.dto;

public class OrderResult {
  private final boolean success;
  private final String message;
  private final Long orderId;

  private OrderResult(boolean success, String message, Long orderId) {
    this.success = success;
    this.message = message;
    this.orderId = orderId;
  }

  public static OrderResult success(Long orderId) {
    return new OrderResult(true, null, orderId);
  }

  public static OrderResult failure(String message) {
    return new OrderResult(false, message, null);
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

  public Long getOrderId() {
    return orderId;
  }
}
