package com.workshop.DTO;

import com.workshop.Entity.Booking;
import com.workshop.Entity.Vendor;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class BookingDTO {

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
    private String companyName;
    private String offerAmount;
    private String txnId;
    private String payment;
    private LocalDate dateEnd;
    private String timeEnd;
    private String bookingType;
    private String description;
    private VendorDTO vendor;
    private int collection;
    private VendorCabsDTO vendorCab;
    private VendorDriversDTO vendorDriver;
    private CabAdminDTO cabAdmin;
    private DriverAdminDTO driverAdmin;
    private String vendorDriverName;
    private String vendorCabName;
    private String cabPlateNo;;
        private int days;

    private String vendorName;
    private String masterAdminDriverName;
    private String masterAdminCabName;
    private String masterAdminCabNoPlate;

private String paymentMethod = "cash"; // cash, online, card

private String paymentType = "full"; // full, partial

private String paymentStatus = "pending"; // pending, paid, failed, cancelled

private Integer paymentAmount = 0;

private Integer remainingAmount = 0;

private String razorpayOrderId;

private String razorpaySignature;

private String razorpayPaymentId;




    public BookingDTO(){
        super();
    }
    // Constructor to map from Booking entity
    public BookingDTO(Booking booking) {
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
        this.paymentAmount=booking.getPaymentAmount();
        this.paymentMethod=booking.getPaymentMethod();
        this.paymentType=booking.getPaymentType();
        this.paymentStatus=booking.getPaymentStatus();
        this.remainingAmount=booking.getRemainingAmount();
        this.razorpayOrderId=booking.getRazorpayOrderId();
        this.razorpaySignature=booking.getRazorpaySignature();
        this.razorpayPaymentId=booking.getRazorpayPaymentId();
        this.gst = booking.getGst();
        this.serviceCharge = booking.getServiceCharge();
        this.days=booking.getDays();
        this.offer = booking.getOffer();
        this.offerPartial = booking.getOfferPartial();
        this.offerAmount = booking.getOfferAmount();
        this.txnId = booking.getTxnId();
        this.collection=booking.getCollection();
        this.payment = booking.getPayment();
        this.dateEnd = booking.getDateEnd();
        this.timeEnd = booking.getTimeEnd();
        this.bookingType = booking.getBookingType();
        this.description = booking.getDescription();
        

        // Map vendorCab and vendorDriver to their respective DTOs
        if (booking.getVendorCab() != null) {
            this.vendorCab = new VendorCabsDTO(booking.getVendorCab());
        }

        if (booking.getVendorDriver() != null) {
            this.vendorDriver = new VendorDriversDTO(booking.getVendorDriver());
        }

        if (booking.getVendor() != null) {
            this.vendor = new VendorDTO(booking.getVendor());
        }

        if(booking.getCabAdmin()!=null){
            this.cabAdmin = new CabAdminDTO(booking.getCabAdmin());
        }

        if(booking.getDriveAdmin()!=null){
            this.driverAdmin=new DriverAdminDTO(booking.getDriveAdmin());
        }
    }

    // Getters and setters...

    

    public int getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentType() {
        return paymentType;
    }
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public Integer getPaymentAmount() {
        return paymentAmount;
    }
    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    public Integer getRemainingAmount() {
        return remainingAmount;
    }
    public void setRemainingAmount(Integer remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }
    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }
    public String getRazorpaySignature() {
        return razorpaySignature;
    }
    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }
    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }
    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    public String getVendorDriverName() {
        return vendorDriverName;
    }
    public void setVendorDriverName(String vendorDriverName) {
        this.vendorDriverName = vendorDriverName;
    }
    public String getVendorCabName() {
        return vendorCabName;
    }
    public void setVendorCabName(String vendorCabName) {
        this.vendorCabName = vendorCabName;
    }
    public String getCabPlateNo() {
        return cabPlateNo;
    }
    public void setCabPlateNo(String cabPlateNo) {
        this.cabPlateNo = cabPlateNo;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public String getMasterAdminDriverName() {
        return masterAdminDriverName;
    }
    public void setMasterAdminDriverName(String masterAdminDriverName) {
        this.masterAdminDriverName = masterAdminDriverName;
    }
    public String getMasterAdminCabName() {
        return masterAdminCabName;
    }
    public void setMasterAdminCabName(String masterAdminCabName) {
        this.masterAdminCabName = masterAdminCabName;
    }
    public String getMasterAdminCabNoPlate() {
        return masterAdminCabNoPlate;
    }
    public void setMasterAdminCabNoPlate(String masterAdminCabNoPlate) {
        this.masterAdminCabNoPlate = masterAdminCabNoPlate;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFromLocation() 
    
    
    
    
    {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserPickup() {
        return userPickup;
    }

    public void setUserPickup(String userPickup) {
        this.userPickup = userPickup;
    }

    public String getUserDrop() {
        return userDrop;
    }

    public void setUserDrop(String userDrop) {
        this.userDrop = userDrop;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserTripType() {
        return userTripType;
    }

    public void setUserTripType(String userTripType) {
        this.userTripType = userTripType;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDriverBhata() {
        return driverBhata;
    }

    public void setDriverBhata(String driverBhata) {
        this.driverBhata = driverBhata;
    }

    public Integer getNightCharges() {
        return nightCharges;
    }

    public void setNightCharges(Integer nightCharges) {
        this.nightCharges = nightCharges;
    }

    public Integer getGst() {
        return gst;
    }

    public void setGst(Integer gst) {
        this.gst = gst;
    }

    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public Integer getOfferPartial() {
        return offerPartial;
    }

    public void setOfferPartial(Integer offerPartial) {
        this.offerPartial = offerPartial;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VendorCabsDTO getVendorCab() {
        return vendorCab;
    }

    public void setVendorCab(VendorCabsDTO vendorCab) {
        this.vendorCab = vendorCab;
    }

    public VendorDriversDTO getVendorDriver() {
        return vendorDriver;
    }

    public void setVendorDriver(VendorDriversDTO vendorDriver) {
        this.vendorDriver = vendorDriver;
    }

    public VendorDTO getVendor() {
        return vendor;
    }

    public void setVendor(VendorDTO vendor) {
        this.vendor = vendor;
    }

    public CabAdminDTO getCabAdmin() {
        return cabAdmin;
    }

    public void setCabAdmin(CabAdminDTO cabAdmin) {
        this.cabAdmin = cabAdmin;
    }

    public DriverAdminDTO getDriverAdmin() {
        return driverAdmin;
    }

    public void setDriverAdmin(DriverAdminDTO driverAdmin) {
        this.driverAdmin = driverAdmin;
    }

    public int getCollection() {
        return collection;
    }

    public void setCollection(int collection) {
        this.collection = collection;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    

    
    
}
