package com.workshop.DTO;

import com.workshop.Entity.VendorCabs;

public class VendorCabsDTO {

    private int id;
    private String carName;
    private String rCNo;
    private String rCImage;
    private String vehicleNo;
    private String vehicleNoImage;
    private String insuranceImage;
    private String permitImage;
    private String authorizationImage;
    private String cabNoPlateImage;
    private String cabImage;
    private String cabFrontImage;
    private String cabBackImage;
    private String cabOtherDetails;
    private String cabSideImage;

    // Constructor to map from VendorCabs entity
    public VendorCabsDTO(VendorCabs vendorCab) {
        this.id = vendorCab.getVendorCabId();
        this.carName = vendorCab.getCarName();
        this.rCNo = vendorCab.getrCNo();
        this.rCImage = vendorCab.getrCImage();
        this.vehicleNo = vendorCab.getVehicleNo();
        this.vehicleNoImage = vendorCab.getVehicleNoImage();
        this.insuranceImage = vendorCab.getInsuranceImage();
        this.permitImage = vendorCab.getPermitImage();
        this.authorizationImage = vendorCab.getAuthorizationImage();
        this.cabNoPlateImage = vendorCab.getCabNoPlateImage();
        this.cabImage = vendorCab.getCabImage();
        this.cabFrontImage = vendorCab.getCabFrontImage();
        this.cabBackImage = vendorCab.getCabBackImage();
        this.cabOtherDetails = vendorCab.getCabOtherDetails();
        this.cabSideImage = vendorCab.getCabSideImage();
    }

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getrCNo() {
        return rCNo;
    }

    public void setrCNo(String rCNo) {
        this.rCNo = rCNo;
    }

    public String getrCImage() {
        return rCImage;
    }

    public void setrCImage(String rCImage) {
        this.rCImage = rCImage;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNoImage() {
        return vehicleNoImage;
    }

    public void setVehicleNoImage(String vehicleNoImage) {
        this.vehicleNoImage = vehicleNoImage;
    }

    public String getInsuranceImage() {
        return insuranceImage;
    }

    public void setInsuranceImage(String insuranceImage) {
        this.insuranceImage = insuranceImage;
    }

    public String getPermitImage() {
        return permitImage;
    }

    public void setPermitImage(String permitImage) {
        this.permitImage = permitImage;
    }

    public String getAuthorizationImage() {
        return authorizationImage;
    }

    public void setAuthorizationImage(String authorizationImage) {
        this.authorizationImage = authorizationImage;
    }

    public String getCabNoPlateImage() {
        return cabNoPlateImage;
    }

    public void setCabNoPlateImage(String cabNoPlateImage) {
        this.cabNoPlateImage = cabNoPlateImage;
    }

    public String getCabImage() {
        return cabImage;
    }

    public void setCabImage(String cabImage) {
        this.cabImage = cabImage;
    }

    public String getCabFrontImage() {
        return cabFrontImage;
    }

    public void setCabFrontImage(String cabFrontImage) {
        this.cabFrontImage = cabFrontImage;
    }

    public String getCabBackImage() {
        return cabBackImage;
    }

    public void setCabBackImage(String cabBackImage) {
        this.cabBackImage = cabBackImage;
    }

    public String getCabOtherDetails() {
        return cabOtherDetails;
    }

    public void setCabOtherDetails(String cabOtherDetails) {
        this.cabOtherDetails = cabOtherDetails;
    }

    public String getCabSideImage() {
        return cabSideImage;
    }

    public void setCabSideImage(String cabSideImage) {
        this.cabSideImage = cabSideImage;
    }
}
