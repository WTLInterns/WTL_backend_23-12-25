package com.workshop.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.workshop.DTO.B2BLoginResponse; // Your B2B Entity
import com.workshop.Entity.B2B;
import com.workshop.Repo.B2BRepo;

@Service
public class B2BService {

    private final B2BRepo repository;

    // Constructor Injection
    public B2BService(B2BRepo repository) {
        this.repository = repository;
    }

    // ✅ Save B2B
    public B2B saveB2B(B2B b2b) {
        return repository.save(b2b);
    }

    // ✅ Get All B2B
    public List<B2B> getAllB2B() {
        return repository.findAll();
    }

    // ✅ Get B2B by ID
    public B2B getB2BById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ✅ Get B2B by Company Name (if you have a custom finder)
    public B2B getB2BByCompanyName(String companyName) {
        return repository.findByCompanyName(companyName).orElse(null);
    }

    // ✅ Delete B2B
    public String deleteB2B(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "B2B deleted successfully";
        }
        return "B2B not found";
    }

    public B2B getB2BByEmail(String businessGmail) {
        return repository.findByBusinessGmail(businessGmail);
    }

    // ✅ Login (Optional) - Similar to Vendor Login
    // Adjust fields (e.g. email/password) to match your B2B Entity
    public B2BLoginResponse b2bLogin(String email, String password) {
        B2B b2b = repository.findByGmail(email); // Create a method findByEmail in B2BRepository
        if (b2b != null && password.equals(b2b.getPassword())) {
            return new B2BLoginResponse(b2b.getId(), b2b.getGmail(), b2b.getPassword());
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}