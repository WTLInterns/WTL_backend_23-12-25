package com.workshop.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Booking;
import com.workshop.Entity.Penalty;
import com.workshop.Entity.Vendor;
import com.workshop.Repo.BookingRepo;
import com.workshop.Repo.PenaltyRepo;
import com.workshop.Repo.VendorRepository;

@Service
public class PenaltyService {

    @Autowired
    private PenaltyRepo penaltyRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private VendorRepository vendorRepo;


   
    public List<Penalty> findPenaltyByVendorid(Long vendorId) {
        return this.penaltyRepo.findByVendorId(vendorId);
    }

    public List<Penalty> getAllPenalties(){
        return this.penaltyRepo.findAll();
    }

    public List<Penalty> getPenaltyByCompanyName(String name){
        return this.penaltyRepo.findByVendor_VendorCompanyName(name);
    }
}
