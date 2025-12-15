package com.workshop.DTO;

public class PricingResponse {

    private int id;
    private int minDistance;
    private int maxDistance;
    private int hatchback;
    private int sedan;
    private int sedanpremium;
    private int suv;
    private int suvplus;
    private int ertiga;

    // ─── GETTERS & SETTERS ────────────────────────────────────────────────────────

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

    public int getErtiga() {
        return ertiga;
    }

    public void setErtiga(int ertiga) {
        this.ertiga = ertiga;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
