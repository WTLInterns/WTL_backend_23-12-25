package com.workshop.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Repository.CarRentalRepository;
import com.workshop.DTO.BookingDTO;
import com.workshop.DTO.CancellationRequest;
import com.workshop.DTO.CancellationResult;
import com.workshop.DTO.CityDTO;
import com.workshop.DTO.LoginRequest;
import com.workshop.DTO.PriceUpdateRequest;
import com.workshop.DTO.StateDTO;
import com.workshop.Entity.Booking;
import com.workshop.Entity.CabInfo;
import com.workshop.Entity.Cities;
import com.workshop.Entity.Penalty;
import com.workshop.Entity.Popup;
import com.workshop.Entity.States;
import com.workshop.Entity.Tripprice;
import com.workshop.Entity.User;
import com.workshop.Entity.Vendor;
import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;
import com.workshop.Repo.BookingRepo;
import com.workshop.Repo.StateRepository;
import com.workshop.Repo.Trip;
import com.workshop.Service.BookingService;
import com.workshop.Service.CabInfoService;
import com.workshop.Service.CitiesService;
import com.workshop.Service.EmailService;
import com.workshop.Service.NotificationService;
import com.workshop.Service.PopupService;
import com.workshop.Service.SmsService;
import com.workshop.Service.StatesService;
import com.workshop.Service.TripRateService;
import com.workshop.Service.TripService;
import com.workshop.Service.UserDetailServiceImpl;
import com.workshop.Service.VendorService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@RestController
public class WtlAdminController {

    private final AuthenticationManagerBuilder authenticationManager;
    @Autowired
    BookingService ser;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TripService tripSer;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SmsService smsService;

    @Autowired
    CabInfoService cabser;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TripRateService tripRateService;

    @Autowired
    private StatesService statesService;

    @Autowired
    private CarRentalRepository carRentalRepository;

    @Autowired
    private CitiesService citiesService;

    @Autowired
    PopupService service;

    @Autowired
    UserDetailServiceImpl userService;

    @Autowired
    VendorService vendorSer;

    @Autowired
    private NotificationService notificationService;

