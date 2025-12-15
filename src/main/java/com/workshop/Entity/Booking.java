package com.workshop.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.workshop.CarRental.Entity.CarRentalUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "user_booking")
@Entity
public class Booking {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String fromLocation;
private String toLocation;
private String tripType;

private LocalDate startDate;
private LocalDate returnDate;
private String time;
private String distance;

private String userId; // userId (consider renaming if more meaningful)
private String bookingId;
private String name;
private String email;
private String phone;
private String userPickup; // user_pickup renamed to follow Java naming conventions
private String userDrop; // user_drop renamed to follow Java naming conventions

private LocalDate date;
private String userTripType; // user_trip_type renamed to follow Java naming conventions
private String bookid; // bookid can be renamed to something more meaningful
private String car;
private String baseAmount;

private Integer amount = 0;
private Integer status = 0;
private String driverBhata = "0"; // Assuming it's meant to be a String, or it can be Integer if numeric
private Integer nightCharges = 0;
private Integer gst = 0;
private Integer serviceCharge = 0;
private String offer;
private Integer offerPartial = 0;
private String offerAmount;
private String txnId = "0"; // Renamed from txnid to txnId (camelCase)
private String payment;
private LocalDate dateEnd;
private String timeEnd;
private String bookingType;
private String description;
private String carrier;
private String companyName;
private int days;

private int collection;

@Enumerated(EnumType.STRING)
private TripStatus tripStatus;

private String startOtp;
private String endOtp;

private String startOdometer;
private String endOdometer;

private LocalDateTime driverEnterOtpTimePreStarted;

private LocalDateTime odoometerEnterTimeStarted;

private LocalDateTime driverEnterOtpTimePostTrip;

private LocalDateTime odoometerEnterTimeEnding;

private String packageName;




@JsonIgnore
@JsonBackReference
// @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "vendor_id")
@ManyToOne
private Vendor vendor;

// @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "bookings"})
// @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "vendor_cab_id")
@ManyToOne
private VendorCabs vendorCab;

// @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "bookings"})
// @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "vendor_driver_id")
@ManyToOne
private VendorDrivers vendorDriver;



@OneToOne
@JoinColumn(name = "penalty_id")
private Penalty penalty;



@JsonIgnore
@ManyToOne
@JsonManagedReference
private CabAdmin cabAdmin;

@JsonIgnore
@ManyToOne
@JsonManagedReference
private DriveAdmin driveAdmin;

// @JsonIgnore
// @ManyToOne
// @JoinColumn(name = "user_id")
// private User user;

// @ManyToOne
    // @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "bookings"})
// @JoinColumn(name = "car_rental_user_id")
// private CarRentalUser carRentalUser;
@JsonIgnore

@ManyToOne(fetch = FetchType.LAZY, optional = true)
@JsonManagedReference

