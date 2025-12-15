package com.workshop.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.workshop.DTO.CityDTO;
import com.workshop.DTO.PriceUpdateRequest;
import com.workshop.DTO.StateDTO;
import com.workshop.Entity.Booking;
import com.workshop.Entity.CabInfo;
import com.workshop.Entity.Cities;
import com.workshop.Entity.Popup;
import com.workshop.Entity.States;
import com.workshop.Entity.Tripprice;
import com.workshop.Entity.User;
import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;
import com.workshop.Repo.StateRepository;
import com.workshop.Repo.Trip;
import com.workshop.Service.BookingService;
import com.workshop.Service.CabInfoService;
import com.workshop.Service.CitiesService;
import com.workshop.Service.EmailService;
import com.workshop.Service.PopupService;
import com.workshop.Service.StatesService;
import com.workshop.Service.TripService;
import com.workshop.Service.UserDetailServiceImpl;
import com.workshop.Service.TripRateService;

@Controller
@CrossOrigin(origins = "http://localhost:3001,https://worldtriplink.com,https://api.worldtriplink.com")
public class BookingController {
	@Autowired
	BookingService ser;

	@Autowired
	private TripService tripSer;

	@Autowired
	CabInfoService cabser;

	@Autowired
	private TripRateService tripRateService;

	@Autowired
	private StatesService statesService;

	@Autowired
	private CitiesService citiesService;

	@Autowired
	private EmailService emailService;

	@Autowired
	PopupService service;

	@Autowired
	UserDetailServiceImpl userService;

	private final String apiKey = "AIzaSyDuZC6kFobB0pnp-k3VcxQIjvb0EhgfnVI"; // Replace with your Google API key

	// @PostMapping("/book")
	// public ResponseEntity<String> book(@RequestBody Booking booking){
	// System.out.println(booking);
	// String bookid = ser.getLastUsedBookingId();
	// if(bookid==null) {
	// bookid = "AIM0";
	// }else {
	// String numericPart = bookid.substring(3);
	//
	// try {
	// // Parse the numeric part to an integer and increment it
	// int numericValue = Integer.parseInt(numericPart);
	// numericValue++;
	//
	// // Create the new bookid with the incremented numeric part
	// bookid = "AIM" + numericValue;
	// } catch (NumberFormatException e) {
	// // Handle the case where the numeric part is not a valid integer
	// System.out.println("Invalid numeric part in bookid: " + numericPart);
	// }
	//
	// }
	//
	// booking.setBookingId(bookid);
	// if(booking.getDistance()=="0") {
	// System.out.println("distance not found");
	// return ResponseEntity.ok("not Successful");
	// }
	// ser.saveBooking(booking);
	// System.out.println("booked ");
	// return ResponseEntity.ok("Booked Successfully");
	//
	// }

	// @GetMapping("/states/{id}")
	// public ResponseEntity<States> getStateById(@PathVariable Long id) {
	// return this.statesService.getStateById(id)
	// .map(ResponseEntity::ok)
	// .orElse(ResponseEntity.notFound().build());
	// }
	//
	// @GetMapping("/all")
	// public List<States> getAllState(){
	// return this.statesService.getAllState();
	// }
	//
	// @Autowired
	// private StateRepository stateRepository;
	//
	// @GetMapping("/api/states")
	// public List<StateDTO> getStates() {
	// return stateRepository.findAll()
	// .stream()
	// .map(state -> new StateDTO(state.getId(), state.getName()))
	// .collect(Collectors.toList());
	// }
	//
	//
	// @GetMapping("/city/all")
	// public List<Cities> getAllCities() {
	// return citiesService.getAllCities();
	// }
	//
	// @GetMapping("/{id}")
	// public Optional<Cities> getCityById(@PathVariable Long id) {
	// return citiesService.getCityById(id);
	// }

	// @GetMapping("/cities/{stateId}")
	// public List<Cities> getCitiesByState(@PathVariable Long stateId) {
	// return citiesService.getCitiesByState(stateId);
	// }

	// @GetMapping("/cities/{stateId}")
	// public ResponseEntity<List<CityDTO>> getCitiesByState(@PathVariable Long
	// stateId) {
	// List<Cities> cities = citiesService.getCitiesByState(stateId);
	// if (cities.isEmpty()) {
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	// }
	//
	// // Convert Cities entities to CityDTO
	// List<CityDTO> cityDTOs = cities.stream()
	// .map(city -> {
	// CityDTO dto = new CityDTO();
	// dto.setId(city.getId());
	// dto.setName(city.getName());
	// return dto;
	// })
	// .collect(Collectors.toList());
	//
	// return ResponseEntity.ok(cityDTOs);
	// }

