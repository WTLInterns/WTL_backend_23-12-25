package com.workshop.CarRental.DTO;
public class TripResponse {
    private String tripId;
    private String userId;
    private String driverId;

    public TripResponse(String tripId, String userId, String driverId) {
        this.tripId = tripId;
        this.userId = userId;
        this.driverId = driverId;
    }

    public TripResponse(){


































        
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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