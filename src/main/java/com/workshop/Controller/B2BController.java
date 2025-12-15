package com.workshop.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.DTO.B2BLoginRequest;
import com.workshop.DTO.B2BLoginResponse;
import com.workshop.Entity.B2B;
import com.workshop.Service.B2BService;
import com.workshop.Service.CloudinaryService;
import com.workshop.*;

@RestController
@RequestMapping("/b2b")
public class B2BController {

    // Upload folder inside static
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    @Autowired
    private B2BService service;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * POST /b2b/add
     * Saves file with the original file name, stores that name in the DB,
     * and returns full image URLs in the JSON response.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addB2B(
            @RequestParam("companyName") String companyName,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("alternateMobileNo") String alternateMobileNo,
            @RequestParam("city") String city,
            @RequestParam("businessGmail") String businessGmail,
            @RequestParam("bankName") String bankName,
            @RequestParam("bankAccountNo") String bankAccountNo,
            @RequestParam("ifscCode") String ifscCode,
            @RequestParam("panNo") String panNo,
            @RequestParam(value = "companyOtherDetails", required = false) String companyOtherDetails,
            @RequestParam("gmail") String gmail,
            @RequestParam("password") String password,

            @RequestPart(value = "companyLogo", required = false) MultipartFile companyLogo,
            @RequestPart(value = "govtApprovalCertificate", required = false) MultipartFile govtApprovalCertificate,
            @RequestPart(value = "companyDocs", required = false) MultipartFile companyDocs,
            @RequestPart(value = "panDocs", required = false) MultipartFile panDocs

    // B2B saved = service.saveB2B(b2b);
    // return ResponseEntity.ok(saved);
    ) {
        try {

            // Save files using their original names
            String logoFileName = (companyLogo != null && !companyLogo.isEmpty())
                    ? cloudinaryService.upload(companyLogo)
                    : null;
            String certFileName = (govtApprovalCertificate != null && !govtApprovalCertificate.isEmpty())
                    ? cloudinaryService.upload(govtApprovalCertificate)
                    : null;
            String docsFileName = (companyDocs != null && !companyDocs.isEmpty())
                    ? cloudinaryService.upload(companyDocs)
                    : null;
            String panFileName = (panDocs != null && !panDocs.isEmpty())
                    ? cloudinaryService.upload(panDocs)
                    : null;

            System.out.println("Saved Company Logo: " + logoFileName);
            System.out.println("Saved Govt Approval Certificate: " + certFileName);
            System.out.println("Saved Company Docs: " + docsFileName);
            System.out.println("Saved PAN Docs: " + panFileName);

            // Create entity and populate fields
            B2B b2b = new B2B();
            b2b.setCompanyName(companyName);
            b2b.setContactNo(contactNo);
            b2b.setAlternateMobileNo(alternateMobileNo);
            b2b.setCityName(city);
            b2b.setBusinessGmail(businessGmail);
            b2b.setBankName(bankName);
            b2b.setBankAccountNo(bankAccountNo);
            b2b.setIfscCode(ifscCode);
            b2b.setPanNo(panNo);
            b2b.setCompanyOtherDetails(companyOtherDetails);
            b2b.setGmail(gmail);
            b2b.setPassword(password);

            // Store filenames in DB
            b2b.setCompanyLogo(logoFileName);
            b2b.setGovtApprovalCertificate(certFileName);
            b2b.setCompanyDocs(docsFileName);
            b2b.setPanDocs(panFileName);

            B2B savedB2B = service.saveB2B(b2b);
            System.out.println("B2B saved with ID: " + savedB2B.getId());

            return ResponseEntity.ok(savedB2B);
        } catch (Exception e) {
            System.err.println("Error in addB2B: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while saving B2B: " + e.getMessage());
        }
    }

    
    @GetMapping("/all")
    public ResponseEntity<List<B2B>> getAllB2B() {
        List<B2B> list = service.getAllB2B();
        System.out.println("Fetched " + list.size() + " B2B records");
        return ResponseEntity.ok(list);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getB2BById(@PathVariable Long id) {
        B2B b2b = service.getB2BById(id);
        if (b2b == null) {
            System.out.println("B2B record not found for ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "B2B record not found"));
        }
        return ResponseEntity.ok(generateB2BResponse(b2b));
    }

  
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteB2B(@PathVariable Long id) {
        String message = service.deleteB2B(id);
        System.out.println("Delete message: " + message);
        if ("B2B deleted successfully".equals(message)) {
            return ResponseEntity.ok(message);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    
    @GetMapping("/email/{businessGmail}")
    public ResponseEntity<?> getB2BByEmail(@PathVariable String businessGmail) {
        B2B b2b = service.getB2BByEmail(businessGmail);
        if (b2b == null) {
            System.out.println("B2B record not found for businessGmail: " + businessGmail);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "B2B record not found"));
        }
        return ResponseEntity.ok(generateB2BResponse(b2b));
    }

   
    @PostMapping("/login")
    public ResponseEntity<?> b2bLogin(@RequestBody B2BLoginRequest loginRequest) {
        try {
            B2BLoginResponse b2bLoginResponse = service.b2bLogin(loginRequest.getGmail(), loginRequest.getPassword());
            System.out.println("Login successful for Gmail: " + loginRequest.getGmail());
            return ResponseEntity.ok(b2bLoginResponse);
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid credentials or other error.");
        }
    }

   
    private void ensureUploadDirExists() {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists() && uploadDir.mkdirs()) {
            System.out.println("✅ Upload directory created: " + UPLOAD_DIR);
        } else {
            System.out.println("Upload directory exists: " + UPLOAD_DIR);
        }
    }

    /**
     * Save file with original filename
     */
    // private String saveFile(MultipartFile file) throws IOException {
    // if (file == null || file.isEmpty()) {
    // System.out.println("No file provided for upload.");
    // return null;
    // }
    // String fileName = file.getOriginalFilename();
    // Path filePath = Paths.get(UPLOAD_DIR, fileName);

    // try (var inputStream = file.getInputStream()) {
    // Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
    // System.out.println("✅ File saved: " + filePath.toAbsolutePath());
    // } catch (IOException e) {
    // System.err.println("❌ File saving failed: " + e.getMessage());
    // throw e;
    // }
    // return fileName;
    // }

    /**
     * Build JSON response with full image URLs
     */
    private Map<String, Object> generateB2BResponse(B2B b2b) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", b2b.getId());
        response.put("companyName", b2b.getCompanyName());
        response.put("contactNo", b2b.getContactNo());
        response.put("alternateMobileNo", b2b.getAlternateMobileNo());
        response.put("city", b2b.getCityName());
        response.put("businessEmail", b2b.getBusinessGmail());
        response.put("bankName", b2b.getBankName());
        response.put("bankAccountNo", b2b.getBankAccountNo());
        response.put("ifscCode", b2b.getIfscCode());
        response.put("panNo", b2b.getPanNo());
        response.put("companyOtherDetails", b2b.getCompanyOtherDetails());

        // Convert file names to full URLs
        response.put("companyLogo", buildFullUrl(b2b.getCompanyLogo()));
        response.put("govtApprovalCertificate", buildFullUrl(b2b.getGovtApprovalCertificate()));
        response.put("companyDocs", buildFullUrl(b2b.getCompanyDocs()));
        response.put("panDocs", buildFullUrl(b2b.getPanDocs()));

        return response;
    }

   
    private String buildFullUrl(String fileName) {
        if (fileName != null) {
            return "https://api.worldtriplink.com/uploads/" + fileName;
        }
        return null;
    }
}
