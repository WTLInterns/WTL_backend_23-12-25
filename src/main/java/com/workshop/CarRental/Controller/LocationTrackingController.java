package com.workshop.CarRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workshop.CarRental.Entity.LocationMessage;
import com.workshop.CarRental.Entity.TripStatusMessage;
import com.workshop.CarRental.Service.LocationService;
import com.workshop.CarRental.Service.WebTripService;
import com.workshop.Repo.BookingRepo;
import com.workshop.Entity.Booking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
public class LocationTrackingController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private WebTripService webTripService;

    @Autowired
    private BookingRepo bookingRepo;

    /**
     * Update driver location via REST API
     */
    @PostMapping("/driver/update")
    public ResponseEntity<Map<String, Object>> updateDriverLocation(@RequestBody LocationMessage locationMessage) {
        try {
            locationService.updateDriverLocation(locationMessage);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Driver location updated successfully");
            response.put("bookingId", locationMessage.getBookingId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update driver location: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Update user location via REST API
     */
    @PostMapping("/user/update")
    public ResponseEntity<Map<String, Object>> updateUserLocation(@RequestBody LocationMessage locationMessage) {
        try {
            locationService.updateUserLocation(locationMessage);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User location updated successfully");
            response.put("bookingId", locationMessage.getBookingId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update user location: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get current driver location
     */
    @GetMapping("/driver/{driverId}/booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> getDriverLocation(
            @PathVariable Long driverId, 
            @PathVariable String bookingId) {
        try {
            LocationMessage location = locationService.getDriverLocation(driverId, bookingId);
            
            Map<String, Object> response = new HashMap<>();
            if (location != null) {
                response.put("status", "success");
                response.put("location", location);
            } else {
                response.put("status", "error");
                response.put("message", "Driver location not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to get driver location: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get current user location
     */
    @GetMapping("/user/{userId}/booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> getUserLocation(
            @PathVariable Long userId, 
            @PathVariable String bookingId) {
        try {
            LocationMessage location = locationService.getUserLocation(userId, bookingId);
            
            Map<String, Object> response = new HashMap<>();
            if (location != null) {
                response.put("status", "success");
                response.put("location", location);
            } else {
                response.put("status", "error");
                response.put("message", "User location not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to get user location: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get trip status for a booking
     */
    @GetMapping("/trip/status/{bookingId}")
    public ResponseEntity<Map<String, Object>> getTripStatus(@PathVariable String bookingId) {
        try {
            TripStatusMessage status = webTripService.getTripStatus(bookingId);
            
            Map<String, Object> response = new HashMap<>();
            if (status != null) {
                response.put("status", "success");
                response.put("tripStatus", status);
            } else {
                response.put("status", "error");
                response.put("message", "Trip status not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to get trip status: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get booking details with location information
     */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Map<String, Object>> getBookingWithLocations(@PathVariable String bookingId) {
        try {
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
            
            Map<String, Object> response = new HashMap<>();
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                
                // Get driver location
                LocationMessage driverLocation = null;
                if (booking.getDriveAdmin() != null) {
                    driverLocation = locationService.getDriverLocation(
                        (long) booking.getDriveAdmin().getId(), bookingId);
                } else if (booking.getVendorDriver() != null) {
                    driverLocation = locationService.getDriverLocation(
                        (long) booking.getVendorDriver().getVendorDriverId(), bookingId);
                }
                
                // Get user location
                LocationMessage userLocation = null;
                if (booking.getCarRentalUser() != null) {
                    userLocation = locationService.getUserLocation(
                        (long) booking.getCarRentalUser().getId(), bookingId);
                }
                
                response.put("status", "success");
                response.put("booking", booking);
                response.put("driverLocation", driverLocation);
                response.put("userLocation", userLocation);
            } else {
                response.put("status", "error");
                response.put("message", "Booking not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to get booking details: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Calculate distance between two points
     */
    @PostMapping("/calculate-distance")
    public ResponseEntity<Map<String, Object>> calculateDistance(@RequestBody Map<String, Object> request) {
        try {
            double lat1 = Double.parseDouble(request.get("lat1").toString());
            double lon1 = Double.parseDouble(request.get("lon1").toString());
            double lat2 = Double.parseDouble(request.get("lat2").toString());
            double lon2 = Double.parseDouble(request.get("lon2").toString());
            
            double distance = locationService.calculateDistance(lat1, lon1, lat2, lon2);
            int estimatedTime = locationService.estimateTime(distance, 40.0);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("distance", distance);
            response.put("estimatedTime", estimatedTime);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to calculate distance: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Check if driver is within proximity of user
     */
    @PostMapping("/check-proximity")
    public ResponseEntity<Map<String, Object>> checkProximity(@RequestBody Map<String, Object> request) {
        try {
            String bookingId = request.get("bookingId").toString();
            double proximityThreshold = 0.05; // 50 meters
            
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
            
            Map<String, Object> response = new HashMap<>();
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                
                if (booking.getCarRentalUser() != null) {
                    // Get driver location
                    LocationMessage driverLocation = null;
                    if (booking.getDriveAdmin() != null) {
                        driverLocation = locationService.getDriverLocation(
                            (long) booking.getDriveAdmin().getId(), bookingId);
                    } else if (booking.getVendorDriver() != null) {
                        driverLocation = locationService.getDriverLocation(
                            (long) booking.getVendorDriver().getVendorDriverId(), bookingId);
                    }
                    
                    if (driverLocation != null) {
                        double distance = locationService.calculateDistance(
                            driverLocation.getLatitude(),
                            driverLocation.getLongitude(),
                            booking.getCarRentalUser().getUserlatitude(),
                            booking.getCarRentalUser().getUserlongitude()
                        );
                        
                        boolean isWithinProximity = distance <= proximityThreshold;
                        
                        response.put("status", "success");
                        response.put("distance", distance);
                        response.put("isWithinProximity", isWithinProximity);
                        response.put("proximityThreshold", proximityThreshold);
                    } else {
                        response.put("status", "error");
                        response.put("message", "Driver location not available");
                    }
                } else {
                    response.put("status", "error");
                    response.put("message", "User location not available");
                }
            } else {
                response.put("status", "error");
                response.put("message", "Booking not found");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to check proximity: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
