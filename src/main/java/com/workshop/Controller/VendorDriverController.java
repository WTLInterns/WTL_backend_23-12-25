package com.workshop.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.Entity.Vendor;
import com.workshop.Entity.VendorCabs;
import com.workshop.Entity.VendorDrivers;
import com.workshop.Repo.VendorRepository;
import com.workshop.Service.CloudinaryService;
import com.workshop.Service.EmailService;
import com.workshop.Service.NotificationService;
import com.workshop.Service.VendorDriverService;

@RestController
public class VendorDriverController {

    @Autowired
    private VendorDriverService vendorDriverService;
    
    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private NotificationService notificationService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/vendorDriver/";
    
    private void ensureUploadDirExists() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists() && uploadDir.mkdirs()) {
            System.out.println("✅ Upload directory created: " + UPLOAD_DIR);
        }
    }

    // ✅ Utility: Save File
    private String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null; // ✅ Return null if no file is uploaded
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ File saved: " + filePath);
        } catch (IOException e) {
            System.err.println("❌ File saving failed: " + e.getMessage());
            throw e;
        }
        return fileName;
    }
    
    @PostMapping("/addVendorDriver/{id}")
    public ResponseEntity<?> addVendorDriver(
        @PathVariable Long id,

        // 📝 TEXT INPUT FIELDS
        @RequestParam(value = "driverName", required = false) String driverName,
        @RequestParam(value = "contactNo", required = false) String contactNo,
        @RequestParam(value = "altContactNo", required = false) String altContactNo,
        @RequestParam(value = "address", required = false) String address,
        @RequestParam(value = "dLNo", required = false) String dLNo,
        @RequestParam(value = "pvcNo", required = false) String pvcNo,
        @RequestParam(value = "emailId", required = false) String emailId,
        @RequestParam(value = "driverOtherDetails", required = false) String driverOtherDetails,

        // 🖼️ IMAGE/FILE FIELDS
        @RequestPart(value = "driverImage", required = false) MultipartFile driverImage,
        @RequestPart(value = "driverSelfie", required = false) MultipartFile driverSelfie,
        @RequestPart(value = "dLnoImage", required = false) MultipartFile dLnoImage,
        @RequestPart(value = "pvcImage", required = false) MultipartFile pvcImage,
        @RequestPart(value = "driverDoc1Image", required = false) MultipartFile driverDoc1Image,
        @RequestPart(value = "driverDoc2Image", required = false) MultipartFile driverDoc2Image,
        @RequestPart(value = "driverDoc3Image", required = false) MultipartFile driverDoc3Image
    ) {
        try {
            // ✅ Upload all images conditionally
            String driverImageUrl = (driverImage != null && !driverImage.isEmpty()) ? cloudinaryService.upload(driverImage) : null;
            String driverSelfieUrl = (driverSelfie != null && !driverSelfie.isEmpty()) ? cloudinaryService.upload(driverSelfie) : null;
            String dLnoImageUrl = (dLnoImage != null && !dLnoImage.isEmpty()) ? cloudinaryService.upload(dLnoImage) : null;
            String pvcImageUrl = (pvcImage != null && !pvcImage.isEmpty()) ? cloudinaryService.upload(pvcImage) : null;
            String driverDoc1ImageUrl = (driverDoc1Image != null && !driverDoc1Image.isEmpty()) ? cloudinaryService.upload(driverDoc1Image) : null;
            String driverDoc2ImageUrl = (driverDoc2Image != null && !driverDoc2Image.isEmpty()) ? cloudinaryService.upload(driverDoc2Image) : null;
            String driverDoc3ImageUrl = (driverDoc3Image != null && !driverDoc3Image.isEmpty()) ? cloudinaryService.upload(driverDoc3Image) : null;

            // ✅ Fetch Vendor by ID
            Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id " + id));

            // ✅ Create and set fields
            VendorDrivers driver = new VendorDrivers();
            driver.setDriverName(driverName);
            driver.setContactNo(contactNo);
            driver.setAltContactNo(altContactNo);
            driver.setAddress(address);
            driver.setdLNo(dLNo);
            driver.setPvcNo(pvcNo);
            driver.setEmailId(emailId);
            driver.setDriverOtherDetails(driverOtherDetails);

            driver.setDriverImage(driverImageUrl);
            driver.setDriverSelfie(driverSelfieUrl);
            driver.setdLnoImage(dLnoImageUrl);
            driver.setPvcImage(pvcImageUrl);
            driver.setDriverDoc1Image(driverDoc1ImageUrl);
            driver.setDriverDoc2Image(driverDoc2ImageUrl);
            driver.setDriverDoc3Image(driverDoc3ImageUrl);

            driver.setVendor(vendor);

            // ✅ Set initial password as last 6 digits of DL number
            String last6 = null;
            if (dLNo != null) {
                last6 = dLNo.length() >= 6 ? dLNo.substring(dLNo.length() - 6) : dLNo;
            }
            driver.setPassword(last6);

            // ✅ Send Email
            String subject = "Driver Registration Successfully - " + driver.getDriverName();
            String message = "<html><body>"
                + "<h2>Dear " + driver.getDriverName() + ",</h2>"
                + "<p>Your registration with WTL Tourism Pvt Ltd has been confirmed.</p>"
                + "<ul>"
                + "<li><strong>Contact No:</strong> " + driver.getContactNo() + "</li>"
                + "<li><strong>Alt Contact No:</strong> " + driver.getAltContactNo() + "</li>"
                + "<li><strong>Address:</strong> " + driver.getAddress() + "</li>"
                + "<li><strong>DL No:</strong> " + driver.getdLNo() + "</li>"
                + "<li><strong>PVC No:</strong> " + driver.getPvcNo() + "</li>"
                + "<li><strong>Email:</strong> " + driver.getEmailId() + "</li>"
                + "<li><strong>Password (DL last 6):</strong> " + last6 + "</li>"
                + "</ul>"
                + "<p>Please do not share these credentials with anyone.</p>"
                + "<hr><p>For support, contact wtltourism@gmail.com</p>"
                + "</body></html>";

            emailService.sendEmail(message, subject, driver.getEmailId());

            // ✅ Save and return response
            VendorDrivers savedDriver = vendorDriverService.addVendorsDriver(driver);
            return ResponseEntity.ok(savedDriver);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error while saving vendor driver: " + e.getMessage());
        }
    }

    // ✅ Utility: Get File Path
    private String getFilePath(String fileName) {
        return fileName != null ? "/uploads/" + fileName : null;
    }
    
    @GetMapping("/vendorDriver/{id}")
    public Optional<VendorDrivers> getVendorDriverById(@PathVariable int id) {
        return this.vendorDriverService.getVendorDriversById(id);
    }
    
    @GetMapping("/getAllVendorDriver")
    public List<VendorDrivers> getAllVendorDriver(){
        return this.vendorDriverService.getAllDrivers();
    }
    
    @GetMapping("/{vendorId}/drivers")
    public ResponseEntity<List<VendorDrivers>> getAllVendorCabByVendorId(@PathVariable Long vendorId) {
        try {
            // Call service method to get all VendorCabs for the given vendorId
            List<VendorDrivers> vendorCabs = vendorDriverService.getOrder(vendorId);

            // Return the list of VendorCabs with a 200 OK response
            return ResponseEntity.ok(vendorCabs);
        } catch (RuntimeException e) {
            // Return a 404 Not Found response if vendor is not found
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/vendorDriver/register-token")
    public ResponseEntity<?> registerVendorDriverToken(@RequestBody Map<String, String> body) {
        try {
            String token = body.get("fcmToken");
            String idStr = body.get("driverId");
            if (token == null || token.isBlank() || idStr == null || idStr.isBlank()) {
                return ResponseEntity.badRequest().body("driverId and fcmToken are required");
            }
            int driverId = Integer.parseInt(idStr);
            VendorDrivers driver = vendorDriverService.getVendorDriversById(driverId)
                .orElseThrow(() -> new RuntimeException("Vendor driver not found"));

            driver.setFcmToken(token);
            driver.setFcmUpdatedAt(java.time.LocalDateTime.now());
            vendorDriverService.addVendorsDriver(driver);

            return ResponseEntity.ok(Map.of(
                "saved", true,
                "driverId", String.valueOf(driver.getVendorDriverId()),
                "updatedAt", driver.getFcmUpdatedAt() != null ? driver.getFcmUpdatedAt().toString() : null
            ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to save token: " + ex.getMessage());
        }
    }

    // ---------------- TEST NOTIFICATION APIS ----------------
    @PostMapping("/vendorDriver/test-notify")
    public ResponseEntity<?> testNotifyVendorDriver(@RequestBody Map<String, String> payload) {
        try {
            String idStr = payload.get("driverId");
            if (idStr == null || idStr.isBlank()) {
                return ResponseEntity.badRequest().body("driverId is required");
            }
            int driverId = Integer.parseInt(idStr);
            VendorDrivers driver = vendorDriverService.getVendorDriversById(driverId)
                    .orElseThrow(() -> new RuntimeException("Vendor driver not found"));

            String token = driver.getFcmToken();
            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("Driver FCM token not set");
            }

            String title = payload.getOrDefault("title", "New Trip Assigned (TEST)");
            String body = payload.getOrDefault("body", "This is a test push to the driver app");

            Map<String, String> data = Map.of(
                    "type", payload.getOrDefault("type", "TRIP_ASSIGNED"),
                    "bookingId", payload.getOrDefault("bookingId", "TEST-BOOKING"),
                    "pickup", payload.getOrDefault("pickup", "Test Pickup"),
                    "drop", payload.getOrDefault("drop", "Test Drop"),
                    "tripType", payload.getOrDefault("tripType", "oneWay"));

            boolean sent = notificationService.sendToToken(token, title, body, data);
            return ResponseEntity.ok(Map.of("sent", sent, "driverId", String.valueOf(driverId)));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed: " + ex.getMessage());
        }
    }

    @PostMapping("/vendorDriver/test-notify-token")
    public ResponseEntity<?> testNotifyVendorDriverByToken(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            if (token == null || token.isBlank()) {
                return ResponseEntity.badRequest().body("token is required");
            }
            String title = payload.getOrDefault("title", "New Trip Assigned (TEST)");
            String body = payload.getOrDefault("body", "This is a test push to the driver app");
            Map<String, String> data = Map.of(
                    "type", payload.getOrDefault("type", "TRIP_ASSIGNED"),
                    "bookingId", payload.getOrDefault("bookingId", "TEST-BOOKING"));

            boolean sent = notificationService.sendToToken(token, title, body, data);
            return ResponseEntity.ok(Map.of("sent", sent));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed: " + ex.getMessage());
        }
    }
}
