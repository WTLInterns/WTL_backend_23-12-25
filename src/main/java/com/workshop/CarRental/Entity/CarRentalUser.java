package com.workshop.CarRental.Entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.workshop.Entity.Booking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import java.time.LocalDateTime;




@Entity
public class CarRentalUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

     @Column(nullable = true)
    private String userName;


    @Column(nullable = true)
    private String lastName;


    @Column(nullable = true)
    private String email;


    @Column(nullable = true)
    private String password;


    @Column(nullable = true)
    private String phone;


    @Column(nullable = true)
    private String gender;


    @Column(nullable = false)
private double userlatitude = 0.0;

@Column(nullable = false)
private double userlongitude = 0.0;

    @Column(nullable = true)
    private String address;

    private String role="USER";

    // private String city;

    // private String state;

    // @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "carRentalUser")



    private List<Booking> bookings;


   


    @Column(name = "fcm_token", nullable = true)
    private String fcmToken;
    
    @Column(name = "fcm_updated_at", nullable = true)
    private LocalDateTime fcmUpdatedAt;


    public CarRentalUser(int id, String userName, String lastName, String email, String password, String phone, double userlatitude,
            double userlongitude, String address, List<Booking> bookings, String role, String gender) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userlatitude = userlatitude;
        this.userlongitude = userlongitude;
        this.address = address;
        this.bookings = bookings;
        this.role=role;
        this.lastName=lastName;
        this.gender=gender;
    }


    public CarRentalUser() {
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


   public double getUserlatitude() {
        return userlatitude;
    }


    public void setUserlatitude(double userlatitude) {
        this.userlatitude = userlatitude;
    }


    public double getUserlongitude() {
        return userlongitude;
    }


    public void setUserlongitude(double userlongitude) {
        this.userlongitude = userlongitude;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public List<Booking> getBookings() {
        return bookings;
    }


    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public LocalDateTime getFcmUpdatedAt() {
        return fcmUpdatedAt;
    }

    public void setFcmUpdatedAt(LocalDateTime fcmUpdatedAt) {
        this.fcmUpdatedAt = fcmUpdatedAt;
    }

    
    
    



    
}
