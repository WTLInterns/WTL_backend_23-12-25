package com.workshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Penalty;
import com.workshop.Entity.Vendor;
import com.workshop.Repo.VendorRepository;
import com.workshop.Service.BookingService;
import com.workshop.Service.PenaltyService;

@RestController
public class PenaltyController {

    @Autowired
    private PenaltyService penaltyService;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/penalty/{bookingId}/{vendorId}")
    public Penalty createPenalty(@PathVariable int bookingId, @PathVariable Long vendorId, @RequestBody Penalty penalty){
        return this.bookingService.createPenalty(bookingId, vendorId, penalty);
    }

    @GetMapping("/getPenalty/{vendorId}")
    public List<Penalty> getPenaltyByVendor(@PathVariable Long vendorId){
        return this.penaltyService.findPenaltyByVendorid(vendorId);
    }

    @GetMapping("/getAllPenalties")
    public List<Penalty> getAllPenalties() {
        return this.penaltyService.getAllPenalties();
    }

    @GetMapping("/getByName/{name}")
    public List<Penalty> getPenaltyByName(@PathVariable String name){
        return this.penaltyService.getPenaltyByCompanyName(name);
    }
    
    
    
    

}
