package com.workshop.Entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Discount {
    
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
private int id;

private String couponCode;

private int priceDiscount;

private String isEnabled;

@JsonFormat(pattern = "yyyy-MM-dd")
private LocalDate expiryDate; // inclusive expiry date

public Discount(int id, String couponCode, int priceDiscount, String isEnabled, LocalDate expiryDate) {
    this.id = id;
    this.couponCode = couponCode;
    this.priceDiscount = priceDiscount;
    this.isEnabled = isEnabled;
    this.expiryDate = expiryDate;
}

public Discount(){
    super();
}

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getCouponCode() {
    return couponCode;
}

public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
}

public int getPriceDiscount() {
    return priceDiscount;
}

public void setPriceDiscount(int priceDiscount) {
    this.priceDiscount = priceDiscount;
}

public String getIsEnabled() {
    return isEnabled;
}

public void setIsEnabled(String isEnabled) {
    this.isEnabled = isEnabled;
}

public LocalDate getExpiryDate() {
    return expiryDate;
}

public void setExpiryDate(LocalDate expiryDate) {
    this.expiryDate = expiryDate;
}






}
