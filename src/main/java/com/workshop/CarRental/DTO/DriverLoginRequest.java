package com.workshop.CarRental.DTO;

public class DriverLoginRequest {
    // @NotBlank(message = "Contact number is required")
    // @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be 10 digits")
    private String contactNo;
    
    // @NotBlank(message = "Password is required")
    private String password;

    // Getters and Setters
    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}