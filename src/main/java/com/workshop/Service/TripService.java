package com.workshop.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.Controller.CabRestController;
import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;
import com.workshop.Repo.OnewayTripRepo;
import com.workshop.Repo.RoundTripRepo;
import com.workshop.Repo.Trip;

import jakarta.transaction.Transactional;

@Service
public class TripService {

	@Autowired
	private OnewayTripRepo repo;

	private String apiKey = "AIzaSyDuZC6kFobB0pnp-k3VcxQIjvb0EhgfnVI";

	@Autowired
	RoundTripRepo roundrepo;

	@Autowired
	private OnewayTripRepo onewayTripRepository;

	// REMOVED: No more controller injection - no circular dependency!
	// @Autowired private CabRestController cabRestController;

	public List<Trip> getonewayTrip(String from, String to) {
		return repo.findBySourceCityAndDestinationCity(to, from);
	}

	public List<Trip> getRoundTrip(String from, String to) {
		return roundrepo.findBySourceCityAndDestinationCity(to, from);
	}

	// UPDATED: Use static method - no injection needed
	public List<onewayTrip> getOneWayTripsWithDefaults(String sourceCity, String sourceState, String destCity,
			String destState) {

		List<Trip> existingTrips = getonewayTrip(destCity, sourceCity);

		if (!existingTrips.isEmpty()) {
			List<onewayTrip> result = new ArrayList<>();
			for (Trip trip : existingTrips) {
				if (trip instanceof onewayTrip) {
					result.add((onewayTrip) trip);
				}
			}
			return result;
		}

		// CHANGED: Direct static call - no injection, no circular dependency
		onewayTrip defaultTrip = CabRestController.getDefaultOneWayTrip();

		// Set the actual extracted location data
		defaultTrip.setSourceCity(sourceCity);
		defaultTrip.setSourceState(sourceState);
		defaultTrip.setDestinationCity(destCity);
		defaultTrip.setDestinationState(destState);

		return List.of(defaultTrip);
	}

	// UPDATED: Use static method - no injection needed
	public List<onewayTrip> getAllData(String sourceCity, String sourceState, String destinationState,
			String destinationCity) {
		// First prefer active Excel override rows for today's date
		LocalDate today = LocalDate.now();
		List<onewayTrip> activeExcelTrips = repo.findActiveByRouteAndDate(
				sourceState, destinationState, sourceCity, destinationCity, today);
		if (activeExcelTrips != null && !activeExcelTrips.isEmpty()) {
			System.out.println("[TripService.getAllData] Using ACTIVE Excel override trips for "
					+ sourceCity + ", " + sourceState + " -> " + destinationCity + ", " + destinationState
					+ " (count=" + activeExcelTrips.size() + ")");
			activeExcelTrips.forEach(t -> System.out.println("  ExcelTrip: id=" + t.getId()
					+ ", hatchback=" + t.getHatchback()
					+ ", sedan=" + t.getSedan()
					+ ", sedanpremium=" + t.getSedanpremium()
					+ ", suv=" + t.getSuv()
					+ ", suvplus=" + t.getSuvplus()
					+ ", ertiga=" + t.getErtiga()
					+ ", startDate=" + t.getStartDate()
					+ ", endDate=" + t.getEndDate() + ")"));
			return activeExcelTrips;
		}

		// Fallback to base onewayTrip rows
		List<onewayTrip> trips = repo.findBySourceStateAndSourceCityAndDestinationStateAndDestinationCity(
				sourceState, sourceCity, destinationState, destinationCity);

		// FIX: Only return a default if there is truly no record in the database
		if (trips == null || trips.isEmpty()) {
			// Only return a default if no record exists
			onewayTrip newTrip = CabRestController.getDefaultOneWayTrip();
			newTrip.setSourceCity(sourceCity);
			newTrip.setSourceState(sourceState);
			newTrip.setDestinationCity(destinationCity);
			newTrip.setDestinationState(destinationState);
			System.out.println("[TripService.getAllData] No DB rows found, returning DEFAULT trip for "
					+ sourceCity + ", " + sourceState + " -> " + destinationCity + ", " + destinationState + ")");
			return List.of(newTrip);
		}

		// Always return the actual database values (including updated Ertiga, etc.)
		System.out.println("[TripService.getAllData] Using BASE DB trips for "
				+ sourceCity + ", " + sourceState + " -> " + destinationCity + ", " + destinationState
				+ " (count=" + trips.size() + ")");
		trips.forEach(t -> System.out.println("  BaseTrip: id=" + t.getId()
				+ ", hatchback=" + t.getHatchback()
				+ ", sedan=" + t.getSedan()
				+ ", sedanpremium=" + t.getSedanpremium()
				+ ", suv=" + t.getSuv()
				+ ", suvplus=" + t.getSuvplus()
				+ ", ertiga=" + t.getErtiga() + ")"));
		return trips;
	}

