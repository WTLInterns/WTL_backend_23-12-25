package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.RegisteredWebsite;
@Repository
public interface WebsiteRegisterRepo extends JpaRepository<RegisteredWebsite, Integer> {

	 RegisteredWebsite findByApiKey(String apiKey);
	
	
}
