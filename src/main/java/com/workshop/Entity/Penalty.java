package com.workshop.Entity;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Penalty {

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pId;

    private String reason;

    private int amount;

    private LocalDate date;

    private String time;

    



    

@JsonIgnore
	@JsonBackReference
	@JoinColumn(name = "vendor_id")
	@ManyToOne
	private Vendor vendor;

    @OneToOne
    private Booking booking;
    
    public Penalty(int pId, String reason, int amount, LocalDate date, String time, Vendor vendor, Booking booking) {
        this.pId = pId;
        this.reason = reason;
        this.amount = amount;
        // this.vendorId = vendorId;
        // this.bookingId = bookingId;
        this.date=date;
        this.time=time;
        this.vendor=vendor;
        this.booking=booking;
    }

    public Penalty() {
        super();
    }

    

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // public int getBookingId() {
    //     return bookingId;
    // }

    // public void setBookingId(int bookingId) {
    //     this.bookingId = bookingId;
    // }

    // public Long getVendorId() {
    //     return vendorId;
    // }

    // public void setVendorId(Long vendorId) {
    //     this.vendorId = vendorId;
    // }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }



    

    
}
