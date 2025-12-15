package com.workshop.Entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OneFifty {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private String sourceState;
	private String sourceCity;
	private String destinationState;
	private String destinationCity;
	private int hatchback;
	private int sedan;
	private int sedanpremium;
	private int suv;
	private int suvplus;

	 private int minDistance;
    private int maxDistance;

    private String status;

    
	private int ertiga;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceState() {
        return sourceState;
    }

    public void setSourceState(String sourceState) {
        this.sourceState = sourceState;
    }

    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public int getHatchback() {
        return hatchback;
    }

    public void setHatchback(int hatchback) {
        this.hatchback = hatchback;
    }

    public int getSedan() {
        return sedan;
    }

    public void setSedan(int sedan) {
        this.sedan = sedan;
    }

    public int getSedanpremium() {
        return sedanpremium;
    }

    public void setSedanpremium(int sedanpremium) {
        this.sedanpremium = sedanpremium;
    }

    public int getSuv() {
        return suv;
    }

    public void setSuv(int suv) {
        this.suv = suv;
    }

    public int getSuvplus() {
        return suvplus;
    }

    public void setSuvplus(int suvplus) {
        this.suvplus = suvplus;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

   

    public int getErtiga() {
        return ertiga;
    }

    public void setErtiga(int ertiga) {
        this.ertiga = ertiga;
    }


    
}
