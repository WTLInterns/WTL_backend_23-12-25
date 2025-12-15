package com.workshop.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.workshop.CarRental.Entity.CarRentalUser;
import com.workshop.CarRental.Repository.CarRentalRepository;
import com.workshop.CarRental.Service.CarRentalBookingService;
import com.workshop.DTO.PricingResponse;
import com.workshop.Entity.Booking;
import com.workshop.Entity.CabInfo;
import com.workshop.Entity.DeviceRequest;
import com.workshop.Entity.User;
import com.workshop.Entity.VendorDrivers;
import com.workshop.Entity.Visitors;
import com.workshop.Entity.onewayTrip;
import com.workshop.Entity.roundTrip;
import com.workshop.Repo.Trip;
import com.workshop.Service.BookingService;
import com.workshop.Service.CabInfoService;
import com.workshop.Service.CityExtractionService;
import com.workshop.Service.CitiesService;
import com.workshop.Service.EmailService;
import com.workshop.Service.OneFiftyService;
import com.workshop.Service.PopupService;
import com.workshop.Service.StatesService;
import com.workshop.Service.TripRateService;
import com.workshop.Service.TripService;
import com.workshop.Service.UserDetailServiceImpl;
import com.workshop.Service.VendorDriverService;
import com.workshop.Service.VisitorService;
import com.workshop.WhatsAppPackage.BookingDetails;
import com.workshop.WhatsAppPackage.WhatsAppResponse;
import com.workshop.WhatsAppPackage.WhatsAppService;
import com.workshop.WhatsAppPackage.WhatsAppServiceException;

@RestController
@RequestMapping("/api")
public class CabRestController {

    @Autowired
    BookingService ser;

    @Autowired
    private TripService tripSer;

    @Autowired
    CabInfoService cabser;

    @Autowired
    private TripRateService tripRateService;

    @Autowired
    private VisitorService visitorService;

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

    @Autowired
    private CarRentalRepository carRentalRepository;

    @Autowired
    private CarRentalBookingService carRentalBookingService;

    @Autowired
    private VendorDriverService vendorDriverService;

    @Autowired
    private CityExtractionService cityExtractionService;

    @Autowired
    private WhatsAppService whatsAppService;

    @Autowired
    private OneFiftyService oneFiftyService;

    	@Autowired
	private com.workshop.CarRental.Controller.LiveTrackingController liveTrackingController;

    @Value("${google.maps.api.key}")
    private String apiKey;

    private static final String DISTANCE_MATRIX_API_URL = "https://maps.googleapis.com/maps/api/distancematrix/json";
    

    // @PostMapping("/cab1")
    // public ResponseEntity<Map<String, Object>> processForm(
    // @RequestParam("tripType") String tripType,
    // @RequestParam("pickupLocation") String pickupLocation,
    // @RequestParam("dropLocation") String dropLocation,
    // @RequestParam("date") String date,
    // @RequestParam(value = "Returndate", required = false) String returndate,
    // @RequestParam("time") String time,
    // @RequestParam(value = "distance", required = false) String distance) {

    // Map<String, Object> response = new HashMap<>();

    // try {
    // // Validate required parameters
    // if (pickupLocation == null || dropLocation == null || date == null || time ==
    // null) {
    // throw new IllegalArgumentException("Missing required parameters");
    // }

    // // Calculate distance using Google Maps API if not provided
    // int oneWayDistance = 0;
    // if (distance == null || distance.isEmpty()) {
    // oneWayDistance = getDistanceFromGoogleMaps(pickupLocation, dropLocation);
    // } else {
    // // Use provided distance after cleaning
    // String numericString = distance.replaceAll("[^0-9.]", "");
    // oneWayDistance = (int) Double.parseDouble(numericString);
    // }

    // List<Trip> tripinfo = new ArrayList<>();
    // int days = 0;
    // int totalDistance = oneWayDistance;

    // String cityName = extractCityName(userService.getLongNameByCity(dropLocation,
    // apiKey), dropLocation);
    // String cityName1 =
    // extractCityName(userService.getLongNameByCity(pickupLocation, apiKey),
    // pickupLocation);

    // // Handle Bengaluru/Bangalore naming inconsistency
    // cityName = cityName.equals("Bengaluru") ? "Bangalore" : cityName;
    // cityName1 = cityName1.equals("Bengaluru") ? "Bangalore" : cityName1;

    // if ("oneWay".equals(tripType)) {
    // tripinfo = tripSer.getonewayTrip(cityName, cityName1);
    // if (tripinfo.isEmpty()) {
    // tripinfo.add(createDefaultOneWayTrip());
    // }
    // } else if ("roundTrip".equals(tripType)) {
    // if (returndate == null || returndate.isEmpty()) {
    // throw new IllegalArgumentException("Return date is required for round
    // trips");
    // }

    // LocalDate localDate1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    // LocalDate localDate2 = LocalDate.parse(returndate,
    // DateTimeFormatter.ISO_DATE);

    // // Validate return date is not before pickup date
    // if (localDate2.isBefore(localDate1)) {
    // throw new IllegalArgumentException("Return date cannot be before pickup
    // date");
    // }

    // days = (int) ChronoUnit.DAYS.between(localDate1, localDate2) + 1;

    // // For round trip, total distance is one way distance multiplied by 2 (going
    // and returning)
    // totalDistance = oneWayDistance * 2;

    // tripinfo = tripSer.getRoundTrip(cityName, cityName1);

    // if (tripinfo.isEmpty()) {
    // tripinfo.add(createDefaultRoundTrip());
    // }
    // } else {
    // throw new IllegalArgumentException("Invalid trip type. Must be 'oneWay' or
    // 'roundTrip'");
    // }

    // List<CabInfo> cabs = cabser.getAll();

    // // Calculate price based on distance and trip type
    // double price = calculatePrice(oneWayDistance, "roundTrip".equals(tripType),
    // days);

    // // Prepare response
    // response.put("tripType", tripType);
    // response.put("pickupLocation", pickupLocation);
    // response.put("dropLocation", dropLocation);
    // response.put("date", date);
    // response.put("returndate", returndate);
    // response.put("time", time);
    // response.put("oneWayDistance", oneWayDistance);
    // response.put("totalDistance", totalDistance);
    // response.put("cabinfo", cabs);
    // response.put("price", price);
    // response.put("tripinfo", tripinfo);
    // response.put("days", days);

    // return ResponseEntity.ok(response);

    // } catch (IllegalArgumentException e) {
    // response.put("error", e.getMessage());
    // return ResponseEntity.badRequest().body(response);
    // } catch (Exception e) {
    // response.put("error", "Failed to process request: " + e.getMessage());
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // }

    // private int getDistanceFromGoogleMaps(String origin, String destination)
    // throws Exception {
    // String urlString = String.format("%s?origins=%s&destinations=%s&key=%s",
    // DISTANCE_MATRIX_API_URL,
    // URLEncoder.encode(origin, StandardCharsets.UTF_8.toString()),
    // URLEncoder.encode(destination, StandardCharsets.UTF_8.toString()), apiKey);

    // URL url = new URL(urlString);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // connection.setRequestMethod("GET");

    // if (connection.getResponseCode() != 200) {
    // throw new RuntimeException("Failed to get distance from Google Maps API");
    // }

    // BufferedReader reader = new BufferedReader(new
    // InputStreamReader(connection.getInputStream()));
    // StringBuilder response = new StringBuilder();
    // String line;
    // while ((line = reader.readLine()) != null) {
    // response.append(line);
    // }
    // reader.close();

    // // Parse JSON response
    // JSONObject jsonResponse = (JSONObject) new
    // org.json.simple.parser.JSONParser().parse(response.toString());
    // JSONObject firstRow = (JSONObject) ((List<?>)
    // jsonResponse.get("rows")).get(0);
    // JSONObject firstElement = (JSONObject) ((List<?>)
    // firstRow.get("elements")).get(0);
    // JSONObject distanceObj = (JSONObject) firstElement.get("distance");

    // // Distance in meters
    // long distanceInMeters = (long) distanceObj.get("value");
    // // Convert to kilometers
    // return (int) (distanceInMeters / 1000);
    // }

    // private String extractCityName(String apiResult, String fallbackLocation) {
    // if (apiResult == null || "Administrative Area Level 3 not
    // found.".equals(apiResult)) {
    // String[] parts = fallbackLocation.split(", ");
    // return parts[0];
    // } else if (apiResult.equals("North Goa") || apiResult.equals("South Goa")) {
    // String[] parts = apiResult.split(", ");
    // return parts[0];
    // } else {
    // String[] parts = apiResult.split(" ");
    // return parts[0];
    // }
    // }

    // private double calculatePrice(int distance, boolean isRoundTrip, int days) {
    // // Basic pricing logic - adjust as needed
    // double baseRatePerKm = 10.0;
    // double price = distance * baseRatePerKm;

    // if (isRoundTrip) {
    // price *= 2; // Round trip is double the one-way price
    // }

    // // Additional charges for multi-day trips
    // if (days > 1) {
    // price += (days - 1) * 500; // 500 per additional day
    // }

    // return price;
    // }

    // private onewayTrip createDefaultOneWayTrip() {
    // return new onewayTrip(
    // null, "", "", "", "",
    // 12, 15, 18, 21, 26,
    // "", null, null, 0
    // );
    // }

    // private roundTrip createDefaultRoundTrip() {
    // return new roundTrip(
    // null, "", "", "", "",
    // 10, 11, 14, 14, 21,
    // ""
    // );
    // }

    // @PostMapping("/cab1")
    // public ResponseEntity<Map<String, Object>> processForm(
    // @RequestParam("tripType") String tripType,
    // @RequestParam("pickupLocation") String pickupLocation,
    // @RequestParam("dropLocation") String dropLocation,
    // @RequestParam("date") String date,
    // @RequestParam(value = "Returndate", required = false) String returndate,
    // @RequestParam("time") String time,
    // @RequestParam(value = "distance", required = false) String distance,
    // @RequestParam(value = "packageName", required = false) String packageName,
    // @RequestParam(value="Endtime", required = false) String Endtime,
    // @RequestParam(value="carType", required = false) String carType)

    // {

    // Map<String, Object> response = new HashMap<>();

    // try {
    // int calculatedDistance = 0;

    // if (distance == null || distance.isEmpty() || distance.equals("0")) {
    // calculatedDistance = getDistanceFromGoogleMaps(pickupLocation, dropLocation);
    // } else {
    // String numericString = distance.replaceAll("[^0-9.]", "");
    // calculatedDistance = (int) Double.parseDouble(numericString);
    // }

    // List<Trip> tripinfo = new ArrayList<>();
    // int days = 0;

    // CityExtractionService.CityStateResult pickupResult =
    // cityExtractionService.extractCityAndState(pickupLocation);
    // CityExtractionService.CityStateResult dropResult =
    // cityExtractionService.extractCityAndState(dropLocation);

    // String cityName1 = pickupResult.getCity(); // Pickup city
    // String cityName = dropResult.getCity(); // Drop city

    // cityName = cityName.equals("Bengaluru") ? "Bangalore" : cityName;
    // cityName1 = cityName1.equals("Bengaluru") ? "Bangalore" : cityName1;