	// @PostMapping("/updateTripPricing")
	// public ResponseEntity<Map<String, String>> updateTripPricing(@RequestBody
	// Tripprice tripPricing) {
	// // Update trip pricing logic
	// this.tripRateService.updateTripPricing(tripPricing);
	//
	// // Create a map to return as a JSON object
	// Map<String, String> response = new HashMap<>();
	// response.put("message", "Trip pricing updated successfully!");
	//
	// return ResponseEntity.ok(response);
	// }
	//
	//
	// @PutMapping("/update-price/{id}")
	// public ResponseEntity<onewayTrip> updateTripPrice(@PathVariable Long id,
	// @RequestBody PriceUpdateRequest request) {
	// onewayTrip updatedTrip = tripSer.updatePrice(id, request.getHatchback(),
	// request.getSedan(),
	// request.getSedanpremium(), request.getSuv(), request.getSuvplus(),
	// request.getSourceState(), request.getSourceCity(),
	// request.getDestinationState(), request.getDestinationCity());
	// return ResponseEntity.ok(updatedTrip);
	// }

	// @PutMapping("/update-prices")
	// public ResponseEntity<Map<String, String>> updatePrices(
	// @RequestParam String sourceState,
	// @RequestParam String destinationState,
	// @RequestParam String sourceCity,
	// @RequestParam String destinationCity,
	// @RequestParam int hatchbackPrice,
	// @RequestParam int sedanPrice,
	// @RequestParam int sedanPremiumPrice,
	// @RequestParam int suvPrice,
	// @RequestParam int suvPlusPrice) {
	//
	// // Call the service to update trip prices
	// tripSer.updatePrices(sourceState, destinationState, sourceCity,
	// destinationCity,
	// hatchbackPrice, sedanPrice, sedanPremiumPrice, suvPrice, suvPlusPrice);
	//
	// // Construct a JSON response
	// Map<String, String> response = new HashMap<>();
	// response.put("message", "Prices updated successfully");
	//
	// return ResponseEntity.ok(response);
	// }

	// @PutMapping("/update-prices")
	// public ResponseEntity<Map<String, String>> updatePrices(@RequestBody
	// PriceUpdateRequest priceUpdateRequest) {
	// tripSer.updatePrices(
	// priceUpdateRequest.getSourceState(),
	// priceUpdateRequest.getDestinationState(),
	// priceUpdateRequest.getSourceCity(),
	// priceUpdateRequest.getDestinationCity(),
	// priceUpdateRequest.getHatchback(),
	// priceUpdateRequest.getSedan(),
	// priceUpdateRequest.getSedanpremium(),
	// priceUpdateRequest.getSuv(),
	// priceUpdateRequest.getSuvplus()
	// );
	//
	// Map<String, String> response = new HashMap<>();
	// response.put("message", "Prices updated successfully");
	//
	// return ResponseEntity.ok(response);
	// }

	//
	// Endpoint to update trip price based on sourceState and destinationState
	// @PutMapping("/update-price/{sourceState}")
	// public ResponseEntity<List<onewayTrip>> updateTripPrice(
	// @RequestParam String sourceState,
	// // @RequestParam String destinationState,
	// @RequestBody PriceUpdateRequest request) {

	// // Call the service to update the price based on the provided states
	// List<onewayTrip> updatedTrips = tripSer.updatePrice(
	// sourceState,
	// request.getDestinationState(),
	// request.getHatchback(),
	// request.getSedan(),
	// request.getSedanpremium(),
	// request.getSuv(),
	// request.getSuvplus()
	// );

	// // Return the list of updated trips
	// return ResponseEntity.ok(updatedTrips);
	// }

	// @PutMapping("/update/{sourceState}")
	// public onewayTrip updatePrice(@PathVariable String sourceState,onewayTrip
	// oneway ){
	// return this.tripSer.updatePrice(sourceState, oneway);
	// }

