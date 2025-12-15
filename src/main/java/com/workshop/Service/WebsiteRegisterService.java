package com.workshop.Service;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.RegisteredWebsite;
import com.workshop.Repo.WebsiteRegisterRepo;

@Service
public class WebsiteRegisterService {

	@Autowired
	WebsiteRegisterRepo repo;
	
	public void save(RegisteredWebsite website) {
		repo.save(website);
	}
	
	 public static String generateApiKey() {
	        SecureRandom secureRandom = new SecureRandom();
	        byte[] key = new byte[32];
	        secureRandom.nextBytes(key);
	        return Base64.getEncoder().encodeToString(key);
	    }
	 
	 public RegisteredWebsite findByApiKey(String apiKey) {
	        return repo.findByApiKey(apiKey);
	    }
	
}
