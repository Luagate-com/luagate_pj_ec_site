package com.northclout.ecsite.dto;

public class UserDTO {
  private long id;
  private String email;
  private String lastName;
  private String firstName;
  private String address;
  private String cardNumberLast4;
  private String cardBrand;
  private Integer cardExpMonth;
  private Integer cardExpYear;
  private String cardName;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCardNumberLast4() {
    return cardNumberLast4;
  }

  public void setCardNumberLast4(String cardNumberLast4) {
    this.cardNumberLast4 = cardNumberLast4;
  }

  public String getCardBrand() {
    return cardBrand;
  }

  public void setCardBrand(String cardBrand) {
    this.cardBrand = cardBrand;
  }

  public Integer getCardExpMonth() {
    return cardExpMonth;
  }

  public void setCardExpMonth(Integer cardExpMonth) {
    this.cardExpMonth = cardExpMonth;
  }

  public Integer getCardExpYear() {
    return cardExpYear;
  }

  public void setCardExpYear(Integer cardExpYear) {
    this.cardExpYear = cardExpYear;
  }

  public String getCardName() {
    return cardName;
  }

  public void setCardName(String cardName) {
    this.cardName = cardName;
  }
}