	@PostMapping("/book")
	public String book(
			@RequestParam("cabId") String cabId,
			@RequestParam("modelName") String modelName,
			@RequestParam("modelType") String modelType,
			@RequestParam("seats") String seats,
			@RequestParam("fuelType") String fuelType,
			@RequestParam("availability") String availability,
			@RequestParam("price") String price,
			@RequestParam("pickupLocation") String pickupLocation,
			@RequestParam("dropLocation") String dropLocation,
			@RequestParam("date") String date,
			@RequestParam("returndate") String returndate,
			@RequestParam("time") String time,
			@RequestParam("tripType") String tripType,
			@RequestParam("distance") String distance,
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("service") String service,
			@RequestParam("gst") String gst,
			@RequestParam("total") String total,
			@RequestParam("days") String days,
			@RequestParam("driverrate") String driverrate,

			@RequestParam("phone") String phone, Model model, Principal principal

	) throws UnsupportedEncodingException {
		System.out.println("Cab ID: " + cabId);
		System.out.println("Model Name: " + modelName);
		System.out.println("Model Type: " + modelType);
		System.out.println("Seats: " + seats);
		System.out.println("Fuel Type: " + fuelType);
		System.out.println("Availability: " + availability);
		System.out.println("Price: " + price);
		System.out.println("Pickup Location: " + pickupLocation);
		System.out.println("Drop Location: " + dropLocation);
		System.out.println("Date: " + date);
		System.out.println("Return Date: " + returndate);
		System.out.println("Time: " + time);
		System.out.println("Trip Type: " + tripType);
		System.out.println("distance: " + distance);
		System.out.println("name: " + name);
		System.out.println("email: " + email);
		System.out.println("phone: " + phone);

		LocalDate localDate1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

		// Use ISO_TIME pattern for parsing time strings
		LocalTime localTime1 = LocalTime.parse(time, DateTimeFormatter.ISO_TIME);

		// User user = userService.getByUsername(principal.getName());
		// System.out.println(user);
		User user;

		String userid;

		if (principal != null) {
			// User is logged in, get their user information
			user = userService.getByUsername(principal.getName());
		} else {
			// User is not logged in, create a guest user
			// You can use a predefined identifier like "guest" or generate a unique
			// identifier
			// For example:
			String guestUserId = "guest";
			user = new User();

			user.setUserid(guestUserId);
		}

		userid = user.getUserid();

		Booking booking = new Booking();
		booking.setFromLocation(pickupLocation);
		booking.setToLocation(dropLocation);
		booking.setTripType(tripType);
		booking.setStartDate(localDate1);
		booking.setTime(time);
		booking.setDistance(distance);
		booking.setName(name);
		booking.setEmail(email);
		booking.setPhone(phone);
		booking.setUserId(userid);
		booking.setUserDrop(dropLocation);
		booking.setUserPickup(pickupLocation);
		booking.setUserTripType(tripType);
		booking.setBookingType("website");
		booking.setDate(localDate1);
		booking.setCar(modelType);
		booking.setAmount(Integer.parseInt(price));
		booking.setGst(Integer.parseInt(gst));
		booking.setServiceCharge(Integer.parseInt(service));

		System.out.println(tripType);
		System.out.println(returndate);
		System.out.println(name);
		System.out.println(email);
		System.out.println(phone);

		if ("roundTrip".equals(tripType)) {
			System.out.println("check");
			System.out.println(returndate);
			LocalDate localDate2 = LocalDate.parse(returndate, DateTimeFormatter.ISO_DATE);
			booking.setReturnDate(localDate2);

		}

		// Set other attributes similarly

		String bookid = ser.getLastUsedBookingId();
		if (bookid == null) {
			bookid = "WTL0";
		} else {
			// String numericPart = bookid.substring(4);

			try {
				// Parse the numeric part to an integer and increment it
				long numericValue = System.currentTimeMillis();

				// Create the new bookid with the incremented numeric part
				bookid = "WTL" + numericValue;
			} catch (NumberFormatException e) {
				// Handle the case where the numeric part is not a valid integer
				System.out.println("Invalid numeric part in bookid: ");
			}
		}

		booking.setBookingId(bookid);
		booking.setBookid(bookid);
		if (booking.getDistance() == null || booking.getDistance().equals("0")) {
			System.out.println("Distance not found");
			// ResponseEntity.ok("Not Successful");
			System.out.println("Not Successful");
		}

		// String apiURL = "https://aimcabbooking.com/confirm-round-api.php";
		// StringBuilder postData = new StringBuilder();

		// postData.append("&date1=").append(URLEncoder.encode("2023-09-08", "UTF-8"));

		// System.out.println(postData);
		JSONObject requestBody = new JSONObject();
		requestBody.put("date1", date);
		requestBody.put("name", name);
		requestBody.put("email", email);
		requestBody.put("car", modelName);
		requestBody.put("distance", distance);
		requestBody.put("phone", phone);
		requestBody.put("user_pickup", pickupLocation);
		requestBody.put("user_drop", dropLocation);
		requestBody.put("time", time);
		requestBody.put("dateend", returndate);
		requestBody.put("timeend", "");
		requestBody.put("days", days);
		requestBody.put("user_trip_type", tripType);
		requestBody.put("amount", total);
		requestBody.put("baseAmount", price);
		requestBody.put("service_charge", service);
		requestBody.put("gst", gst);
		requestBody.put("driver_bhata", driverrate);

		// Add other properties as needed...

		// Convert the JSON object to a string
		String jsonRequestBody = requestBody.toString();

		try {
			String apiURL = "https://aimcabbooking.com/confirm-round-api-wtl.php";

			// Create the URL
			URL url = new URL(apiURL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// Set the HTTP method to POST
			connection.setRequestMethod("POST");

			// Set the request headers
			connection.setRequestProperty("Content-Type", "application/json");

			// Enable input/output streams for the connection
			connection.setDoOutput(true);

			// Write the JSON data to the connection's output stream
			try (OutputStream os = connection.getOutputStream()) {
				os.write(jsonRequestBody.getBytes("UTF-8"));
			}

			// Get the response from the API
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Read and process the response
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// Handle the API response here, if needed
				String apiResponse = response.toString();
				System.out.println("API Response: " + apiResponse);

				// Continue with your code as needed...
			} else {
				System.out.println("API request failed with response code: " + responseCode);
				// Handle the error
				System.out.println("API request failed");
			}

			// Close the connection
			connection.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
			// Handle any exceptions that occur during the request
			System.out.println("Not Successful");
		}
		System.out.println("before booking");
		ser.saveBooking(booking);
		System.out.println("Booked ");

		String subject = "Booking Confirmation - " + bookid;
		String message = "<h3>Hello " + name + ",</h3>" +
				"<p>Your booking has been confirmed.</p>" +
				"<p><strong>Booking Details:</strong></p>" +
				"<ul>" +
				"<li><strong>Booking ID:</strong> " + bookid + "</li>" +
				"<li><strong>Pickup Location:</strong> " + pickupLocation + "</li>" +
				"<li><strong>Drop Location:</strong> " + dropLocation + "</li>" +
				"<li><strong>Trip Type:</strong> " + tripType + "</li>" +
				"<li><strong>Date:</strong> " + date + "</li>" +
				"<li><strong>Time:</strong> " + time + "</li>" +
				"<li><strong>Amount Paid:</strong> ₹" + total + "</li>" +
				"</ul>" +
				"<p>Thank you for choosing us!</p>" +
				"<p><strong>Terms and Conditions:</strong></p>" +
				"<ul>" +
				"<li>Toll charges, parking fees, and other taxes are not included and will be charged as applicable.</li>"
				+
				"<li>Prices may be extended in case of route changes, additional stops, or waiting time.</li>" +
				"<li>Please refer to our website or contact support for detailed terms and conditions.</li>" +
				"</ul>";

		boolean emailSent = emailService.sendEmail(message, subject, email);

		if (emailSent) {
			System.out.println("Booking confirmation email sent successfully.");
		} else {
			System.out.println("Failed to send booking confirmation email.");
		}

		return "redirect:/testpage";
	}

