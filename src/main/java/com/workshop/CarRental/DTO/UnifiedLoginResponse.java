package com.workshop.CarRental.DTO;

public class UnifiedLoginResponse {
    private String status;  // "success" or "failed"
    private String role;    // "USER" or "DRIVER"
    private Object data;    // Login data (user or driver)
    private int userId;
    private String username;
    private String message;

    // Getters and Setters

    public UnifiedLoginResponse(){
        super();
    }

    

    public UnifiedLoginResponse(String status, String role, Object data, int userId, String username,
            String message) {
        this.status = status;
        this.role = role;
        this.data = data;
        this.userId = userId;
        this.username = username;
        this.message = message;
    }



    public String getStatus() {
        return status;
    }

    

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



    public int getUserId() {
        return userId;
    }



    public void setUserId(int userId) {
        this.userId = userId;
    }



    public String getUsername() {
        return username;
    }



    public void setUsername(String username) {
        this.username = username;
    }



    public String getMessage() {
        return message;
    }



    public void setMessage(String message) {
        this.message = message;
    }

    
}
