package com.workshop.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class CabInfo {
	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    private String modelName;
	    private String modelType;
	    private int seats;
	    private String fuelType;
	    private String availability;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getModelName() {
			return modelName;
		}
		public void setModelName(String modelName) {
			this.modelName = modelName;
		}
		public String getModelType() {
			return modelType;
		}
		public void setModelType(String modelType) {
			this.modelType = modelType;
		}
		public int getSeats() {
			return seats;
		}
		public void setSeats(int seats) {
			this.seats = seats;
		}
		public String getFuelType() {
			return fuelType;
		}
		public void setFuelType(String fuelType) {
			this.fuelType = fuelType;
		}
		public String getAvailability() {
			return availability;
		}
		public void setAvailability(String availability) {
			this.availability = availability;
		}
		public CabInfo(int id, String modelName, String modelType, int seats, String fuelType, String availability) {
			super();
			this.id = id;
			this.modelName = modelName;
			this.modelType = modelType;
			this.seats = seats;
			this.fuelType = fuelType;
			this.availability = availability;
		}
		public CabInfo() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "CabInfo [id=" + id + ", modelName=" + modelName + ", modelType=" + modelType + ", seats=" + seats
					+ ", fuelType=" + fuelType + ", availability=" + availability + "]";
		}
	    
	    

}
