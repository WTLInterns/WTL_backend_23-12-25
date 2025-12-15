package com.workshop.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.Entity.CabAdmin;
import com.workshop.Entity.DriveAdmin;
import com.workshop.Entity.OutSourceCarCab;
import com.workshop.Service.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // Create or Update vehicle
    @PostMapping("/save")
    public ResponseEntity<OutSourceCarCab> saveVehicle(
        @RequestParam(value="vehicleNameAndRegNo",required = false) String vehicleNameAndRegNo,
//        @RequestParam("status") String status,
        @RequestParam(value="vehicleRcNo",required = false) String vehicleRcNo,
        @RequestParam(value="insurance",required = false) MultipartFile insurance,
        @RequestParam(value="permit",required = false) MultipartFile permit,
        @RequestParam(value="authorization",required = false) MultipartFile authorization,
        @RequestParam(value="carNoPlate",required = false) String carNoPlate,
        @RequestParam(value="carImage",required = false) MultipartFile carImage,
        @RequestParam(value="frontImage",required = false) MultipartFile frontImage,
        @RequestParam(value="backImage",required = false) MultipartFile backImage,
        @RequestParam(value="sideImage",required = false) MultipartFile sideImage,
        @RequestParam(value="carOtherDetails",required = false) String carOtherDetails) {

        System.out.println("Vehicle Name and RegNo: " + vehicleNameAndRegNo);
//        System.out.println("Status: " + status);
        // Add other parameters to debug

        // Your current logic
        OutSourceCarCab vehicle = new OutSourceCarCab();
        vehicle.setVehicleNameAndRegNo(vehicleNameAndRegNo);
        vehicle.setVehicleRcNo(vehicleRcNo);
        vehicle.setCarNoPlate(carNoPlate);
        vehicle.setCarOtherDetails(carOtherDetails);
        vehicle.setStatus("PENDING");

        try {
            // Your save logic here
            OutSourceCarCab savedVehicle = vehicleService.saveVehicle(vehicle, insurance, permit, authorization, carImage, frontImage, backImage, sideImage);
            return ResponseEntity.ok(savedVehicle);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    // Get all vehicles
    @GetMapping("/all")
    public ResponseEntity<List<OutSourceCarCab>> getAllVehicles() {
        List<OutSourceCarCab> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<OutSourceCarCab> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete vehicle by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    
    
    @PutMapping("/{id}/status")
   	public ResponseEntity<OutSourceCarCab> changeStatus( @PathVariable Long id, @RequestBody Map<String, String> requestBody) {

   	   
   	    String status = requestBody.get("status");

   	    try {
   	    	OutSourceCarCab updatedOrder = vehicleService.udpateStatus(id, status);
   	        return ResponseEntity.ok(updatedOrder);
   	    } catch (NoSuchElementException e) {
   	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
   	    }
   	}


    @GetMapping("/outsource/{status}")
    public ResponseEntity<?> getCabsByStatus(@PathVariable String status) {
        List<OutSourceCarCab> cabs = vehicleService.getCabByStatus(status);
        if (cabs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No cabs found with status: " + status);
        }
        return ResponseEntity.ok(cabs); // Return the list of cabs with HTTP 200 OK
    }
}