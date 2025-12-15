package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

	User findByUsername(String name);
	
	User findFirstByOrderByUseridDesc();

	User findByEmail(String email);

    
	
	

	
}
