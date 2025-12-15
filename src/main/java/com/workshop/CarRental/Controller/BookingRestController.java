package com.workshop.CarRental.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.workshop.CarRental.Entity.TripStatusMessage;
import com.workshop.CarRental.Service.WebTripService;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingRestController {

    private final WebTripService tripService;

    @Autowired
    public BookingRestController(WebTripService tripService) {
        this.tripService = tripService;
    }

    // REST fallback for both initial and final OTP verification
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, Object> body) {
        String bookingId = body.get("bookingId") != null ? body.get("bookingId").toString() : null;
        String otp = body.get("otp") != null ? body.get("otp").toString() : null;
        String otpType = body.get("otpType") != null ? body.get("otpType").toString() : "INITIAL";

        if (bookingId == null || bookingId.isEmpty() || otp == null || otp.length() != 6) {
            return ResponseEntity.badRequest().body("Invalid request: bookingId or otp is missing/invalid");
        }

        TripStatusMessage message = new TripStatusMessage();
        message.setBookingId(bookingId);
        message.setOtp(otp);

        if ("FINAL".equalsIgnoreCase(otpType)) {
            // For final OTP, end the trip
            tripService.endTrip(message);
            return ResponseEntity.ok("Final OTP verification processed");
        } else {
            // For initial OTP, verify start OTP
            tripService.verifyOtp(message);
            return ResponseEntity.ok("Initial OTP verification processed");
        }
    }

    // Start trip via REST (driver confirms start odometer)
    @PostMapping("/start-trip")
    public ResponseEntity<String> startTrip(@RequestBody Map<String, Object> body) {
        String bookingId = body.get("bookingId") != null ? body.get("bookingId").toString().trim() : null;
        if (bookingId == null || bookingId.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request: bookingId is missing");
        }

        Double startOdometer = null;
        Double destLat = null;
        Double destLng = null;
        Long driverId = null;
        Long userId = null;
        try {
            if (body.get("startOdometer") != null) {
                startOdometer = Double.parseDouble(body.get("startOdometer").toString());
            }
        } catch (NumberFormatException ignored) {}
        try {
            if (body.get("destinationLatitude") != null) {
                destLat = Double.parseDouble(body.get("destinationLatitude").toString());
            }
            if (body.get("destinationLongitude") != null) {
                destLng = Double.parseDouble(body.get("destinationLongitude").toString());
            }
        } catch (NumberFormatException ignored) {}
        try {
            if (body.get("driverId") != null) {
                driverId = Long.parseLong(body.get("driverId").toString());
            }
            if (body.get("userId") != null) {
                userId = Long.parseLong(body.get("userId").toString());
            }
        } catch (NumberFormatException ignored) {}

        TripStatusMessage message = new TripStatusMessage();
        message.setBookingId(bookingId);
        if (startOdometer != null) message.setStartOdometer(startOdometer);
        if (destLat != null) message.setDestinationLatitude(destLat);
        if (destLng != null) message.setDestinationLongitude(destLng);
        if (driverId != null) message.setDriverId(driverId);
        if (userId != null) message.setUserId(userId);

        tripService.startTrip(message);
        return ResponseEntity.ok("Trip start processed");
    }

    // REST endpoint to request final OTP (driver asks user to display OTP)
    @PostMapping("/request-final-otp")
    public ResponseEntity<String> requestFinalOtp(@RequestBody Map<String, Object> body) {
        String bookingId = body.get("bookingId") != null ? body.get("bookingId").toString() : null;
        Double endOdometer = null;
        try {
            if (body.get("endOdometer") != null) {
                endOdometer = Double.parseDouble(body.get("endOdometer").toString());
            }
        } catch (NumberFormatException ignored) {}

        if (bookingId == null || bookingId.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid request: bookingId is missing");
        }

        TripStatusMessage message = new TripStatusMessage();
        message.setBookingId(bookingId);
        message.setAction("REQUEST_FINAL_OTP");
        if (endOdometer != null) {
            message.setEndOdometer(endOdometer);
        }

        tripService.sendFinalOtp(message);
        return ResponseEntity.ok("Final OTP request sent");
    }
}
