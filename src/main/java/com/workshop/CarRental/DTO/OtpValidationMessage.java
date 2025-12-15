package com.workshop.CarRental.DTO;

public class OtpValidationMessage {
    private int bookingId;
    private int driverId;
    private String otp;

    public OtpValidationMessage() {
    }

    public OtpValidationMessage(int bookingId, int driverId, String otp) {
        this.bookingId = bookingId;
        this.driverId = driverId;
        this.otp = otp;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}