	public int getRoundDistance(LocalDate localDate1, LocalTime time1, LocalDate localDate2, LocalTime time2,
			String distance) {

		int Distance = Integer.parseInt(distance);

		int days = localDate1.until(localDate2).getDays() + 1;

		int driver = (int) (days * 300);
		int service = Distance * 2;

		if (service > 300 && days == 1) {
			return -1;
		} else if (service > 600 && days == 2) {
			return -2;
		} else if (service > 900 && days == 3) {
			return -3;
		} else if (service > 1200 && days == 4) {
			return -4;
		} else if (service > 1500 && days == 5) {
			return -5;
		} else if (service > 1800 && days == 6) {
			return -6;
		} else if (service > 2100 && days == 7) {
			return -7;
		} else if (service > 2400 && days == 8) {
			return -8;
		} else if (service > 2700 && days == 9) {
			return -9;
		} else if (service > 3000 && days == 10) {
			return -10;
		}

		int roundDist = Distance * 2;
		int intAmount = -1;
		int dr = days * 300;

		if (roundDist <= 300 && days == 1) {
			intAmount = 300 * days;
		} else if (roundDist > 300 && days == 1) {
			int dPlus = roundDist - 300;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 600 && days == 2) {
			intAmount = 300 * days;
		} else if (roundDist > 600 && days == 2) {
			int dPlus = roundDist - 600;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 900 && days == 3) {
			intAmount = 300 * days;
		} else if (roundDist > 900 && days == 3) {
			int dPlus = roundDist - 900;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 1200 && days == 4) {
			intAmount = 300 * days;
		} else if (roundDist > 1200 && days == 4) {
			int dPlus = roundDist - 1200;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 1500 && days == 5) {
			intAmount = 300 * days;
		} else if (roundDist > 1500 && days == 5) {
			int dPlus = roundDist - 1500;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 1800 && days == 6) {
			intAmount = 300 * days;
		} else if (roundDist > 1800 && days == 6) {
			int dPlus = roundDist - 1800;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 2100 && days == 7) {
			intAmount = 300 * days;
		} else if (roundDist > 2100 && days == 7) {
			int dPlus = roundDist - 1500;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 2400 && days == 8) {
			intAmount = 300 * days;
		} else if (roundDist > 2400 && days == 8) {
			int dPlus = roundDist - 2400;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 2700 && days == 9) {
			intAmount = 300 * days;
		} else if (roundDist > 2700 && days == 9) {
			int dPlus = roundDist - 2700;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 3000 && days == 10) {
			intAmount = 300 * days;
		} else if (roundDist > 3000 && days == 10) {
			int dPlus = roundDist - 3000;
			intAmount = 300 * days + dPlus;
		} else if (roundDist <= 30) {
			intAmount = -1;
		}

		if (intAmount == -1) {
			System.out.println("Service is unavailable for the given distance");
		}

		return intAmount;
	}

	public onewayTrip updatePrice(Long id, int hatchback, int sedan, int sedanpremium, int suv, int suvplus,
			String sourceState, String sourceCity, String destinationState, String destinationCity) {
		Optional<onewayTrip> tripOptional = repo.findById(id);
		if (tripOptional.isPresent()) {
			onewayTrip trip = tripOptional.get();
			trip.setHatchback(hatchback);
			trip.setSedan(sedan);
			trip.setSedanpremium(sedanpremium);
			trip.setSuv(suv);
			trip.setSuvplus(suvplus);
			trip.setSourceState(sourceState);
			trip.setSourceCity(sourceCity);
			trip.setDestinationState(destinationState);
			trip.setDestinationCity(destinationCity);
			return repo.save(trip);
		} else {
			throw new RuntimeException("Trip not found with ID: " + id);
		}
	}

