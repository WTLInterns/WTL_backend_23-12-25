package com.workshop.Repo;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.workshop.DTO.UserRegistrationDto;
import com.workshop.Entity.User;

public interface UserServiceRepo extends UserDetailsService {
	
	User save(UserRegistrationDto userRegistrationDto);

}