	@GetMapping("/thankyou")
	public String getpage() {
		return "thankyou";
	}

	@GetMapping("/testpage")
	public String getpagetest() {
		return "test";
	}

	public String locationExtractor(String pickupLocation, String dropLocation) {
		// Extract city and state from pickupLocation
		String[] pickup = extractCityAndState(pickupLocation);
		// Extract city and state from dropLocation
		String[] drop = extractCityAndState(dropLocation);

		// pickup[0] = source city, pickup[1] = source state
		// drop[0] = destination city, drop[1] = destination state
		String sourceCity = pickup[0];
		String sourceState = pickup[1];
		String destinationCity = drop[0];
		String destinationState = drop[1];

		// Build and return a formatted string with the extracted values
		return "Source City: " + sourceCity + "\n"
				+ "Source State: " + sourceState + "\n"
				+ "Destination City: " + destinationCity + "\n"
				+ "Destination State: " + destinationState;
	}

	// Helper method to extract city and state from a location string.
	// Assumes the location is formatted as "City, State, ..." and returns the first
	// two parts.
	private String[] extractCityAndState(String location) {
		String[] parts = location.split(",");
		if (parts.length >= 2) {
			return new String[] { parts[0].trim(), parts[1].trim() };
		}
		return new String[] { "Unknown City", "Unknown State" };
	}

