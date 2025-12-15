package com.workshop.Entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String vendorCompanyName;
    private String contactNo;
    private String alternateMobileNo;
    private String city;
    private String vendorEmail;
    private String bankName;
    private String bankAccountNo;
    private String ifscCode;
    private String aadharNo;
    private String panNo;
    private String udyogAadharNo;
    private String govtApprovalCertificate;
    private String vendorDocs;
    private String vendorImage;
    private String aadharPhoto;
    private String panPhoto;
    private String vendorOtherDetails;
    private String password;

    @JsonManagedReference
    // @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    @OneToMany
    private List<Booking> booking;

    // One-to-many relationship with VendorCabs
    @JsonManagedReference
    // @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    @OneToMany
    private List<VendorCabs> vendorCabs;

    // One-to-many relationship with VendorDrivers
    @JsonManagedReference
    // @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    @OneToMany
    private List<VendorDrivers> vendorDrivers;

    // @JsonIgnore
	@JsonManagedReference
    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Penalty> penalty;
	
	@Column(name = "fcm_token")
	private String fcmToken;

    @Column(name = "fcm_updated_at")
    private LocalDateTime fcmUpdatedAt;


    

    // @JsonManagedReference
    // @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Penalty> penalty;

    public Vendor(Long id, String vendorCompanyName, String contactNo, String alternateMobileNo, String city,
            String vendorEmail, String bankName, String bankAccountNo, String ifscCode, String aadharNo, String panNo,
            String udyogAadharNo, String govtApprovalCertificate, String vendorDocs, String vendorImage,
            String aadharPhoto, String panPhoto, String vendorOtherDetails, String password, List<Booking> booking,
            List<VendorCabs> vendorCabs, List<VendorDrivers> vendorDrivers, List<Penalty> penalty , String fcmToken) {
        this.id = id;
        this.vendorCompanyName = vendorCompanyName;
        this.contactNo = contactNo;
        this.alternateMobileNo = alternateMobileNo;
        this.city = city;
        this.vendorEmail = vendorEmail;
        this.bankName = bankName;
        this.bankAccountNo = bankAccountNo;
        this.ifscCode = ifscCode;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
        this.udyogAadharNo = udyogAadharNo;
        this.govtApprovalCertificate = govtApprovalCertificate;
        this.vendorDocs = vendorDocs;
        this.vendorImage = vendorImage;
        this.aadharPhoto = aadharPhoto;
        this.panPhoto = panPhoto;
        this.vendorOtherDetails = vendorOtherDetails;
        this.password = password;
        this.booking = booking;
        this.vendorCabs = vendorCabs;
        this.vendorDrivers = vendorDrivers;
        this.penalty = penalty;
        this.fcmToken=fcmToken;
        
    }

    public Vendor() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVendorCompanyName() {
        return vendorCompanyName;
    }

    public void setVendorCompanyName(String vendorCompanyName) {
        this.vendorCompanyName = vendorCompanyName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getUdyogAadharNo() {
        return udyogAadharNo;
    }

    public void setUdyogAadharNo(String udyogAadharNo) {
        this.udyogAadharNo = udyogAadharNo;
    }

    public String getGovtApprovalCertificate() {
        return govtApprovalCertificate;
    }

    public void setGovtApprovalCertificate(String govtApprovalCertificate) {
        this.govtApprovalCertificate = govtApprovalCertificate;
    }

    public String getVendorDocs() {
        return vendorDocs;
    }

    public void setVendorDocs(String vendorDocs) {
        this.vendorDocs = vendorDocs;
    }

    public String getVendorImage() {
        return vendorImage;
    }

    public void setVendorImage(String vendorImage) {
        this.vendorImage = vendorImage;
    }

    public String getAadharPhoto() {
        return aadharPhoto;
    }

    public void setAadharPhoto(String aadharPhoto) {
        this.aadharPhoto = aadharPhoto;
    }

    public String getPanPhoto() {
        return panPhoto;
    }

    public void setPanPhoto(String panPhoto) {
        this.panPhoto = panPhoto;
    }

    public String getVendorOtherDetails() {
        return vendorOtherDetails;
    }

    public void setVendorOtherDetails(String vendorOtherDetails) {
        this.vendorOtherDetails = vendorOtherDetails;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    public List<VendorCabs> getVendorCabs() {
        return vendorCabs;
    }

    public void setVendorCabs(List<VendorCabs> vendorCabs) {
        this.vendorCabs = vendorCabs;
    }

    public List<VendorDrivers> getVendorDrivers() {
        return vendorDrivers;
    }

    public void setVendorDrivers(List<VendorDrivers> vendorDrivers) {
        this.vendorDrivers = vendorDrivers;
    }

    public List<Penalty> getPenalty() {
        return penalty;
    }

    public void setPenalty(List<Penalty> penalty) {
        this.penalty = penalty;
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
