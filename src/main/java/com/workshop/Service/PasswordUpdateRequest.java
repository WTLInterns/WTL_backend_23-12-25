package com.workshop.Service;

import org.springframework.stereotype.Service;

@Service
public class PasswordUpdateRequest {
  private String password;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  

}