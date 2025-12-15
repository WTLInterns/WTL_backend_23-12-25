package com.workshop.CarRental.DTO;

public class DriverLoginResponse {
    private boolean success;
    private String message;
    private Integer driverId;
    private String driverName;
    private String role;

    // Constructors
    public DriverLoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public DriverLoginResponse(boolean success, String message, 
                             Integer driverId, String driverName, String role) {
        this.success = success;
        this.message = message;
        this.driverId = driverId;
        this.driverName = driverName;
        this.role = role;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Integer getDriverId() { return driverId; }
    public String getDriverName() { return driverName; }
    public String getRole() { return role; }
}