	public void updatePrices(String sourceState, String destinationState, String sourceCity, String destinationCity,
			int hatchbackPrice, int sedanPrice, int sedanPremiumPrice, int suvPrice, int suvPlusPrice, Integer ertiga) {
		// DEBUG LOGGING: Log all received parameters
		System.out.println("[DEBUG] TripService.updatePrices called with:");
		System.out.println("  sourceState=" + sourceState);
		System.out.println("  destinationState=" + destinationState);
		System.out.println("  sourceCity=" + sourceCity);
		System.out.println("  destinationCity=" + destinationCity);
		System.out.println("  hatchbackPrice=" + hatchbackPrice);
		System.out.println("  sedanPrice=" + sedanPrice);
		System.out.println("  sedanPremiumPrice=" + sedanPremiumPrice);
		System.out.println("  suvPrice=" + suvPrice);
		System.out.println("  suvPlusPrice=" + suvPlusPrice);
		System.out.println("  ertiga=" + ertiga);

		List<onewayTrip> trips = this.repo.findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(
				sourceState, destinationState, sourceCity, destinationCity);

		if (trips == null || trips.isEmpty()) {
			// If no trip exists, create a new one
			onewayTrip newTrip = new onewayTrip();
			newTrip.setSourceState(sourceState);
			newTrip.setDestinationState(destinationState);
			newTrip.setSourceCity(sourceCity);
			newTrip.setDestinationCity(destinationCity);
			newTrip.setHatchback(hatchbackPrice);
			newTrip.setSedan(sedanPrice);
			newTrip.setSedanpremium(sedanPremiumPrice);
			newTrip.setSuv(suvPrice);
			newTrip.setSuvplus(suvPlusPrice);
			if (ertiga != null) {
				newTrip.setErtiga(ertiga);
			}
			newTrip.setStatus("s"); // or whatever default status is appropriate
			System.out.println("[DEBUG] Creating new onewayTrip with values: " + newTrip);
			this.repo.save(newTrip);
		} else {
			for (onewayTrip trip : trips) {
				trip.setHatchback(hatchbackPrice);
				trip.setSedan(sedanPrice);
				trip.setSedanpremium(sedanPremiumPrice);
				trip.setSuv(suvPrice);
				trip.setSuvplus(suvPlusPrice);
				if (ertiga != null) {
					trip.setErtiga(ertiga);
				}
				System.out
						.println("[DEBUG] Updating existing onewayTrip (id=" + trip.getId() + ") with values: " + trip);
			}
			this.repo.saveAll(trips);
		}
	}

	public onewayTrip updatePrice(String sourceState, String destinationState, onewayTrip oneway) {
		oneway.setSourceState(sourceState);
		oneway.setDestinationState(destinationState);
		return this.repo.save(oneway);
	}

	public void updatePricesByRoundWay(String sourceState, String destinationState, String sourceCity,
			String destinationCity, int hatchbackPrice, int sedanPrice, int sedanPremiumPrice, int suvPrice,
			int suvPlusPrice, Integer ertiga) {
		List<roundTrip> trips = this.roundrepo.findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(
				sourceState, destinationState, sourceCity, destinationCity);

		if (trips == null || trips.isEmpty()) {
			// If no trip exists, create a new one
			roundTrip newTrip = new roundTrip();
			newTrip.setSourceState(sourceState);
			newTrip.setDestinationState(destinationState);
			newTrip.setSourceCity(sourceCity);
			newTrip.setDestinationCity(destinationCity);
			newTrip.setHatchback(hatchbackPrice);
			newTrip.setSedan(sedanPrice);
			newTrip.setSedanpremium(sedanPremiumPrice);
			newTrip.setSuv(suvPrice);
			newTrip.setSuvplus(suvPlusPrice);
			if (ertiga != null) {
				newTrip.setErtiga(ertiga);
			}
			newTrip.setStatus("s");
			System.out.println("[DEBUG] Creating new roundTrip with values: " + newTrip);
			this.roundrepo.save(newTrip);
		} else {
			for (roundTrip trip : trips) {
				trip.setHatchback(hatchbackPrice);
				trip.setSedan(sedanPrice);
				trip.setSedanpremium(sedanPremiumPrice);
				trip.setSuv(suvPrice);
				trip.setSuvplus(suvPlusPrice);
				if (ertiga != null) {
					trip.setErtiga(ertiga);
				}
				System.out
						.println("[DEBUG] Updating existing roundTrip (id=" + trip.getId() + ") with values: " + trip);
			}
			this.roundrepo.saveAll(trips);
		}
	}

