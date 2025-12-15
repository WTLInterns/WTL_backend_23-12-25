package com.workshop.DTO;

public class B2BLoginRequest {
    
    private String gmail;
    private String password;

    public B2BLoginRequest() {
    }

    public B2BLoginRequest(String gmail, String password) {
        this.gmail = gmail;
        this.password = password;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
