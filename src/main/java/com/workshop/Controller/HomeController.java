package com.workshop.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@GetMapping
	public String home(Model model, Principal principal) {
		
		  boolean loggedIn = principal != null;

	        // Add the loggedIn attribute to the model
	        model.addAttribute("loggedIn", loggedIn);
	        
	        if (loggedIn) {
	            String username = principal.getName(); // This gets the username
	            model.addAttribute("username", username);
	        }
		return "index";
	}

	
	
}
