package com.workshop.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class CabAdmin{
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String vehicleNameAndRegNo;
    private String vehicleRcNo;
    private String carOtherDetails;
//    public String vehicleName;
    private String status;

    
    private String vehicleRcImg;
    private String insurance; //image filename or path
    private String permit; //image filename or path
    private String fitnessCert; //image filename or path
    private String cabImage;
    private String frontImage; //image filename or path
    private String backImage; //image filename or path
    private String sideImage; //image filename or path

@JsonIgnore
	@OneToMany(mappedBy = "cabAdmin")
	@JsonBackReference
	private List<Booking> booking;
	public CabAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CabAdmin(Long id, String vehicleNameAndRegNo, String vehicleRcNo, String carOtherDetails,
			String vehicleRcImg, String insurance, String permit, String fitnessCert, String cabImage,
			String frontImage, String backImage, String sideImage, String status, List<Booking> booking) {
		super();
		this.id = id;
		this.vehicleNameAndRegNo = vehicleNameAndRegNo;
		this.vehicleRcNo = vehicleRcNo;
		this.carOtherDetails = carOtherDetails;
		this.vehicleRcImg = vehicleRcImg;
		this.insurance = insurance;
		this.permit = permit;
		this.fitnessCert = fitnessCert;
		this.cabImage = cabImage;
		this.frontImage = frontImage;
		this.backImage = backImage;
		this.sideImage = sideImage;
		this.status=status;
		this.booking=booking;
//		this.vehicleName=vehicleName;
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
	public String getVehicleRcImg() {
		return vehicleRcImg;
	}
	public void setVehicleRcImg(String vehicleRcImg) {
		this.vehicleRcImg = vehicleRcImg;
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
	public String getFitnessCert() {
		return fitnessCert;
	}
	public void setFitnessCert(String fitnessCert) {
		this.fitnessCert = fitnessCert;
	}
	public String getCabImage() {
		return cabImage;
	}
	public void setCabImage(String cabImage) {
		this.cabImage = cabImage;
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
//	public String getVehicleName() {
//		return vehicleName;
//	}
//	public void setVehicleName(String vehicleName) {
//		this.vehicleName = vehicleName;
//	}
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

	
    
	
	
    
	
}