	public List<onewayTrip> getOneWayTripData(String pickupLocation, String dropLocation) {
		if (pickupLocation == null || dropLocation == null) {
			throw new IllegalArgumentException("Pickup and drop locations cannot be null");
		}

		String[] pickup = extractCityAndState(pickupLocation);
		String[] drop = extractCityAndState(dropLocation);

		String sourceCity = pickup[0];
		String sourceState = pickup[1];
		String destinationCity = drop[0];
		String destinationState = drop[1];

		Map<String, Object> distanceResult = calculateDistanceBetweenLocations(pickupLocation, dropLocation);

		if (!(boolean) distanceResult.get("success")) {
			throw new RuntimeException("Failed to calculate distance: " + distanceResult.get("message"));
		}

		String distanceText = (String) distanceResult.get("distanceText");
		double distanceInKm = (double) distanceResult.get("distanceInKm");

		List<onewayTrip> trips = getAllData(sourceCity, sourceState, destinationState, destinationCity);

		trips.forEach(trip -> {
			trip.setDistance(distanceInKm);
		});
		
		return trips;
	}

	// Backwards-compatible helper for controllers (note lowercase 'o')
	public List<onewayTrip> getoneWayTripData(String pickupLocation, String dropLocation) {
		if (pickupLocation == null || dropLocation == null) {
			throw new IllegalArgumentException("Pickup and drop locations cannot be null");
		}

		String[] pickup = extractCityAndState(pickupLocation);
		String[] drop = extractCityAndState(dropLocation);

		String sourceCity = pickup[0];
		String sourceState = pickup[1];
		String destinationCity = drop[0];
		String destinationState = drop[1];

		// Reuse getAllData so Excel overrides and base pricing are applied consistently
		return getAllData(sourceCity, sourceState, destinationState, destinationCity);
	}

	private Map<String, Object> calculateDistanceBetweenLocations(String origin, String destination) {
		Map<String, Object> result = new HashMap<>();

		try {
			String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json" +
					"?origins=" + URLEncoder.encode(origin, StandardCharsets.UTF_8) +
					"&destinations=" + URLEncoder.encode(destination, StandardCharsets.UTF_8) +
					"&key=" + apiKey +
					"&units=metric" +
					"&mode=driving";

			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(response.getBody());

			String apiStatus = root.path("status").asText();
			if (!"OK".equals(apiStatus)) {
				result.put("success", false);
				result.put("message", "Google API error: " + apiStatus +
						". Error message: " + root.path("error_message").asText(""));
				return result;
			}

			JsonNode elements = root.path("rows").get(0).path("elements").get(0);
			String elementStatus = elements.path("status").asText();
			if (!"OK".equals(elementStatus)) {
				result.put("success", false);
				result.put("message", "Distance calculation failed: " + elementStatus);
				return result;
			}

			String distanceText = elements.path("distance").path("text").asText();
			double distanceInKm = elements.path("distance").path("value").asDouble() / 1000.0;

			result.put("success", true);
			result.put("distanceText", distanceText);
			result.put("distanceInKm", distanceInKm);
			return result;

		} catch (Exception e) {
			result.put("success", false);
			result.put("message", "Error calculating distance: " + e.getMessage());
			return result;
		}
	}

	private String[] extractCityAndState(String location) {
		String[] parts = location.split(",");
		if (parts.length >= 2) {
			return new String[] { parts[0].trim(), parts[1].trim() };
		}
		return new String[] { "Unknown City", "Unknown State" };
	}

