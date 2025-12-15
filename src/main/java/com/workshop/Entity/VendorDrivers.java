package com.workshop.Entity;

import java.util.List;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class VendorDrivers {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vendorDriverId;

	private String driverName;

	private String contactNo;

	private String altContactNo;

	private String address;

	private String driverImage;

	private String driverSelfie;

	private String dLNo;

	private String pvcNo;

	private String dLnoImage;

	private String pvcImage;

	private String driverDoc1Image;

	private String driverDoc2Image;

	private String driverDoc3Image;

	private String emailId;

	private String driverOtherDetails;

	private String role="DRIVER";

	private String password;

	@Column(nullable = true)
	private Double driverLatitude;

	@Column(nullable = true)
	private Double driverLongitude;

	@Column(nullable = true, length = 255)
	private String fcmToken;

	@Column(nullable = true)
	private LocalDateTime fcmUpdatedAt;

	@JsonBackReference
	// @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	// One-to-many relationship with Booking
	// @JsonManagedReference
	@JsonIgnore
	// @OneToMany(mappedBy = "vendorDriver", cascade = CascadeType.ALL, fetch =
	// FetchType.LAZY)
	@OneToMany(mappedBy = "vendorDriver")

	private List<Booking> booking;

	public VendorDrivers() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VendorDrivers(int vendorDriverId, String driverName, String contactNo, String altContactNo, String address,
			String driverImage, String driverSelfie, String dLNo, String pvcNo, String dLnoImage, String pvcImage,
			String driverDoc1Image, String driverDoc2Image, String driverDoc3Image, String emailId,
			String driverOtherDetails, Vendor vendor, List<Booking> booking, String role, String password, Double driverLatitude, Double driverLongitude, String fcmToken, LocalDateTime fcmUpdatedAt) {
		this.vendorDriverId = vendorDriverId;
		this.driverName = driverName;
		this.contactNo = contactNo;
		this.altContactNo = altContactNo;
		this.address = address;
		this.driverImage = driverImage;
		this.driverSelfie = driverSelfie;
		this.dLNo = dLNo;
		this.pvcNo = pvcNo;
		this.dLnoImage = dLnoImage;
		this.pvcImage = pvcImage;
		this.driverDoc1Image = driverDoc1Image;
		this.driverDoc2Image = driverDoc2Image;
		this.driverDoc3Image = driverDoc3Image;
		this.emailId = emailId;
		this.driverOtherDetails = driverOtherDetails;
		this.vendor = vendor;
		this.booking = booking;
		this.role=role;
		this.password=password;
		this.driverLatitude=driverLatitude;
		this.driverLongitude=driverLongitude;
		this.fcmToken = fcmToken;
		this.fcmUpdatedAt = fcmUpdatedAt;
	}

	public int getVendorDriverId() {
		return vendorDriverId;
	}

	public void setVendorDriverId(int vendorDriverId) {
		this.vendorDriverId = vendorDriverId;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAltContactNo() {
		return altContactNo;
	}

	public void setAltContactNo(String altContactNo) {
		this.altContactNo = altContactNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDriverImage() {
		return driverImage;
	}

	public void setDriverImage(String driverImage) {
		this.driverImage = driverImage;
	}

	public String getDriverSelfie() {
		return driverSelfie;
	}

	public void setDriverSelfie(String driverSelfie) {
		this.driverSelfie = driverSelfie;
	}

	public String getdLNo() {
		return dLNo;
	}

	public void setdLNo(String dLNo) {
		this.dLNo = dLNo;
	}

	public String getPvcNo() {
		return pvcNo;
	}

	public void setPvcNo(String pvcNo) {
		this.pvcNo = pvcNo;
	}

	public String getdLnoImage() {
		return dLnoImage;
	}

	public void setdLnoImage(String dLnoImage) {
		this.dLnoImage = dLnoImage;
	}

	public String getPvcImage() {
		return pvcImage;
	}

	public void setPvcImage(String pvcImage) {
		this.pvcImage = pvcImage;
	}

	public String getDriverDoc1Image() {
		return driverDoc1Image;
	}

	public void setDriverDoc1Image(String driverDoc1Image) {
		this.driverDoc1Image = driverDoc1Image;
	}

	public String getDriverDoc2Image() {
		return driverDoc2Image;
	}

	public void setDriverDoc2Image(String driverDoc2Image) {
		this.driverDoc2Image = driverDoc2Image;
	}

	public String getDriverDoc3Image() {
		return driverDoc3Image;
	}

	public void setDriverDoc3Image(String driverDoc3Image) {
		this.driverDoc3Image = driverDoc3Image;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDriverOtherDetails() {
		return driverOtherDetails;
	}

	public void setDriverOtherDetails(String driverOtherDetails) {
		this.driverOtherDetails = driverOtherDetails;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<Booking> getBooking() {
		return booking;
	}

	public void setBooking(List<Booking> booking) {
		this.booking = booking;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getDriverLatitude() {
		return driverLatitude;
	}

	public void setDriverLatitude(Double driverLatitude) {
		this.driverLatitude = driverLatitude;
	}

	public Double getDriverLongitude() {
		return driverLongitude;
	}

	public void setDriverLongitude(Double driverLongitude) {
		this.driverLongitude = driverLongitude;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public LocalDateTime getFcmUpdatedAt() {
		return fcmUpdatedAt;
	}

	public void setFcmUpdatedAt(LocalDateTime fcmUpdatedAt) {
		this.fcmUpdatedAt = fcmUpdatedAt;
	}

}
