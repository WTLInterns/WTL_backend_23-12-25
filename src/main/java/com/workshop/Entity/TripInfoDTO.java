package com.workshop.Entity;

import java.util.List;

import com.workshop.Repo.Trip;

public class TripInfoDTO {
	
	private List<Trip> tripInfo;
    private int distance;
    private String pickupCity;
    private String dropCity;
    private List<CabInfo> cabInfo;
	public List<Trip> getTripInfo() {
		return tripInfo;
	}
	public void setTripInfo(List<Trip> tripInfo) {
		this.tripInfo = tripInfo;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getPickupCity() {
		return pickupCity;
	}
	public void setPickupCity(String pickupCity) {
		this.pickupCity = pickupCity;
	}
	public String getDropCity() {
		return dropCity;
	}
	public void setDropCity(String dropCity) {
		this.dropCity = dropCity;
	}
	public List<CabInfo> getCabInfo() {
		return cabInfo;
	}
	public void setCabInfo(List<CabInfo> cabInfo) {
		this.cabInfo = cabInfo;
	}
	public TripInfoDTO(List<Trip> tripInfo, int distance, String pickupCity, String dropCity, List<CabInfo> cabInfo) {
		super();
		this.tripInfo = tripInfo;
		this.distance = distance;
		this.pickupCity = pickupCity;
		this.dropCity = dropCity;
		this.cabInfo = cabInfo;
	}
	@Override
	public String toString() {
		return "TripInfoDTO [tripInfo=" + tripInfo + ", distance=" + distance + ", pickupCity=" + pickupCity
				+ ", dropCity=" + dropCity + ", cabInfo=" + cabInfo + "]";
	}
	public TripInfoDTO() {
		super();
	}
    
    
    

}
