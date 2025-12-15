package com.workshop.DTO;


public class PriceUpdateRequest {
    private int hatchback;
    private int sedan;
    private int sedanpremium;
    private int suv;
    private int suvplus;

    private String sourceState;
    private String sourceCity;
    private String destinationState;
    private String destinationCity;




    // Getters and Setters

    public String getSourceState() {
        return sourceState;
    }

    public void setSourceState(String sourceState) {
        this.sourceState = sourceState;
    }

    // Getter and Setter for sourceCity
    public String getSourceCity() {
        return sourceCity;
    }

    public void setSourceCity(String sourceCity) {
        this.sourceCity = sourceCity;
    }

    // Getter and Setter for destinationState
    public String getDestinationState() {
        return destinationState;
    }

    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }

    // Getter and Setter for destinationCity
    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }
    public int getHatchback() { return hatchback; }
    public void setHatchback(int hatchback) { this.hatchback = hatchback; }

    public int getSedan() { return sedan; }
    public void setSedan(int sedan) { this.sedan = sedan; }

    public int getSedanpremium() { return sedanpremium; }
    public void setSedanpremium(int sedanpremium) { this.sedanpremium = sedanpremium; }

    public int getSuv() { return suv; }
    public void setSuv(int suv) { this.suv = suv; }

    public int getSuvplus() { return suvplus; }
    public void setSuvplus(int suvplus) { this.suvplus = suvplus; }
}
