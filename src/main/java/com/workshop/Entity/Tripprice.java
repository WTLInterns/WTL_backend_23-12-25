package com.workshop.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "trip_pricing") 
public class Tripprice {
    
    
 // Specify the table name if it differs from the class name
    
    
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; // Primary key
    
        @Column(name = "source_state", nullable = false)
        private String sourceState; // Source state
    
        @Column(name = "source_city", nullable = false)
        private String sourceCity; // Source city
    
        @Column(name = "destination_state", nullable = false)
        private String destinationState; // Destination state
    
        @Column(name = "destination_city", nullable = false)
        private String destinationCity; // Destination city
    
        @Column(name = "hatchback", nullable = false)
        private Double hatchback; // Price for hatchback
    
        @Column(name = "sedan", nullable = false)
        private Double sedan; // Price for sedan
    
        @Column(name = "sedan_premium", nullable = false)
        private Double sedanPremium; // Price for sedan premium
    
        @Column(name = "suv", nullable = false)
        private Double suv; // Price for SUV
    
        @Column(name = "suv_plus", nullable = false)
        private Double suvPlus; // Price for SUV+
    
        // Getters and Setters
    
        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
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
    
        public Double getHatchback() {
            return hatchback;
        }
    
        public void setHatchback(Double hatchback) {
            this.hatchback = hatchback;
        }
    
        public Double getSedan() {
            return sedan;
        }
    
        public void setSedan(Double sedan) {
            this.sedan = sedan;
        }
    
        public Double getSedanPremium() {
            return sedanPremium;
        }
    
        public void setSedanPremium(Double sedanPremium) {
            this.sedanPremium = sedanPremium;
        }
    
        public Double getSuv() {
            return suv;
        }
    
        public void setSuv(Double suv) {
            this.suv = suv;
        }
    
        public Double getSuvPlus() {
            return suvPlus;
        }
    
        public void setSuvPlus(Double suvPlus) {
            this.suvPlus = suvPlus;
        }
    
    }