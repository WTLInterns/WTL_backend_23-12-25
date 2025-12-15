package com.workshop.DTO;

import com.workshop.Entity.CabAdmin;

public class CabAdminDTO {
    
    private Long id;

    private String vehicleNameAndRegNo;
    private String vehicleRcNo;
    private String carOtherDetails;
//    public String vehicleName;
    private String status;


    public CabAdminDTO(CabAdmin cabAdmin){
        this.id=cabAdmin.getId();
        this.vehicleNameAndRegNo=cabAdmin.getVehicleNameAndRegNo();
        this.vehicleRcNo=cabAdmin.getVehicleRcNo();
        this.carOtherDetails=cabAdmin.getCarOtherDetails();
        this.status=cabAdmin.getStatus();
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getVehicleNameAndRegNo() {
        return vehicleNameAndRegNo;
    }


    public void setVehicleNameAndRegNo(String vehicleNameAndRegNo) {
        this.vehicleNameAndRegNo = vehicleNameAndRegNo;
    }


    public String getVehicleRcNo() {
        return vehicleRcNo;
    }


    public void setVehicleRcNo(String vehicleRcNo) {
        this.vehicleRcNo = vehicleRcNo;
    }


    public String getCarOtherDetails() {
        return carOtherDetails;
    }


    public void setCarOtherDetails(String carOtherDetails) {
        this.carOtherDetails = carOtherDetails;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }
    



}
