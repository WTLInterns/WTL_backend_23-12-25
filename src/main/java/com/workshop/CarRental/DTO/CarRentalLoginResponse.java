package com.workshop.CarRental.DTO;

public class CarRentalLoginResponse {
    

    private String message;
    private int id;



    private String username;

    private String role;

    public CarRentalLoginResponse(){
        super();
    }

    public CarRentalLoginResponse(String message,String username, String role,int id) {
        this.username = username;
        this.role = role;
        this.message=message;
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    
    

    
}