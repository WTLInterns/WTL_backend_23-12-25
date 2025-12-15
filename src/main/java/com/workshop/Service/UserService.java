package com.workshop.Service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.workshop.DTO.UserRegistrationDto;
import com.workshop.Entity.User;


public interface UserService extends UserDetailsService{
	
	User save(UserRegistrationDto registrationDto);

	List<User> findAllUsers();

}
