package com.workshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class OutSourceCarCab {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNameAndRegNo;
    private String vehicleRcNo;
    private String insurance; //image filename or path
    private String permit; //image filename or path
    private String authorization; //image filename or path
    private String carNoPlate;
    private String carImage; //image filename or path
    private String frontImage; //image filename or path
    private String backImage; //image filename or path
    private String sideImage; //image filename or path
    private String carOtherDetails;
    private String status;
    
    
	public OutSourceCarCab() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OutSourceCarCab(Long id, String vehicleNameAndRegNo, String vehicleRcNo, String insurance, String permit,
			String authorization, String carNoPlate, String carImage, String frontImage, String backImage,String status,
			String sideImage, String carOtherDetails) {
		super();
		this.id = id;
		this.vehicleNameAndRegNo = vehicleNameAndRegNo;
		this.vehicleRcNo = vehicleRcNo;
		this.insurance = insurance;
		this.permit = permit;
		this.authorization = authorization;
		this.carNoPlate = carNoPlate;
		this.carImage = carImage;
		this.frontImage = frontImage;
		this.backImage = backImage;
		this.sideImage = sideImage;
		this.carOtherDetails = carOtherDetails;
		this.status=status;
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
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	public String getPermit() {
		return permit;
	}
	public void setPermit(String permit) {
		this.permit = permit;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public String getCarNoPlate() {
		return carNoPlate;
	}
	public void setCarNoPlate(String carNoPlate) {
		this.carNoPlate = carNoPlate;
	}
	public String getCarImage() {
		return carImage;
	}
	public void setCarImage(String carImage) {
		this.carImage = carImage;
	}
	public String getFrontImage() {
		return frontImage;
	}
	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}
	public String getBackImage() {
		return backImage;
	}
	public void setBackImage(String backImage) {
		this.backImage = backImage;
	}
	public String getSideImage() {
		return sideImage;
	}
	public void setSideImage(String sideImage) {
		this.sideImage = sideImage;
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