    @JoinColumn(name = "car_rental_user_id", nullable = true)
    private CarRentalUser carRentalUser;






// @JsonIgnore
// @JsonBackReference
    // @OneToOne
    // @JoinColumn(name = "penalty_id")
    // private Penalty penalty;







@Column(name = "payment_method")
private String paymentMethod = "cash"; // cash, online, card

@Column(name = "payment_type")
private String paymentType = "full"; // full, partial

@Column(name = "payment_status")
private String paymentStatus = "pending"; // pending, paid, failed, cancelled

@Column(name = "payment_amount")
private Integer paymentAmount = 0;

@Column(name = "remaining_amount")
private Integer remainingAmount = 0;

@Column(name = "razorpay_order_id")
private String razorpayOrderId;

@Column(name = "razorpay_Signature")
private String razorpaySignature;

@Column(name = "razorpay_payment_id")
private String razorpayPaymentId;




public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getFromLocation() {
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

public Vendor getVendor() {
return vendor;
}

public void setVendor(Vendor vendor) {
this.vendor = vendor;
}

public VendorCabs getVendorCab() {
return vendorCab;
}

public void setVendorCab(VendorCabs vendorCab) {
this.vendorCab = vendorCab;
}

public VendorDrivers getVendorDriver() {
return vendorDriver;
}

public void setVendorDriver(VendorDrivers vendorDriver) {
this.vendorDriver = vendorDriver;
}



public CabAdmin getCabAdmin() {
return cabAdmin;
}

public void setCabAdmin(CabAdmin cabAdmin) {
this.cabAdmin = cabAdmin;
}

public DriveAdmin getDriveAdmin() {
return driveAdmin;
}

public void setDriveAdmin(DriveAdmin driveAdmin) {
this.driveAdmin = driveAdmin;
}

public CarRentalUser getCarRentalUser() {
return carRentalUser;
}

public void setCarRentalUser(CarRentalUser carRentalUser) {
this.carRentalUser = carRentalUser;
}



public Penalty getPenalty() {
return penalty;
}

public void setPenalty(Penalty penalty) {
this.penalty = penalty;
}

public String getCarrier() {
return carrier;
}

public void setCarrier(String carrier) {
this.carrier = carrier;
}



public LocalDateTime getDriverEnterOtpTimePreStarted() {
return driverEnterOtpTimePreStarted;
}



	public void setDriverEnterOtpTimePreStarted(LocalDateTime driverEnterOtpTimePreStarted) {
		this.driverEnterOtpTimePreStarted = driverEnterOtpTimePreStarted;
	}



	public LocalDateTime getOdoometerEnterTimeStarted() {
		return odoometerEnterTimeStarted;
	}





	public void setOdoometerEnterTimeStarted(LocalDateTime odoometerEnterTimeStarted) {
		this.odoometerEnterTimeStarted = odoometerEnterTimeStarted;
	}



	public LocalDateTime getDriverEnterOtpTimePostTrip() {
		return driverEnterOtpTimePostTrip;
	}



	public void setDriverEnterOtpTimePostTrip(LocalDateTime driverEnterOtpTimePostTrip) {
		this.driverEnterOtpTimePostTrip = driverEnterOtpTimePostTrip;
	}



	public LocalDateTime getOdoometerEnterTimeEnding() {
		return odoometerEnterTimeEnding;
	}



		public void setOdoometerEnterTimeEnding(LocalDateTime odoometerEnterTimeEnding) {
		this.odoometerEnterTimeEnding = odoometerEnterTimeEnding;
	}

	public String getStartOdometer() {
		return startOdometer;
	}

	public void setStartOdometer(String startOdometer) {
		this.startOdometer = startOdometer;
	}

	public String getEndOdometer() {
		return endOdometer;
	}

	public void setEndOdometer(String endOdometer) {
		this.endOdometer = endOdometer;
	}

	public String getStartOtp() {
		return startOtp;
	}

	public void setStartOtp(String startOtp) {
		this.startOtp = startOtp;
	}

	public String getEndOtp() {
		return endOtp;
	}

	public void setEndOtp(String endOtp) {
		this.endOtp = endOtp;
	}

	public TripStatus getTripStatus() {
		return tripStatus;
	}

	public void setTripStatus(TripStatus tripStatus) {
		this.tripStatus = tripStatus;
	}



	public int getCollection() {
return collection;
}



public void setCollection(int collection) {
this.collection = collection;
}



public String getCompanyName() {
return companyName;
}



public void setCompanyName(String companyName) {
this.companyName = companyName;
}



public String getPackageName() {
return packageName;
}



public void setPackageName(String packageName) {
this.packageName = packageName;
}


// public User getUser() {
// return user;
// }

// public void setUser(User user) {
// this.user = user;
// }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
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

@Override
public String toString() {
return "Booking [id=" + id + ", fromLocation=" + fromLocation + ", toLocation=" + toLocation + ", tripType="
+ tripType + ", startDate=" + startDate + ", returnDate=" + returnDate + ", time=" + time
+ ", distance=" + distance + ", userId=" + userId + ", bookingId=" + bookingId + ", name=" + name
+ ", email=" + email + ", phone=" + phone + ", userPickup=" + userPickup + ", userDrop=" + userDrop
+ ", date=" + date + ", userTripType=" + userTripType + ", bookid=" + bookid + ", car=" + car
+ ", baseAmount=" + baseAmount + ", amount=" + amount + ", status=" + status + ", driverBhata="
+ driverBhata + ", nightCharges=" + nightCharges + ", gst=" + gst + ", serviceCharge=" + serviceCharge
+ ", offer=" + offer + ", offerPartial=" + offerPartial + ", offerAmount=" + offerAmount + ", txnId="
+ txnId + ", payment=" + payment + ", dateEnd=" + dateEnd + ", timeEnd=" + timeEnd + ", bookingType="
+ bookingType + ", description=" + description + ", carrier=" + carrier + ", companyName=" + companyName
+ ", days=" + days + ", collection=" + collection + ", driverEnterOtpTimePreStarted="
+ driverEnterOtpTimePreStarted + ", startOdometer=" + startOdometer
+ ", odoometerEnterTimeStarted=" + odoometerEnterTimeStarted + ", driverEnterOtpTimePostTrip="
+ driverEnterOtpTimePostTrip + ", endOdometer=" + endOdometer + ", odoometerEnterTimeEnding="
+ odoometerEnterTimeEnding + ", packageName=" + packageName + ", vendor=" + vendor + ", vendorCab="
+ vendorCab + ", vendorDriver=" + vendorDriver + ", penalty=" + penalty + ", cabAdmin=" + cabAdmin
+ ", driveAdmin=" + driveAdmin + ", carRentalUser=" + carRentalUser + ", paymentMethod=" + paymentMethod
+ ", paymentType=" + paymentType + ", paymentStatus=" + paymentStatus + ", paymentAmount="
+ paymentAmount + ", remainingAmount=" + remainingAmount + ", razorpayOrderId=" + razorpayOrderId
+ ", razorpaySignature=" + razorpaySignature + ", razorpayPaymentId=" + razorpayPaymentId + "]";
}

public Booking(int id, String fromLocation, String toLocation, String tripType, LocalDate startDate,
LocalDate returnDate, String time, String distance, String userId, String bookingId, String name,
String email, String phone, String userPickup, String userDrop, LocalDate date, String userTripType,
String bookid, String car, String baseAmount, Integer amount, Integer status, String driverBhata,
Integer nightCharges, Integer gst, Integer serviceCharge, String offer, Integer offerPartial,
String offerAmount, String txnId, String payment, LocalDate dateEnd, String timeEnd, String bookingType,
String description, String carrier, String companyName, int days, int collection,
LocalDateTime driverEnterOtpTimePreStarted, String startOdometer, LocalDateTime odoometerEnterTimeStarted,
LocalDateTime driverEnterOtpTimePostTrip, String endOdometer, LocalDateTime odoometerEnterTimeEnding,
String packageName, Vendor vendor, VendorCabs vendorCab, VendorDrivers vendorDriver, Penalty penalty,
CabAdmin cabAdmin, DriveAdmin driveAdmin, CarRentalUser carRentalUser, String paymentMethod,
String paymentType, String paymentStatus, Integer paymentAmount, Integer remainingAmount,
String razorpayOrderId, String razorpaySignature, String razorpayPaymentId) {
super();
this.id = id;
this.fromLocation = fromLocation;
this.toLocation = toLocation;
this.tripType = tripType;
this.startDate = startDate;
this.returnDate = returnDate;
this.time = time;
this.distance = distance;
this.userId = userId;
this.bookingId = bookingId;
this.name = name;
this.email = email;
this.phone = phone;
this.userPickup = userPickup;
this.userDrop = userDrop;
this.date = date;
this.userTripType = userTripType;
this.bookid = bookid;
this.car = car;
this.baseAmount = baseAmount;
this.amount = amount;
this.status = status;
this.driverBhata = driverBhata;
this.nightCharges = nightCharges;
this.gst = gst;
this.serviceCharge = serviceCharge;
this.offer = offer;
this.offerPartial = offerPartial;
this.offerAmount = offerAmount;
this.txnId = txnId;
this.payment = payment;
this.dateEnd = dateEnd;
this.timeEnd = timeEnd;
this.bookingType = bookingType;
this.description = description;
this.carrier = carrier;
this.companyName = companyName;
this.days = days;
this.collection = collection;
this.driverEnterOtpTimePreStarted = driverEnterOtpTimePreStarted;
this.startOdometer = startOdometer;
this.odoometerEnterTimeStarted = odoometerEnterTimeStarted;
this.driverEnterOtpTimePostTrip = driverEnterOtpTimePostTrip;
this.endOdometer = endOdometer;
this.odoometerEnterTimeEnding = odoometerEnterTimeEnding;
this.packageName = packageName;
this.vendor = vendor;
this.vendorCab = vendorCab;
this.vendorDriver = vendorDriver;
this.penalty = penalty;
this.cabAdmin = cabAdmin;
this.driveAdmin = driveAdmin;
this.carRentalUser = carRentalUser;
this.paymentMethod = paymentMethod;
this.paymentType = paymentType;
this.paymentStatus = paymentStatus;
this.paymentAmount = paymentAmount;
this.remainingAmount = remainingAmount;
this.razorpayOrderId = razorpayOrderId;
this.razorpaySignature = razorpaySignature;
this.razorpayPaymentId = razorpayPaymentId;
}

public Booking() {
super();
// TODO Auto-generated constructor stub
}
}