	@PostMapping("/cab")
	public String processForm(
			@RequestParam("tripType") String tripType,
			@RequestParam("pickupLocation") String pickupLocation,
			@RequestParam("dropLocation") String dropLocation,
			@RequestParam("date") String date,
			@RequestParam("Returndate") String returndate,

			@RequestParam("time") String time,
			@RequestParam("distance") String distance,
			Model model

	) {
		// Store form data in the model
		List<Trip> tripinfo = new ArrayList<>();
		System.out.println(distance);
		String numericString = distance.replaceAll("[^0-9.]", "");
		double floatValue = Double.parseDouble(numericString);

		int Dist = (int) floatValue;
		// int Dist = Integer.parseInt(distance);

		int Distance = 0;
		int days = 0;
		System.out.println(Distance);
		String cityName = "";
		String city1 = userService.getLongNameByCity(dropLocation, apiKey);
		if ("Administrative Area Level 3 not found.".equals(city1)) {
			city1 = dropLocation;
			String[] parts = city1.split(", ");
			cityName = parts[0];
			System.out.println(cityName + " test");
		} else if ("North Goa".equals(city1) || "South Goa".equals(city1)) {
			String[] parts = city1.split(", ");
			cityName = parts[0];
		} else {
			String[] parts = city1.split(" ");
			cityName = parts[0];
		}

		String city2 = userService.getLongNameByCity(pickupLocation, apiKey);
		String[] parts1 = city2.split(" ");
		String cityName1 = parts1[0];

		System.out.println(city1 + " this is city 1 values");
		System.out.println(city2);
		System.out.println(cityName);
		System.out.println(cityName1);

		if (cityName.equals("Bengaluru")) {
			cityName = "Bangalore";
		} else if (cityName1.equals("Bengaluru")) {
			cityName1 = "Bangalore";

		}

		if ("oneWay".equals(tripType)) {
			tripinfo = tripSer.getonewayTrip(cityName, cityName1);

			if (tripinfo.isEmpty()) {
				// Assign a default value to tripinfo if it is empty
				// You may need to modify this based on your actual default value
				System.out.println("it is empty ");

				onewayTrip defaultTrip = new onewayTrip(
						null, // Set default value for id (you might want to adjust this)
						"", // Set default value for sourceState
						"", // Set default value for sourceCity
						"", // Set default value for destinationState
						"", // Set default value for destinationCity
						14, // Set default value for hatchback
						16, // Set default value for sedan
						20, // Set default value for sedanpremium
						27, // Set default value for suv
						26, // Set default value for suvplus
						"",
						null, null, 0, 19
				// Set default value for status
				);

				tripinfo.add(defaultTrip);
			}

			Distance = Dist;
			System.out.println(Distance);
		} else if ("roundTrip".equals(tripType)) {

			String startdate = date;
			String enddate = returndate;
			String time1 = time;
			String endtime = "23:30:00";
			String distances = Integer.toString(Dist);
			System.out.println(startdate + " " + enddate + " " + time + " " + distance);
			LocalDate localDate1 = LocalDate.parse(startdate, DateTimeFormatter.ISO_DATE);
			LocalDate localDate2 = LocalDate.parse(enddate, DateTimeFormatter.ISO_DATE);
			days = (int) ChronoUnit.DAYS.between(localDate1, localDate2);
			days++;
			System.out.println(days);

			// Use ISO_TIME pattern for parsing time strings
			LocalTime localTime1 = LocalTime.parse(time1, DateTimeFormatter.ISO_TIME);
			LocalTime localTime2 = LocalTime.parse(endtime, DateTimeFormatter.ISO_TIME);

			Distance = tripSer.getRoundDistance(localDate1, localTime1, localDate2, localTime2, distances);

			tripinfo = tripSer.getRoundTrip(cityName, cityName1);

			if (tripinfo.isEmpty()) {
				// Assign a default value to tripinfo if it is empty
				// You may need to modify this based on your actual default value
				System.out.println("round trip is empty ");

				roundTrip defaultTrip = new roundTrip(
						null, "", "", "", "",
						12, 15, 18, 21, 26,
						"", null, null, 0, 15);

				tripinfo.add(defaultTrip);
			}

		}

		List<CabInfo> c = cabser.getAll();
		System.out.println(tripinfo + " trip info  ");

		System.out.println(tripType);
		System.out.println(pickupLocation);
		model.addAttribute("tripType", tripType);
		model.addAttribute("pickupLocation", pickupLocation);
		model.addAttribute("dropLocation", dropLocation);
		model.addAttribute("date", date);
		model.addAttribute("returndate", returndate);
		System.out.println("returndate " + returndate);
		model.addAttribute("time", time);
		model.addAttribute("distance", Distance);
		model.addAttribute("cabinfo", c);
		model.addAttribute("price", 10);
		model.addAttribute("tripinfo", tripinfo);
		model.addAttribute("days", days);

		// Redirect to another page (replace "redirect:/destinationPage" with the actual
		// destination URL)
		return "Cabs";
	}

