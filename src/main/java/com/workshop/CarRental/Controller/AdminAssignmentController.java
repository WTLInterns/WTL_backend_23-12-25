package com.workshop.CarRental.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Booking;
import com.workshop.Service.BookingService;

@RestController
@CrossOrigin(origins = "*")
public class AdminAssignmentController {

    private final BookingService bookingService;

    @Autowired
    public AdminAssignmentController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Assign an Admin Driver (DriveAdmin) via POST as requested
    // Body: {"driverAdminId": 303}
    @PostMapping("/booking/{bookingId}/assign-drive-admin")
    public ResponseEntity<?> assignDriveAdminPost(
            @PathVariable("bookingId") int bookingId,
            @RequestBody Map<String, Object> body
    ) {
        try {
            Object raw = body.get("driverAdminId");
            if (raw == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("status", "error", "message", "driverAdminId is required"));
            }
            int driveAdminId = Integer.parseInt(raw.toString());

            Booking updated = bookingService.assignDriveAdminToBooking(driveAdminId, bookingId);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "Booking or Admin Driver not found"));
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
