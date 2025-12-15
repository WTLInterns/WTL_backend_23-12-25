package com.workshop.Entity;

import java.util.List;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class DriveAdmin {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String DriverName; 
	
	private String contactNo;
	
	private String AltMobNum;
	
	private String emailId;
	
	private String Adress;
	
	private String aadhaNo;
	
	private String drLicenseNo;
	
	private String pvcNo2;
	
	private String DriverImgSelfie; //Image with filename & path
	
	private String Aadhar; //Image with filename & path
	
	private String DrLicenceNum; //Image with filename & path
	
	private String PvcNo; //Image with filename & path
	
	private String otherDetails;
	
	private String status;

private String role="ADMIN_DRIVER";

	private String password;

	@Column(nullable = true)
	private Double driverLatitude;

	@Column(nullable = true)
	private Double driverLongitude;

    @Column(nullable = true, length = 255)
    private String fcmToken;

    @Column(nullable = true)
    private LocalDateTime fcmUpdatedAt;

	@JsonIgnore
	@OneToMany(mappedBy = "driveAdmin")
	@JsonBackReference
	private List<Booking> booking;

	public DriveAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DriveAdmin(int id, String DriverName, String contactNo, String AltMobNum, String emailId, String Adress,
			String aadhaNo, String drLicenseNo, String pvcNo2, String DriverImgSelfie, String Aadhar,
			String DrLicenceNum, String pvcNo, String otherDetails, String status, String role, String password,
			Double driverLatitude, Double driverLongitude, List<Booking> booking, String fcmToken, LocalDateTime fcmUpdatedAt) {
		this.id = id;
		this.DriverName = DriverName;
		this.contactNo = contactNo;
		this.AltMobNum = AltMobNum;
		this.emailId = emailId;
		this.Adress = Adress;
		this.aadhaNo = aadhaNo;
		this.drLicenseNo = drLicenseNo;
		this.pvcNo2 = pvcNo2;
		this.DriverImgSelfie = DriverImgSelfie;
		this.Aadhar = Aadhar;
		this.DrLicenceNum = DrLicenceNum;
		this.PvcNo = PvcNo;
		this.otherDetails = otherDetails;
		this.status = status;
		this.role = role;
		this.password = password;
		this.driverLatitude = driverLatitude;
		this.driverLongitude = driverLongitude;
		this.booking = booking;
		this.fcmToken = fcmToken;
		this.fcmUpdatedAt = fcmUpdatedAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDriverName() {
		return DriverName;
	}

	public void setDriverName(String driverName) {
		DriverName = driverName;
	}

	public String getcontactNo() {
		return contactNo;
	}

	public void setcontactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getAltMobNum() {
		return AltMobNum;
	}

	public void setAltMobNum(String altMobNum) {
		AltMobNum = altMobNum;
	}

	public String getAdress() {
		return Adress;
	}

	public void setAdress(String adress) {
		Adress = adress;
	}

	public String getDriverImgSelfie() {
		return DriverImgSelfie;
	}

	public void setDriverImgSelfie(String driverImgSelfie) {
		DriverImgSelfie = driverImgSelfie;
	}

	public String getAadhar() {
		return Aadhar;
	}

	public void setAadhar(String aadhar) {
		Aadhar = aadhar;
	}

	public String getDrLicenceNum() {
		return DrLicenceNum;
	}

	public void setDrLicenceNum(String drLicenceNum) {
		DrLicenceNum = drLicenceNum;
	}

	public String getPvcNo() {
		return PvcNo;
	}

	public void setPvcNo(String pvcNo) {
		PvcNo = pvcNo;
	}

	public String getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAadhaNo() {
		return aadhaNo;
	}

	public void setAadhaNo(String aadhaNo) {
		this.aadhaNo = aadhaNo;
	}

	public String getDrLicenseNo() {
		return drLicenseNo;
	}

	public void setDrLicenseNo(String drLicenseNo) {
		this.drLicenseNo = drLicenseNo;
	}

	public String getPvcNo2() {
		return pvcNo2;
	}

	public void setPvcNo2(String pvcNo2) {
		this.pvcNo2 = pvcNo2;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