    WtlAdminController(AuthenticationManagerBuilder authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // private final String apiKey = "AIzaSyDuZC6kFobB0pnp-k3VcxQIjvb0EhgfnVI"; //
    // Replace with your Google API key

    @GetMapping("/states/{id}")
    public ResponseEntity<States> getStateById(@PathVariable Long id) {
        return this.statesService.getStateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public List<States> getAllState() {
        return this.statesService.getAllState();
    }

    @Autowired
    private StateRepository stateRepository;

    @GetMapping("/api/states")
    public List<StateDTO> getStates() {
        return stateRepository.findAll()
                .stream()
                .map(state -> new StateDTO(state.getId(), state.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/city/all")
    public List<Cities> getAllCities() {
        return citiesService.getAllCities();
    }

    @GetMapping("/city/{id}")
    public Optional<Cities> getCityById(@PathVariable Long id) {
        return citiesService.getCityById(id);
    }

    @GetMapping("/cities/{stateId}")
    public ResponseEntity<List<CityDTO>> getCitiesByState(@PathVariable Long stateId) {
        List<Cities> cities = citiesService.getCitiesByState(stateId);
        if (cities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        // Convert Cities entities to CityDTO
        List<CityDTO> cityDTOs = cities.stream()
                .map(city -> {
                    CityDTO dto = new CityDTO();
                    dto.setId(city.getId());
                    dto.setName(city.getName());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(cityDTOs);
    }

    @PostMapping("/updateTripPricing")
    public ResponseEntity<Map<String, String>> updateTripPricing(@RequestBody Tripprice tripPricing) {
        // Update trip pricing logic
        this.tripRateService.updateTripPricing(tripPricing);

        // Create a map to return as a JSON object
        Map<String, String> response = new HashMap<>();
        response.put("message", "Trip pricing updated successfully!");

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-price/{id}")
    public ResponseEntity<onewayTrip> updateTripPrice(@PathVariable Long id, @RequestBody PriceUpdateRequest request) {
        onewayTrip updatedTrip = tripSer.updatePrice(id, request.getHatchback(), request.getSedan(),
                request.getSedanpremium(), request.getSuv(), request.getSuvplus(), request.getSourceState(),
                request.getSourceCity(), request.getDestinationState(), request.getDestinationCity());
        return ResponseEntity.ok(updatedTrip);
    }

    @PutMapping("/update-prices")
    public ResponseEntity<Map<String, String>> updatePrices(
            @RequestParam String sourceState,
            @RequestParam String destinationState,
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam int hatchbackPrice,
            @RequestParam int sedanPrice,
            @RequestParam int sedanPremiumPrice,
            @RequestParam int suvPrice,
            @RequestParam int suvPlusPrice,
            @RequestParam(required = false) Integer ertiga) {

        // DEBUG LOGGING: Log all received parameters
        System.out.println("[DEBUG] /update-prices called with:");
        System.out.println("  sourceState=" + sourceState);
        System.out.println("  destinationState=" + destinationState);
        System.out.println("  sourceCity=" + sourceCity);
        System.out.println("  destinationCity=" + destinationCity);
        System.out.println("  hatchbackPrice=" + hatchbackPrice);
        System.out.println("  sedanPrice=" + sedanPrice);
        System.out.println("  sedanPremiumPrice=" + sedanPremiumPrice);
        System.out.println("  suvPrice=" + suvPrice);
        System.out.println("  suvPlusPrice=" + suvPlusPrice);
        System.out.println("  ertiga=" + ertiga);

        // Call the service to update trip prices
        tripSer.updatePrices(sourceState, destinationState, sourceCity, destinationCity,
                hatchbackPrice, sedanPrice, sedanPremiumPrice, suvPrice, suvPlusPrice, ertiga);

        // Construct a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Prices updated successfully");

        return ResponseEntity.ok(response);
    }

    @Transactional
    @DeleteMapping("/delete-booking/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
        String responseMessage = ser.deleteBookingByBookingId(bookingId);
        if (responseMessage.contains("not found")) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if the booking does not exist
        }
        return ResponseEntity.ok(responseMessage); // Return 200 OK with the success message
    }

    @GetMapping("/details")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings); // Return the list of bookings with HTTP 200 OK status
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<BookingDTO> getBookingSById(@PathVariable int id) {
        // Call the service to fetch the booking as a DTO
        BookingDTO bookingDTO = bookingService.getBooking(id);

        // Check if the booking was found
        if (bookingDTO != null) {
            return ResponseEntity.ok(bookingDTO); // Return HTTP 200 OK with the booking DTO data
        } else {
            return ResponseEntity.notFound().build(); // Return HTTP 404 if booking is not found
        }
    }

    @PutMapping("/updateBooking/{id}")
    public ResponseEntity<BookingDTO> updateBooking(
            @PathVariable int id,
            @RequestBody BookingDTO bookingDTO) {

        try {
            BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
            if (updatedBooking != null) {
                return ResponseEntity.ok(updatedBooking);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Booking> changeStatus(
            @PathVariable int id,
            @RequestBody Map<String, Integer> requestBody) {

        int newStatus = requestBody.get("status"); // Extract status from the request body

        try {
            // Call the service to update the booking status
            Booking updatedBooking = bookingService.updateStatus(id, newStatus);
            return ResponseEntity.ok(updatedBooking); // Return the updated booking
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Return 404 if booking not found
        }
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        this.bookingService.deleteBooking(id);
    }

    @GetMapping("/getStatus/{status}")
    public List<Booking> getBookingByStatus(@PathVariable int status) {
        return this.bookingService.getBookingByStatus(status);
    }

    @PutMapping("/update-roundway-prices")
    public ResponseEntity<Map<String, String>> updateRoundWayPrices(
            @RequestParam String sourceState,
            @RequestParam String destinationState,
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam int hatchbackPrice,
            @RequestParam int sedanPrice,
            @RequestParam int sedanPremiumPrice,
            @RequestParam int suvPrice,
            @RequestParam int suvPlusPrice,
            @RequestParam(required = false) Integer ertiga) {

        // Call the service to update trip prices (now with Ertiga)
        tripSer.updatePricesByRoundWay(sourceState, destinationState, sourceCity, destinationCity,
                hatchbackPrice, sedanPrice, sedanPremiumPrice, suvPrice, suvPlusPrice, ertiga);

        // Construct a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Prices updated successfully");

        return ResponseEntity.ok(response);
    }

    // Enhanced vendor assignment email notification (now only ever targets vendor email)
    private void sendVendorAssignmentEmail(Booking booking, Vendor assignedVendor) {
        String subject = "Vendor Assigned - Booking Confirmation " + booking.getBookid();
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Vendor Assignment Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif; color: #333; background-color: #f5f5f5;\">\n"
                +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px 0; text-align: center; background-color: #1a1f2e;\">\n" +
                "                <h1 style=\"color: #ffffff; margin: 0;\">WTL Cab Service</h1>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">\n"
                +
                "                    <tr>\n" +
                "                        <td style=\"padding: 30px 30px 20px 30px;\">\n" +
                "                            <div style=\"text-align: center; margin-bottom: 25px;\">\n" +
                "                                <div style=\"background-color: #28a745; color: white; padding: 15px; border-radius: 50px; display: inline-block; margin-bottom: 15px;\">\n"
                +
                "                                    <span style=\"font-size: 24px;\">✓</span>\n" +
                "                                </div>\n" +
                "                                <h2 style=\"color: #28a745; margin: 0;\">Vendor Successfully Assigned!</h2>\n"
                +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 20px;\">Dear "
                + booking.getName() + ",</p>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 25px; background-color: #d4edda; padding: 15px; border-left: 4px solid #28a745; border-radius: 4px;\">\n"
                +
                "                                Great news! We have successfully assigned a vendor to your booking. Your trip is now confirmed and being processed.\n"
                +
                "                            </p>\n" +
                "                            \n" +
                "                            <h3 style=\"color: #1a1f2e; margin-bottom: 15px;\">📋 Booking Details</h3>\n"
                +
                "                            <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: separate; border-spacing: 0; border: 1px solid #e0e0e0; border-radius: 6px; overflow: hidden; margin-bottom: 25px;\">\n"
                +
                "                                <tr style=\"background-color: #f8f9fa;\">\n" +
                "                                    <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #e0e0e0; width: 40%;\">Detail</th>\n"
                +
                "                                    <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #e0e0e0; width: 60%;\">Information</th>\n"
                +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Booking ID</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; color: #007bff; font-weight: bold;\">"
                + booking.getBookid() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Pickup Location</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getUserPickup() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Drop Location</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getUserDrop() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Trip Type</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getTripType() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Date</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getDate() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Time</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getTime() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; font-weight: bold; background-color: #f8f9fa;\">Amount</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; font-weight: bold; color: #28a745; background-color: #f8f9fa;\">₹"
                + booking.getAmount() + "</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                            \n" +
                "                            <h3 style=\"color: #1a1f2e; margin-bottom: 15px;\">🚗 Assigned Vendor Details</h3>\n"
                +
                "                            <div style=\"background-color: #e3f2fd; padding: 20px; border-radius: 8px; margin-bottom: 25px; border-left: 4px solid #2196f3;\">\n"
                +
                "                                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold; width: 30%;\">Vendor Name:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #1976d2;\">"
                + (assignedVendor.getVendorCompanyName() != null ? assignedVendor.getVendorCompanyName() : "N/A")
                + "</td>\n" +
                "                                    </tr>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold;\">Contact Number:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #1976d2;\">"
                + (assignedVendor.getContactNo() != null ? assignedVendor.getContactNo() : "N/A") + "</td>\n" +
                "                                    </tr>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold;\">Email:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #1976d2;\">"
                + (assignedVendor.getVendorEmail() != null ? assignedVendor.getVendorEmail() : "N/A") + "</td>\n" +
                "                                    </tr>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold;\">Location:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #1976d2;\">"
                + (assignedVendor.getCity() != null ? assignedVendor.getCity() : "N/A") + "</td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                            </div>\n" +
                "                            \n" +
                "                            <div style=\"background-color: #fff3cd; padding: 15px; border-radius: 8px; margin-bottom: 25px; border-left: 4px solid #ffc107;\">\n"
                +
                "                                <h4 style=\"color: #856404; margin: 0 0 10px 0;\">📞 Next Steps:</h4>\n"
                +
                "                                <ul style=\"color: #856404; margin: 0; padding-left: 20px;\">\n" +
                "                                    <li>The assigned vendor will contact you shortly to confirm pickup details</li>\n"
                +
                "                                    <li>Please keep your phone accessible for vendor communication</li>\n"
                +
                "                                    <li>You will receive driver and vehicle details once assigned</li>\n"
                +
                "                                </ul>\n" +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 20px;\">Thank you for choosing WTL Cab Service. We're committed to providing you with a safe and comfortable journey!</p>\n"
                +
                "                            \n" +
                "                            <div style=\"text-align: center; margin: 25px 0;\">\n" +
                "                                <p style=\"font-size: 14px; color: #666; margin: 0;\">Need help? Contact our support team</p>\n"
                +
                "                                <p style=\"font-size: 16px; color: #007bff; font-weight: bold; margin: 5px 0;\">📞 Customer Support: +91-XXXXXXXXXX</p>\n"
                +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 14px; margin: 25px 0 10px 0;\"><strong>Terms and Conditions:</strong></p>\n"
                +
                "                            <ul style=\"font-size: 13px; color: #666; margin-bottom: 25px;\">\n" +
                "                                <li>Toll charges, parking fees, and other taxes are not included and will be charged as applicable</li>\n"
                +
                "                                <li>Prices may be extended in case of route changes, additional stops, or waiting time</li>\n"
                +
                "                                <li>Please refer to our website or contact support for detailed terms and conditions</li>\n"
                +
                "                            </ul>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 0;\">Best regards,<br><strong>WTL Cab Service Team</strong></p>\n"
                +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px; text-align: center; font-size: 12px; color: #666;\">\n" +
                "                <p>&copy; 2025 WTL Cab Service. All rights reserved.</p>\n" +
                "                <p>If you have any questions, please contact our customer support at <a href=\"mailto:support@wtlcabs.com\" style=\"color: #1a1f2e;\">support@wtlcabs.com</a></p>\n"
                +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        // IMPORTANT: Do NOT send this email to the user anymore.
        // If this helper is called anywhere, it will only send to the vendor email.
        if (assignedVendor != null && assignedVendor.getVendorEmail() != null
                && !assignedVendor.getVendorEmail().trim().isEmpty()) {
            boolean emailSent = emailService.sendEmail(message, subject, assignedVendor.getVendorEmail());

            if (emailSent) {
                System.out.println(
                        "Vendor assignment confirmation email sent successfully to vendor: "
                                + assignedVendor.getVendorEmail());
            } else {
                System.out.println(
                        "Failed to send vendor assignment confirmation email to vendor: "
                                + assignedVendor.getVendorEmail());
            }
        } else {
            System.out.println("[WARNING] sendVendorAssignmentEmail called but vendor email is null/empty");
        }
    }

    // Vendor notification email when assigned to a booking
    private void sendVendorNotificationEmail(Booking booking, Vendor assignedVendor) {
        System.out.println("[DEBUG] sendVendorNotificationEmail called for vendor: " + assignedVendor.getId());

        // Validate vendor email
        if (assignedVendor.getVendorEmail() == null || assignedVendor.getVendorEmail().trim().isEmpty()) {
            System.out.println("[ERROR] Cannot send vendor notification: Vendor email is null or empty");
            return;
        }

        String subject = "New Booking Assignment - " + booking.getBookid();
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>New Booking Assignment</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-family: Arial, sans-serif; color: #333; background-color: #f5f5f5;\">\n"
                +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px 0; text-align: center; background-color: #1a1f2e;\">\n" +
                "                <h1 style=\"color: #ffffff; margin: 0;\">WTL Cab Service</h1>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">\n"
                +
                "                    <tr>\n" +
                "                        <td style=\"padding: 30px 30px 20px 30px;\">\n" +
                "                            <div style=\"text-align: center; margin-bottom: 25px;\">\n" +
                "                                <div style=\"background-color: #007bff; color: white; padding: 15px; border-radius: 50px; display: inline-block; margin-bottom: 15px;\">\n"
                +
                "                                    <span style=\"font-size: 24px;\">🚗</span>\n" +
                "                                </div>\n" +
                "                                <h2 style=\"color: #007bff; margin: 0;\">New Booking Assigned!</h2>\n"
                +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 20px;\">Dear "
                + (assignedVendor.getVendorCompanyName() != null ? assignedVendor.getVendorCompanyName() : "Vendor")
                + ",</p>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 25px; background-color: #e3f2fd; padding: 15px; border-left: 4px solid #007bff; border-radius: 4px;\">\n"
                +
                "                                You have been assigned a new booking! Please review the details below and prepare for the trip.\n"
                +
                "                            </p>\n" +
                "                            \n" +
                "                            <h3 style=\"color: #1a1f2e; margin-bottom: 15px;\">📋 Booking Details</h3>\n"
                +
                "                            <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse: separate; border-spacing: 0; border: 1px solid #e0e0e0; border-radius: 6px; overflow: hidden; margin-bottom: 25px;\">\n"
                +
                "                                <tr style=\"background-color: #f8f9fa;\">\n" +
                "                                    <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #e0e0e0; width: 40%;\">Detail</th>\n"
                +
                "                                    <th style=\"padding: 12px 15px; text-align: left; border-bottom: 1px solid #e0e0e0; width: 60%;\">Information</th>\n"
                +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Booking ID</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; color: #007bff; font-weight: bold;\">"
                + booking.getBookid() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Pickup Location</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getUserPickup() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Drop Location</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getUserDrop() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Trip Type</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getTripType() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Date</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getDate() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0; font-weight: bold;\">Time</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; border-bottom: 1px solid #e0e0e0;\">"
                + booking.getTime() + "</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 12px 15px; font-weight: bold; background-color: #f8f9fa;\">Trip Amount</td>\n"
                +
                "                                    <td style=\"padding: 12px 15px; font-weight: bold; color: #28a745; background-color: #f8f9fa;\">₹"
                + booking.getAmount() + "</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                            \n" +
                "                            <h3 style=\"color: #1a1f2e; margin-bottom: 15px;\">👤 Customer Details</h3>\n"
                +
                "                            <div style=\"background-color: #fff3cd; padding: 20px; border-radius: 8px; margin-bottom: 25px; border-left: 4px solid #ffc107;\">\n"
                +
                "                                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold; width: 30%;\">Customer Name:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #856404;\">"
                + booking.getName() + "</td>\n" +
                "                                    </tr>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold;\">Phone Number:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #856404;\">"
                + booking.getPhone() + "</td>\n" +
                "                                    </tr>\n" +
                "                                    <tr>\n" +
                "                                        <td style=\"padding: 5px 0; font-weight: bold;\">Email:</td>\n"
                +
                "                                        <td style=\"padding: 5px 0; color: #856404;\">"
                + booking.getEmail() + "</td>\n" +
                "                                    </tr>\n" +
                "                                </table>\n" +
                "                            </div>\n" +
                "                            \n" +
                "                            <div style=\"background-color: #d4edda; padding: 15px; border-radius: 8px; margin-bottom: 25px; border-left: 4px solid #28a745;\">\n"
                +
                "                                <h4 style=\"color: #155724; margin: 0 0 10px 0;\">📞 Action Required:</h4>\n"
                +
                "                                <ul style=\"color: #155724; margin: 0; padding-left: 20px;\">\n" +
                "                                    <li>Contact the customer to confirm pickup details and timing</li>\n"
                +
                "                                    <li>Assign a driver and vehicle for this booking</li>\n" +
                "                                    <li>Ensure the driver has customer contact information</li>\n" +
                "                                    <li>Confirm pickup location and any special instructions</li>\n" +
                "                                </ul>\n" +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 20px;\">Please ensure timely service and maintain the quality standards expected by WTL Cab Service.</p>\n"
                +
                "                            \n" +
                "                            <div style=\"text-align: center; margin: 25px 0;\">\n" +
                "                                <p style=\"font-size: 14px; color: #666; margin: 0;\">Need support? Contact WTL Admin</p>\n"
                +
                "                                <p style=\"font-size: 16px; color: #007bff; font-weight: bold; margin: 5px 0;\">📞 Admin Support: +91-XXXXXXXXXX</p>\n"
                +
                "                            </div>\n" +
                "                            \n" +
                "                            <p style=\"font-size: 16px; line-height: 24px; margin-bottom: 0;\">Best regards,<br><strong>WTL Cab Service Admin Team</strong></p>\n"
                +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 20px; text-align: center; font-size: 12px; color: #666;\">\n" +
                "                <p>&copy; 2025 WTL Cab Service. All rights reserved.</p>\n" +
                "                <p>For any queries, contact admin at <a href=\"mailto:admin@wtlcabs.com\" style=\"color: #1a1f2e;\">admin@wtlcabs.com</a></p>\n"
                +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        System.out.println("[DEBUG] Attempting to send email to vendor: " + assignedVendor.getVendorEmail());

        try {
            boolean emailSent = emailService.sendEmail(message, subject, assignedVendor.getVendorEmail());

            if (emailSent) {
                System.out.println("[SUCCESS] Vendor assignment notification email sent successfully to: "
                        + assignedVendor.getVendorEmail());
            } else {
                System.out.println("[FAILED] Failed to send vendor assignment notification email to: "
                        + assignedVendor.getVendorEmail());
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Exception while sending vendor email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Legacy method for backward compatibility
    private void sendConfirmationEmail(String name, String email, String bookingId,
            String pickupLocation, String dropLocation,
            String tripType, LocalDate date, String time,
            Integer total) {
        String subject = "Booking Confirmation - " + bookingId;
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Booking Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; background-color: #f5f7fa;\">\n"
                +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 0;\">\n"
                +
                "        <tr>\n" +
                "            <td align=\"center\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: 0 auto;\">\n"
                +
                "                    <tr>\n" +
                "                        <td align=\"center\" style=\"padding: 20px 0;\">\n" +
                "                            <h1 style=\"color: #ffffff; margin: 0; font-size: 32px; font-weight: 700;\">WTL Cab Service</h1>\n"
                +
                "                            <p style=\"color: #e0e0e0; margin: 8px 0 0 0; font-size: 16px;\">Reliable. Comfortable. On-time.</p>\n"
                +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: -30px auto 0; background-color: #ffffff; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.1);\">\n"
                +
                "        <tr>\n" +
                "            <td style=\"padding: 40px 35px;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"text-align: center; margin-bottom: 30px;\">\n" +
                "                                <div style=\"background-color: #e8f5e9; width: 70px; height: 70px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 20px;\">\n"
                +
                "                                    <span style=\"font-size: 30px; color: #4caf50;\">✓</span>\n" +
                "                                </div>\n" +
                "                                <h2 style=\"color: #2e7d32; margin: 0 0 10px 0; font-size: 28px; font-weight: 700;\">Booking Confirmed!</h2>\n"
                +
                "                                <p style=\"color: #666; margin: 0; font-size: 18px;\">Thank you for choosing WTL Cab Service</p>\n"
                +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"background-color: #f8f9fa; border-radius: 10px; padding: 25px; margin-bottom: 30px; border: 1px solid #e9ecef;\">\n"
                +
                "                                <h3 style=\"margin-top: 0; color: #1a1f2e; font-size: 20px; font-weight: 600;\">Hello "
                + name + ",</h3>\n" +
                "                                <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0;\">Your booking has been successfully confirmed. Here are the details of your trip:</p>\n"
                +
                "                            </div>\n" +
                "\n" +
                "                            <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom: 30px;\">\n"
                +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 15px 20px; background: linear-gradient(to right, #667eea, #764ba2); color: white; border-radius: 8px 8px 0 0; text-align: center;\">\n"
                +
                "                                        <h3 style=\"margin: 0; font-size: 20px; font-weight: 600;\">Booking ID: "
                + bookingId + "</h3>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 0; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 8px 8px;\">\n"
                +
                "                                        <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                                            <tr>\n" +
                "                                                <td style=\"padding: 20px;\">\n" +
                "                                                    <h4 style=\"margin: 0 0 15px 0; color: #1a1f2e; font-size: 18px; font-weight: 600; border-bottom: 2px solid #667eea; padding-bottom: 8px;\">Trip Information</h4>\n"
                +
                "                                                    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Trip Type:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + tripType + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Date:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + date + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Time:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + time + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Distance:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + (pickupLocation.equals(dropLocation) ? "Local Trip" : "Outstation") + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Pickup Location:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + pickupLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Drop-off Location:</strong></td>\n"
                +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">"
                + dropLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "\n" +
                "                            <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; padding: 25px; margin-bottom: 30px; color: white; text-align: center;\">\n"
                +
                "                                <h3 style=\"margin: 0 0 15px 0; font-size: 22px; font-weight: 600;\">Payment Summary</h3>\n"
                +
                "                                <div style=\"font-size: 36px; font-weight: 700; margin-bottom: 5px;\">₹"
                + total + "</div>\n" +
                "                                <p style=\"margin: 0; font-size: 16px; opacity: 0.9;\">Amount Paid Successfully</p>\n"
                +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"margin-bottom: 30px;\">\n" +
                "                                <h3 style=\"color: #1a1f2e; margin: 0 0 20px 0; font-size: 20px; font-weight: 600; text-align: center;\">Important Information</h3>\n"
                +
                "                                <ul style=\"padding-left: 20px; margin: 0;\">\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Please arrive at the pickup location at least 10 minutes before the scheduled time.</li>\n"
                +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Driver details will be shared 30 minutes before pickup time.</li>\n"
                +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">For any changes or cancellations, contact us at least 2 hours before pickup.</li>\n"
                +
                "                                    <li style=\"font-size: 15px; line-height: 24px;\">Toll charges, parking fees, and other taxes are not included and will be charged as applicable.</li>\n"
                +
                "                                </ul>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"text-align: center; margin: 30px 0;\">\n" +
                "                                <a href=\"tel:+919876543210\" style=\"display: inline-block; background-color: #667eea; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Call Support</a>\n"
                +
                "                                <a href=\"mailto:support@wtlcabs.com\" style=\"display: inline-block; background-color: #2e7d32; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Email Support</a>\n"
                +
                "                            </div>\n" +
                "\n" +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0; text-align: center;\">We wish you a safe and comfortable journey!</p>\n"
                +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin: 10px 0 0 0; text-align: center;\"><strong>Best regards,</strong><br>WTL Cab Service Team</p>\n"
                +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 25px 35px; background-color: #f8f9fa; border-radius: 0 0 12px 12px; border-top: 1px solid #eee;\">\n"
                +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                +
                "                    <tr>\n" +
                "                        <td style=\"text-align: center; font-size: 13px; color: #6c757d;\">\n" +
                "                            <p style=\"margin: 0 0 15px 0;\">&copy; 2025 WTL Cab Service. All rights reserved.</p>\n"
                +
                "                            <p style=\"margin: 0;\">If you have any questions, please contact our customer support at <a href=\"mailto:support@wtlcabs.com\" style=\"color: #667eea; text-decoration: none;\">support@wtlcabs.com</a> or call <a href=\"tel:+919876543210\" style=\"color: #667eea; text-decoration: none;\">+91 9876543210</a></p>\n"
                +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        emailService.sendEmail(message, subject, email);
    }

    @PutMapping("/{bookingId}/assignVendor/{vendorId}")
    public ResponseEntity<Booking> assignVendorToBooking(
            @PathVariable int bookingId,
            @PathVariable Long vendorId) {

        // Call the service method to assign vendor
        System.out.println("[API] assignVendorToBooking bookingId=" + bookingId + " vendorId=" + vendorId);
        Booking updatedBooking = bookingService.assignVendorToBooking(bookingId, vendorId);

        if (updatedBooking == null) {
            // If the booking or vendor was not found, return a 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Try to notify the assigned vendor via FCM and send email notification
        try {
            Vendor assignedVendor = vendorSer.getVendorById(vendorId);
            System.out.println("[API] assignVendorToBooking -> assignedVendor="
                    + (assignedVendor == null ? "null" : assignedVendor.getId())
                    + " hasToken=" + (assignedVendor != null && assignedVendor.getFcmToken() != null));

            if (assignedVendor != null && assignedVendor.getFcmToken() != null
                    && !assignedVendor.getFcmToken().isBlank()) {
                Map<String, String> data = new HashMap<>();
                data.put("type", "BOOKING_ASSIGNED");
                data.put("bookingId", String.valueOf(updatedBooking.getBookid()));
                data.put("pickup", String.valueOf(updatedBooking.getUserPickup()));
                data.put("drop", String.valueOf(updatedBooking.getUserDrop()));
                data.put("tripType", String.valueOf(updatedBooking.getTripType()));
                data.put("date", String.valueOf(updatedBooking.getDate()));
                data.put("time", String.valueOf(updatedBooking.getTime()));

                String title = "New Booking Assigned";
                String body = "Booking #" + updatedBooking.getBookid() + " has been assigned to you.";
                String vtk = assignedVendor.getFcmToken();
                System.out.println("[API] Sending FCM to vendorId=" + assignedVendor.getId() + " tokenPrefix="
                        + vtk.substring(0, Math.min(12, vtk.length())));
                notificationService.sendToToken(assignedVendor.getFcmToken(), title, body, data);
            }

            // Send email notifications to both user and vendor
            if (assignedVendor != null) {
                System.out.println("[DEBUG] Vendor found: ID=" + assignedVendor.getId() +
                        ", CompanyName=" + assignedVendor.getVendorCompanyName() +
                        ", Email=" + assignedVendor.getVendorEmail());

                // Send assignment notification email only to the vendor
                if (assignedVendor.getVendorEmail() != null && !assignedVendor.getVendorEmail().trim().isEmpty()) {
                    System.out.println(
                            "[DEBUG] Sending vendor notification email to: " + assignedVendor.getVendorEmail());
                    sendVendorNotificationEmail(updatedBooking, assignedVendor);
                } else {
                    System.out.println(
                            "[WARNING] Vendor email is null or empty for vendor ID: " + assignedVendor.getId());
                }
            } else {
                System.out.println("[ERROR] Assigned vendor is null for vendor ID: " + vendorId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error sending vendor assignment notifications: " + ex.getMessage());
        }
        // If the vendor is assigned successfully, return the updated booking
        return ResponseEntity.ok(updatedBooking);
    }

    @PostMapping("/register-vendor-token")
    public ResponseEntity<?> registerAdminToken(@RequestBody Map<String, String> body) {
        String token = body.get("fcmToken");
        String id = body.get("userId");
        Vendor user = vendorSer.getVendorById((long) Integer.parseInt(id));

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        user.setFcmToken(token);
        // record update time for traceability
        user.setFcmUpdatedAt(LocalDateTime.now());
        vendorSer.saveVendor(user);

        return ResponseEntity.ok("Token saved at " + user.getFcmUpdatedAt());
    }

    @PostMapping("/test-notify")
    public ResponseEntity<?> testNotify(@RequestBody Map<String, Object> payload) {
        try {
            String token = (String) payload.get("token");
            String title = (String) payload.getOrDefault("title", "Test Notification");
            String body = (String) payload.getOrDefault("body", "This is a test message");

            @SuppressWarnings("unchecked")
            Map<String, String> data = (Map<String, String>) payload.getOrDefault("data",
                    new HashMap<String, String>());

            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("token is required");
            }

            boolean ok = notificationService.sendToToken(token, title, body, data);
            return ResponseEntity.ok(Map.of("sent", ok));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + ex.getMessage());
        }
    }

    @PostMapping("/test-notify-admin")
    public ResponseEntity<?> testNotifyAdmin(@RequestBody Map<String, Object> payload) {
        try {
            String token = (String) payload.get("token");
            String title = (String) payload.getOrDefault("title", "Test Notification");
            String body = (String) payload.getOrDefault("body", "This is a test message");

            @SuppressWarnings("unchecked")
            Map<String, String> data = (Map<String, String>) payload.getOrDefault("data",
                    new HashMap<String, String>());

            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("token is required");
            }

            // Build Notification (for general clients) and Webpush config (for browsers)
            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification
                    .builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // Explicit Webpush config helps Chrome display notifications in background
            com.google.firebase.messaging.WebpushNotification webpushNotification = new com.google.firebase.messaging.WebpushNotification(
                    title,
                    body,
                    "/file.svg" // icon path served from vendor/public
            );

            com.google.firebase.messaging.WebpushFcmOptions fcmOptions = com.google.firebase.messaging.WebpushFcmOptions
                    .withLink("/Dashboard");

            com.google.firebase.messaging.WebpushConfig webpushConfig = com.google.firebase.messaging.WebpushConfig
                    .builder()
                    .setNotification(webpushNotification)
                    .putHeader("TTL", "86400")
                    .setFcmOptions(fcmOptions)
                    .build();

            com.google.firebase.messaging.Message.Builder builder = com.google.firebase.messaging.Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .setWebpushConfig(webpushConfig);

            if (data != null && !data.isEmpty()) {
                builder.putAllData(data);
            }

            com.google.firebase.messaging.Message message = builder.build();

            try {
                String response = com.google.firebase.messaging.FirebaseMessaging.getInstance().send(message);
                String tokenPrefix = token.substring(0, Math.min(12, token.length()));
                System.out.println("[AdminSDK][FCM] Test sent. tokenPrefix=" + tokenPrefix + " title='" + title
                        + "' response=" + response);
                return ResponseEntity.ok(Map.of("sent", true, "messageId", response));
            } catch (com.google.firebase.messaging.FirebaseMessagingException e) {
                String tokenPrefix = token.substring(0, Math.min(12, token.length()));
                System.out.println("[AdminSDK][FCM] Test failed. tokenPrefix=" + tokenPrefix + " errorCode="
                        + e.getErrorCode() + " message=" + e.getMessage());
                return ResponseEntity
                        .ok(Map.of("sent", false, "errorCode", e.getErrorCode(), "message", e.getMessage()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed: " + ex.getMessage());
        }
    }

    @GetMapping("/{vendorId}/vendorByBookings")
    public ResponseEntity<List<Booking>> getBookingsByVendor(@PathVariable Long vendorId) {
        List<Booking> bookings = bookingService.getBookingByVendor(vendorId);
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Returns 204 if no bookings found
        }
        return ResponseEntity.ok(bookings); // Returns 200 with the list of bookings
    }

    @PutMapping("/{bookingId}/assignVendorCab/{vendorCabId}")

    public ResponseEntity<Booking> assignVendorCabToBooking(
            @PathVariable int bookingId,
            @PathVariable int vendorCabId) {

        // Call the service method to assign vendor
        Booking updatedBooking = bookingService.assignVendorCabToBooking(bookingId, vendorCabId);

        if (updatedBooking == null) {
            // If the booking or vendor was not found, return a 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Send email notifications if both vendor and driver are assigned
        if (updatedBooking.getVendor() != null && updatedBooking.getVendorCab() != null
                && updatedBooking.getVendorDriver() != null) {
            System.out.println("[assignVendorCab] Both vendor and driver assigned, sending email notifications");

            // Send email to user
            sendUserBookingConfirmationEmail(updatedBooking);

            // Send email to driver
            sendDriverTripAssignmentEmail(updatedBooking);
        } else {
            System.out.println("[assignVendorCab] Vendor or driver not fully assigned yet. Vendor: " +
                    (updatedBooking.getVendor() != null) + ", VendorCab: " +
                    (updatedBooking.getVendorCab() != null) + ", VendorDriver: " +
                    (updatedBooking.getVendorDriver() != null));
        }

        return ResponseEntity.ok(updatedBooking);
    }

    @PutMapping("/{bookingId}/assignVendorDriver/{vendorDriverId}")
    public ResponseEntity<Booking> assignVendorDriverToBooking(
            @PathVariable int bookingId,
            @PathVariable int vendorDriverId) {

        // Call the service method to assign vendor
        System.out.println(
                "[API] assignVendorDriverToBooking bookingId=" + bookingId + " vendorDriverId=" + vendorDriverId);
        Booking updatedBooking = bookingService.assignVendorDriverToBooking(bookingId, vendorDriverId);

        if (updatedBooking == null) {
            // If the booking or vendor was not found, return a 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Always attempt to notify the driver in realtime if a driver token exists
        try {
            if (updatedBooking.getVendorDriver() != null && updatedBooking.getVendorDriver().getFcmToken() != null
                    && !updatedBooking.getVendorDriver().getFcmToken().isBlank()) {
                Map<String, String> data = new HashMap<>();
                data.put("type", "TRIP_ASSIGNED");
                data.put("bookingId", String.valueOf(updatedBooking.getBookid()));
                data.put("pickup", String.valueOf(updatedBooking.getUserPickup()));
                data.put("drop", String.valueOf(updatedBooking.getUserDrop()));
                data.put("tripType", String.valueOf(updatedBooking.getTripType()));
                data.put("date", String.valueOf(updatedBooking.getDate()));
                data.put("time", String.valueOf(updatedBooking.getTime()));

                String title = "New Trip Assigned";
                String body = "Trip #" + updatedBooking.getBookid() + " assigned. Pickup: "
                        + updatedBooking.getUserPickup();
                String dtk = updatedBooking.getVendorDriver().getFcmToken();
                System.out
                        .println("[API] Sending FCM to driverId=" + updatedBooking.getVendorDriver().getVendorDriverId()
                                + " tokenPrefix=" + dtk.substring(0, Math.min(12, dtk.length())));
                notificationService.sendToToken(dtk, title, body, data);
            } else {
                System.out.println("[API] Driver token missing, skipping driver FCM.");
            }
        } catch (Exception e) {
            System.out.println("[API] Driver FCM send failed: " + e.getMessage());
        }

        // Always send a basic driver assignment email to the user when a vendor driver
        // is assigned/reassigned
        if (updatedBooking.getVendorDriver() != null) {
            sendUserDriverAssignmentEmail(updatedBooking, false);
        }

        // Send email notifications if both vendor and driver are assigned
        if (updatedBooking.getVendor() != null && updatedBooking.getVendorCab() != null
                && updatedBooking.getVendorDriver() != null) {
            System.out.println("[assignVendorDriver] Both vendor and driver assigned, sending email notifications");

            // Send email to user
            sendUserBookingConfirmationEmail(updatedBooking);

            // Send email to driver
            sendDriverTripAssignmentEmail(updatedBooking);
        } else {
            System.out.println("[assignVendorDriver] Vendor or driver not fully assigned yet. Vendor: " +
                    (updatedBooking.getVendor() != null) + ", VendorCab: " +
                    (updatedBooking.getVendorCab() != null) + ", VendorDriver: " +
                    (updatedBooking.getVendorDriver() != null));
        }

        return ResponseEntity.ok(updatedBooking);
    }

    @PostMapping("/wtlLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    @Autowired
    private BookingRepo bookingRepo;

    /**
     * Sends booking confirmation email to the user with complete trip details
     */
    private void sendUserBookingConfirmationEmail(Booking booking) {
        try {
            String subject = "Booking Confirmed - Trip #" + booking.getBookid();
            String message = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Booking Confirmation</title>"
                    + "</head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; margin: 0; padding: 0;'>"
                    + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;'>"
                    + "<div style='background-color: #28a745; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>✅ Booking Confirmed!</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<h3 style='color: #28a745; font-size: 20px; margin-bottom: 20px;'>Hello " + booking.getName()
                    + ",</h3>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-bottom: 20px;'>Great news! Your cab booking has been confirmed and a driver has been assigned. Below are your complete trip details:</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Trip Information</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Booking ID:</strong> "
                    + booking.getBookid() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Pickup Location:</strong> "
                    + booking.getUserPickup() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Drop Location:</strong> "
                    + booking.getUserDrop() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Trip Type:</strong> "
                    + booking.getTripType() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Date:</strong> "
                    + booking.getDate() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Time:</strong> "
                    + booking.getTime() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Amount:</strong> ₹"
                    + booking.getAmount() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #e7f3ff; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Vehicle & Driver Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Vehicle:</strong> "
                    + booking.getVendorCab().getCarName() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Vehicle Number:</strong> "
                    + booking.getVendorCab().getVehicleNo() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Driver Name:</strong> "
                    + booking.getVendorDriver().getDriverName() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Driver Contact:</strong> "
                    + booking.getVendorDriver().getContactNo() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #fff3cd; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ffc107;'>"
                    + "<p style='margin: 0; font-size: 14px; color: #856404;'><strong>Important:</strong> Please be ready at your pickup location 5 minutes before the scheduled time. Your driver will contact you shortly before arrival.</p>"
                    + "</div>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-top: 20px;'>Thank you for choosing our service! We wish you a safe and pleasant journey.</p>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 20px; background-color: #f1f1f1; color: #777777; font-size: 14px;'>"
                    + "<p style='margin: 0;'>Need help? Contact us at <a href='mailto:support@wtl.com' style='color: #007BFF; text-decoration: none;'>support@wtl.com</a></p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            boolean emailSent = emailService.sendEmail(message, subject, booking.getEmail());

            if (emailSent) {
                System.out.println("[EMAIL] User booking confirmation sent successfully to: " + booking.getEmail());
            } else {
                System.out.println("[EMAIL] Failed to send user booking confirmation to: " + booking.getEmail());
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] Error sending user booking confirmation: " + e.getMessage());
        }
    }

    /**
     * Sends a booking cancellation email to the user.
     */
    private void sendUserBookingCancellationEmail(Booking booking) {
        try {
            if (booking.getEmail() == null || booking.getEmail().isBlank()) {
                System.out.println("[EMAIL] User email not available, skipping cancellation email");
                return;
            }

            String subject = "Booking Cancelled - Trip #" + booking.getBookid();
            String message = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Booking Cancelled</title>"
                    + "</head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; margin: 0; padding: 0;'>"
                    + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;'>"
                    + "<div style='background-color: #dc3545; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>Booking Cancelled</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<h3 style='color: #dc3545; font-size: 20px; margin-bottom: 20px;'>Hello "
                    + booking.getName() + ",</h3>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-bottom: 20px;'>Your booking has been cancelled. Below are the details of your trip:</p>"
                    + "<div style='background-color: #f8d7da; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #721c24; margin-top: 0;'>Trip Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Booking ID:</strong> "
                    + booking.getBookid() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Pickup:</strong> "
                    + booking.getUserPickup() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Drop:</strong> "
                    + booking.getUserDrop() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Date:</strong> "
                    + booking.getDate() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Time:</strong> "
                    + booking.getTime() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-top: 20px;'>If this was unexpected or you have any questions, please contact our support team.</p>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 20px; background-color: #f1f1f1; color: #777777; font-size: 14px;'>"
                    + "<p style='margin: 0;'>Need help? Contact us at <a href='mailto:support@wtl.com' style='color: #007BFF; text-decoration: none;'>support@wtl.com</a></p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            boolean emailSent = emailService.sendEmail(message, subject, booking.getEmail());

            if (emailSent) {
                System.out.println("[EMAIL] User booking cancellation sent successfully to: " + booking.getEmail());
            } else {
                System.out.println("[EMAIL] Failed to send user booking cancellation to: " + booking.getEmail());
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] Error sending user booking cancellation: " + e.getMessage());
        }
    }

    /**
     * Sends a simple driver assignment email to the user for both vendor and
     * admin-driver flows.
     */
    private void sendUserDriverAssignmentEmail(Booking booking, boolean adminDriver) {
        try {
            if (booking.getEmail() == null || booking.getEmail().isBlank()) {
                System.out.println("[EMAIL] User email not available, skipping driver assignment email");
                return;
            }

            String driverName;
            String driverPhone;
            String driverTypeLabel;

            if (adminDriver && booking.getDriveAdmin() != null) {
                driverName = booking.getDriveAdmin().getDriverName();
                // DriveAdmin uses getcontactNo() (lowercase 'c') as the getter
                driverPhone = booking.getDriveAdmin().getcontactNo();
                driverTypeLabel = "Admin Driver";
            } else if (!adminDriver && booking.getVendorDriver() != null) {
                driverName = booking.getVendorDriver().getDriverName();
                driverPhone = booking.getVendorDriver().getContactNo();
                driverTypeLabel = "Driver";
            } else {
                System.out.println("[EMAIL] No driver entity present for assignment email");
                return;
            }

            // Resolve cab details from either vendor cab or admin cab
            String cabName = null;
            String cabNo = null;
            if (booking.getVendorCab() != null) {
                cabName = booking.getVendorCab().getCarName();
                cabNo = booking.getVendorCab().getVehicleNo();
            } else if (booking.getCabAdmin() != null) {
                cabName = booking.getCabAdmin().getVehicleNameAndRegNo();
                cabNo = booking.getCabAdmin().getVehicleRcNo();
            }

            String subject = "Driver Assigned - Trip #" + booking.getBookid();
            String message = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Driver Assigned</title>"
                    + "</head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; margin: 0; padding: 0;'>"
                    + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;'>"
                    + "<div style='background-color: #17a2b8; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>Driver Assigned</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<h3 style='color: #17a2b8; font-size: 20px; margin-bottom: 20px;'>Hello "
                    + booking.getName() + ",</h3>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-bottom: 20px;'>A "
                    + driverTypeLabel
                    + " has been assigned to your trip. Here are the details:</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Trip Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Booking ID:</strong> "
                    + booking.getBookid() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Pickup:</strong> "
                    + booking.getUserPickup() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Drop:</strong> "
                    + booking.getUserDrop() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Date:</strong> "
                    + booking.getDate() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Time:</strong> "
                    + booking.getTime() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #e7f3ff; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Driver Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Name:</strong> "
                    + driverName + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Contact:</strong> "
                    + driverPhone + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #e7f3ff; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Cab Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Cab Name:</strong> "
                    + (cabName != null ? cabName : "Cab details will be shared with you shortly before your trip") + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Vehicle No:</strong> "
                    + (cabNo != null ? cabNo : "Cab details will be shared with you shortly before your trip") + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-top: 20px;'>Please keep your phone accessible; the driver may contact you before pickup.</p>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 20px; background-color: #f1f1f1; color: #777777; font-size: 14px;'>"
                    + "<p style='margin: 0;'>Need help? Contact our support team.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            boolean emailSent = emailService.sendEmail(message, subject, booking.getEmail());
            if (emailSent) {
                System.out.println("[EMAIL] User driver assignment email sent successfully to: " + booking.getEmail());
            } else {
                System.out.println("[EMAIL] Failed to send user driver assignment email to: " + booking.getEmail());
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] Error sending user driver assignment email: " + e.getMessage());
        }
    }

    /**
     * Sends trip assignment email to the driver with complete trip details
     */
    private void sendDriverTripAssignmentEmail(Booking booking) {
        try {
            if (booking.getVendorDriver() == null || booking.getVendorDriver().getEmailId() == null) {
                System.out.println("[EMAIL] Driver email not available, skipping driver email notification");
                return;
            }

            String driverEmail = booking.getVendorDriver().getEmailId();
            String subject = "New Trip Assigned - Trip #" + booking.getBookid();
            String message = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Trip Assignment</title>"
                    + "</head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; margin: 0; padding: 0;'>"
                    + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;'>"
                    + "<div style='background-color: #17a2b8; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>🚗 New Trip Assigned!</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<h3 style='color: #17a2b8; font-size: 20px; margin-bottom: 20px;'>Hello "
                    + booking.getVendorDriver().getDriverName() + ",</h3>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-bottom: 20px;'>You have been assigned a new trip. Please review the details below and prepare for the journey:</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #dc3545; margin-top: 0;'>Trip Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Trip ID:</strong> "
                    + booking.getBookid() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Pickup Location:</strong> "
                    + booking.getUserPickup() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Drop Location:</strong> "
                    + booking.getUserDrop() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Trip Type:</strong> "
                    + booking.getTripType() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Date:</strong> "
                    + booking.getDate() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Time:</strong> "
                    + booking.getTime() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #dc3545;'>Trip Amount:</strong> ₹"
                    + booking.getAmount() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #e7f3ff; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Customer Information</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Customer Name:</strong> "
                    + booking.getName() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong style='color: #007BFF;'>Customer Mobile:</strong> "
                    + booking.getPhone() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #d4edda; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #28a745;'>"
                    + "<h4 style='color: #155724; margin-top: 0;'>Your Vehicle Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #155724;'><strong>Vehicle:</strong> "
                    + booking.getVendorCab().getCarName() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #155724;'><strong>Vehicle Number:</strong> "
                    + booking.getVendorCab().getVehicleNo() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #fff3cd; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ffc107;'>"
                    + "<p style='margin: 0; font-size: 14px; color: #856404;'><strong>Instructions:</strong></p>"
                    + "<ul style='margin: 10px 0 0 20px; font-size: 14px; color: #856404;'>"
                    + "<li>Contact the customer 15 minutes before pickup time</li>"
                    + "<li>Arrive at pickup location on time</li>"
                    + "<li>Ensure vehicle is clean and ready</li>"
                    + "<li>Drive safely and follow traffic rules</li>"
                    + "</ul>"
                    + "</div>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-top: 20px;'>Please confirm your availability and start preparing for this trip. Safe driving!</p>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 20px; background-color: #f1f1f1; color: #777777; font-size: 14px;'>"
                    + "<p style='margin: 0;'>Questions? Contact support at <a href='mailto:driver-support@wtl.com' style='color: #007BFF; text-decoration: none;'>driver-support@wtl.com</a></p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            boolean emailSent = emailService.sendEmail(message, subject, driverEmail);

            if (emailSent) {
                System.out.println("[EMAIL] Driver trip assignment sent successfully to: " + driverEmail);
            } else {
                System.out.println("[EMAIL] Failed to send driver trip assignment to: " + driverEmail);
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] Error sending driver trip assignment: " + e.getMessage());
        }
    }

    /**
     * Sends trip assignment email to an Admin (DriveAdmin) driver.
     */
    private void sendAdminDriverTripAssignmentEmail(Booking booking) {
        try {
            if (booking.getDriveAdmin() == null || booking.getDriveAdmin().getEmailId() == null) {
                System.out.println("[EMAIL] Admin driver email not available, skipping admin driver email notification");
                return;
            }

            String driverEmail = booking.getDriveAdmin().getEmailId();
            String subject = "New Trip Assigned - Trip #" + booking.getBookid();
            String message = "<!DOCTYPE html>"
                    + "<html lang='en'>"
                    + "<head>"
                    + "<meta charset='UTF-8'>"
                    + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
                    + "<title>Trip Assignment</title>"
                    + "</head>"
                    + "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; margin: 0; padding: 0;'>"
                    + "<div style='max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); overflow: hidden;'>"
                    + "<div style='background-color: #17a2b8; color: #ffffff; padding: 20px; text-align: center;'>"
                    + "<h1 style='margin: 0; font-size: 24px; font-weight: bold;'>🚗 New Trip Assigned!</h1>"
                    + "</div>"
                    + "<div style='padding: 20px;'>"
                    + "<h3 style='color: #17a2b8; font-size: 20px; margin-bottom: 20px;'>Hello "
                    + booking.getDriveAdmin().getDriverName() + ",</h3>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-bottom: 20px;'>You have been assigned a new trip. Please review the details below and prepare for the journey:</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #dc3545; margin-top: 0;'>Trip Details</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Trip ID:</strong> "
                    + booking.getBookid() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Pickup Location:</strong> "
                    + booking.getUserPickup() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Drop Location:</strong> "
                    + booking.getUserDrop() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Trip Type:</strong> "
                    + booking.getTripType() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Date:</strong> "
                    + booking.getDate() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Time:</strong> "
                    + booking.getTime() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Trip Amount:</strong> ₹"
                    + booking.getAmount() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #e7f3ff; padding: 15px; border-radius: 8px; margin: 20px 0;'>"
                    + "<h4 style='color: #007BFF; margin-top: 0;'>Customer Information</h4>"
                    + "<ul style='list-style-type: none; padding: 0; margin: 0;'>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Customer Name:</strong> "
                    + booking.getName() + "</li>"
                    + "<li style='margin-bottom: 8px; font-size: 14px; color: #555555;'><strong>Customer Mobile:</strong> "
                    + booking.getPhone() + "</li>"
                    + "</ul>"
                    + "</div>"
                    + "<div style='background-color: #fff3cd; padding: 15px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #ffc107;'>"
                    + "<p style='margin: 0; font-size: 14px; color: #856404;'><strong>Instructions:</strong></p>"
                    + "<ul style='margin: 10px 0 0 20px; font-size: 14px; color: #856404;'>"
                    + "<li>Contact the customer 15 minutes before pickup time</li>"
                    + "<li>Arrive at pickup location on time</li>"
                    + "<li>Ensure vehicle is clean and ready</li>"
                    + "<li>Drive safely and follow traffic rules</li>"
                    + "</ul>"
                    + "</div>"
                    + "<p style='font-size: 16px; line-height: 1.5; color: #333333; margin-top: 20px;'>Please confirm your availability and start preparing for this trip. Safe driving!</p>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 20px; background-color: #f1f1f1; color: #777777; font-size: 14px;'>"
                    + "<p style='margin: 0;'>Questions? Contact support at <a href='mailto:driver-support@wtl.com' style='color: #007BFF; text-decoration: none;'>driver-support@wtl.com</a></p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            boolean emailSent = emailService.sendEmail(message, subject, driverEmail);
            if (emailSent) {
                System.out.println("[EMAIL] Admin driver trip assignment sent successfully to: " + driverEmail);
            } else {
                System.out.println("[EMAIL] Failed to send admin driver trip assignment to: " + driverEmail);
            }
        } catch (Exception e) {
            System.out.println("[EMAIL] Error sending admin driver trip assignment: " + e.getMessage());
        }
    }

    // @PostMapping("/customBooking")
    // public Booking createCustomBooking(@RequestBody Booking b) {
    // return this.bookingService.createCustomBooking(b);
    // }

    @PostMapping("/create")
    public String createBooking(
            @RequestParam(required = false) String fromLocation,
            @RequestParam(required = false) String toLocation,
            @RequestParam(required = false) String tripType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(required = false) String time,
            @RequestParam(required = false) String distance,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String bookingId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String userPickup,
            @RequestParam(required = false) String comanyName,
            @RequestParam(required = false) String userDrop,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String userTripType,
            @RequestParam(required = false) String car,
            @RequestParam(required = false) String baseAmount,
            @RequestParam(required = false) Integer amount,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String driverBhata,
            @RequestParam(required = false) Integer nightCharges,
            @RequestParam(required = false) Integer gst,
            @RequestParam(required = false) Integer serviceCharge,
            @RequestParam(required = false) String offer,
            @RequestParam(required = false) Integer offerPartial,
            @RequestParam(required = false) String offerAmount,
            @RequestParam(required = false) String txnId,
            @RequestParam(required = false) String payment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
            @RequestParam(required = false) String timeEnd,
            @RequestParam(required = false) int collection,
            @RequestParam(required = false) String bookingType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String carrier,
            @RequestParam(required = false) String driverEnterOtpTimePreStarted,
            @RequestParam(required = false) String odoometerStarted,
            @RequestParam(required = false) String odoometerEnterTimeStarted,
            @RequestParam(required = false) String driverEnterOtpTimePostTrip,
            @RequestParam(required = false) String odometerEnding,
            @RequestParam(required = false) int days,
            @RequestParam(required = false) String odoometerEnterTimeEnding) {

        Booking booking = new Booking();
        CarRentalUser carRental;

        // SOLUTION 1: Check if user already exists, if not create new one
        Optional<CarRentalUser> existingUser = carRentalRepository.findByPhone(phone);

        if (existingUser.isPresent()) {
            // ✅ Use existing user
            carRental = existingUser.get();
        } else {
            // ✅ Create new user with all required fields
            carRental = new CarRentalUser();
            carRental.setPhone(phone);
            String c = phone;
            String encodedPassword = passwordEncoder.encode(c);

            carRental.setPassword(encodedPassword);
            carRental.setUserName(name); // ✅ Add required fields
            carRental.setEmail(email); // ✅ Add email
            carRental.setRole("USER"); // ✅ Add role if required
            // Add any other required fields for CarRentalUser

            // ✅ SAVE CarRentalUser FIRST
            carRental = carRentalRepository.save(carRental);
        }

        // Generate unique booking ID
        String bookids = "CSTM_WTL" + System.currentTimeMillis();
        booking.setBookid(bookids);
        booking.setBookingId(bookids);
        booking.setFromLocation(userPickup);
        booking.setToLocation(userDrop);
        booking.setTripType(tripType);
        booking.setStartDate(startDate);
        booking.setReturnDate(returnDate);
        booking.setCompanyName(comanyName);
        booking.setStatus(0);
        booking.setTime(time);
        booking.setDistance(distance);
        booking.setUserId(userId);
        booking.setName(name);
        booking.setEmail(email);
        booking.setPhone(phone);
        booking.setUserPickup(userPickup);
        booking.setUserDrop(userDrop);
        booking.setDate(date);
        booking.setUserTripType(userTripType);
        booking.setCar(car);
        booking.setDays(days);
        booking.setBaseAmount(baseAmount);
        booking.setAmount(amount);
        booking.setStatus(status);
        booking.setDriverBhata(driverBhata);
        booking.setNightCharges(nightCharges);
        booking.setGst(gst);
        booking.setServiceCharge(serviceCharge);
        booking.setOffer(offer);
        booking.setOfferPartial(offerPartial);
        booking.setOfferAmount(offerAmount);
        booking.setTxnId(txnId);
        booking.setPayment(payment);
        booking.setDateEnd(dateEnd);
        booking.setTimeEnd(timeEnd);
        booking.setCollection(collection);
        booking.setBookingType(bookingType);
        booking.setDescription(description);
        booking.setCarrier(carrier);

        // Guard parsing of optional date-time fields to avoid NPE when frontend omits
        // them
        if (driverEnterOtpTimePreStarted != null && !driverEnterOtpTimePreStarted.isBlank()) {
            booking.setDriverEnterOtpTimePreStarted(LocalDateTime.parse(driverEnterOtpTimePreStarted));
        }
        booking.setStartOdometer(odoometerStarted);
        if (odoometerEnterTimeStarted != null && !odoometerEnterTimeStarted.isBlank()) {
            booking.setOdoometerEnterTimeStarted(LocalDateTime.parse(odoometerEnterTimeStarted));
        }
        if (driverEnterOtpTimePostTrip != null && !driverEnterOtpTimePostTrip.isBlank()) {
            booking.setDriverEnterOtpTimePostTrip(LocalDateTime.parse(driverEnterOtpTimePostTrip));
        }
        booking.setEndOdometer(odometerEnding);
        if (odoometerEnterTimeEnding != null && !odoometerEnterTimeEnding.isBlank()) {
            booking.setOdoometerEnterTimeEnding(LocalDateTime.parse(odoometerEnterTimeEnding));
        }

        // Set the saved CarRentalUser
        booking.setCarRentalUser(carRental);

        // Persist and return
        bookingRepo.save(booking);
        return "Booking created successfully!";
    }

    @GetMapping("/get/{sourceCity}/{sourceState}/{destinationCity}/{destinationState}")
    public List<onewayTrip> getDate(@PathVariable String sourceCity, @PathVariable String sourceState,
            @PathVariable String destinationState, @PathVariable String destinationCity) {
        return this.tripSer.getAllData(sourceCity, sourceState, destinationState, destinationCity);

    }

    @GetMapping("/oneWay/{pickupLocation}/{dropLocation}")
    public List<onewayTrip> getoneWayTripData(
            @PathVariable String pickupLocation,
            @PathVariable String dropLocation) {
        return tripSer.getoneWayTripData(pickupLocation, dropLocation);
    }

    @GetMapping("/roundTrip/{pickupLocation}/{dropLocation}")
    public List<roundTrip> getRoundWayTripData(
            @PathVariable String pickupLocation,
            @PathVariable String dropLocation) {
        return tripSer.getRoundWayTripData(pickupLocation, dropLocation);
    }

    @PostMapping("/rounprice")
    public ResponseEntity<roundTrip> createRoundWayTripPrice(
            @RequestParam String sourceState,
            @RequestParam String destinationState,
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam int hatchbackPrice,
            @RequestParam int sedanPrice,
            @RequestParam int sedanPremiumPrice,
            @RequestParam int suvPrice,
            @RequestParam int suvPlusPrice,
            @RequestParam(required = false, defaultValue = "s") String status) {

        // Call the service method which contains your provided code.
        roundTrip savedTrip = tripSer.postRoundTripprice(
                sourceState,
                destinationState,
                sourceCity,
                destinationCity,
                hatchbackPrice,
                sedanPrice,
                sedanPremiumPrice,
                suvPrice,
                suvPlusPrice,
                status);

        // Return the saved one-way trip pricing object along with HTTP 200 OK status.
        return ResponseEntity.ok(savedTrip);
    }

    @GetMapping("/oneWay2/{pickupLocation}/{dropLocation}")
    public List<onewayTrip> getoneWayTripData2(
            @PathVariable String pickupLocation,
            @PathVariable String dropLocation) {
        return tripSer.getoneWayTripData(pickupLocation, dropLocation);
    }

    @PostMapping("/oneprice")
    public ResponseEntity<onewayTrip> createOneWayTripPrice(
            @RequestParam String sourceState,
            @RequestParam String destinationState,
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam int hatchbackPrice,
            @RequestParam int sedanPrice,
            @RequestParam int sedanPremiumPrice,
            @RequestParam int suvPrice,
            @RequestParam int suvPlusPrice,
            @RequestParam(required = false, defaultValue = "s") String status) {

        onewayTrip savedTrip = tripSer.postOneWayTripprice(
                sourceState,
                destinationState,
                sourceCity,
                destinationCity,
                hatchbackPrice,
                sedanPrice,
                sedanPremiumPrice,
                suvPrice,
                suvPlusPrice,
                status);

        return ResponseEntity.ok(savedTrip);
    }

    // @GetMapping("/{vendorId}/length/vendorByBookings")
    // public ResponseEntity<List<Booking>> getBookingsByVendorLength(@PathVariable
    // Long vendorId) {
    // List<Booking> bookings = bookingService.getBookingByVendor(vendorId);
    // return bookings.length();

    // }

    // Excel code

    @PutMapping("/{bookingId}/assignCabAdmin/{cabAdminId}")
    public ResponseEntity<Booking> assignCabAdminToBooking(@PathVariable int bookingId, @PathVariable Long cabAdminId) {

        Booking updatedBooking = bookingService.assignCabAdminToBooking(cabAdminId, bookingId);

        if (updatedBooking == null) {
            // If the booking or vendor was not found, return a 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // No separate email on cab admin assignment; user will be emailed when driver is assigned.

        return ResponseEntity.ok(updatedBooking);

    }

    @PutMapping("/{bookingId}/assignDriveAdmin/{driveAdminId}")
    public ResponseEntity<Booking> assignDriveAdminToBooking(@PathVariable int bookingId,
            @PathVariable int driveAdminId) {

        Booking updatedBooking = bookingService.assignDriveAdminToBooking(driveAdminId, bookingId);

        if (updatedBooking == null) {
            // If the booking or vendor was not found, return a 404 Not Found
            return ResponseEntity.notFound().build();
        }
        // Send emails for admin driver assignment: user + admin driver
        sendUserDriverAssignmentEmail(updatedBooking, true);
        sendAdminDriverTripAssignmentEmail(updatedBooking);

        return ResponseEntity.ok(updatedBooking);

    }

    @PostMapping("/cancel-trip/{bookingId}")
    public ResponseEntity<?> cancelTrip(@PathVariable String bookingId) {
        try {
            Optional<Booking> optional = bookingRepo.findByBookingId(bookingId);
            if (optional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("status", "error", "message", "Booking not found"));
            }

            Booking booking = optional.get();
            // Mark as cancelled (status code 5 used in UI)
            try {
                booking.setStatus(5);
            } catch (Exception ignored) {
            }

            // Clear any assigned resources
            booking.setVendor(null);
            booking.setVendorCab(null);
            booking.setVendorDriver(null);
            booking.setDriveAdmin(null);
            booking.setCabAdmin(null);

            Booking saved = bookingRepo.save(booking);

            // Send cancellation email to user
            sendUserBookingCancellationEmail(saved);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Booking cancelled successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Failed to cancel booking"));
        }
    }

    @GetMapping("/send-sms")
    public String sendSms(
            @RequestParam String phoneNumber,
            @RequestParam String carrier,
            @RequestParam String message) { // Add the 'message' parameter
        boolean isSent = smsService.sendSms(phoneNumber, carrier, message); // Pass all three parameters
        if (isSent) {
            return "SMS sent successfully!";
        } else {
            return "Failed to send SMS.";
        }
    }

    // send manual

    @PostMapping("/send-manual/{email:.+}")
    public ResponseEntity<?> sendManualEmailWithPath(@PathVariable("email") String email) {
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("jaywantmhala928@gmail.com"); // Use your sender email
            helper.setTo(email);
            helper.setSubject("Vendor Registration - Please Fill the Details");

            // HTML formatted email content
            String emailContent = "<html><body>" +
                    "<p>Hi,</p>" +
                    "<p>Please click on the following link to fill in the vendor registration details:</p>" +
                    "<p><a href='http://192.168.231.233:3001/vendor-registration'  " +
                    "style='color: #007bff; text-decoration: none; font-weight: bold;'>Complete Vendor Registration</a></p>"
                    +
                    "<p>Thank you.</p>" +
                    "<p>Best Regards,<br>WTL Tourism Pvt. Ltd.</p>" +
                    "</body></html>";

            helper.setText(emailContent, true); // Enable HTML content
            mailSender.send(mimeMessage); // Send email

            return ResponseEntity.ok("Email sent successfully");
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + ex.getMessage());
        }

    }

    @PostMapping("/validate")
    public Map<String, Object> bookingValidated(
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam String email) {
        return bookingService.bookingValidated(name, phone, email);
    }

    @PutMapping("/booking/{id}")
    public ResponseEntity<BookingDTO> updateBookingById(@PathVariable int id, @RequestBody BookingDTO bookingDTO) {
        // Debug: Log incoming DTO
        System.out.println("[DEBUG] updateBookingById called for id=" + id);
        System.out.println("[DEBUG] Incoming BookingDTO: " + bookingDTO);
        try {
            BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
            if (updatedBooking != null) {
                System.out.println("[DEBUG] Booking updated successfully: " + updatedBooking);
                return ResponseEntity.ok(updatedBooking); // Return HTTP 200 OK with the updated booking DTO
            } else {
                System.out.println("[DEBUG] Booking not found for id=" + id);
                return ResponseEntity.notFound().build(); // Return HTTP 404 if booking is not found
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Exception in updateBookingById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