	@GetMapping("/cabs")
	public String cabs() {
		return "Cabs";
	}

	@RequestMapping("favicon.ico")
	public ResponseEntity<Void> returnNoFavicon() {
		// Return 204 No Content instead of trying to render a view
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/getBooking")
	public List<Booking> getBooking(@RequestBody Map<String, String> requestBody) {
		String userid = requestBody.get("userid");

		return ser.getBooking(userid);

	}

	@GetMapping("/getUserBooking")
	public String getUserBooking(Model model, Principal principal) {
		boolean loggedIn = principal != null;

		// Add the loggedIn attribute to the model
		model.addAttribute("loggedIn", loggedIn);

		if (principal != null) {
			User user = userService.getByUsername(principal.getName());
			String userid = user.getUserid();
			List<Booking> booking = ser.getBooking(userid);
			System.out.println(booking);
			model.addAttribute("booking", booking);

			return "myTrip";
		} else {
			return "login";
		}
	}

	@GetMapping("/getAllBooking")
	public List<Booking> getAll() {
		return ser.getAll();
	}

	// @DeleteMapping("/delete-booking/{bookingId}")
	// public ResponseEntity<String> deleteBooking(@PathVariable String bookingId) {
	// String responseMessage = ser.deleteBookingByBookingId(bookingId);
	// if (responseMessage.contains("not found")) {
	// return ResponseEntity.notFound().build(); // Return 404 Not Found if the
	// booking does not exist
	// }
	// return ResponseEntity.ok(responseMessage); // Return 200 OK with the success
	// message
	// }
	// @DeleteMapping("/deleteBooking/{id}")
	// public void deleteBooking(@RequestBody Map<String, String> requestBody) {
	// String ids = requestBody.get("id");
	// // System.out.println(ids);
	// int id = Integer.parseInt(ids);
	// //public ResponseEntity<Booking>
	// //deleteBooking(@PathVariable String bookingId) {

	// // //System.out.println(id);

	// // Booking b = ser.findBookingbyId(id);
	// // //System.out.println(b);
	// // ser.deleteBooking(b);
	// ser.deleteBooking1(id);
	// }

	@PostMapping("/getPrice")
	public List<Trip> getPrice(@RequestBody Map<String, String> requestBody) {
		String to = requestBody.get("to");
		String from = requestBody.get("from");
		String tripType = requestBody.get("tripType");

		String city1 = userService.getLongNameByCity(to, apiKey);
		String[] parts = city1.split(" ");
		String cityName = parts[0];
		String city2 = userService.getLongNameByCity(from, apiKey);
		String[] parts1 = city2.split(" ");
		String cityName1 = parts1[0];

		System.out.println(city1);
		System.out.println(city2);
		System.out.println(cityName);
		System.out.println(cityName1);

		if ("oneWay".equals(tripType)) {
			return tripSer.getonewayTrip(cityName, cityName1);
		} else if ("roundTrip".equals(tripType)) {
			return tripSer.getRoundTrip(cityName, cityName1);
		} else {
			// Handle other cases or return an empty list if needed
			return new ArrayList<>();
		}
	}

	@PostMapping("/invoice")
	public String bookCab(
			@RequestParam("cabId") String cabId,
			@RequestParam("modelName") String modelName,
			@RequestParam("modelType") String modelType,
			@RequestParam("seats") String seats,
			@RequestParam("fuelType") String fuelType,
			@RequestParam("availability") String availability,
			@RequestParam("price") String price,
			@RequestParam("pickupLocation") String pickupLocation,
			@RequestParam("dropLocation") String dropLocation,
			@RequestParam("date") String date,
			@RequestParam("returndate") String returndate,
			@RequestParam("time") String time,
			@RequestParam("tripType") String tripType,
			@RequestParam("distance") String distance,
			@RequestParam("days") String days,

			Model model) {
		// Print all the values
		System.out.println("Cab ID: " + cabId);
		System.out.println("Model Name: " + modelName);
		System.out.println("Model Type: " + modelType);
		System.out.println("Seats: " + seats);
		System.out.println("Fuel Type: " + fuelType);
		System.out.println("Availability: " + availability);
		System.out.println("Price: " + price);
		System.out.println("Pickup Location: " + pickupLocation);
		System.out.println("Drop Location: " + dropLocation);
		System.out.println("Date: " + date);
		System.out.println("Return Date: " + returndate);
		System.out.println("Time: " + time);
		System.out.println("Trip Type: " + tripType);
		System.out.println("Distance: " + distance);
		System.out.println("Days: " + days);

		int driverrate = 0;
		int gst = 0;
		int service = 0;

		if ("oneWay".equals(tripType)) {
			// driverrate = 250;
			gst = (Integer.parseInt(price) / 100) * 10;
			service = (Integer.parseInt(price) / 100) * 5;

		} else if ("roundTrip".equals(tripType)) {
			driverrate = Integer.parseInt(days) * 300;
			gst = ((Integer.parseInt(price) + driverrate) / 100) * 10;
			service = ((Integer.parseInt(price) + driverrate) / 100) * 5;

		}
		// int driverrate = Integer.parseInt(days)*300;
		System.out.println(driverrate);
		System.out.println(price);

		System.out.println("gst " + gst);
		System.out.println("service " + service);

		int totalAmount = Integer.parseInt(price) + gst + service + driverrate;
		System.out.println(totalAmount);
		// Store the cab information in the model for the next page
		model.addAttribute("driverrate", driverrate);
		model.addAttribute("days", days);
		model.addAttribute("gst", gst);
		model.addAttribute("service", service);
		model.addAttribute("total", totalAmount);

		model.addAttribute("cabId", cabId);
		model.addAttribute("modelName", modelName);
		model.addAttribute("modelType", modelType);
		model.addAttribute("seats", seats);
		model.addAttribute("fuelType", fuelType);
		model.addAttribute("availability", availability);
		model.addAttribute("price", price);
		model.addAttribute("pickupLocation", pickupLocation);
		model.addAttribute("dropLocation", dropLocation);
		model.addAttribute("date", date);
		model.addAttribute("returndate", returndate);
		model.addAttribute("time", time);
		model.addAttribute("tripType", tripType);
		model.addAttribute("distance", distance);

		// Redirect to the next page (replace "redirect:/nextPage" with the actual
		// destination URL)
		return "invoice";
	}

	@Autowired
	private CabInfoService cabInfoService;

	// @GetMapping("/invoice")
	// public ResponseEntity<List<CabInfo>> getAllCabs() {
	// List<CabInfo> cabList = cabInfoService.getAll();
	// return ResponseEntity.ok(cabList);
	// }

	// @Autowired
	// private BookingService bookingService;
	// @GetMapping("/invoice")
	// public ResponseEntity<List<Booking>> getAllBookings() {
	// List<Booking> bookings = bookingService.getAll();
	// return ResponseEntity.ok(bookings); // Return the list of bookings with HTTP
	// 200 OK status
	// }

	@GetMapping("/invoice")
	public ResponseEntity<List<CabInfo>> getInvoice() {
		// Retrieve the list of CabInfo objects
		List<CabInfo> cabList = cabInfoService.getAll();

		// Return the list as a JSON response
		return ResponseEntity.ok(cabList);
	}

	@Autowired
	private BookingService bookingService;

	// @GetMapping("/details")
	// public ResponseEntity<List<Booking>> getAllBookings() {
	// List<Booking> bookings = bookingService.getAllBookings();
	// return ResponseEntity.ok(bookings); // Return the list of bookings with HTTP
	// 200 OK status
	// }

	@PostMapping("/popup/save")
	public String save(@ModelAttribute Popup popup) {
		//
		//
		service.save(popup);
		return "redirect:/";

	}

}
