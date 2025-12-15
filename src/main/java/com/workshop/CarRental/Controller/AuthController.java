package com.workshop.CarRental.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.CarRental.DTO.CarRentalLoginRequest;
import com.workshop.CarRental.DTO.CarRentalLoginResponse;
import com.workshop.CarRental.DTO.DriverLoginRequest;
import com.workshop.CarRental.DTO.DriverLoginResponse;
import com.workshop.CarRental.DTO.UnifiedLoginRequest;
import com.workshop.CarRental.DTO.UnifiedLoginResponse;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Service.AuthService;
import com.workshop.CarRental.Service.CarRentalBookingService;
import com.workshop.DTO.LoginRequest;
import com.workshop.Entity.User;
import com.workshop.Service.EmailService;

@RestController
// @CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CarRentalBookingService carRentalBookingService;

    @Autowired
    private AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public CarRentalUser createCarRentalUser(@RequestBody CarRentalUser carRentalUser){
        return this.authService.registerUser(carRentalUser);

    }

    @GetMapping("/getById")
    public CarRentalUser getByUserId(@PathVariable int id){
        return this.carRentalBookingService.getUserById(id);
    }

    // @PostMapping("/userlogin")
    // public ResponseEntity<CarRentalLoginResponse> loginUser(@RequestBody CarRentalLoginRequest loginRequest) {
    //     CarRentalLoginResponse response = authService.authenticateUser(loginRequest);

    //     if (response.getUsername() != null) {
    //         return ResponseEntity.ok()
    //                 .header("X-Auth-Status", "success")
    //                 .body(response);
    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    //                 .header("X-Auth-Status", "failed")
    //                 .body(response);
    //     }
    // }
    


    @PostMapping("/driverlogin")
    public ResponseEntity<?> loginDriver(
             @RequestBody DriverLoginRequest loginRequest) {
        
                DriverLoginResponse response = authService.authenticateDriver(loginRequest);
        
        if (response.getDriverName() != null) {
            return ResponseEntity.ok()
                    .header("X-Auth-Status", "success")
                    .body(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("X-Auth-Status", "failed")
                    .body(response);
        }
    }


    
    


      @Autowired
    private EmailService emailService;

    private Map<String, String> otpStorage = new HashMap<>();

    // Send OTP to email
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); 
        otpStorage.put(email, otp);

        String subject = "Password Reset OTP";
        String message = "Your OTP for password reset is: " + otp;

        emailService.sendEmail(email, subject, message);

        return ResponseEntity.ok("OTP sent successfully to " + email);
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    // Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        String newPassword = request.get("password");

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            String encryptedPassword = passwordEncoder.encode(newPassword); // Encrypt password
            boolean isUpdated = carRentalBookingService.updatePassword(email, encryptedPassword);

            if (isUpdated) {
                otpStorage.remove(email); // Remove OTP after successful verification
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update password. User not found.");
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }


    @PostMapping("/carRentalLogin")
    public ResponseEntity<?> login(@RequestBody CarRentalLoginRequest loginRequest) {
        CarRentalUser user = authService.login(loginRequest.getMobile(), loginRequest.getPassword());
        if (user != null) {
            // user.setPassword(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }


    // same
    
    // @PostMapping("/login")
    // public ResponseEntity<UnifiedLoginResponse> login(@RequestBody UnifiedLoginRequest loginRequest) {
    //     String mobile = loginRequest.getMobile();
    //     String password = loginRequest.getPassword();
    
    //     UnifiedLoginResponse unifiedResponse = new UnifiedLoginResponse();
    
    //     // Try user authentication
    //     CarRentalLoginRequest userRequest = new CarRentalLoginRequest();
    //     userRequest.setMobile(mobile);
    //     userRequest.setPassword(password);
    
    //     CarRentalLoginResponse userResponse = authService.authenticateUser(userRequest);
    
    //     if (userResponse != null && userResponse.getUsername() != null) {
    //         unifiedResponse.setStatus("success");
    //         unifiedResponse.setRole("USER");
    //         unifiedResponse.setData(userResponse);
    //         return ResponseEntity.ok().header("X-Auth-Status", "success").body(unifiedResponse);
    //     }
    
    //     // Try driver authentication
    //     DriverLoginRequest driverRequest = new DriverLoginRequest();
    //     driverRequest.setContactNo(mobile);
    //     driverRequest.setPassword(password);
    
    //     DriverLoginResponse driverResponse = authService.authenticateDriver(driverRequest);
    
    //     if (driverResponse != null && driverResponse.getDriverName() != null) {
    //         unifiedResponse.setStatus("success");
    //         unifiedResponse.setRole("DRIVER");
    //         unifiedResponse.setData(driverResponse);
    //         return ResponseEntity.ok().header("X-Auth-Status", "success").body(unifiedResponse);
    //     }
    
    //     // If both fail
    //     unifiedResponse.setStatus("failed");
    //     unifiedResponse.setRole(null);
    //     unifiedResponse.setData(null);
    
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    //             .header("X-Auth-Status", "failed")
    //             .body(unifiedResponse);
    // }


    @PostMapping("/login1")
    public ResponseEntity<UnifiedLoginResponse> handleLogin( @RequestBody UnifiedLoginRequest loginRequest) {
        UnifiedLoginResponse response = authService.combineLogin(loginRequest);
        
        return ResponseEntity.status(determineHttpStatus(response.getStatus()))
                .body(response);
    }

    private HttpStatus determineHttpStatus(String serviceStatus) {
        return switch (serviceStatus) {
            case "success" -> HttpStatus.OK;
            case "invalid_password" -> HttpStatus.UNAUTHORIZED;
            case "not_found" -> HttpStatus.NOT_FOUND;
            case "error" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    @GetMapping("/getProfile/{id}")
    public CarRentalUser getProfile(@PathVariable int id){
return this.carRentalBookingService.getProfile(id);
    }
    

    @PutMapping("/update-profile/{id}")
    public CarRentalUser updateProfile(@PathVariable int id, @RequestBody CarRentalUser carRentalUser){
return this.carRentalBookingService.updateProfileById(id, carRentalUser);
    }

    

    @GetMapping("/getCarRentalUserById/{id}")
    public CarRentalUser getUserById(@PathVariable int id){
        return this.carRentalBookingService.getCarRentalUserById(id);
    }
    
    @DeleteMapping("/deleteCarRentalUser/{userid}")
    public ResponseEntity<?> deleteCarRentalUser(@PathVariable int userid) {
        try {
            this.carRentalBookingService.deleteCarRentalUser(userid);
            return ResponseEntity.ok("CarRentalUser deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


}
