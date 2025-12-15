package com.workshop.DTO;

import com.workshop.Entity.VendorDrivers;

public class VendorDriversDTO {

    private int id;
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

    // Constructor to map from VendorDrivers entity
    public VendorDriversDTO(VendorDrivers vendorDriver) {
        this.id = vendorDriver.getVendorDriverId();
        this.driverName = vendorDriver.getDriverName();
        this.contactNo = vendorDriver.getContactNo();
        this.altContactNo = vendorDriver.getAltContactNo();
        this.address = vendorDriver.getAddress();
        this.driverImage = vendorDriver.getDriverImage();
        this.driverSelfie = vendorDriver.getDriverSelfie();
        this.dLNo = vendorDriver.getdLNo();
        this.pvcNo = vendorDriver.getPvcNo();
        this.dLnoImage = vendorDriver.getdLnoImage();
        this.pvcImage = vendorDriver.getPvcImage();
        this.driverDoc1Image = vendorDriver.getDriverDoc1Image();
        this.driverDoc2Image = vendorDriver.getDriverDoc2Image();
        this.driverDoc3Image = vendorDriver.getDriverDoc3Image();
        this.emailId = vendorDriver.getEmailId();
        this.driverOtherDetails = vendorDriver.getDriverOtherDetails();
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
