package com.workshop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workshop.DTO.VendorLoginResponse;
import com.workshop.Entity.Booking;
import com.workshop.Entity.Vendor;
import com.workshop.Repo.VendorRepository;

import jakarta.persistence.EntityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@Service
public class VendorService {
    private final VendorRepository repository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public VendorService(VendorRepository repository) {
        this.repository = repository;
    }

    // ✅ Save Vendor
    public Vendor saveVendor(Vendor vendor) {
        vendor.setPassword("Vendor@123");
        return repository.save(vendor);
    }

    // ✅ Get All Vendors
    public List<Vendor> getAllVendors() {
        return repository.findAll();
    }

    // ✅ Get Vendor by ID
    public Vendor getVendorById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ✅ Get Vendor by Company Name
    public Vendor getVendorByCompanyName(String vendorCompanyName) {
        return repository.findByVendorCompanyName(vendorCompanyName).orElse(null);
    }

    // // ✅ Get Vendor by Email (NEW METHOD)
    public Vendor getVendorByEmail(String email) {
    return repository.findByVendorEmail(email);
    }

    // ✅ Delete Vendor
    public String deleteVendor(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Vendor deleted successfully";
        }
        return "Vendor not found";
    }

    public VendorLoginResponse vendorLogin(String email, String password) {
        Vendor vendor = repository.findByVendorEmail(email); // Find vendor by email

        if (vendor != null && password.equals(vendor.getPassword())) { // Check if passwords match
            // Return VendorLoginResponse with the desired fields
            return new VendorLoginResponse(vendor.getId(), vendor.getVendorEmail(), vendor.getPassword());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }


    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";

    private void ensureUploadDirExists() {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path target = Paths.get(UPLOAD_DIR, filename);
        try (var in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return filename;
    }

    // public List<Booking> getBookingsByVendor(Long vendorId) {
    // Optional<Vendor> vendorOpt = repository.findById(vendorId);
    // if (vendorOpt.isPresent()) {
    // return vendorOpt.get().getBooking();
    // } else {
    // throw new RuntimeException("Vendor not found with id " + vendorId);
    // }
    // }

    // public List<Booking> getBookingByVendor(Long vendorId) {
    // return repository.findByVendorId(vendorId);
    // }


    public Vendor updatePassword(Vendor vendor, Long id) {
        Optional<Vendor> existingVendorOpt = this.repository.findById(id);

        if (existingVendorOpt.isPresent()) {
            Vendor existingVendor = existingVendorOpt.get();

            // Update only the password field
            existingVendor.setPassword(passwordEncoder.encode(vendor.getPassword()));

            // Save the updated vendor (this keeps the existing ID)
            return repository.save(existingVendor);
        } else {
            throw new EntityNotFoundException("Vendor not found with ID: " + id);
        }
    }



     public Vendor updateVendorFields(
            Vendor existing,
            String vendorCompanyName,
            String contactNo,
            String alternateMobileNo,
            String city,
            String vendorEmail,
            String bankName,
            String bankAccountNo,
            String ifscCode,
            String aadharNo,
            String panNo,
            String udyogAadharNo,
            MultipartFile govtApprovalCertificate,
            MultipartFile vendorDocs,
            MultipartFile vendorImage,
            MultipartFile aadharPhoto,
            MultipartFile panPhoto,
            String vendorOtherDetails) throws IOException {
        // 1) Update plain-text fields if non-null
        if (vendorCompanyName != null)
            existing.setVendorCompanyName(vendorCompanyName);
        if (contactNo != null)
            existing.setContactNo(contactNo);
        if (alternateMobileNo != null)
            existing.setAlternateMobileNo(alternateMobileNo);
        if (city != null)
            existing.setCity(city);
        if (vendorEmail != null)
            existing.setVendorEmail(vendorEmail);
        if (bankName != null)
            existing.setBankName(bankName);
        if (bankAccountNo != null)
            existing.setBankAccountNo(bankAccountNo);
        if (ifscCode != null)
            existing.setIfscCode(ifscCode);
        if (aadharNo != null)
            existing.setAadharNo(aadharNo);
        if (panNo != null)
            existing.setPanNo(panNo);
        if (udyogAadharNo != null)
            existing.setUdyogAadharNo(udyogAadharNo);
        if (vendorOtherDetails != null)
            existing.setVendorOtherDetails(vendorOtherDetails);

        // // 2) Ensure upload directory exists
        // ensureUploadDirExists();

        // 3) Replace each file if a new one was provided
        if (govtApprovalCertificate != null && !govtApprovalCertificate.isEmpty()) {
            String uploadedUrl = cloudinaryService.upload(govtApprovalCertificate);
            existing.setGovtApprovalCertificate(uploadedUrl);
        }
    
        if (vendorDocs != null && !vendorDocs.isEmpty()) {
            String uploadedUrl = cloudinaryService.upload(vendorDocs);
            existing.setVendorDocs(uploadedUrl);
        }
    
        if (vendorImage != null && !vendorImage.isEmpty()) {
            String uploadedUrl = cloudinaryService.upload(vendorImage);
            existing.setVendorImage(uploadedUrl);
        }
    
        if (aadharPhoto != null && !aadharPhoto.isEmpty()) {
            String uploadedUrl = cloudinaryService.upload(aadharPhoto);
            existing.setAadharPhoto(uploadedUrl);
        }
    
        if (panPhoto != null && !panPhoto.isEmpty()) {
            String uploadedUrl = cloudinaryService.upload(panPhoto);
            existing.setPanPhoto(uploadedUrl);
        }

        // 4) Persist and return the updated entity
        return repository.save(existing);
    }

    public Vendor updatePassword(String password, Long id) {
        Vendor vendor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // encode password if using Spring Security
        // vendor.setPassword(passwordEncoder.encode(password));
        vendor.setPassword(password);
        return repository.save(vendor);
    }

    

}