    // if ("oneWay".equalsIgnoreCase(tripType)) {
    // List<onewayTrip> oneWayTrips = tripSer.getOneWayTripsWithDefaults(
    // cityName1,
    // pickupResult.getState(),
    // cityName,
    // dropResult.getState()
    // );

    // for (onewayTrip o : oneWayTrips) {
    // tripinfo.add(o);
    // }
    // }
    // else if ("roundTrip".equalsIgnoreCase(tripType)) {
    // LocalDate localDate1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
    // LocalDate localDate2 = LocalDate.parse(returndate,
    // DateTimeFormatter.ISO_DATE);

    // days = (int) ChronoUnit.DAYS.between(localDate1, localDate2) + 1;

    // if (distance == null || distance.isEmpty() || distance.equals("0") ||
    // distance.equals("-1")) {
    // calculatedDistance = calculatedDistance * days;
    // }

    // tripinfo = tripSer.getRoundTrip(cityName, cityName1);

    // if (tripinfo.isEmpty()) {
    // tripinfo.add(createDefaultRoundTrip());
    // }
    // }

    // if ("transfer".equalsIgnoreCase(tripType)) {
    // // --- Transfer Pricing Logic ---
    // // Car types: sedan, ertiga, innova crysta
    // String car = carType != null ? carType.toLowerCase() : "sedan";
    // double basePrice = 0;
    // double extraHourRate = 0;
    // double extraKmRate = 0;
    // int maxHours = 0;
    // int maxKm = 0;
    // double total = 0;
    // double gst = 0;
    // double da = 0;
    // double toll = 0;
    // double parking = 0;
    // double subtotal = 0;
    // String packageNameUsed = packageName != null ?
    // packageName.trim().toLowerCase() : "";
    // int usedKm = calculatedDistance;
    // int usedHours = 0;
    // try {
    // LocalTime start = LocalTime.parse(time);
    // LocalTime end = LocalTime.parse(Endtime);
    // usedHours = (int) ChronoUnit.HOURS.between(start, end);
    // if (usedHours <= 0) usedHours = 8; // default for 8hr/80km
    // } catch (Exception ex) {
    // usedHours = 8;
    // }
    // // 1. Distance slabs (point-to-point, not packages)
    // if (packageNameUsed.isEmpty() || packageNameUsed.contains("transfer") ||
    // packageNameUsed.contains("slab")) {
    // // Distance slab pricing
    // if (car.equals("sedan")) {
    // if (usedKm <= 10) basePrice = 450;
    // else if (usedKm <= 20) basePrice = 600;
    // else if (usedKm <= 30) basePrice = 800;
    // else if (usedKm <= 40) basePrice = 950;
    // else basePrice = 950 + (usedKm - 40) * 12; // extra km
    // extraHourRate = 150;
    // extraKmRate = 12;
    // } else if (car.equals("ertiga")) {
    // if (usedKm <= 10) basePrice = 650;
    // else if (usedKm <= 20) basePrice = 850;
    // else if (usedKm <= 30) basePrice = 1250;
    // else if (usedKm <= 40) basePrice = 1600;
    // else basePrice = 1600 + (usedKm - 40) * 16; // extra km
    // extraHourRate = 200;
    // extraKmRate = 16;
    // } else if (car.equals("innova crysta")) {
    // if (usedKm <= 40) basePrice = 2200;
    // else basePrice = 2200 + (usedKm - 40) * 22; // extra km
    // extraHourRate = 400;
    // extraKmRate = 22;
    // } else {
    // basePrice = 600; // fallback
    // extraHourRate = 150;
    // extraKmRate = 12;
    // }
    // subtotal = basePrice;
    // gst = subtotal * 0.05;
    // total = subtotal + gst;
    // // No DA in transfer/regular slabs
    // // User can add parking/toll if needed
    // }
    // // 2. 8hr/80km package
    // else if (packageNameUsed.contains("8hr") || packageNameUsed.contains("80km"))
    // {
    // maxHours = 8;
    // maxKm = 80;
    // if (car.equals("sedan")) {
    // basePrice = 2200;
    // extraHourRate = 150;
    // extraKmRate = 12;
    // } else if (car.equals("ertiga")) {
    // basePrice = 2200;
    // extraHourRate = 200;
    // extraKmRate = 16;
    // } else if (car.equals("innova crysta")) {
    // basePrice = 2200;
    // extraHourRate = 400;
    // extraKmRate = 22;
    // } else {
    // basePrice = 2200;
    // extraHourRate = 150;
    // extraKmRate = 12;
    // }
    // int extraHours = Math.max(0, usedHours - maxHours);
    // int extraKms = Math.max(0, usedKm - maxKm);
    // double extraHourCharges = extraHours * extraHourRate;
    // double extraKmCharges = extraKms * extraKmRate;
    // // For demo, get parking/toll from request or set 0
    // try { parking = Double.parseDouble(response.getOrDefault("parking",
    // "0").toString()); } catch (Exception e) { parking = 0; }
    // try { toll = Double.parseDouble(response.getOrDefault("toll",
    // "0").toString()); } catch (Exception e) { toll = 0; }
    // subtotal = basePrice + extraHourCharges + extraKmCharges + parking + toll;
    // gst = subtotal * 0.05;
    // total = subtotal + gst;
    // }
    // // 3. Full Day/300km package
    // else if (packageNameUsed.contains("full day") ||
    // packageNameUsed.contains("300km")) {
    // maxHours = 24;
    // maxKm = 300;
    // if (car.equals("sedan")) {
    // basePrice = 12 * 300;
    // extraHourRate = 150;
    // extraKmRate = 12;
    // } else if (car.equals("ertiga")) {
    // basePrice = 16 * 300;
    // extraHourRate = 200;
    // extraKmRate = 16;
    // } else if (car.equals("innova crysta")) {
    // basePrice = 22 * 300;
    // extraHourRate = 400;
    // extraKmRate = 22;
    // } else {
    // basePrice = 12 * 300;
    // extraHourRate = 150;
    // extraKmRate = 12;
    // }
    // int extraHours = Math.max(0, usedHours - maxHours);
    // int extraKms = Math.max(0, usedKm - maxKm);
    // double extraHourCharges = extraHours * extraHourRate;
    // double extraKmCharges = extraKms * extraKmRate;
    // // DA, parking, toll extra in full day
    // da = 300;
    // // For demo, get parking/toll from request or set 0
    // try { parking = Double.parseDouble(response.getOrDefault("parking",
    // "0").toString()); } catch (Exception e) { parking = 0; }
    // try { toll = Double.parseDouble(response.getOrDefault("toll",
    // "0").toString()); } catch (Exception e) { toll = 0; }
    // subtotal = basePrice + extraHourCharges + extraKmCharges + da + parking +
    // toll;
    // gst = subtotal * 0.05;
    // total = subtotal + gst;
    // }
    // // Build response
    // Map<String, Object> transferDetails = new HashMap<>();
    // transferDetails.put("package", packageNameUsed.isEmpty() ? "slab" :
    // packageNameUsed);
    // transferDetails.put("basePrice", basePrice);
    // transferDetails.put("usedKm", usedKm);
    // transferDetails.put("usedHours", usedHours);
    // transferDetails.put("extraHourRate", extraHourRate);
    // transferDetails.put("extraKmRate", extraKmRate);
    // transferDetails.put("subtotal", subtotal);
    // transferDetails.put("gst", gst);
    // transferDetails.put("total", total);
    // transferDetails.put("carType", carType);
    // transferDetails.put("da", da);
    // transferDetails.put("parking", parking);
    // transferDetails.put("toll", toll);
    // response.put("transferDetails", transferDetails);
    // response.put("tripType", tripType);
    // response.put("pickupLocation", pickupLocation);
    // response.put("dropLocation", dropLocation);
    // response.put("date", date);
    // response.put("returndate", returndate);
    // response.put("time", time);
    // response.put("Endtime", Endtime);
    // response.put("distance", calculatedDistance);
    // response.put("cabinfo", cabser.getAll());
    // response.put("days", 1);
    // return ResponseEntity.ok(response);
    // }

    // if ("car-rental".equalsIgnoreCase(tripType)) {
    // // --- All Car Rental Logic ---
    // // Dynamic per km rate based on carType
    // double perKmRate = 0.0;
    // switch (carType.toLowerCase()) {
    // case "hatchback": perKmRate = 11.0; break;
    // case "sedan": perKmRate = 12.0; break;
    // case "sedanpremium": perKmRate = 13.0; break;
    // case "suv": perKmRate = 14.0; break;
    // case "suvplus": perKmRate = 26.0; break;
    // default: perKmRate = 12.0; // fallback
    // }
    // // Rental packages calculated dynamically
    // class RentalPackage {
    // String name;
    // int maxHours;
    // int maxKm;
    // double price;
    // RentalPackage(String name, int maxHours, int maxKm, double price) {
    // this.name = name;
    // this.maxHours = maxHours;
    // this.maxKm = maxKm;
    // this.price = price;
    // }
    // }
    // List<RentalPackage> packages = Arrays.asList(
    // new RentalPackage("4hr/40km", 4, 40, 40 * perKmRate),
    // new RentalPackage("8hr/80km", 8, 80, 80 * perKmRate),
    // new RentalPackage("Full Day/300km", 24, 300, 300 * perKmRate)
    // );
    // double extraHourRate = 150.0; // Rs/hr

    // // 1. Validate city
    // if (!cityName.equalsIgnoreCase(cityName1)) {
    // response.put("error", "City package not applicable: Pickup and drop must be
    // in the same city for rental.");
    // return ResponseEntity.ok(response);
    // }

    // // 2. Get user-selected package (for demo, pick first package; in real use,
    // get from request)
    // // Select package based on user input, default to first if not provided or
    // invalid
    // RentalPackage selectedPkg = packages.get(0);
    // if (packageName != null && !packageName.isEmpty()) {
    // for (RentalPackage pkg : packages) {
    // if (pkg.name.equalsIgnoreCase(packageName.trim())) {
    // selectedPkg = pkg;
    // break;
    // }
    // }
    // }
    // // 3. Parse time (assume 'time' is in format HH:mm, and Endtime param is
    // available)
    // int usedHours = 0;
    // try {
    // LocalTime start = LocalTime.parse(time);
    // LocalTime end = LocalTime.parse(Endtime);
    // usedHours = (int) ChronoUnit.HOURS.between(start, end);
    // if (usedHours <= 0) usedHours = selectedPkg.maxHours; // fallback
    // } catch (Exception ex) {
    // usedHours = selectedPkg.maxHours;
    // }
    // int usedKm = calculatedDistance;
    // // 4. Calculate extra charges
    // int extraHours = Math.max(0, usedHours - selectedPkg.maxHours);
    // int extraKms = Math.max(0, usedKm - selectedPkg.maxKm);
    // double extraHourCharges = extraHours * extraHourRate;
    // double extraKmCharges = extraKms * perKmRate;
    // double basePrice = selectedPkg.price;
    // double subtotal = basePrice + extraHourCharges + extraKmCharges;
    // double gst = subtotal * 0.05;
    // double total = subtotal + gst;
    // // 5. Build detailed response
    // Map<String, Object> rentalDetails = new HashMap<>();
    // rentalDetails.put("package", selectedPkg.name);
    // rentalDetails.put("basePrice", basePrice);
    // rentalDetails.put("maxHours", selectedPkg.maxHours);
    // rentalDetails.put("maxKm", selectedPkg.maxKm);
    // rentalDetails.put("usedHours", usedHours);
    // rentalDetails.put("usedKm", usedKm);
    // rentalDetails.put("extraHours", extraHours);
    // rentalDetails.put("extraHourCharges", extraHourCharges);
    // rentalDetails.put("extraKms", extraKms);
    // rentalDetails.put("extraKmCharges", extraKmCharges);
    // rentalDetails.put("subtotal", subtotal);
    // rentalDetails.put("gst", gst);
    // rentalDetails.put("total", total);
    // rentalDetails.put("carType", carType);
    // rentalDetails.put("perKmRate", perKmRate);
    // response.put("rentalDetails", rentalDetails);
    // response.put("tripType", tripType);
    // response.put("pickupLocation", pickupLocation);
    // response.put("dropLocation", dropLocation);
    // response.put("date", date);
    // response.put("returndate", returndate);
    // response.put("time", time);
    // response.put("Endtime", Endtime);
    // response.put("distance", calculatedDistance);
    // response.put("cabinfo", cabser.getAll());
    // response.put("days", 1);
    // return ResponseEntity.ok(response);
    // }

