package com.workshop.WhatsAppPackage;


public class BookingDetails {
    private String bookingId;
    private String customerName;
    private String pickupLocation;
    private String dropLocation;
    private String date;
    private String time;
    private String tripType;
    private String price;

    public BookingDetails() {}

    public BookingDetails(String bookingId, String customerName, String pickupLocation,
                          String dropLocation, String date, String time, String tripType, String price) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.date = date;
        this.time = time;
        this.tripType = tripType;
        this.price = price;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTripType() { return tripType; }
    public void setTripType(String tripType) { this.tripType = tripType; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
}

