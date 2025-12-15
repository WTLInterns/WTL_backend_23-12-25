package com.workshop.CarRental.Entity;

public class TripStatusMessage {
    private String bookingId;
    private String action; // "SEND_OTP", "VERIFY_OTP", "START_TRIP", "END_TRIP"
    private String type; // Used for client-side processing
    private String otp;
    private Double startOdometer;
    private Double endOdometer;
    private String destination;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private Long driverId;
    private Long userId;
    
    public String getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getOtp() {
        return otp;
    }
    
    public void setOtp(String otp) {
        this.otp = otp;
    }
    
    public Double getStartOdometer() {
        return startOdometer;
    }
    
    public void setStartOdometer(Double startOdometer) {
        this.startOdometer = startOdometer;
    }
    
    public Double getEndOdometer() {
        return endOdometer;
    }
    
    public void setEndOdometer(Double endOdometer) {
        this.endOdometer = endOdometer;
    }
    
    public String getDestination() {
        return destination;
    }
    
    public void setDestination(String destination) {
        this.destination = destination;
    }
    
    public Double getDestinationLatitude() {
        return destinationLatitude;
    }
    
    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }
    
    public Double getDestinationLongitude() {
        return destinationLongitude;
    }
    
    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
    
    public Long getDriverId() {
        return driverId;
    }
    
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
} 