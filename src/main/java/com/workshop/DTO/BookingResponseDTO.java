package com.workshop.DTO;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workshop.Entity.Booking;


@JsonInclude(JsonInclude.Include.NON_NULL)
class BookingResponseDTO {

    private int id;
    private String fromLocation;
    private String toLocation;
    private String tripType;
    private LocalDate startDate;
    private LocalDate returnDate;
    private String time;
    private String distance;

    private String userId;
    private String bookingId;
    private String name;
    private String email;
    private String phone;
    private String userPickup;
    private String userDrop;

    private LocalDate date;
    private String userTripType;
    private String bookid;
    private String car;
    private String baseAmount;

    private Integer amount;
    private Integer status;
    private String driverBhata;
    private Integer nightCharges;
    private Integer gst;
    private Integer serviceCharge;
    private String offer;
    private Integer offerPartial;
    private String offerAmount;
    private String txnId;
    private String payment;
    private LocalDate dateEnd;
    private String timeEnd;
    private String bookingType;
    private String description;

    private VendorDTO vendor; // Nested DTO for Vendor (can be null)

    public BookingResponseDTO(Booking booking) {
        this.id = booking.getId();
        this.fromLocation = booking.getFromLocation();
        this.toLocation = booking.getToLocation();
        this.tripType = booking.getTripType();
        this.startDate = booking.getStartDate();
        this.returnDate = booking.getReturnDate();
        this.time = booking.getTime();
        this.distance = booking.getDistance();

        this.userId = booking.getUserId();
        this.bookingId = booking.getBookingId();
        this.name = booking.getName();
        this.email = booking.getEmail();
        this.phone = booking.getPhone();
        this.userPickup = booking.getUserPickup();
        this.userDrop = booking.getUserDrop();

        this.date = booking.getDate();
        this.userTripType = booking.getUserTripType();
        this.bookid = booking.getBookid();
        this.car = booking.getCar();
        this.baseAmount = booking.getBaseAmount();

        this.amount = booking.getAmount();
        this.status = booking.getStatus();
        this.driverBhata = booking.getDriverBhata();
        this.nightCharges = booking.getNightCharges();
        this.gst = booking.getGst();
        this.serviceCharge = booking.getServiceCharge();
        this.offer = booking.getOffer();
        this.offerPartial = booking.getOfferPartial();
        this.offerAmount = booking.getOfferAmount();
        this.txnId = booking.getTxnId();
        this.payment = booking.getPayment();
        this.dateEnd = booking.getDateEnd();
        this.timeEnd = booking.getTimeEnd();
        this.bookingType = booking.getBookingType();
        this.description = booking.getDescription();

        // If vendor is not null, map the vendor to VendorDTO
        if (booking.getVendor() != null) {
            this.vendor = new VendorDTO(booking.getVendor());
        }
    }

    // Getters and Setters
}
