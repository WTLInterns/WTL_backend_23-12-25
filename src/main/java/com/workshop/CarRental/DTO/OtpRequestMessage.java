package com.workshop.CarRental.DTO;

public class OtpRequestMessage {
    private int bookingId;
    private int driverId;

    public OtpRequestMessage() { 
    }

    public OtpRequestMessage(int bookingId, int driverId) {
        this.bookingId = bookingId;
        this.driverId = driverId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }
}