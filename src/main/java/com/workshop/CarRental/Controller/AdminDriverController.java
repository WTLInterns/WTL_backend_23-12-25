package com.workshop.CarRental.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.Booking;
import com.workshop.Service.BookingService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AdminDriverController {

    private final BookingService bookingService;

    @Autowired
    public AdminDriverController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Returns bookings assigned to a specific Admin Driver (DriveAdmin)
    @GetMapping("/admin-driver/{id}")
    public ResponseEntity<List<Booking>> getAdminDriverBookings(@PathVariable("id") int driveAdminId) {
        List<Booking> bookings = bookingService.getBookingsByDriveAdminId(driveAdminId);
        return ResponseEntity.ok(bookings);
    }
}
