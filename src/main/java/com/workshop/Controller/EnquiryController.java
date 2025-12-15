package com.workshop.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Enquiry;
import com.workshop.Service.EnquiryService;


@RestController
public class EnquiryController {
   
    @Autowired
    private EnquiryService enquiryService;

    @PostMapping("/create-enquiry")
    public Enquiry createEnquiry(@RequestBody Enquiry enquiry){
        return this.enquiryService.save(enquiry);
    }

    @GetMapping("/getAllEnquiry")
    public List<Enquiry> getAllEnquiry(){
        return this.enquiryService.getAllEnquiry();
    }

    @GetMapping("/getEnquiryById/{id}")
    public Enquiry getEnquiryById(@PathVariable int id){
        return this.enquiryService.getEnquiryById(id);
    }

    @DeleteMapping("/deleteEnquiry/{id}")
    public void deleteEnquiry(@PathVariable int id){
        this.enquiryService.deleteEnquiry(id);
    }

   
}