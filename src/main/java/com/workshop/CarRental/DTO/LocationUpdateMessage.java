package com.workshop.CarRental.DTO;

public class LocationUpdateMessage {
    private int userId;
    private int bookingId;
    private String role;
    private Double latitude;
    private Double longitude;

    public LocationUpdateMessage(){

    }

    public LocationUpdateMessage(int userId, int bookingId, String role, Double latitude, Double longitude) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.role = role;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    // Constructors, getters, setters

    

    
}

