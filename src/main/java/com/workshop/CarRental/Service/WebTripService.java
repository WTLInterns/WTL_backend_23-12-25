package com.workshop.CarRental.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.workshop.CarRental.Entity.TripStatusMessage;
import com.workshop.Repo.BookingRepo;
import com.workshop.Entity.Booking;
import com.workshop.Entity.TripStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class WebTripService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final BookingRepo bookingRepo;
    private final Map<String, String> bookingOtps = new HashMap<>();
    private final Map<String, String> finalOtps = new HashMap<>();
    
    @Autowired
    public WebTripService(SimpMessagingTemplate messagingTemplate, BookingRepo bookingRepo) {
        this.messagingTemplate = messagingTemplate;
        this.bookingRepo = bookingRepo;
    }
    
    /**
     * Handles different OTP actions including storing user-generated OTPs
     */
    public void sendOtp(TripStatusMessage message) {
        String rawBookingId = message.getBookingId();
        String bookingId = rawBookingId != null ? rawBookingId.trim() : null;
        if ("STORE_OTP".equals(message.getAction())) {
            // Store OTP generated on user side when driver is nearby
            if (bookingId == null) {
                System.out.println("[OTP] STORE_OTP received with null bookingId");
                return;
            }
            bookingOtps.put(bookingId, message.getOtp());
            System.out.println("[OTP] Stored start OTP for booking " + bookingId + ": " + message.getOtp() +
                " | mapSize=" + bookingOtps.size());
            
            // Update booking with start OTP
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setStartOtp(message.getOtp());
                booking.setDriverEnterOtpTimePreStarted(LocalDateTime.now());
                bookingRepo.save(booking);
            }
            
        } else if ("STORE_FINAL_OTP".equals(message.getAction())) {
            // Store final OTP for trip end verification
            if (bookingId == null) {
                System.out.println("[OTP] STORE_FINAL_OTP received with null bookingId");
                return;
            }
            finalOtps.put(bookingId, message.getOtp());
            System.out.println("[OTP] Stored final OTP for booking " + bookingId + ": " + message.getOtp());
            
            // Update booking with end OTP
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(message.getBookingId());
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setEndOtp(message.getOtp());
                booking.setDriverEnterOtpTimePostTrip(LocalDateTime.now());
                bookingRepo.save(booking);
            }

            // Notify driver that final OTP is ready (ack)
            TripStatusMessage driverAck = new TripStatusMessage();
            driverAck.setBookingId(bookingId);
            driverAck.setAction("FINAL_OTP_READY");
            driverAck.setType("FINAL_OTP_READY");
            driverAck.setDriverId(message.getDriverId());
            driverAck.setUserId(message.getUserId());
            messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId + "/driver-notifications",
                driverAck
            );
            
        } else {
            // Legacy manual OTP generation
            String otp = generateOtp();
            if (bookingId == null) {
                System.out.println("[OTP] AUTO generation received with null bookingId");
                return;
            }
            bookingOtps.put(bookingId, otp);
            System.out.println("[OTP] Auto-generated and stored start OTP for booking " + bookingId + ": " + otp);
            
            // Update booking with start OTP
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(message.getBookingId());
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setStartOtp(otp);
                booking.setDriverEnterOtpTimePreStarted(LocalDateTime.now());
                bookingRepo.save(booking);
            }
            
            TripStatusMessage userResponse = new TripStatusMessage();
            userResponse.setBookingId(bookingId);
            userResponse.setAction("OTP_SENT");
            userResponse.setOtp(otp);
            userResponse.setType("OTP_SENT");
            userResponse.setDriverId(message.getDriverId());
            userResponse.setUserId(message.getUserId());
            
            messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId + "/user-notifications", 
                userResponse
            );
        }
    }
    
    /**
     * Handles final OTP generation or storage for trip end verification
     */
    public void sendFinalOtp(TripStatusMessage message) {
        String rawBookingId = message.getBookingId();
        String bookingId = rawBookingId != null ? rawBookingId.trim() : null;
        if ("REQUEST_FINAL_OTP".equals(message.getAction())) {
            // This is a request from driver to generate final OTP for the user
            TripStatusMessage userResponse = new TripStatusMessage();
            userResponse.setBookingId(bookingId);
            userResponse.setAction("REQUEST_FINAL_OTP");
            userResponse.setType("REQUEST_FINAL_OTP");
            userResponse.setDriverId(message.getDriverId());
            userResponse.setUserId(message.getUserId());
            
            // Send request to user to display final OTP
            messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId + "/user-notifications", 
                userResponse
            );

        } else if ("STORE_FINAL_OTP".equals(message.getAction())) {
            // This is the final OTP generated on user side, store it for verification
            if (bookingId == null) {
                System.out.println("[OTP] STORE_FINAL_OTP received with null bookingId");
                return;
            }
            finalOtps.put(bookingId, message.getOtp());
            System.out.println("[OTP] Stored final OTP for booking " + bookingId + ": " + message.getOtp());
            
            // Update booking with end OTP
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setEndOtp(message.getOtp());
                booking.setDriverEnterOtpTimePostTrip(LocalDateTime.now());
                bookingRepo.save(booking);
            }

            // Notify driver that final OTP is ready (ack)
            TripStatusMessage driverAck = new TripStatusMessage();
            driverAck.setBookingId(bookingId);
            driverAck.setAction("FINAL_OTP_READY");
            driverAck.setType("FINAL_OTP_READY");
            driverAck.setDriverId(message.getDriverId());
            driverAck.setUserId(message.getUserId());
            messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId + "/driver-notifications",
                driverAck
            );
        }
    }
    
    /**
     * Verifies OTP entered by driver
     */
    public void verifyOtp(TripStatusMessage message) {
        String bookingId = message.getBookingId() != null ? message.getBookingId().trim() : null;
        String providedOtp = message.getOtp();
        String storedOtp = bookingId != null ? bookingOtps.get(bookingId) : null;
        boolean isValid = storedOtp != null && storedOtp.equals(providedOtp);
        System.out.println("[OTP] Verify start OTP | bookingId=" + bookingId + ", provided=" + providedOtp + ", stored(mem)=" + storedOtp + ", valid(mem)=" + isValid);
        if (storedOtp == null) {
            System.out.println("[OTP] No in-memory start OTP for bookingId=" + bookingId + ". Trying DB fallback...");
            Optional<Booking> db = bookingId != null ? bookingRepo.findByBookingId(bookingId) : Optional.empty();
            if (db.isPresent()) {
                String dbOtp = db.get().getStartOtp();
                boolean dbValid = dbOtp != null && dbOtp.equals(providedOtp);
                System.out.println("[OTP] DB fallback start OTP | bookingId=" + bookingId + ", stored(db)=" + dbOtp + ", valid(db)=" + dbValid);
                isValid = dbValid;
            } else {
                System.out.println("[OTP] DB fallback: booking not found for bookingId=" + bookingId);
            }
        }
        
        TripStatusMessage response = new TripStatusMessage();
        response.setBookingId(bookingId);
        response.setAction(isValid ? "OTP_VERIFIED" : "OTP_INVALID");
        response.setType(isValid ? "OTP_VERIFIED" : "OTP_INVALID");
        response.setDriverId(message.getDriverId());
        response.setUserId(message.getUserId());
        
        // Notify both user and driver about verification result
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/user-notifications", 
            response
        );
        
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/driver-notifications", 
            response
        );
        
        // If OTP is verified, remove it from storage and update booking status
        if (isValid) {
            bookingOtps.remove(bookingId);
            
            // Update booking status to indicate OTP verified and ready for trip start
            Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setTripStatus(TripStatus.DRIVER_ARRIVED);
                bookingRepo.save(booking);
            }
        }
    }
    
    /**
     * Records start odometer and begins the trip
     */
    public void startTrip(TripStatusMessage message) {
        String rawBookingId = message.getBookingId();
        String bookingId = rawBookingId != null ? rawBookingId.trim() : null;
        System.out.println("[TRIP] StartTrip called for bookingId=" + bookingId + ", startOdo=" + message.getStartOdometer());
        // Update booking with trip start information
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setTripStatus(TripStatus.TRIP_STARTED);
            booking.setStartOdometer(message.getStartOdometer() != null ? message.getStartOdometer().toString() : "0");
            booking.setOdoometerEnterTimeStarted(LocalDateTime.now());
            bookingRepo.save(booking);
        }
        
        TripStatusMessage response = new TripStatusMessage();
        response.setBookingId(bookingId);
        response.setAction("TRIP_STARTED");
        response.setType("TRIP_STARTED");
        response.setStartOdometer(message.getStartOdometer());
        response.setDestination(message.getDestination());
        response.setDestinationLatitude(message.getDestinationLatitude());
        response.setDestinationLongitude(message.getDestinationLongitude());
        response.setDriverId(message.getDriverId());
        response.setUserId(message.getUserId());
        
        // Notify both user and driver that trip has started
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/user-notifications", 
            response
        );
        
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/driver-notifications", 
            response
        );
    }
    
    /**
     * Handles trip end with final OTP verification
     */
    public void endTrip(TripStatusMessage message) {
        String bookingId = message.getBookingId() != null ? message.getBookingId().trim() : null;
        String providedOtp = message.getOtp();
        String storedFinalOtp = bookingId != null ? finalOtps.get(bookingId) : null;
        boolean isValid = storedFinalOtp != null && storedFinalOtp.equals(providedOtp);
        System.out.println("[OTP] Verify final OTP | bookingId=" + bookingId + ", provided=" + providedOtp + ", stored(mem)=" + storedFinalOtp + ", valid(mem)=" + isValid);
        if (storedFinalOtp == null) {
            System.out.println("[OTP] No in-memory final OTP for bookingId=" + bookingId + ". Trying DB fallback...");
            Optional<Booking> db = bookingId != null ? bookingRepo.findByBookingId(bookingId) : Optional.empty();
            if (db.isPresent()) {
                String dbOtp = db.get().getEndOtp();
                boolean dbValid = dbOtp != null && dbOtp.equals(providedOtp);
                System.out.println("[OTP] DB fallback final OTP | bookingId=" + bookingId + ", stored(db)=" + dbOtp + ", valid(db)=" + dbValid);
                isValid = dbValid;
            } else {
                System.out.println("[OTP] DB fallback: booking not found for bookingId=" + bookingId);
            }
        }
        
        if (!isValid) {
            TripStatusMessage response = new TripStatusMessage();
            response.setBookingId(bookingId);
            response.setAction("FINAL_OTP_INVALID");
            response.setType("FINAL_OTP_INVALID");
            response.setDriverId(message.getDriverId());
            response.setUserId(message.getUserId());
            messagingTemplate.convertAndSend(
                "/topic/booking/" + bookingId + "/driver-notifications", 
                response
            );
            return;
        }
        
        double startOdo = message.getStartOdometer() != null ? message.getStartOdometer() : 0;
        double endOdo = message.getEndOdometer() != null ? message.getEndOdometer() : 0;
        
        // Update booking with trip end information
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setTripStatus(TripStatus.TRIP_ENDED);
            booking.setEndOdometer(message.getEndOdometer() != null ? message.getEndOdometer().toString() : "0");
            booking.setOdoometerEnterTimeEnding(LocalDateTime.now());
            booking.setStatus(3); // Completed status
            bookingRepo.save(booking);
        }
        
        TripStatusMessage response = new TripStatusMessage();
        response.setBookingId(bookingId);
        response.setAction("TRIP_ENDED");
        response.setType("TRIP_ENDED");
        response.setStartOdometer(startOdo);
        response.setEndOdometer(endOdo);
        response.setDriverId(message.getDriverId());
        response.setUserId(message.getUserId());
        
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/user-notifications", 
            response
        );
        
        messagingTemplate.convertAndSend(
            "/topic/booking/" + bookingId + "/driver-notifications", 
            response
        );
        
        // Clean up OTP storage
        finalOtps.remove(bookingId);
    }

    /**
     * Generates a 6-digit OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    /**
     * Get current trip status for a booking
     */
    public TripStatusMessage getTripStatus(String bookingId) {
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            TripStatusMessage statusMessage = new TripStatusMessage();
            statusMessage.setBookingId(bookingId);
            statusMessage.setAction("TRIP_STATUS");
            statusMessage.setType("STATUS_UPDATE");
            statusMessage.setStartOdometer(booking.getStartOdometer() != null ? 
                Double.parseDouble(booking.getStartOdometer()) : null);
            statusMessage.setEndOdometer(booking.getEndOdometer() != null ? 
                Double.parseDouble(booking.getEndOdometer()) : null);
            
            return statusMessage;
        }
        return null;
    }
}
 