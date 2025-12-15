package com.workshop.CarRental.DTO;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Trip {

    @Id
    private String tripId;
    private String userId;
    private String driverId;
    private TripStatus status;
    private LocalDateTime createdAt;
    public Trip(String tripId, String userId, String driverId, LocalDateTime createdAt, TripStatus status) {
        this.tripId = tripId;
        this.userId = userId;
        this.driverId = driverId;
        this.createdAt = createdAt;
        this.status=status;
    }

    public Trip(){
        super();
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
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    
    
    public enum TripStatus {
        REQUESTED, ACCEPTED, IN_PROGRESS, COMPLETED, CANCELLED
    }



    public TripStatus getStatus() {
        return status;
    }
    public void setStatus(TripStatus status) {
        this.status = status;
    }

    



}