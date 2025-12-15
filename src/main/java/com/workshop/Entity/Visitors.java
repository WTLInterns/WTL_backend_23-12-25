package com.workshop.Entity;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Visitors {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String dropLocation;
    private String pickupLocation;
    private String tripType;
    private LocalDate date;
    private LocalDate returnDate;
    private String baseAmount;
    private LocalDate visitDate;
    private String carType;
    private String name;
    private String phone;
    private String status;
    private Long path;

    public Visitors() {
        // Default constructor
    }

    public Visitors(int id, String dropLocation, String pickupLocation, String tripType, LocalDate date,
                    LocalDate returnDate, String baseAmount, LocalDate visitDate, String carType,
                    String name, String phone, String status, Long path) {
        this.id = id;
        this.dropLocation = dropLocation;
        this.pickupLocation = pickupLocation;
        this.tripType = tripType;
        this.date = date;
        this.returnDate = returnDate;
        this.baseAmount = baseAmount;
        this.visitDate = visitDate;
        this.carType = carType;
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.path = path;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPath() {
        return path;
    }

    public void setPath(Long path) {
        this.path = path;
    }
}