    // List<CabInfo> cabs = cabser.getAll();

    // response.put("tripType", tripType);
    // response.put("pickupLocation", pickupLocation);
    // response.put("dropLocation", dropLocation);
    // response.put("date", date);
    // response.put("returndate", returndate);
    // response.put("time", time);
    // response.put("distance", calculatedDistance);
    // response.put("cabinfo", cabs);
    // response.put("price", 10);
    // response.put("tripinfo", tripinfo);
    // response.put("days", days);

    // return ResponseEntity.ok(response);

    // } catch (Exception e) {
    // response.put("error", "Failed to process request: " + e.getMessage());
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    // }
    // }

    // for short route
    private int getShortRouteFare(int distance, String cabType) {
        int rate = 0;

        if (distance <= 50) {
            switch (cabType.toLowerCase()) {
                case "hatchback":
                    rate = 15;
                    break;
                case "sedan":
                    rate = 16;
                    break;
                case "sedanpremium":
                    rate = 18;
                    break;
                case "suv":
                    rate = 20;
                    break;
                case "suvplus":
                    rate = 22;
                    break;
                case "ertiga":
                    rate = 19;
                    break;
            }
        } else if (distance <= 100) {
            switch (cabType.toLowerCase()) {
                case "hatchback":
                    rate = 14;
                    break;
                case "sedan":
                    rate = 15;
                    break;
                case "sedanpremium":
                    rate = 17;
                    break;
                case "suv":
                    rate = 19;
                    break;
                case "suvplus":
                    rate = 21;
                    break;
                case "ertiga":
                    rate = 18;
                    break;
            }
        } else if (distance <= 150) {
            switch (cabType.toLowerCase()) {
                case "hatchback":
                    rate = 13;
                    break;
                case "sedan":
                    rate = 14;
                    break;
                case "sedanpremium":
                    rate = 16;
                    break;
                case "suv":
                    rate = 18;
                    break;
                case "suvplus":
                    rate = 20;
                    break;
                case "ertiga":
                    rate = 17;
                    break;
            }
        }

        return rate * distance;
    }

    // private int calculateAffordableFare(int distance, String cabType) {
    // double baseRate = 0, maxRate = 0;

    // switch (cabType.toLowerCase()) {
    // case "hatchback": baseRate = 8; maxRate = 10; break;
    // case "sedan": baseRate = 10; maxRate = 12; break;
    // case "sedanpremium": baseRate = 12; maxRate = 14; break;
    // case "suv": baseRate = 14; maxRate = 16; break;
    // case "suvplus": baseRate = 16; maxRate = 18; break;
    // case "ertiga": baseRate = 18; maxRate = 20; break;
    // default: baseRate = 8; maxRate = 10; break;
    // }

    // double ratePerKm = baseRate + ((maxRate - baseRate) / 5.0);
    // double baseFare = ratePerKm * distance;

    // double finalFare = baseFare;

    // if (distance <= 5) {
    // double multiplier = 1.4 - (distance * 0.20);
    // finalFare = baseFare * multiplier;
    // } else if (distance <= 15) {
    // finalFare = baseFare;
    // } else if (distance <= 30) {
    // double multiplier = 1.0 - ((distance - 15) * 0.07);
    // finalFare = baseFare * multiplier;
    // } else {
    // double multiplier = 0.85 - ((distance - 30) * 0.05);
    // multiplier = Math.max(multiplier, 0.75);
    // finalFare = baseFare * multiplier;
    // }

    // finalFare = Math.min(finalFare, 2700);

    // return (int) Math.round(finalFare);
    // }

    // Add this helper method outside of calculateAffordableFare method
    private int getAdditionalRate(String type) {
        switch (type.toLowerCase()) {
            case "hatchback":
                return 15;
            case "sedan":
                return 18;
            case "ertiga":
                return 22;
            case "suv":
                return 25;
            case "sedanpremium":
                return 20;
            case "suvplus":
                return 27;
            default:
                return 15;
        }
    }

    // Updated calculateAffordableFare method
    private int calculateAffordableFare(int distance, String cabType) {
        cabType = cabType.toLowerCase();

        System.out
                .println(String.format("🚗 Calculating fare for %s, distance: %d km", cabType.toUpperCase(), distance));

        int finalFare = 0;

        if (distance >= 1 && distance <= 10) {
            // Distance 1-10 km: Fixed prices
            switch (cabType) {
                case "hatchback":
                    finalFare = 460;
                    break;
                case "sedan":
                    finalFare = 450;
                    break;
                case "ertiga":
                    finalFare = 500;
                    break;
                case "suv":
                    finalFare = 2500;
                    break;
                case "sedanpremium":
                    finalFare = 480;
                    break;
                case "suvplus":
                    finalFare = 2600;
                    break;
                default:
                    finalFare = 460;
                    break;
            }
            System.out.println(String.format("   Range 1-10km: Fixed price ₹%d", finalFare));

        } else if (distance >= 11 && distance <= 20) {
            // Distance 11-20 km: Fixed prices
            switch (cabType) {
                case "hatchback":
                    finalFare = 650;
                    break;
                case "sedan":
                    finalFare = 700;
                    break;
                case "ertiga":
                    finalFare = 800;
                    break;
                case "suv":
                    finalFare = 2500;
                    break;
                case "sedanpremium":
                    finalFare = 750;
                    break;
                case "suvplus":
                    finalFare = 2600;
                    break;
                default:
                    finalFare = 650;
                    break;
            }
            System.out.println(String.format("   Range 11-20km: Fixed price ₹%d", finalFare));

        } else if (distance >= 21 && distance <= 30) {
            // Distance 21-30 km: Fixed prices
            switch (cabType) {
                case "hatchback":
                    finalFare = 800;
                    break;
                case "sedan":
                    finalFare = 900;
                    break;
                case "ertiga":
                    finalFare = 1100;
                    break;
                case "suv":
                    finalFare = 2500;
                    break;
                case "sedanpremium":
                    finalFare = 1000;
                    break;
                case "suvplus":
                    finalFare = 2700;
                    break;
                default:
                    finalFare = 800;
                    break;
            }
            System.out.println(String.format("   Range 21-30km: Fixed price ₹%d", finalFare));

        } else if (distance >= 31 && distance <= 40) {
            // Distance 31-40 km: Fixed prices
            switch (cabType) {
                case "hatchback":
                    finalFare = 1000;
                    break;
                case "sedan":
                    finalFare = 1200;
                    break;
                case "ertiga":
                    finalFare = 1400;
                    break;
                case "suv":
                    finalFare = 2500;
                    break;
                case "sedanpremium":
                    finalFare = 1300;
                    break;
                case "suvplus":
                    finalFare = 2800;
                    break;
                default:
                    finalFare = 1000;
                    break;
            }
            System.out.println(String.format("   Range 31-40km: Fixed price ₹%d", finalFare));

        } else if (distance >= 41 && distance <= 50) {
            // Distance 41-50 km: Base price + additional rate * 10
            int basePriceAt40km = 0;
            switch (cabType) {
                case "hatchback":
                    basePriceAt40km = 1000;
                    break;
                case "sedan":
                    basePriceAt40km = 1200;
                    break;
                case "ertiga":
                    basePriceAt40km = 1400;
                    break;
                case "suv":
                    basePriceAt40km = 2500;
                    break;
                case "sedanpremium":
                    basePriceAt40km = 1300;
                    break;
                case "suvplus":
                    basePriceAt40km = 2800;
                    break;
                default:
                    basePriceAt40km = 1000;
                    break;
            }

            int additionalRate = getAdditionalRate(cabType);
            int extraKm = 10;
            finalFare = basePriceAt40km + (additionalRate * extraKm);

            System.out.println(String.format("   Range 41-50km: Base ₹%d + (₹%d × %d km) = ₹%d",
                    basePriceAt40km, additionalRate, extraKm, finalFare));

        } else if (distance >= 51 && distance <= 80) {
            // Distance 51-80 km: Base price + (distance-50) * rate
            int basePriceAt50km = 0;
            switch (cabType) {
                case "hatchback":
                    basePriceAt50km = 1000 + (getAdditionalRate(cabType) * 10);
                    break;
                case "sedan":
                    basePriceAt50km = 1200 + (getAdditionalRate(cabType) * 10);
                    break;
                case "ertiga":
                    basePriceAt50km = 1400 + (getAdditionalRate(cabType) * 10);
                    break;
                case "suv":
                    basePriceAt50km = 2500 + (getAdditionalRate(cabType) * 10);
                    break;
                case "sedanpremium":
                    basePriceAt50km = 1300 + (getAdditionalRate(cabType) * 10);
                    break;
                case "suvplus":
                    basePriceAt50km = 2800 + (getAdditionalRate(cabType) * 10);
                    break;
                default:
                    basePriceAt50km = 1000 + (getAdditionalRate(cabType) * 10);
                    break;
            }

            int additionalRate = getAdditionalRate(cabType);
            int extraDistance = distance - 50;
            finalFare = basePriceAt50km + (additionalRate * extraDistance);

            System.out.println(String.format("   Range 51-80km: Base ₹%d + (₹%d × %d km) = ₹%d",
                    basePriceAt50km, additionalRate, extraDistance, finalFare));

        } else if (distance >= 81 && distance <= 85) {
            // Distance 81-85 km: Fixed prices
            switch (cabType) {
                case "hatchback":
                    finalFare = 2200;
                    break;
                case "sedan":
                    finalFare = 2400;
                    break;
                case "ertiga":
                    finalFare = 2600;
                    break;
                case "suv":
                    finalFare = 3000;
                    break;
                case "sedanpremium":
                    finalFare = 2500;
                    break;
                case "suvplus":
                    finalFare = 3200;
                    break;
                default:
                    finalFare = 2200;
                    break;
            }
            System.out.println(String.format("   Range 81-85km: Fixed price ₹%d", finalFare));

        } else if (distance >= 86 && distance <= 120) {
            // Distance 86-120 km: Base price + (distance-80) * rate
            int basePriceAt80km = 0;
            switch (cabType) {
                case "hatchback":
                    basePriceAt80km = 2200;
                    break;
                case "sedan":
                    basePriceAt80km = 2400;
                    break;
                case "ertiga":
                    basePriceAt80km = 2600;
                    break;
                case "suv":
                    basePriceAt80km = 3000;
                    break;
                case "sedanpremium":
                    basePriceAt80km = 2500;
                    break;
                case "suvplus":
                    basePriceAt80km = 3200;
                    break;
                default:
                    basePriceAt80km = 2200;
                    break;
            }

            int additionalRate = getAdditionalRate(cabType);
            int extraDistance = distance - 80;
            finalFare = basePriceAt80km + (additionalRate * extraDistance);

            System.out.println(String.format("   Range 86-120km: Base ₹%d + (₹%d × %d km) = ₹%d",
                    basePriceAt80km, additionalRate, extraDistance, finalFare));

        } else if (distance >= 121 && distance <= 150) {
            // Distance 121-150 km: Base price + (distance-121) * rate
            int basePriceAt121km = 0;
            switch (cabType) {
                case "hatchback":
                    basePriceAt121km = 2600;
                    break;
                case "sedan":
                    basePriceAt121km = 2800;
                    break;
                case "ertiga":
                    basePriceAt121km = 3000;
                    break;
                case "suv":
                    basePriceAt121km = 3500;
                    break;
                case "sedanpremium":
                    basePriceAt121km = 2900;
                    break;
                case "suvplus":
                    basePriceAt121km = 3700;
                    break;
                default:
                    basePriceAt121km = 2600;
                    break;
            }

            int additionalRate = getAdditionalRate(cabType);
            int extraDistance = distance - 121;
            finalFare = basePriceAt121km + (additionalRate * extraDistance);

            System.out.println(String.format("   Range 121-150km: Base ₹%d + (₹%d × %d km) = ₹%d",
                    basePriceAt121km, additionalRate, extraDistance, finalFare));

        } else {
            // Distance outside supported range
            System.out.println(String.format("⚠️ Distance %d km is outside supported range (1-150 km)", distance));
            return 0;
        }

        // Apply maximum fare cap
        finalFare = Math.min(finalFare, 2700);

        if (finalFare == 2700) {
            System.out.println(String.format("🚗 %s - Fare capped at maximum ₹2700", cabType.toUpperCase()));
        }

        System.out.println(String.format("✅ %s Final Fare: ₹%d (₹%.2f/km)",
                cabType.toUpperCase(), finalFare, (double) finalFare / distance));

        return finalFare;
    }