	public List<roundTrip> getRoundWayTripData(String pickupLocation, String dropLocation) {
		if (pickupLocation == null || dropLocation == null) {
			throw new IllegalArgumentException("Pickup and drop locations cannot be null");
		}

		String[] pickup = extractCityAndState(pickupLocation);
		String[] drop = extractCityAndState(dropLocation);

		String sourceCity = pickup[0];
		String sourceState = pickup[1];
		String destinationCity = drop[0];
		String destinationState = drop[1];

		return getAllData1(sourceCity, sourceState, destinationState, destinationCity);
	}

	public List<roundTrip> getAllData1(String sourceCity, String sourceState,
			String destinationState,
			String destinationCity) {
		return roundrepo.findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(
				sourceState, destinationState, sourceCity, destinationCity);
	}

	public onewayTrip postOneWayTripprice(String sourceState, String destinationState, String sourceCity,
			String destinationCity, int hatchbackPrice, int sedanPrice, int sedanPremiumPrice, int suvPrice,
			int suvPlusPrice, String status) {

		onewayTrip o = new onewayTrip();
		o.setSourceState(sourceState);
		o.setDestinationState(destinationState);
		o.setSourceCity(sourceCity);
		o.setDestinationCity(destinationCity);
		o.setHatchback(hatchbackPrice);
		o.setSedan(sedanPrice);
		o.setSedanpremium(sedanPremiumPrice);
		o.setSuv(suvPrice);
		o.setSuvplus(suvPlusPrice);
		o.setStatus("s");

		return this.repo.save(o);
	}

	public roundTrip postRoundTripprice(String sourceState, String destinationState, String sourceCity,
			String destinationCity, int hatchbackPrice, int sedanPrice, int sedanPremiumPrice, int suvPrice,
			int suvPlusPrice, String status) {

		roundTrip o = new roundTrip();
		o.setSourceState(sourceState);
		o.setDestinationState(destinationState);
		o.setSourceCity(sourceCity);
		o.setDestinationCity(destinationCity);
		o.setHatchback(hatchbackPrice);
		o.setSedan(sedanPrice);
		o.setSedanpremium(sedanPremiumPrice);
		o.setSuv(suvPrice);
		o.setSuvplus(suvPlusPrice);
		o.setStatus("s");

		return this.roundrepo.save(o);
	}

	public List<onewayTrip> getAllTransportRates() {
		return this.repo.findAll();
	}

	public List<roundTrip> getAllRoundTripTransportRates() {
		return this.roundrepo.findAll();
	}

	public boolean doesCityStatePairExist(String city, String state) {
		try {
			List<onewayTrip> asSource = onewayTripRepository.findBySourceCityIgnoreCaseAndSourceStateIgnoreCase(city,
					state);
			if (!asSource.isEmpty()) {
				return true;
			}

			List<onewayTrip> asDestination = onewayTripRepository
					.findByDestinationCityIgnoreCaseAndDestinationStateIgnoreCase(city, state);
			return !asDestination.isEmpty();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<String> getAllUniqueCities() {
		try {
			List<String> cities = new ArrayList<>();

			List<String> sourceCities = onewayTripRepository.findDistinctSourceCities();
			cities.addAll(sourceCities);

			List<String> destCities = onewayTripRepository.findDistinctDestinationCities();
			cities.addAll(destCities);

			return cities.stream().distinct().collect(Collectors.toList());

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public String[] findLocationInOnewayTrip(String location) {
		try {
			List<onewayTrip> asSource = onewayTripRepository.findBySourceCityIgnoreCase(location);
			if (!asSource.isEmpty()) {
				onewayTrip trip = asSource.get(0);
				return new String[] { trip.getSourceCity(), trip.getSourceState() };
			}

			List<onewayTrip> asDestination = onewayTripRepository.findByDestinationCityIgnoreCase(location);
			if (!asDestination.isEmpty()) {
				onewayTrip trip = asDestination.get(0);
				return new String[] { trip.getDestinationCity(), trip.getDestinationState() };
			}

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}