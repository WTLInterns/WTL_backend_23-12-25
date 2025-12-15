package com.workshop.CarRental.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workshop.CarRental.Entity.LocationMessage;
import com.workshop.CarRental.Entity.TripStatusMessage;
import com.workshop.CarRental.Repository.CarRentalRepository;
import com.workshop.Repo.BookingRepo;
import com.workshop.Repo.DriveAdminRepository;
import com.workshop.Repo.VendorDriversRepository;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.Entity.Booking;
import com.workshop.Entity.DriveAdmin;
import com.workshop.Entity.VendorDrivers;
import com.workshop.Entity.TripStatus;

import java.util.Optional;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class LocationService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final CarRentalRepository carRentalRepository;
    private final DriveAdminRepository driveAdminRepository;
    private final VendorDriversRepository vendorDriversRepository;
    private final BookingRepo bookingRepo;
    private final Map<String, String> proximityOtps = new HashMap<>();
    
    @Autowired
    public LocationService(SimpMessagingTemplate messagingTemplate,
                           CarRentalRepository carRentalRepository,
                           DriveAdminRepository driveAdminRepository,
                           VendorDriversRepository vendorDriversRepository,
                           BookingRepo bookingRepo) {
        this.messagingTemplate = messagingTemplate;
        this.carRentalRepository = carRentalRepository;
        this.driveAdminRepository = driveAdminRepository;
        this.vendorDriversRepository = vendorDriversRepository;
        this.bookingRepo = bookingRepo;
    }
    
    /**
     * Updates driver location and broadcasts to the user
     * Also checks proximity and triggers OTP if driver is within 50m
     */
    @Transactional
    public void updateDriverLocation(LocationMessage locationMessage) {
        try {
            System.out.println("📥 [DriverLoc][IN] bookingId=" + locationMessage.getBookingId()
                    + ", driverId=" + locationMessage.getDriverId()
                    + ", lat=" + locationMessage.getLatitude()
                    + ", lng=" + locationMessage.getLongitude());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to log incoming driver location: " + e.getMessage());
        }
        // Save location to database
        saveDriverLocation(locationMessage.getDriverId(), locationMessage.getLatitude(), locationMessage.getLongitude());
        
        // Calculate distance and estimated time
        double distance = 0.0;
        try {
            distance = calculateDistanceToUser(locationMessage);
        } catch (Exception ex) {
            System.err.println("⚠️ Failed to calculate distance to user (continuing without distance): " + ex.getMessage());
        }
        int estimatedTime = estimateTime(distance, 40.0); // 40 km/h average speed
        
        locationMessage.setDistance(distance);
        locationMessage.setEstimatedTime(estimatedTime);
        
        // Check if driver is within 50m of user
        if (distance <= 0.05) { // 50 meters = 0.05 km
            checkAndTriggerProximityAlert(locationMessage);
        }
        
        // Ensure client can identify this as a driver update
        locationMessage.setUserType("DRIVER");
        
        // Send location to the specific user subscribed to this booking
        String destination = "/topic/booking/" + locationMessage.getBookingId() + "/driver-location";
        try {
            System.out.println("📤 [DriverLoc][OUT] -> " + destination +
                    " payload={bookingId=" + locationMessage.getBookingId() +
                    ", driverId=" + locationMessage.getDriverId() +
                    ", lat=" + locationMessage.getLatitude() +
                    ", lng=" + locationMessage.getLongitude() +
                    ", distanceKm=" + locationMessage.getDistance() +
                    ", etaMin=" + locationMessage.getEstimatedTime() + "}");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to log outgoing driver location: " + e.getMessage());
        }
        messagingTemplate.convertAndSend(destination, locationMessage);
    }
    
    /**
     * Updates user location and broadcasts to the driver
     */
    public void updateUserLocation(LocationMessage locationMessage) {
        try {
            System.out.println("📥 [UserLoc][IN] bookingId=" + locationMessage.getBookingId()
                    + ", userId=" + locationMessage.getUserId()
                    + ", lat=" + locationMessage.getLatitude()
                    + ", lng=" + locationMessage.getLongitude());
        } catch (Exception e) {
            System.err.println("⚠️ Failed to log incoming user location: " + e.getMessage());
        }
        // Save location to database
        saveUserLocation(locationMessage.getUserId(), locationMessage.getLatitude(), locationMessage.getLongitude());
        
        // Ensure client can identify this as a user update
        locationMessage.setUserType("USER");
        
        // Send location to the specific driver subscribed to this booking
        String destination = "/topic/booking/" + locationMessage.getBookingId() + "/user-location";
        try {
            System.out.println("📤 [UserLoc][OUT] -> " + destination +
                    " payload={bookingId=" + locationMessage.getBookingId() +
                    ", userId=" + locationMessage.getUserId() +
                    ", lat=" + locationMessage.getLatitude() +
                    ", lng=" + locationMessage.getLongitude() + "}");
        } catch (Exception e) {
            System.err.println("⚠️ Failed to log outgoing user location: " + e.getMessage());
        }
        messagingTemplate.convertAndSend(destination, locationMessage);
    }
    
    /**
     * Checks if driver is within proximity and triggers OTP generation
     */
    private void checkAndTriggerProximityAlert(LocationMessage locationMessage) {
        // Find the booking
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(locationMessage.getBookingId());
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // Only trigger if trip hasn't started yet and OTP hasn't been generated
            if ((booking.getTripStatus() == TripStatus.DRIVER_ON_THE_WAY || 
                booking.getTripStatus() == TripStatus.ASSIGNED) &&
                !proximityOtps.containsKey(locationMessage.getBookingId())) {
                
                // Generate OTP automatically
                String autoOtp = generateOtp();
                proximityOtps.put(locationMessage.getBookingId(), autoOtp);
                
                // Update booking status to DRIVER_ARRIVED and store OTP
                booking.setTripStatus(TripStatus.DRIVER_ARRIVED);
                booking.setStartOtp(autoOtp);
                booking.setDriverEnterOtpTimePreStarted(LocalDateTime.now());
                bookingRepo.save(booking);
                
                // Send OTP to user for display
                TripStatusMessage otpMessage = new TripStatusMessage();
                otpMessage.setBookingId(locationMessage.getBookingId());
                otpMessage.setAction("AUTO_OTP_GENERATED");
                otpMessage.setType("OTP_GENERATED");
                otpMessage.setOtp(autoOtp);
                otpMessage.setDriverId(locationMessage.getDriverId());
                otpMessage.setUserId(locationMessage.getUserId());
                
                messagingTemplate.convertAndSend(
                    "/topic/booking/" + locationMessage.getBookingId() + "/user-notifications", 
                    otpMessage
                );
                
                // Send notification to driver
                TripStatusMessage driverAlert = new TripStatusMessage();
                driverAlert.setBookingId(locationMessage.getBookingId());
                driverAlert.setAction("NEAR_USER");
                driverAlert.setType("PROXIMITY_ALERT");
                driverAlert.setDriverId(locationMessage.getDriverId());
                driverAlert.setUserId(locationMessage.getUserId());
                
                messagingTemplate.convertAndSend(
                    "/topic/booking/" + locationMessage.getBookingId() + "/driver-notifications", 
                    driverAlert
                );
            }
        }
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
     * Get stored proximity OTP for verification
     */
    public String getProximityOtp(String bookingId) {
        return proximityOtps.get(bookingId);
    }
    
    /**
     * Remove proximity OTP after verification
     */
    public void removeProximityOtp(String bookingId) {
        proximityOtps.remove(bookingId);
    }
    
    /**
     * Calculates distance between driver and user for a specific booking
     */
    private double calculateDistanceToUser(LocationMessage locationMessage) {
        Optional<Booking> bookingOpt = bookingRepo.findByBookingId(locationMessage.getBookingId());
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            CarRentalUser user = booking.getCarRentalUser();
            if (user != null) {
                return calculateDistance(
                    locationMessage.getLatitude(), 
                    locationMessage.getLongitude(),
                    user.getUserlatitude(), 
                    user.getUserlongitude()
                );
            }
        }
        return 0.0;
    }
    
    /**
     * Updates the database with the driver's location
     */  
    public void saveDriverLocation(Long driverId, double latitude, double longitude) {
        // Try to update DriveAdmin first
        driveAdminRepository.findById(driverId.intValue()).ifPresent(admin -> {
            admin.setDriverLatitude(latitude);
            admin.setDriverLongitude(longitude);
            driveAdminRepository.save(admin);
        });
        
        // Try to update VendorDrivers if not found in DriveAdmin
        vendorDriversRepository.findById(driverId.intValue()).ifPresent(vendor -> {
            vendor.setDriverLatitude(latitude);
            vendor.setDriverLongitude(longitude);
            vendorDriversRepository.save(vendor);
        });
    }
    
    /**
     * Updates the database with the user's location
     */
    public void saveUserLocation(Long userId, double latitude, double longitude) {
        carRentalRepository.findById(userId.intValue()).ifPresent(user -> {
            user.setUserlatitude(latitude);
            user.setUserlongitude(longitude);
            carRentalRepository.save(user);
        });
    }
    
    /**
     * Calculates distance between two points using Haversine formula
     */
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in km
    }
    
    /**
     * Estimate time based on distance and average speed
     */
    public int estimateTime(double distanceInKm, double avgSpeedKmh) {
        if (avgSpeedKmh <= 0) {
            avgSpeedKmh = 40; // Default average speed in city
        }
        
        // Time in minutes = (distance / speed) * 60
        return (int) ((distanceInKm / avgSpeedKmh) * 60);
    }
    
    /**
     * Get current location of a driver
     */
    public LocationMessage getDriverLocation(Long driverId, String bookingId) {
        LocationMessage locationMessage = new LocationMessage();
        locationMessage.setBookingId(bookingId);
        locationMessage.setDriverId(driverId);
        locationMessage.setUserType("DRIVER");
        
        // Try to get from DriveAdmin
        Optional<DriveAdmin> adminOpt = driveAdminRepository.findById(driverId.intValue());
        if (adminOpt.isPresent()) {
            DriveAdmin admin = adminOpt.get();
            locationMessage.setLatitude(admin.getDriverLatitude());
            locationMessage.setLongitude(admin.getDriverLongitude());
            return locationMessage;
        }
        
        // Try to get from VendorDrivers
        Optional<VendorDrivers> vendorOpt = vendorDriversRepository.findById(driverId.intValue());
        if (vendorOpt.isPresent()) {
            VendorDrivers vendor = vendorOpt.get();
            locationMessage.setLatitude(vendor.getDriverLatitude());
            locationMessage.setLongitude(vendor.getDriverLongitude());
            return locationMessage;
        }
        
        return null;
    }
    
    /**
     * Get current location of a user
     */
    public LocationMessage getUserLocation(Long userId, String bookingId) {
        LocationMessage locationMessage = new LocationMessage();
        locationMessage.setBookingId(bookingId);
        locationMessage.setUserId(userId);
        locationMessage.setUserType("USER");
        
        Optional<CarRentalUser> userOpt = carRentalRepository.findById(userId.intValue());
        if (userOpt.isPresent()) {
            CarRentalUser user = userOpt.get();
            locationMessage.setLatitude(user.getUserlatitude());
            locationMessage.setLongitude(user.getUserlongitude());
            return locationMessage;
        }
        
        return null;
    }
} 