    // Optional: Method to get pricing explanation
    private String getPricingInfo(int distance, String cabType) {
        String distanceCategory;
        String pricing;

        if (distance <= 5) {
            distanceCategory = "Short Distance";
            pricing = "Premium pricing (+20-40%)";
        } else if (distance <= 15) {
            distanceCategory = "Medium Distance";
            pricing = "Standard pricing";
        } else if (distance <= 30) {
            distanceCategory = "Long Distance";
            pricing = "Discounted pricing (-5 to -15%)";
        } else {
            distanceCategory = "Very Long Distance";
            pricing = "Heavy discount (-15 to -25%)";
        }

        return String.format("Distance: %dkm (%s) - %s for %s",
                distance, distanceCategory, pricing, cabType);
    }

    @PostMapping("/cab1")
    public ResponseEntity<Map<String, Object>> processForm(
            @RequestParam("tripType") String tripType,
            @RequestParam("pickupLocation") String pickupLocation,
            @RequestParam("dropLocation") String dropLocation,
            @RequestParam("date") String date,
            @RequestParam(value = "Returndate", required = false) String returndate,
            @RequestParam("time") String time,
            @RequestParam(value = "distance", required = false) String distance,
            @RequestParam(value = "packageName", required = false) String packageName) {
        Map<String, Object> response = new HashMap<>();

        try {
            int calculatedDistance = 0;
            int responseDistance = 0;

            if (distance == null || distance.isEmpty() || distance.equals("0") || distance.equals("-1")) {
                calculatedDistance = getDistanceFromGoogleMaps(pickupLocation, dropLocation);
            } else {
                String numericString = distance.replaceAll("[^0-9.]", "");
                calculatedDistance = (int) Double.parseDouble(numericString);
            }

            // By default, use the raw calculated distance for the response.
            // For round trips we will override this later with the chargeable km.
            responseDistance = calculatedDistance;

            List<Trip> tripinfo = new ArrayList<>();
            int days = 0;

            String cityName = "Unknown City";
            String cityName1 = "Unknown City";

            try {
                String dropLocationResult = userService.getLongNameByCity(dropLocation, apiKey);
                cityName = extractCityName(dropLocationResult, dropLocation);
                System.out.println("🏙️ Extracted drop city: " + cityName + " from: " + dropLocationResult);
            } catch (Exception e) {
                System.err.println("❌ Error extracting drop city: " + e.getMessage());
                cityName = extractCityName(null, dropLocation);
            }

            try {
                String pickupLocationResult = userService.getLongNameByCity(pickupLocation, apiKey);
                cityName1 = extractCityName(pickupLocationResult, pickupLocation);
                System.out.println("🏙️ Extracted pickup city: " + cityName1 + " from: " + pickupLocationResult);
            } catch (Exception e) {
                System.err.println("❌ Error extracting pickup city: " + e.getMessage());
                cityName1 = extractCityName(null, pickupLocation);
            }

            cityName = cityName.equals("Bengaluru") ? "Bangalore" : cityName;
            cityName1 = cityName1.equals("Bengaluru") ? "Bangalore" : cityName1;

            if ("oneWay".equalsIgnoreCase(tripType)) {
                List<onewayTrip> oneWayTrips = tripSer.getoneWayTripData(pickupLocation, dropLocation);

                if (oneWayTrips.isEmpty()) {
                    oneWayTrips.add(createDefaultOneWayTrip());
                }

                for (onewayTrip o : oneWayTrips) {
                    try {
                        if (calculatedDistance <= 150) {
                            System.out.println(
                                    "💰 Using DB-based pricing for distance ≤ 150 km: " + calculatedDistance + " km");

                            PricingResponse pricing = this.oneFiftyService.applyPricing(calculatedDistance); // Call DB
                                                                                                             // pricing
                                                                                                             // method

                            if (pricing != null) {
                                o.setHatchback(pricing.getHatchback());
                                o.setSedan(pricing.getSedan());
                                o.setSedanpremium(pricing.getSedanpremium());
                                o.setSuv(pricing.getSuv());
                                o.setSuvplus(pricing.getSuvplus());
                                o.setErtiga(pricing.getErtiga());
                            } else {
                                // Fallback to default pricing if pricing service returns null
                                System.out.println("⚠️ Pricing service returned null, using fallback pricing");
                                o.setHatchback(o.getHatchback() * calculatedDistance);
                                o.setSedan(o.getSedan() * calculatedDistance);
                                o.setSedanpremium(o.getSedanpremium() * calculatedDistance);
                                o.setSuv(o.getSuv() * calculatedDistance);
                                o.setSuvplus(o.getSuvplus() * calculatedDistance);
                                o.setErtiga(o.getErtiga() * calculatedDistance);
                            }
                        } else {
                            o.setHatchback(o.getHatchback() * calculatedDistance);
                            o.setSedan(o.getSedan() * calculatedDistance);
                            o.setSedanpremium(o.getSedanpremium() * calculatedDistance);
                            o.setSuv(o.getSuv() * calculatedDistance);
                            o.setSuvplus(o.getSuvplus() * calculatedDistance);
                            o.setErtiga(o.getErtiga() * calculatedDistance);
                        }

                        o.setDistance((double) calculatedDistance);
                        tripinfo.add(o);
                    } catch (Exception e) {
                        System.err.println("❌ Error processing trip pricing: " + e.getMessage());
                        e.printStackTrace();
                        // Add default trip with basic pricing as fallback
                        onewayTrip defaultTrip = createDefaultOneWayTrip();
                        defaultTrip.setHatchback(defaultTrip.getHatchback() * calculatedDistance);
                        defaultTrip.setSedan(defaultTrip.getSedan() * calculatedDistance);
                        defaultTrip.setSedanpremium(defaultTrip.getSedanpremium() * calculatedDistance);
                        defaultTrip.setSuv(defaultTrip.getSuv() * calculatedDistance);
                        defaultTrip.setSuvplus(defaultTrip.getSuvplus() * calculatedDistance);
                        defaultTrip.setErtiga(defaultTrip.getErtiga() * calculatedDistance);
                        defaultTrip.setDistance((double) calculatedDistance);
                        tripinfo.add(defaultTrip);
                        break; // Exit loop to avoid multiple default entries
                    }
                }
            }

            else if ("roundTrip".equalsIgnoreCase(tripType)) {
                LocalDate localDate1 = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
                LocalDate localDate2 = LocalDate.parse(returndate, DateTimeFormatter.ISO_DATE);
                days = (int) ChronoUnit.DAYS.between(localDate1, localDate2) + 1;

                // For round trips, base calculation is: actual one-way km * 2 (go + return)
                int roundTripKm = calculatedDistance * 2;

                int perKm = 300;
                int baseKm = perKm * days;

                // Chargeable km is the higher of actual round-trip distance and base km
                int chargeableKm = Math.max(roundTripKm, baseKm);

                // Use this value for distance sent back to frontend as well
                responseDistance = chargeableKm;

                // Use full pickup and drop locations so TripService can extract City + State
                // This matches how one-way Excel routes are keyed and allows round-trip Excel
                // and manual prices to be applied correctly.
                List<roundTrip> roundTrips = tripSer.getRoundWayTripData(pickupLocation, dropLocation);

                if (roundTrips.isEmpty()) {
                    roundTrips.add(createDefaultRoundTrip());
                }

                for (roundTrip t : roundTrips) {
                    int hatchbackPrice = chargeableKm * t.getHatchback();
                    int sedanPrice = chargeableKm * t.getSedan();
                    int sedanPremiumPrice = chargeableKm * t.getSedanpremium();
                    int suvPrice = chargeableKm * t.getSuv();
                    int suvPlusPrice = chargeableKm * t.getSuvplus();
                    int ertigaPrice = chargeableKm * t.getErtiga();

                    t.setHatchback(hatchbackPrice);
                    t.setSedan(sedanPrice);
                    t.setSedanpremium(sedanPremiumPrice);
                    t.setSuv(suvPrice);
                    t.setSuvplus(suvPlusPrice);
                    t.setErtiga(ertigaPrice);

                    tripinfo.add(t);
                }
            }

            else if ("rental".equalsIgnoreCase(tripType)) {

                if (calculatedDistance == 0) {
                    System.out.println("⚠️ Distance not calculated, attempting to calculate from pickup location");
                    if (pickupLocation != null && !pickupLocation.isEmpty()) {
                        String destination = (dropLocation != null && !dropLocation.isEmpty()) ? dropLocation
                                : pickupLocation;
                        calculatedDistance = getDistanceFromGoogleMaps(pickupLocation, destination);
                        System.out
                                .println(String.format("📍 Calculated distance for rental: %d km", calculatedDistance));
                    }
                }

                String estimatedTime = "N/A";
                if (pickupLocation != null && !pickupLocation.isEmpty() &&
                        dropLocation != null && !dropLocation.isEmpty() &&
                        !pickupLocation.equals(dropLocation)) {

                    try {
                        String encodedPickup = URLEncoder.encode(pickupLocation, "UTF-8");
                        String encodedDrop = URLEncoder.encode(dropLocation, "UTF-8");

                        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                                "origin=" + encodedPickup +
                                "&destination=" + encodedDrop +
                                "&key=" + apiKey +
                                "&traffic_model=best_guess" +
                                "&departure_time=now";

                        RestTemplate restTemplate = new RestTemplate();
                        String jsonResponse = restTemplate.getForObject(url, String.class);

                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(jsonResponse);

                        if ("OK".equals(root.path("status").asText())) {
                            JsonNode routes = root.path("routes");
                            if (routes.isArray() && routes.size() > 0) {
                                JsonNode legs = routes.get(0).path("legs");
                                if (legs.isArray() && legs.size() > 0) {
                                    JsonNode duration = legs.get(0).path("duration");
                                    estimatedTime = duration.path("text").asText();
                                    System.out.println("🕒 Google Maps estimated time: " + estimatedTime);
                                }
                            }
                        } else {
                            System.out.println("⚠️ Google Maps API error: " + root.path("status").asText());
                            throw new Exception("API returned: " + root.path("status").asText());
                        }

                    } catch (Exception e) {
                        System.out.println("⚠️ Could not get travel time from API: " + e.getMessage());
                        if (calculatedDistance > 0) {
                            int estimatedMinutes = (int) Math.ceil((calculatedDistance * 60.0) / 30.0);
                            int hours = estimatedMinutes / 60;
                            int minutes = estimatedMinutes % 60;
                            estimatedTime = hours > 0
                                    ? String.format("%d hr%s %d min%s", hours, hours > 1 ? "s" : "", minutes,
                                            minutes != 1 ? "s" : "")
                                    : String.format("%d min%s", minutes, minutes != 1 ? "s" : "");
                            System.out.println(String.format("🕒 Fallback estimated time: %s", estimatedTime));
                        }
                    }
                }

                String actualPackageName = packageName;
                boolean autoUpgraded = false;

                if (calculatedDistance > 79) {
                    actualPackageName = "8hrs/80km";
                    autoUpgraded = true;
                    System.out.println(String.format("🔄 Distance %d km > 79km: Auto-upgrading to 8hrs/80km package",
                            calculatedDistance));
                }

                if ("4hrs/40Km".equalsIgnoreCase(actualPackageName)
                        || "4hrs/40km".equalsIgnoreCase(actualPackageName)) {
                    System.out.println("4hrs/40km package was selected");

                    List<roundTrip> rentalTrips = tripSer.getRoundWayTripData(pickupLocation, dropLocation);

                    if (rentalTrips.isEmpty()) {
                        rentalTrips.add(createDefaultRoundTrip());
                    }

                    for (roundTrip r : rentalTrips) {
                        System.out.println(
                                String.format("💰 Calculating rental pricing for distance: %d km", calculatedDistance));

                        if (calculatedDistance >= 1 && calculatedDistance <= 10) {
                            System.out.println("   Range 1-10km: Fixed prices for 4hrs/40km package");
                            r.setHatchback(450);
                            r.setSedan(500);
                            r.setSedanpremium(2600);
                            r.setSuv(2600);
                            r.setSuvplus(2600);
                            r.setErtiga(550);

                        } else if (calculatedDistance >= 11 && calculatedDistance <= 20) {
                            System.out.println("   Range 11-20km: Fixed prices for 4hrs/40km package");
                            r.setHatchback(650);
                            r.setSedan(700);
                            r.setSedanpremium(2600);
                            r.setSuv(2600);
                            r.setSuvplus(2600);
                            r.setErtiga(800);

                        } else if (calculatedDistance >= 21 && calculatedDistance <= 30) {
                            System.out.println("   Range 21-30km: Fixed prices for 4hrs/40km package");
                            r.setHatchback(800);
                            r.setSedan(900);
                            r.setSedanpremium(2600);
                            r.setSuv(2600);
                            r.setSuvplus(2600);
                            r.setErtiga(1100);

                        } else if (calculatedDistance >= 31 && calculatedDistance <= 40) {
                            System.out.println("   Range 31-40km: Fixed prices for 4hrs/40km package");
                            r.setHatchback(1000);
                            r.setSedan(1200);
                            r.setSedanpremium(2600);
                            r.setSuv(2600);
                            r.setSuvplus(2600);
                            r.setErtiga(1400);

                        } else if (calculatedDistance >= 41 && calculatedDistance <= 79) {
                            int extraKm = calculatedDistance - 40;
                            System.out.println("   Distance outside 1-40km range for 4hrs/40km package");
                            r.setHatchback(1000 + extraKm * 11);
                            r.setSedan(1200 + extraKm * 12);
                            r.setSedanpremium(2600 + extraKm * 14);
                            r.setSuv(2600 + extraKm * 16);
                            r.setSuvplus(2600 + extraKm * 19);
                            r.setErtiga(1400 + extraKm * 14);
                        }

                        else if (calculatedDistance >= 80) {
                            int extraKm = calculatedDistance - 80;
                            System.out.println("   Distance above 80km for 4hrs/40km package");
                            r.setHatchback(1800 + extraKm * 11);
                            r.setSedan(2200 + extraKm * 12);
                            r.setSedanpremium(2600 + extraKm * 14);
                            r.setSuv(2600 + extraKm * 16);
                            r.setSuvplus(3500 + extraKm * 19);
                            r.setErtiga(2600 + extraKm * 14);
                        }

                        r.setDistance((double) calculatedDistance);
                        tripinfo.add(r);

                        System.out.println(String.format(
                                "✅ Final Rental Prices - Hatchback: ₹%d, Sedan: ₹%d, Ertiga: ₹%d, SUV: ₹%d, Distance: %.1f km",
                                r.getHatchback(), r.getSedan(), r.getErtiga(), r.getSuv(), r.getDistance()));
                    }

                    response.put("selectedPackage", "4hrs/40km package was chosen");
                    response.put("availablePackages", "Available packages: 4hrs/40km, 8hrs/80km");

                } else if ("8hrs/80km".equalsIgnoreCase(actualPackageName)
                        || "8hrs/80Km".equalsIgnoreCase(actualPackageName)) {
                    System.out.println("8hrs/80km package was selected");

                    List<roundTrip> rentalTrips = tripSer.getRoundWayTripData(pickupLocation, dropLocation);

                    if (rentalTrips.isEmpty()) {
                        rentalTrips.add(createDefaultRoundTrip());
                    }

                    for (roundTrip r : rentalTrips) {
                        System.out.println(
                                String.format("💰 Calculating rental pricing for 8hrs/80km package, distance: %d km",
                                        calculatedDistance));

                        if (calculatedDistance >= 80) {
                            int extraKm = calculatedDistance - 80;
                            System.out
                                    .println("   Distance above 80km for 8hrs/80km package - applying dynamic pricing");

                            r.setHatchback(1800 + extraKm * 11);
                            r.setSedan(2000 + extraKm * 12);
                            r.setSedanpremium(3000 + extraKm * 14);
                            r.setSuv(3000 + extraKm * 16);
                            r.setSuvplus(3000 + extraKm * 19);
                            r.setErtiga(2200 + extraKm * 14);
                        }

                        r.setDistance((double) calculatedDistance);
                        tripinfo.add(r);

                        System.out.println(String.format(
                                "✅ Final Rental Prices (8hrs/80km) - Hatchback: ₹%d, Sedan: ₹%d, Ertiga: ₹%d, SUV: ₹%d, Distance: %.1f km",
                                r.getHatchback(), r.getSedan(), r.getErtiga(), r.getSuv(), r.getDistance()));
                    }

                    if (autoUpgraded) {
                        response.put("selectedPackage", "Auto-upgraded to 8hrs/80km package (distance > 79km)");
                        response.put("originalPackage", packageName);
                        response.put("upgradeReason", "Distance exceeds 79km limit for 4hrs/40km package");
                    } else {
                        response.put("selectedPackage", "8hrs/80km package was chosen");
                    }
                    response.put("availablePackages", "Available packages: 4hrs/40km, 8hrs/80km");

                } else {
                    System.out.println("No valid package selected");
                    response.put("message", "Please select a valid package");
                    response.put("availablePackages", "Available packages: 4hrs/40km, 8hrs/80km");
                }

                // Add estimated time to response for rental trips
                response.put("estimatedTravelTime", estimatedTime);
                response.put("autoUpgraded", autoUpgraded);
            }

            List<CabInfo> cabs = cabser.getAll();

            response.put("tripType", tripType);
            response.put("pickupLocation", pickupLocation);
            response.put("dropLocation", dropLocation);
            response.put("date", date);
            response.put("returndate", returndate);
            response.put("time", time);
            response.put("distance", responseDistance);
            response.put("cabinfo", cabs);
            response.put("tripinfo", tripinfo);
            response.put("days", days);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Invalid argument: " + e.getMessage());
            response.put("error", "Invalid request parameters: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error in /api/cab1: " + e.getMessage());
            e.printStackTrace();

            // Provide a more detailed error response for debugging
            response.put("error", "Failed to process request: " + e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());
            response.put("timestamp", java.time.LocalDateTime.now().toString());

            // Add some default data to prevent frontend crashes
            response.put("tripType", tripType);
            response.put("pickupLocation", pickupLocation);
            response.put("dropLocation", dropLocation);
            response.put("date", date);
            response.put("distance", 0);
            response.put("cabinfo", cabser.getAll());
            response.put("tripinfo", new ArrayList<>());
            response.put("days", 0);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private int getDistanceFromGoogleMaps(String origin, String destination) throws Exception {
        String urlString = String.format("%s?origins=%s&destinations=%s&key=%s",
                DISTANCE_MATRIX_API_URL,
                URLEncoder.encode(origin, StandardCharsets.UTF_8.toString()),
                URLEncoder.encode(destination, StandardCharsets.UTF_8.toString()),
                apiKey);

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed to get distance from Google Maps API");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONObject jsonResponse = (JSONObject) new org.json.simple.parser.JSONParser().parse(response.toString());
        JSONObject firstRow = (JSONObject) ((List<?>) jsonResponse.get("rows")).get(0);
        JSONObject firstElement = (JSONObject) ((List<?>) firstRow.get("elements")).get(0);
        JSONObject distanceObj = (JSONObject) firstElement.get("distance");

        long distanceInMeters = (long) distanceObj.get("value");
        return (int) (distanceInMeters / 1000);
    }

    private String extractCityName(String apiResult, String fallbackLocation) {
        try {
            if (apiResult == null || "Administrative Area Level 3 not found.".equals(apiResult)) {
                if (fallbackLocation != null && !fallbackLocation.trim().isEmpty()) {
                    String[] parts = fallbackLocation.split(", ");
                    if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                        return parts[0].trim();
                    }
                }
                return "Unknown City";
            } else if (apiResult.equals("North Goa") || apiResult.equals("South Goa")) {
                String[] parts = apiResult.split(", ");
                if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                    return parts[0].trim();
                }
                return apiResult; // Return the original if splitting fails
            } else {
                String[] parts = apiResult.split(" ");
                if (parts.length > 0 && !parts[0].trim().isEmpty()) {
                    return parts[0].trim();
                }
                return apiResult; // Return the original if splitting fails
            }
        } catch (Exception e) {
            System.err.println("❌ Error in extractCityName: " + e.getMessage());
            e.printStackTrace();
            return fallbackLocation != null ? fallbackLocation : "Unknown City";
        }
    }

