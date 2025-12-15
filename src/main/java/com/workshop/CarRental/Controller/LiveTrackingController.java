package com.workshop.CarRental.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.CarRental.Entity.LocationMessage;
import com.workshop.CarRental.Entity.TripStatusMessage;
import com.workshop.CarRental.Service.LocationService;
import com.workshop.CarRental.Service.WebTripService;

@RestController
public class LiveTrackingController {

    private final LocationService locationService;
    private final WebTripService tripService;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public LiveTrackingController(LocationService locationService, WebTripService tripService, SimpMessagingTemplate messagingTemplate) {
        this.locationService = locationService;
        this.tripService = tripService;
        this.messagingTemplate = messagingTemplate;
    }

    
    @MessageMapping("/driver-location")
    public void updateDriverLocation(@Payload LocationMessage locationMessage) {
        try {
            System.out.println("📨 [/app/driver-location] Received: bookingId=" + locationMessage.getBookingId()
                    + ", driverId=" + locationMessage.getDriverId()
                    + ", lat=" + locationMessage.getLatitude()
                    + ", lng=" + locationMessage.getLongitude());

            // Basic validation to avoid broadcasting bad payloads
            if (locationMessage.getBookingId() == null || locationMessage.getBookingId().isEmpty()) {
                System.err.println("❌ Missing bookingId in driver location payload. Skipping broadcast.");
                return;
            }
            if (locationMessage.getLatitude() == 0.0 && locationMessage.getLongitude() == 0.0) {
                System.err.println("⚠️ Invalid coordinates (0,0) in driver location payload. Skipping save/broadcast.");
                return;
            }

            locationService.saveDriverLocation(
                    locationMessage.getDriverId(),
                    locationMessage.getLatitude(),
                    locationMessage.getLongitude()
            );

            locationService.updateDriverLocation(locationMessage);
        } catch (Exception e) {
            System.err.println("❌ Error handling /driver-location message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    @MessageMapping("/user-location")
    public void updateUserLocation(@Payload LocationMessage locationMessage) {
        try {
            System.out.println("📨 [/app/user-location] Received: bookingId=" + locationMessage.getBookingId()
                    + ", userId=" + locationMessage.getUserId()
                    + ", lat=" + locationMessage.getLatitude()
                    + ", lng=" + locationMessage.getLongitude());

            if (locationMessage.getBookingId() == null || locationMessage.getBookingId().isEmpty()) {
                System.err.println("❌ Missing bookingId in user location payload. Skipping broadcast.");
                return;
            }
            if (locationMessage.getLatitude() == 0.0 && locationMessage.getLongitude() == 0.0) {
                System.err.println("⚠️ Invalid coordinates (0,0) in user location payload. Skipping save/broadcast.");
                return;
            }

            locationService.saveUserLocation(
                    locationMessage.getUserId(),
                    locationMessage.getLatitude(),
                    locationMessage.getLongitude()
            );
            
            locationService.updateUserLocation(locationMessage);
        } catch (Exception e) {
            System.err.println("❌ Error handling /user-location message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    @MessageMapping("/start-otp")
    public void sendOtp(@Payload TripStatusMessage message) {
       
            tripService.sendOtp(message);
        
    }

    /**
     * Handles OTP verification from driver
     */
    @MessageMapping("/verify-otp")
    public void verifyOtp(@Payload TripStatusMessage message) {
        tripService.verifyOtp(message);
    }

    /**
     * Final OTP: request/store via a dedicated endpoint
     */
    @MessageMapping("/final-otp")
    public void handleFinalOtp(@Payload TripStatusMessage message) {
        // Delegates to WebTripService.sendFinalOtp which handles REQUEST_FINAL_OTP and STORE_FINAL_OTP
        tripService.sendFinalOtp(message);
    }

    /**
     * Verify final OTP and complete trip via a dedicated endpoint
     */
    @MessageMapping("/verify-final-otp")
    public void verifyFinalOtp(@Payload TripStatusMessage message) {
        // WebTripService.endTrip() validates final OTP and publishes TRIP_ENDED or FINAL_OTP_INVALID
        tripService.endTrip(message);
    }

    /**
     * Acknowledge client connection events.
     * Clients send a message to /app/connect with at least bookingId, userId, userType.
     * We broadcast a CONNECTED ack to the appropriate booking topic so clients can confirm presence.
     */
    @MessageMapping("/connect")
    public void handleConnect(@Payload Map<String, Object> message) {
        try {
            String bookingId = message.get("bookingId") != null ? String.valueOf(message.get("bookingId")) : null;
            String userType = message.get("userType") != null ? String.valueOf(message.get("userType")) : "";

            Map<String, Object> ack = new HashMap<>();
            ack.put("type", "CONNECTED");
            ack.put("action", "CONNECTED");
            ack.put("bookingId", bookingId);
            ack.put("userId", message.get("userId"));
            ack.put("userType", userType);
            ack.put("timestamp", System.currentTimeMillis());

            if (bookingId != null && !bookingId.isEmpty()) {
                if ("DRIVER".equalsIgnoreCase(userType)) {
                    messagingTemplate.convertAndSend("/topic/booking/" + bookingId + "/driver-notifications", ack);
                } else {
                    messagingTemplate.convertAndSend("/topic/booking/" + bookingId + "/user-notifications", ack);
                }
            } else {
                // If no bookingId provided, fall back to an admin-wide notification channel
                messagingTemplate.convertAndSend("/topic/admin-notifications", ack);
            }
        } catch (Exception e) {
            System.err.println("❌ Error handling /connect message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles trip start (recording odometer and destination)
     */
    @MessageMapping("/start-trip")
    public void startTrip(@Payload TripStatusMessage message) {
        tripService.startTrip(message);
    }

    /**
     * Handles trip end (recording odometer and final OTP verification)
     */
    @MessageMapping("/end-trip")
    public void endTrip(@Payload TripStatusMessage message) {
        tripService.endTrip(message);
    }

    @MessageMapping("/admin-notifications")
    @SendTo("/topic/admin-notifications")
    public Map<String, Object> handleAdminNotifications(Map<String, Object> message) {
        String type = (String) message.get("type");
        String userId = (String) message.get("userId");
        
        System.out.println("🔔 Admin notification request: " + type + " from user: " + userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("type", "ADMIN_SUBSCRIBED");
        response.put("userId", userId);
        response.put("timestamp", System.currentTimeMillis());
        
        return response;
    }

    // Method to send real-time booking notification to admin
    public void sendAdminBookingNotification(String bookingId, String customerName, 
                                           String pickup, String drop, String amount, 
                                           String carType, String tripType, String phone) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "NEW_BOOKING");
            notification.put("timestamp", System.currentTimeMillis());
            
            Map<String, Object> booking = new HashMap<>();
            booking.put("bookingId", bookingId);
            booking.put("customerName", customerName);
            booking.put("customerPhone", phone);
            booking.put("pickup", pickup);
            booking.put("drop", drop);
            booking.put("amount", amount);
            booking.put("carType", carType);
            booking.put("tripType", tripType);
            
            notification.put("booking", booking);
            
            System.out.println("📡 Sending WebSocket notification to admin: " + bookingId);
            messagingTemplate.convertAndSend("/topic/admin-notifications", notification);
            
        } catch (Exception e) {
            System.err.println("❌ Error sending WebSocket notification to admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 