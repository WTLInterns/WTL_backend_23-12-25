package com.workshop.DTO;

public class VendorLoginResponse {

    private Long vendorId;
    private String email;
    private String password;

    // Constructor to initialize the response
    public VendorLoginResponse(Long vendorId, String email, String password) {
        this.vendorId = vendorId;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