    public static onewayTrip getDefaultOneWayTrip() {
        return new onewayTrip(
                null, "", "", "", "",
                14, 16, 18, 32, 27,
                "", null, null, 0, 20);
    }

    private onewayTrip createDefaultOneWayTrip() {
        return getDefaultOneWayTrip();
    }

    private roundTrip createDefaultRoundTrip() {
        return new roundTrip(
                null, "", "", "", "",
                11, 12, 14, 19, 20,
                "", null, null, 0, 14);
    }

    @GetMapping("/pricing")
    public ResponseEntity<Map<String, Object>> getPricing(
            @RequestParam("sourceCity") String sourceCity,
            @RequestParam("destCity") String destCity,
            @RequestParam(value = "tripType", required = false, defaultValue = "oneWay") String tripType) {
        Map<String, Object> response = new HashMap<>();

        try {
            CityExtractionService.CityStateResult sourceResult = cityExtractionService.extractCityAndState(sourceCity);
            CityExtractionService.CityStateResult destResult = cityExtractionService.extractCityAndState(destCity);

            String normalizedSourceCity = sourceResult.getCity().equals("Bengaluru") ? "Bangalore"
                    : sourceResult.getCity();
            String normalizedDestCity = destResult.getCity().equals("Bengaluru") ? "Bangalore" : destResult.getCity();

            List<Trip> tripinfo = new ArrayList<>();

            if ("oneWay".equals(tripType)) {
                List<onewayTrip> oneWayTrips = tripSer.getOneWayTripsWithDefaults(
                        normalizedSourceCity,
                        sourceResult.getState(),
                        normalizedDestCity,
                        destResult.getState());

                for (onewayTrip o : oneWayTrips) {
                    tripinfo.add(o);
                }
            } else if ("roundTrip".equals(tripType)) {
                // UNCHANGED: Keep original roundTrip logic
                tripinfo = tripSer.getRoundTrip(normalizedDestCity, normalizedSourceCity);
                if (tripinfo.isEmpty()) {
                    tripinfo.add(createDefaultRoundTrip());
                }

            }

            int dynamicDistance;
            try {
                dynamicDistance = getDistanceFromGoogleMaps(sourceCity, destCity);
                if ("roundTrip".equals(tripType)) {
                    dynamicDistance = dynamicDistance * 2;
                }
            } catch (Exception e) {
                dynamicDistance = 100;
                if ("roundTrip".equals(tripType)) {
                    dynamicDistance = 200;
                }
            }

            response.put("sourceCity", sourceCity);
            response.put("destCity", destCity);
            response.put("tripType", tripType);
            response.put("distance", dynamicDistance);
            response.put("pricingInfo", tripinfo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("error", "Failed to retrieve pricing: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/by-driver/{vendorDriverId}")
    public ResponseEntity<List<Booking>> getBookingsByVendorDriver(
            @PathVariable int vendorDriverId) {

        List<Booking> bookings = ser.getBookingsByVendorDriverId(vendorDriverId);

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/bookings/by-user/{id}")
    public ResponseEntity<?> getBookingsByCarRental(@PathVariable int id) {
        var bookings = ser.getBookingDTOsByCarRentalUserId(id);
        if (bookings == null || bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/invoice1")
    public Map<String, Object> bookCab(
            @RequestParam(value = "modelName", required = false) String modelName,
            @RequestParam(value = "modelType", required = false) String modelType,
            @RequestParam(value = "seats", required = false) String seats,
            @RequestParam(value = "fuelType", required = false) String fuelType,
            @RequestParam(value = "availability", required = false) String availability,
            @RequestParam(value = "price", required = false) String price,
            @RequestParam(value = "pickupLocation", required = false) String pickupLocation,
            @RequestParam(value = "dropLocation", required = false) String dropLocation,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "returndate", required = false) String returndate,
            @RequestParam(value = "time", required = false) String time,
            @RequestParam(value = "tripType", required = false) String tripType,
            @RequestParam(value = "distance", required = false) String distance,
            @RequestParam(value = "days", required = false) String days) {

        int driverrate = 0;
        int gst = 0;
        int service = 0;

        if ("oneWay".equals(tripType)) {
            gst = (Integer.parseInt(price) / 100) * 15;
            service = (Integer.parseInt(price) / 100) * 10;
        } else if ("roundTrip".equals(tripType)) {
            driverrate = Integer.parseInt(days) * 300;
            gst = ((Integer.parseInt(price) + driverrate) / 100) * 15;
            service = ((Integer.parseInt(price) + driverrate) / 100) * 10;
        }

        int totalAmount = Integer.parseInt(price) + gst + service + driverrate;

        Map<String, Object> response = new HashMap<>();
        response.put("driverrate", driverrate);
        response.put("days", days);
        response.put("gst", gst);
        response.put("service", service);
        response.put("total", totalAmount);
        response.put("modelName", modelName);
        response.put("modelType", modelType);
        response.put("seats", seats);
        response.put("fuelType", fuelType);
        response.put("availability", availability);
        response.put("price", price);
        response.put("pickupLocation", pickupLocation);
        response.put("dropLocation", dropLocation);
        response.put("date", date);
        response.put("returndate", returndate);
        response.put("time", time);
        response.put("tripType", tripType);
        response.put("distance", distance);

        return response;
    }

    @PostMapping("/bookingConfirm")
    public ResponseEntity<Map<String, Object>> createBooking(
            @RequestParam String cabId,
            @RequestParam String modelName,
            @RequestParam String modelType,
            @RequestParam String seats,
            @RequestParam String fuelType,
            @RequestParam String availability,
            @RequestParam String price,
            @RequestParam String pickupLocation,
            @RequestParam String dropLocation,
            @RequestParam String date,
            @RequestParam(required = false) String returndate,
            @RequestParam String time,
            @RequestParam String tripType,
            @RequestParam String distance,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String service,
            @RequestParam String gst,
            @RequestParam String total,
            @RequestParam String days,
            @RequestParam String driverrate,
            @RequestParam String phone,
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "cash") String paymentMethod,
            @RequestParam(defaultValue = "full") String paymentType,
            @RequestParam(defaultValue = "pending") String paymentStatus,
            @RequestParam(defaultValue = "0") String amountPaid,
            @RequestParam(required = false) String remainingAmount,
            @RequestParam(required = false) String razorpayOrderId,
            @RequestParam(required = false) String razorpayPaymentId,
            @RequestParam(required = false) String razorpaySignature) {

        try {
            Booking booking = new Booking();
            booking.setFromLocation(pickupLocation);
            booking.setToLocation(dropLocation);
            booking.setTripType(tripType);
            booking.setStartDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            booking.setTime(time);
            booking.setDistance(distance);
            booking.setName(name);
            booking.setEmail(email);
            booking.setPhone(phone);
            booking.setUserDrop(dropLocation);
            booking.setUserPickup(pickupLocation);
            booking.setUserTripType(tripType);
            booking.setBookingType("website");
            booking.setDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            booking.setCar(modelType);
            booking.setBaseAmount(price);
            booking.setAmount(Integer.parseInt(total));
            booking.setGst(Integer.parseInt(gst));
            booking.setServiceCharge(Integer.parseInt(service));

            booking.setPaymentMethod(paymentMethod);
            booking.setPaymentType(paymentType);
            booking.setPaymentStatus(paymentStatus);
            booking.setPaymentAmount(Integer.parseInt(amountPaid));

            // Calculate remaining amount
            if (remainingAmount != null && !remainingAmount.isEmpty()) {
                booking.setRemainingAmount(Integer.parseInt(remainingAmount));
            } else {
                int totalAmount = Integer.parseInt(total);
                int paidAmount = Integer.parseInt(amountPaid);
                int r = totalAmount - paidAmount;
                booking.setRemainingAmount(r);
            }

            // Razorpay details for online payments
            if (razorpayOrderId != null && !razorpayOrderId.isEmpty()) {
                booking.setRazorpayOrderId(razorpayOrderId);
            }
            if (razorpayPaymentId != null && !razorpayPaymentId.isEmpty()) {
                booking.setRazorpayPaymentId(razorpayPaymentId);
            }

            if (razorpaySignature != null && !razorpaySignature.isEmpty()) {
                booking.setRazorpaySignature(razorpaySignature);
            }

            if ("roundTrip".equals(tripType) && returndate != null) {
                booking.setReturnDate(LocalDate.parse(returndate, DateTimeFormatter.ISO_DATE));
            }

            if (userId != null) {
                CarRentalUser carRentalUser = this.carRentalRepository.findById(userId).orElse(null);
                booking.setCarRentalUser(carRentalUser);
            }

            String bookid = "WTL" + System.currentTimeMillis();
            booking.setBookingId(bookid);
            booking.setBookid(bookid);

            ser.saveBooking(booking);

            callExternalBookingApi(
                    date, name, email, modelName, distance, phone,
                    pickupLocation, dropLocation, time, returndate,
                    days, tripType, total, price, service, gst, driverrate);

            sendConfirmationEmail(name, email, bookid, pickupLocation,
                    dropLocation, tripType, date, time, total);

            sendWhatsAppBookingConfirmation(phone, bookid, name, pickupLocation,
                    dropLocation, date, time, tripType, total);

            User user = userService.findByid(Integer.parseInt("152"));
            String body = "New booking from " + name + " • ₹" + total;

            // Debug: Log the data being sent to notification
            System.out.println("🔔 Sending notification with data:");
            System.out.println("   📋 Booking ID: " + bookid);
            System.out.println("   👤 Customer: " + name);
            System.out.println("   📍 Pickup: " + pickupLocation);
            System.out.println("   📍 Drop: " + dropLocation);
            System.out.println("   💰 Amount: " + total);
            System.out.println("   🚗 Car: " + modelName + " " + modelType);
            System.out.println("   🎯 Trip Type: " + tripType);
            System.out.println("   📱 Phone: " + phone);
            System.out.println("   🔑 FCM Token: " + (user.getFcmToken() != null ? "Present" : "NULL"));

            // Send rich notification with booking data
            sendBookingNotification(
                    user.getFcmToken(),
                    "New Booking",
                    body,
                    bookid,
                    name,
                    pickupLocation,
                    dropLocation,
                    total,
                    modelName + " " + modelType,
                    tripType,
                    phone);

            // Send real-time WebSocket notification to admin
            liveTrackingController.sendAdminBookingNotification(
                    bookid,
                    name,
                    pickupLocation,
                    dropLocation,
                    total,
                    modelName + " " + modelType,
                    tripType,
                    phone);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "bookingId", bookid,
                    "message", "Booking created successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Booking creation failed"));
        }
    }

    @PostMapping("/register-admin-token")
    public ResponseEntity<?> registerAdminToken(@RequestBody Map<String, String> body) {
        String token = body.get("fcmToken");
        String id = body.get("userId");
        User user = userService.findByid(Integer.parseInt(id));

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token is required");
        }

        user.setFcmToken(token);
        userService.save(user);

        return ResponseEntity.ok("Token saved");
    }

    public void sendNotification(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Notification sent: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingNotification(String token, String title, String body,
            String bookingId, String customerName,
            String pickup, String drop, String amount, String carType,
            String tripType, String customerPhone) {
        try {
            System.out.println("🔥 sendBookingNotification called with:");
            System.out.println("   Token: " + (token != null ? "Present" : "NULL"));
            System.out.println("   Title: " + title);
            System.out.println("   Body: " + body);
            System.out.println("   BookingId: " + bookingId);
            System.out.println("   CustomerName: " + customerName);
            System.out.println("   Pickup: " + pickup);
            System.out.println("   Drop: " + drop);
            System.out.println("   Amount: " + amount);
            System.out.println("   CarType: " + carType);
            System.out.println("   TripType: " + tripType);
            System.out.println("   CustomerPhone: " + customerPhone);
            // Create rich notification title and body with actual booking data
            String richTitle = "🚗 New " + (tripType != null ? tripType : "oneWay") + " Booking";
            String richBody = (customerName != null ? customerName : "New Customer") +
                    " • ₹" + (amount != null ? amount : "0") +
                    "\n📍 " + (pickup != null ? pickup.substring(0, Math.min(pickup.length(), 30)) : "Unknown") +
                    " → " + (drop != null ? drop.substring(0, Math.min(drop.length(), 30)) : "Unknown");

            Notification notification = Notification.builder()
                    .setTitle(richTitle)
                    .setBody(richBody)
                    .build();

            Map<String, String> data = new HashMap<>();
            data.put("bookingId", bookingId != null ? bookingId : "");
            data.put("customerName", customerName != null ? customerName : "Unknown Customer");
            data.put("pickup", pickup != null ? pickup : "Unknown Pickup");
            data.put("drop", drop != null ? drop : "Unknown Drop");
            data.put("amount", amount != null ? amount : "0");
            data.put("carType", carType != null ? carType : "Unknown Car");
            data.put("tripType", tripType != null ? tripType : "oneWay");
            data.put("customerPhone", customerPhone != null ? customerPhone : "");
            data.put("type", "booking");
            data.put("timestamp", String.valueOf(System.currentTimeMillis()));

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .putAllData(data)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("📱 Booking notification sent: " + response);
            System.out.println("📊 Notification data: " + data);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ Failed to send booking notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/websiteBooking")
    public ResponseEntity<Map<String, Object>> createWebsiteBooking(
            @RequestParam String cabId,
            @RequestParam String modelName,
            @RequestParam String modelType,
            @RequestParam String seats,
            @RequestParam String fuelType,
            @RequestParam String availability,
            @RequestParam String price,
            @RequestParam String pickupLocation,
            @RequestParam String dropLocation,
            @RequestParam String date,
            @RequestParam(required = false) String returndate,
            @RequestParam String time,
            @RequestParam String tripType,
            @RequestParam String distance,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String service,
            @RequestParam String gst,
            @RequestParam String total,
            @RequestParam String days,
            @RequestParam String driverrate,
            @RequestParam String phone,
            @RequestParam(required = false) String packageName) {

        try {
            String bookid = "WTL" + System.currentTimeMillis();

            Booking booking = new Booking();
            booking.setFromLocation(pickupLocation);
            booking.setToLocation(dropLocation);
            booking.setTripType(tripType);
            booking.setStartDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            booking.setTime(time);
            booking.setDistance(distance);
            booking.setName(name);
            booking.setEmail(email);
            booking.setPhone(phone);
            booking.setUserDrop(dropLocation);
            booking.setUserPickup(pickupLocation);
            booking.setUserTripType(tripType);
            booking.setBookingType("website");
            booking.setDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
            booking.setCar(modelType);
            booking.setAmount(Integer.parseInt(price));
            booking.setGst(Integer.parseInt(gst));
            booking.setServiceCharge(Integer.parseInt(service));
            booking.setPackageName(packageName);

            if ("roundTrip".equals(tripType) || "round-trip".equals(tripType)) {
                try {
                    if (returndate != null && !returndate.trim().isEmpty()) {
                        LocalDate returnDate = LocalDate.parse(returndate, DateTimeFormatter.ISO_DATE);
                        booking.setReturnDate(returnDate);
                    } else {
                        booking.setReturnDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
                    }
                } catch (Exception e) {
                    booking.setReturnDate(LocalDate.parse(date, DateTimeFormatter.ISO_DATE));
                }
            }

            booking.setBookingId(bookid);
            booking.setBookid(bookid);

            ser.saveBooking(booking);

            sendConfirmationEmail(name, email, bookid, pickupLocation,
                    dropLocation, tripType, date, time, price);

            sendWhatsAppBookingConfirmation(phone, bookid, name, pickupLocation,
                    dropLocation, date, time, tripType, price);

            // Send real-time WebSocket notification to admin
            liveTrackingController.sendAdminBookingNotification(
                    bookid,
                    name,
                    pickupLocation,
                    dropLocation,
                    price,
                    modelType,
                    tripType,
                    phone);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "bookingId", bookid,
                    "message", "Booking created successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Booking creation failed: " + e.getMessage()));
        }
    }

    private void sendWhatsAppBookingConfirmation(String phone, String bookingId, String name,
            String pickupLocation, String dropLocation,
            String date, String time, String tripType, String price) {
        try {
            // logger.info("Attempting to send WhatsApp confirmation for booking: {}",
            // bookingId);

            // Validate phone number first
            if (!whatsAppService.isValidPhoneNumber(phone)) {
                // logger.warn("Invalid phone number for WhatsApp: {} (Booking: {})", phone,
                // bookingId);
                return;
            }

            // Create booking details object with proper setters
            BookingDetails bookingDetails = new BookingDetails();
            bookingDetails.setBookingId(bookingId);
            bookingDetails.setCustomerName(name);
            bookingDetails.setPickupLocation(pickupLocation);
            bookingDetails.setDropLocation(dropLocation);
            bookingDetails.setDate(date);
            bookingDetails.setTime(time);
            bookingDetails.setTripType(tripType);
            bookingDetails.setPrice(price);

            // whatsapp msg
            WhatsAppResponse whatsAppResponse = whatsAppService.sendBookingConfirmation(phone, bookingDetails);

            if (whatsAppResponse != null) {
                // logger.info("WhatsApp booking confirmation sent successfully for booking:
                // {}", bookingId);
            } else {
                // logger.warn("WhatsApp API returned empty response for booking: {}",
                // bookingId);
            }

        } catch (WhatsAppServiceException whatsAppException) {
            // logger.error("Failed to send WhatsApp booking confirmation for {}: {}",
            // bookingId, whatsAppException.getMessage());
        } catch (Exception e) {
            // logger.error("Unexpected error sending WhatsApp confirmation for {}: {}",
            // bookingId, e.getMessage());
        }
    }

    private void callExternalBookingApi(
            String date, String name, String email, String car,
            String distance, String phone, String userPickup,
            String userDrop, String time, String dateend,
            String days, String userTripType, String amount,
            String baseAmount, String serviceCharge, String gst,
            String driverBhata) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("date1", date);
            requestBody.put("name", name);
            requestBody.put("email", email);
            requestBody.put("car", car);
            requestBody.put("distance", distance);
            requestBody.put("phone", phone);
            requestBody.put("user_pickup", userPickup);
            requestBody.put("user_drop", userDrop);
            requestBody.put("time", time);
            requestBody.put("dateend", dateend);
            requestBody.put("timeend", "");
            requestBody.put("days", days);
            requestBody.put("user_trip_type", userTripType);
            requestBody.put("amount", amount);
            requestBody.put("baseAmount", baseAmount);
            requestBody.put("service_charge", serviceCharge);
            requestBody.put("gst", gst);
            requestBody.put("driver_bhata", driverBhata);

            String apiURL = "https://aimcabbooking.com/confirm-round-api-wtl.php";
            HttpURLConnection connection = (HttpURLConnection) new URL(apiURL).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.toString().getBytes("UTF-8"));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendConfirmationEmail(String name, String email, String bookingId,
            String pickupLocation, String dropLocation,
            String tripType, String date, String time,
            String total) {
        String subject = "Booking Confirmation - " + bookingId;
        String message = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Booking Confirmation</title>\n" +
                "</head>\n" +
                "<body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; color: #333; background-color: #f5f7fa;\">\n" +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px 0;\">\n" +
                "        <tr>\n" +
                "            <td align=\"center\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: 0 auto;\">\n" +
                "                    <tr>\n" +
                "                        <td align=\"center\" style=\"padding: 20px 0;\">\n" +
                "                            <h1 style=\"color: #ffffff; margin: 0; font-size: 32px; font-weight: 700;\">WTL Cab Service</h1>\n" +
                "                            <p style=\"color: #e0e0e0; margin: 8px 0 0 0; font-size: 16px;\">Reliable. Comfortable. On-time.</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "\n" +
                "    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width: 650px; margin: -30px auto 0; background-color: #ffffff; border-radius: 12px; box-shadow: 0 10px 30px rgba(0,0,0,0.1);\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 40px 35px;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"text-align: center; margin-bottom: 30px;\">\n" +
                "                                <div style=\"background-color: #e8f5e9; width: 70px; height: 70px; border-radius: 50%; display: inline-flex; align-items: center; justify-content: center; margin-bottom: 20px;\">\n" +
                "                                    <span style=\"font-size: 30px; color: #4caf50;\">✓</span>\n" +
                "                                </div>\n" +
                "                                <h2 style=\"color: #2e7d32; margin: 0 0 10px 0; font-size: 28px; font-weight: 700;\">Booking Confirmed!</h2>\n" +
                "                                <p style=\"color: #666; margin: 0; font-size: 18px;\">Thank you for choosing WTL Cab Service</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"background-color: #f8f9fa; border-radius: 10px; padding: 25px; margin-bottom: 30px; border: 1px solid #e9ecef;\">\n" +
                "                                <h3 style=\"margin-top: 0; color: #1a1f2e; font-size: 20px; font-weight: 600;\">Hello " + name + ",</h3>\n" +
                "                                <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0;\">Your booking has been successfully confirmed. Here are the details of your trip:</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin-bottom: 30px;\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 15px 20px; background: linear-gradient(to right, #667eea, #764ba2); color: white; border-radius: 8px 8px 0 0; text-align: center;\">\n" +
                "                                        <h3 style=\"margin: 0; font-size: 20px; font-weight: 600;\">Booking ID: " + bookingId + "</h3>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding: 0; border: 1px solid #e0e0e0; border-top: none; border-radius: 0 0 8px 8px;\">\n" +
                "                                        <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                            <tr>\n" +
                "                                                <td style=\"padding: 20px;\">\n" +
                "                                                    <h4 style=\"margin: 0 0 15px 0; color: #1a1f2e; font-size: 18px; font-weight: 600; border-bottom: 2px solid #667eea; padding-bottom: 8px;\">Trip Information</h4>\n" +
                "                                                    <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Trip Type:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + tripType + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Date:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + date + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Time:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + time + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Distance:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + (pickupLocation.equals(dropLocation) ? "Local Trip" : "Outstation") + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Pickup Location:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + pickupLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0;\"><strong style=\"color: #555;\">Drop-off Location:</strong></td>\n" +
                "                                                        </tr>\n" +
                "                                                        <tr>\n" +
                "                                                            <td style=\"padding: 8px 0; color: #333;\">" + dropLocation + "</td>\n" +
                "                                                        </tr>\n" +
                "                                                    </table>\n" +
                "                                                </td>\n" +
                "                                            </tr>\n" +
                "                                        </table>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "\n" +
                "                            <div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 8px; padding: 25px; margin-bottom: 30px; color: white; text-align: center;\">\n" +
                "                                <h3 style=\"margin: 0 0 15px 0; font-size: 22px; font-weight: 600;\">Payment Summary</h3>\n" +
                "                                <div style=\"font-size: 36px; font-weight: 700; margin-bottom: 5px;\">₹" + total + "</div>\n" +
                "                                <p style=\"margin: 0; font-size: 16px; opacity: 0.9;\">Amount Paid Successfully</p>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"margin-bottom: 30px;\">\n" +
                "                                <h3 style=\"color: #1a1f2e; margin: 0 0 20px 0; font-size: 20px; font-weight: 600; text-align: center;\">Important Information</h3>\n" +
                "                                <ul style=\"padding-left: 20px; margin: 0;\">\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Please arrive at the pickup location at least 10 minutes before the scheduled time.</li>\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">Driver details will be shared 30 minutes before pickup time.</li>\n" +
                "                                    <li style=\"margin-bottom: 12px; font-size: 15px; line-height: 24px;\">For any changes or cancellations, contact us at least 2 hours before pickup.</li>\n" +
                "                                    <li style=\"font-size: 15px; line-height: 24px;\">Toll charges, parking fees, and other taxes are not included and will be charged as applicable.</li>\n" +
                "                                </ul>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <div style=\"text-align: center; margin: 30px 0;\">\n" +
                "                                <a href=\"tel:+919876543210\" style=\"display: inline-block; background-color: #667eea; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Call Support</a>\n" +
                "                                <a href=\"mailto:support@wtlcabs.com\" style=\"display: inline-block; background-color: #2e7d32; color: white; text-decoration: none; padding: 14px 28px; border-radius: 30px; font-weight: 600; font-size: 16px; margin: 0 10px;\">Email Support</a>\n" +
                "                            </div>\n" +
                "\n" +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin-bottom: 0; text-align: center;\">We wish you a safe and comfortable journey!</p>\n" +
                "                            <p style=\"font-size: 16px; line-height: 26px; margin: 10px 0 0 0; text-align: center;\"><strong>Best regards,</strong><br>WTL Cab Service Team</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 25px 35px; background-color: #f8f9fa; border-radius: 0 0 12px 12px; border-top: 1px solid #eee;\">\n" +
                "                <table role=\"presentation\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align: center; font-size: 13px; color: #6c757d;\">\n" +
                "                            <p style=\"margin: 0 0 15px 0;\">&copy; 2025 WTL Cab Service. All rights reserved.</p>\n" +
                "                            <p style=\"margin: 0;\">If you have any questions, please contact our customer support at <a href=\"mailto:support@wtlcabs.com\" style=\"color: #667eea; text-decoration: none;\">support@wtlcabs.com</a> or call <a href=\"tel:+919876543210\" style=\"color: #667eea; text-decoration: none;\">+91 9876543210</a></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>";

        emailService.sendEmail(message, subject, email);
    }

    @PostMapping("/get-user-location/{id}")
    public CarRentalUser saveUserLocation(@PathVariable int id) {
        return ser.saveUserLocation(id);
    }

    @PutMapping("/update-Location/{id}")
    public CarRentalUser updateUserLocation(@PathVariable int id, @RequestParam double latitude,
            @RequestParam double longitude) {
        return this.carRentalBookingService.updateLocation(id, latitude,
                longitude);
    }

    @PutMapping("/update-driver-location/{vendorDriverId}")
    public VendorDrivers updateDriverLocation(
            @PathVariable int vendorDriverId,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return this.vendorDriverService.updateLocationDriver(
                vendorDriverId,
                latitude,
                longitude);
    }

    @PutMapping("/enterOtpTimePre/{id}")
    public Booking updateEnterOtpTimePreTrip(@PathVariable int id) {
        return this.ser.updateDriverEnterOtpTimePreStarted(id);
    }

    @PutMapping("/enterOtpTimePost/{id}")
    public Booking udpateEnterOtpTimePostTrip(@PathVariable int id) {
        return this.ser.updateDriverEnterOtpTimePostStarted(id);
    }

    @PutMapping("startingmeter/{id}")
    public Booking startOdoometerStarting(@PathVariable int id, @RequestParam String meter) {
        return this.ser.enterOdooMeterStarted(id, meter);
    }

    @PutMapping("/endTimer/{id}")
    public Booking endOdoometerEnding(@PathVariable int id, @RequestParam String meter) {
        return this.ser.enterOdoometerEnding(id, meter);
    }

    @PutMapping("/udpatePrice/{id}")
    public Booking updatePrice(@PathVariable int id, @RequestParam int amount) {
        return this.ser.updatePrice(id, amount);
    }

    @PostMapping("/createFirstPage")
    public Visitors visitorFirstPage(@RequestBody Visitors visitors) {
        return this.visitorService.createFirstPage(visitors);
    }

    @PutMapping("/{id}/secondPage")
    public Visitors createSecondPage(@PathVariable int id, @RequestBody Visitors visitors) {
        return this.visitorService.createSecondPage(id, visitors);
    }

    @GetMapping("/getAllVisitor")
    public List<Visitors> getAllVisitor() {
        return this.visitorService.getAllVisitor();
    }

    @GetMapping("/test-notification")
    public ResponseEntity<Map<String, Object>> testNotification() {
        try {
            User user = userService.findByid(Integer.parseInt("152"));
            System.out.println("🧪 Testing notification...");
            System.out.println("🔑 FCM Token: " + (user.getFcmToken() != null ? "Present" : "NULL"));

            if (user.getFcmToken() != null) {
                sendBookingNotification(
                        user.getFcmToken(),
                        "Test Notification",
                        "This is a test",
                        "TEST123",
                        "Test Customer",
                        "Test Pickup Location",
                        "Test Drop Location",
                        "1000",
                        "Test Car",
                        "oneWay",
                        "9999999999");
                return ResponseEntity.ok(Map.of("status", "success", "message", "Test notification sent"));
            } else {
                return ResponseEntity.ok(Map.of("status", "error", "message", "FCM token is null"));
            }
        } catch (Exception e) {
            System.err.println("❌ Test notification error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/latest-booking")
    public ResponseEntity<Map<String, Object>> getLatestBooking() {
        try {
            // Get the latest booking from the database
            List<Booking> allBookings = ser.getAllBookings();

            Map<String, Object> response = new HashMap<>();

            if (allBookings.isEmpty()) {
                response.put("hasNewBooking", false);
                response.put("message", "No bookings found");
                return ResponseEntity.ok(response);
            }

            // Sort bookings by ID (assuming higher ID means more recent)
            Booking latestBooking = allBookings.stream()
                    .max((b1, b2) -> Integer.compare(b1.getId(), b2.getId()))
                    .orElse(null);

            if (latestBooking != null) {
                response.put("hasNewBooking", true);
                response.put("booking", latestBooking);
                response.put("bookingId", latestBooking.getId());
                response.put("customerName", latestBooking.getName());

                // Try multiple field sources for pickup/drop locations
                String pickupLoc = latestBooking.getUserPickup();
                if (pickupLoc == null || pickupLoc.trim().isEmpty()) {
                    pickupLoc = latestBooking.getFromLocation();
                }
                String dropLoc = latestBooking.getUserDrop();
                if (dropLoc == null || dropLoc.trim().isEmpty()) {
                    dropLoc = latestBooking.getToLocation();
                }

                response.put("pickup", pickupLoc != null ? pickupLoc : "Unknown Pickup");
                response.put("drop", dropLoc != null ? dropLoc : "Unknown Drop");
                response.put("tripType", latestBooking.getTripType());
                response.put("date", latestBooking.getDate());
                response.put("time", latestBooking.getTime());
                response.put("amount", latestBooking.getAmount());
                response.put("customerPhone", latestBooking.getPhone());
                response.put("carType", latestBooking.getCar());
                response.put("timestamp", System.currentTimeMillis());

                System.out.println("📊 Latest booking data: ID=" + latestBooking.getId() +
                        ", Customer=" + latestBooking.getName() +
                        ", Pickup=" + pickupLoc +
                        ", Drop=" + dropLoc +
                        ", Amount=" + latestBooking.getAmount() +
                        ", Phone=" + latestBooking.getPhone());
            } else {
                response.put("hasNewBooking", false);
                response.put("message", "No valid booking found");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error fetching latest booking: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("hasNewBooking", false);
            errorResponse.put("error", "Failed to fetch latest booking: " + e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/device")
    public ResponseEntity<String> receiveDeviceId(@RequestBody DeviceRequest request) {
        String deviceId = request.getDeviceId();
        System.out.println("📱 Received Device ID: " + deviceId);
        // Optionally: save to DB or associate with user session
        return ResponseEntity.ok("Device ID received: " + deviceId);
    }

    @GetMapping("/oneFiftypricing")
    public PricingResponse getPricingGet(
            // @RequestParam("pickupLocation") String pickupLocation,
            // @RequestParam("dropLocation") String dropLocation,
            @RequestParam("distance") int distance) {

        return this.oneFiftyService.applyPricing(distance);
    }

    @PutMapping("/oneFiftyupdate")
    public ResponseEntity<PricingResponse> updatePricing(
            @RequestParam int id,
            @RequestParam int minDistance,
            @RequestParam int maxDistance,
            @RequestParam int hatchback,
            @RequestParam int sedan,
            @RequestParam int sedanpremium,
            @RequestParam int suv,
            @RequestParam int suvplus,
            @RequestParam int ertiga) {
        try {
            PricingResponse response = oneFiftyService.updatePricingWithParams(
                    id, minDistance, maxDistance,
                    hatchback, sedan, sedanpremium,
                    suv, suvplus, ertiga);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/oneFiftyCreate")
    public ResponseEntity<PricingResponse> createPricing(
            @RequestParam int minDistance,
            @RequestParam int maxDistance,
            @RequestParam int hatchback,
            @RequestParam int sedan,
            @RequestParam int sedanpremium,
            @RequestParam int suv,
            @RequestParam int suvplus,
            @RequestParam int ertiga) {
        try {
            PricingResponse response = oneFiftyService.createPricingWithParams(
                    minDistance, maxDistance,
                    hatchback, sedan, sedanpremium,
                    suv, suvplus, ertiga);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}