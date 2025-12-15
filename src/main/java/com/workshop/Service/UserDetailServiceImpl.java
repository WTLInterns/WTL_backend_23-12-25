package com.workshop.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.DTO.UserRegistrationDto;
import com.workshop.Entity.User;
import com.workshop.Repo.UserRepo;
import com.workshop.Repo.UserServiceRepo;

@Service
public class UserDetailServiceImpl implements UserService {

	@Autowired
	UserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repo.findByUsername(username);
		System.out.println(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				authorities);

		return userDetails;
	}

	@Override
	public User save(UserRegistrationDto userRegistrationDto) {
		String hashedPassword = passwordEncoder.encode(userRegistrationDto.getPassword());
		User user = new User(userRegistrationDto.getUsername(), userRegistrationDto.getEmail(), hashedPassword,
				userRegistrationDto.getPhone(), userRegistrationDto.getUserid(),null, userRegistrationDto.getRole());
		return repo.save(user);
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public User getByUsername(String username) {
		// TODO Auto-generated method stub
		return repo.findByUsername(username);
	}

	public User getLastUser() {
		return repo.findFirstByOrderByUseridDesc();
	}

	public void save(User user) {
		repo.save(user);
		// TODO Auto-generated method stub

	}

	public User findByid(Integer id) {
		return repo.findById(id).get();
	}

	public String getDistanceByPickupAndDrop(String pickup, String drop, String apiKey) {
		try {
			// URL encode the city names
			String encodedPickup = URLEncoder.encode(pickup, "UTF-8");
			String encodedDrop = URLEncoder.encode(drop, "UTF-8");

			// Build the URL with encoded city names
			String apiUrl = UriComponentsBuilder
					.fromUriString("https://maps.googleapis.com/maps/api/distancematrix/json")
					.queryParam("origins", encodedPickup)
					.queryParam("destinations", encodedDrop)
					.queryParam("key", apiKey)
					.toUriString();

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode root = objectMapper.readTree(response.getBody());

			// Ensure there is at least one row and one element in the response
			if (root.has("rows") && root.get("rows").isArray() && root.get("rows").size() > 0) {
				JsonNode elements = root.get("rows").get(0).path("elements");
				if (elements.isArray() && elements.size() > 0) {
					String distanceText = elements.get(0).path("distance").path("text").asText();
					return distanceText;
				}
			}

			return "Distance information not found in the response.";
		} catch (IOException e) {
			e.printStackTrace();
			return "Error retrieving distance.";
		}
	}

	public String getLongNameByCity(String city, String apiKey) {
		String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + city + "&key=" + apiKey;

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode root = objectMapper.readTree(response.getBody());
			JsonNode result = root.path("results").get(0);

			// Find the administrative area level 3 (Jalgaon) component
			JsonNode adminAreaLevel3Component = null;
			for (JsonNode component : result.path("address_components")) {
				JsonNode types = component.path("types");
				if (types.isArray() && types.size() > 0
						&& types.get(0).asText().equals("administrative_area_level_3")) {
					adminAreaLevel3Component = component;
					break;
				}
			}

			if (adminAreaLevel3Component != null) {
				String longName = adminAreaLevel3Component.path("long_name").asText();
				return longName;
			} else {
				return "Administrative Area Level 3 not found.";
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "Error retrieving long name.";
		}
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public User login(String username, String password) {
		User user = repo.findByUsername(username);
		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}

}
