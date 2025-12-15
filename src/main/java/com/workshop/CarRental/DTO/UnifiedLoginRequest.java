package com.workshop.CarRental.DTO;

public class UnifiedLoginRequest {
    private String mobile;
    private String password;
    private String fcmToken;

    
    public UnifiedLoginRequest(){
        super();
    }

    public UnifiedLoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    // Getters and Setters
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}

