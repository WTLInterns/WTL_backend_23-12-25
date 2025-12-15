package com.workshop.CarRental.DTO;

public class TripRequest {
    private String userId;
    
    private String driverId;


    public TripRequest(){
        super();
    }

    public TripRequest(String userId, String driverId) {
        this.userId = userId;
        this.driverId = driverId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


    
}