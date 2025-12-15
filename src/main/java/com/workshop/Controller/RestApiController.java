package com.workshop.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.Entity.BookingRequest;
import com.workshop.Entity.CabInfo;
import com.workshop.Entity.FormInfo;
import com.workshop.Entity.TripInfoDTO;
import com.workshop.Repo.Trip;
import com.workshop.Service.BookingService;
import com.workshop.Service.CabInfoService;
import com.workshop.Service.TripService;
import com.workshop.Service.UserDetailServiceImpl;
import com.workshop.Service.WebsiteRegisterService;

@RestController
@RequestMapping("/api")
public class RestApiController {

	@Autowired
	UserDetailServiceImpl pincode;

	@Autowired
	BookingService ser;

	@Autowired
	TripService tripSer;

	@Autowired
	CabInfoService cabser;
	@Autowired
	WebsiteRegisterService webService;

	private final String apiKey = "AIzaSyDuZC6kFobB0pnp-k3VcxQIjvb0EhgfnVI";

	@GetMapping("/test")
	public String test() {
		return "test";
	}

	@PostMapping("/formInfo")
	public ResponseEntity<?> forminfo(@RequestBody FormInfo form) {
		System.out.println(form.getApikey());
		System.out.println(form.getEmail());
		if (webService.findByApiKey(form.getApikey()) != null) {

			TripInfoDTO Info = new TripInfoDTO();
			List<Trip> tripinfo = new ArrayList<>();
			// get name of district from pickup location and drop location
			String distance = pincode.getDistanceByPickupAndDrop(form.getPickup(), form.getDrop(), apiKey);
			String numericString = distance.replaceAll("[^0-9.]", "");
			double floatValue = Double.parseDouble(numericString);

			int Dist = (int) floatValue;
			System.out.println(Dist);
			String pickup = pincode.getLongNameByCity(form.getPickup(), apiKey);
			String drop = pincode.getLongNameByCity(form.getDrop(), apiKey);

			System.out.println();

			int Distance = 0;
			int days = 0;
			System.out.println(Distance);

			String city1 = pincode.getLongNameByCity(form.getPickup(), apiKey);
			String[] parts = city1.split(" ");
			String cityName = parts[0];
			String city2 = pincode.getLongNameByCity(form.getDrop(), apiKey);
			String[] parts1 = city2.split(" ");
			String cityName1 = parts1[0];

			System.out.println(city1);
			System.out.println(city2);
			System.out.println(cityName);
			System.out.println(cityName1);

			if ("oneWay".equals(form.getTripType())) {
				tripinfo = tripSer.getonewayTrip(cityName, cityName1);
				Distance = Dist;
				System.out.println(Distance);
			} else if ("roundTrip".equals(form.getTripType())) {

				//// String startdate = form.getDate();
				// String enddate = returndate;
				// String time1 = time;
				String endtime = "23:30:00";
				String distances = Integer.toString(Dist);
				// System.out.println(startdate+" "+enddate+" "+time+" "+distance);
				LocalDate localDate1 = form.getDate();
				LocalDate localDate2 = form.getEndDate();
				days = (int) ChronoUnit.DAYS.between(form.getDate(), form.getEndDate());
				days++;
				System.out.println(days);

				// Use ISO_TIME pattern for parsing time strings
				LocalTime localTime1 = form.getTime();
				LocalTime localTime2 = LocalTime.parse(endtime, DateTimeFormatter.ISO_TIME);

				Distance = tripSer.getRoundDistance(localDate1, localTime1, localDate2, localTime2, distances);

				tripinfo = tripSer.getRoundTrip(cityName, cityName1);
			}

			List<CabInfo> c = cabser.getAll();
			System.out.println(tripinfo);

			Info.setCabInfo(c);
			Info.setTripInfo(tripinfo);
			Info.setDistance(Distance);
			Info.setDropCity(form.getDrop());
			Info.setPickupCity(form.getPickup());

			// calculate distance
			return ResponseEntity.ok(Info);

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API key");
		}
	}

	@PostMapping("/bookCab")
	public ResponseEntity<?> bookcab(@RequestBody BookingRequest booking) {
		System.out.println(booking.getApikey());

		if (webService.findByApiKey(booking.getApikey()) != null) {

			JSONObject requestBody = new JSONObject();
			requestBody.put("date1", booking.getDate());
			requestBody.put("name", booking.getName());
			requestBody.put("email", booking.getEmail());
			requestBody.put("car", booking.getModelName());
			requestBody.put("distance", booking.getDistance());
			requestBody.put("phone", booking.getPhone());
			requestBody.put("user_pickup", booking.getPickupLocation());
			requestBody.put("user_drop", booking.getDropLocation());
			requestBody.put("time", booking.getTime());
			requestBody.put("dateend", booking.getReturndate());
			requestBody.put("timeend", "");
			requestBody.put("days", booking.getDays());
			requestBody.put("user_trip_type", booking.getTripType());
			requestBody.put("amount", booking.getTotal());
			requestBody.put("baseAmount", booking.getPrice());
			requestBody.put("service_charge", booking.getService());
			requestBody.put("gst", booking.getGst());
			requestBody.put("driver_bhata", booking.getDriverrate());

			// Add other properties as needed...

			// Convert the JSON object to a string
			String jsonRequestBody = requestBody.toString();

			try {
				String apiURL = "https://aimcabbooking.com/confirm-round-api.php";
				// String apiURL = "";
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
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("api request failed");
				}

				// Close the connection
				connection.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
				// Handle any exceptions that occur during the request
				System.out.println("Not Successful");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("could not send request");
			}
			return ResponseEntity.ok("booked sucessfully");

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid api key");
		}

	}

}
