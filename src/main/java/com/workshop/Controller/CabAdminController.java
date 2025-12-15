package com.workshop.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.workshop.Entity.OutSourceCarCab;
import com.workshop.Service.CabAdminService;
import com.workshop.Service.VehicleService;

@RestController
@RequestMapping("/cabAdmin")
public class CabAdminController {

    @Autowired
    private CabAdminService cabAdminService;

    // Create or Update vehicle
    @PostMapping("/save")
    public ResponseEntity<CabAdmin> saveCabAdmin(
            @RequestParam(value="vehicleNameAndRegNo", required = false) String vehicleNameAndRegNo,
            @RequestParam(value="vehicleRcNo", required = false) String vehicleRcNo,
            @RequestParam(value="carOtherDetails", required = false) String carOtherDetails,
            @RequestParam(value="status", required = false) String status,
            // @RequestParam("vehicleName") String vehicleName,

            @RequestParam(value="vehicleRcImg", required = false) MultipartFile vehicleRcImg,
            @RequestParam(value="insurance", required = false) MultipartFile insurance,
            @RequestParam(value="permit", required = false) MultipartFile permit,
            @RequestParam(value="fitnessCert", required = false) MultipartFile fitnessCert,
            @RequestParam(value="cabImage", required = false) MultipartFile cabImage,
            @RequestParam(value="frontImage", required = false) MultipartFile frontImage,
            @RequestParam(value="backImage", required = false) MultipartFile backImage,
            @RequestParam(value="sideImage", required = false) MultipartFile sideImage

    ) {

        CabAdmin cabAdmin = new CabAdmin();
        cabAdmin.setVehicleNameAndRegNo(vehicleNameAndRegNo);
        cabAdmin.setVehicleRcNo(vehicleRcNo);
        cabAdmin.setCarOtherDetails(carOtherDetails);
        cabAdmin.setStatus(status);
        // cabAdmin.setVehicleName(vehicleName);

        try {
            // Prepare an array of images to pass to the service
            MultipartFile[] images = new MultipartFile[] {
                    vehicleRcImg, insurance, permit,
                    fitnessCert, frontImage, backImage, sideImage, cabImage
            };

            CabAdmin cabAdminSaved = cabAdminService.saveCabAdmin(cabAdmin, vehicleRcImg, insurance, permit,
                    fitnessCert, cabImage, frontImage, backImage, sideImage);
            return ResponseEntity.ok(cabAdminSaved);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Get all vehicles
    @GetMapping("/all")
    public ResponseEntity<List<CabAdmin>> getAllVehicles() {
        List<CabAdmin> vehicles = cabAdminService.getCabAdmin();
        return ResponseEntity.ok(vehicles);
    }

    // Get vehicle by ID
    @GetMapping("/{id}")
    public ResponseEntity<CabAdmin> getVehicleById(@PathVariable Long id) {
        return cabAdminService.getCabAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CabAdmin> changeStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {

        String status = requestBody.get("status");

        try {
            CabAdmin updatedOrder = cabAdminService.udpateStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/cab/{status}")
    public ResponseEntity<?> getCabsByStatus(@PathVariable String status) {
        List<CabAdmin> cabs = cabAdminService.getCabByStatus(status);
        if (cabs.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No cabs found with status: " + status);
        }
        return ResponseEntity.ok(cabs); // Return the list of cabs with HTTP 200 OK
    }

}