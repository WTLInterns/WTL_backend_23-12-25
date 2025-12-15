package com.workshop.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.Entity.Cities;

@Service
public class CityExtractionService {

    @Autowired
    private TripService tripService;

    @Autowired
    private CitiesService citiesService;

    @Autowired
    private StatesService statesService;

    private static final String API_KEY = "AIzaSyDuZC6kFobB0pnp-k3VcxQIjvb0EhgfnVI";

    public String findNearestSubcity(String address) {
        return findNearestSubcity(address, API_KEY);
    }

    public String findNearestSubcity(String address, String apiKey) {
        try {
            String[] parts = address.split("[,;]");

            for (String part : parts) {
                String cleaned = part.trim();

                if (cleaned.length() > 2) {
                    String[] cityState = findInOnewayTripTable(cleaned);
                    if (cityState != null) {
                        return cityState[0];
                    }

                    if (doesCityExistInDatabase(cleaned)) {
                        return cleaned;
                    }

                    String cleanedPart = cleanAddressPart(cleaned);
                    if (cleanedPart.length() > 2 && doesCityExistInDatabase(cleanedPart)) {
                        return cleanedPart;
                    }
                }
            }

            String extractedCity = getMultiLevelCityFromGoogle(address, apiKey);

            if (extractedCity != null && !extractedCity.isEmpty()
                    && !extractedCity.equals("Administrative Area not found.")) {

                String dbMatch = findCityInDatabase(extractedCity);
                if (dbMatch != null) {
                    return dbMatch;
                }
            }

            String similarMatch = findMostSimilarCity(java.util.Arrays.asList(parts));
            if (similarMatch != null) {
                return similarMatch;
            }
            return "Unknown City";

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown City";
        }
    }

    public CityStateResult extractCityAndState(String address) {
        try {
            String[] parts = address.split("[,;]");

            for (String part : parts) {
                String cleaned = part.trim();
                if (cleaned.length() > 2) {
                    String[] cityState = findInOnewayTripTable(cleaned);
                    if (cityState != null) {
                        String city = cityState[0];
                        String state = cityState[1];
                        if (city != null && state != null && !city.equals(state)) {
                            return new CityStateResult(city, state, true, "Direct database match");
                        }
                    }
                }
            }

            CityStateResult googleResult = getGoogleCityAndState(address);
            if (googleResult.isSuccess()) {
                String city = googleResult.getCity();
                String state = googleResult.getState();

                if (city != null && state != null && !city.equals(state)) {
                    if (tripService.doesCityStatePairExist(city, state)) {
                        return new CityStateResult(city, state, true, "Google API + Database verified");
                    } else {
                        CityStateResult similarResult = findSimilarCityInDatabase(city, state);
                        if (similarResult.isSuccess()) {
                            return similarResult;
                        }
                    }
                }
            }

            CityStateResult fallbackResult = findBestMatchInDatabase(parts);
            if (fallbackResult.isSuccess()) {
                return fallbackResult;
            }

            String fallbackCity = extractCityFromAddress(address);
            String fallbackState = extractStateFromAddress(address);

            if (fallbackCity != null && fallbackState != null && !fallbackCity.equals(fallbackState)) {
                return new CityStateResult(fallbackCity, fallbackState, true, "Fallback extraction");
            }

            return new CityStateResult("Unknown City", "Unknown State", false, "No match found in database");

        } catch (Exception e) {
            e.printStackTrace();
            return new CityStateResult("Unknown City", "Unknown State", false, "Error: " + e.getMessage());
        }
    }

    private String extractCityFromAddress(String address) {
        try {
            String[] knownCities = { "Pune", "Mumbai", "Bangalore", "Delhi", "Chennai", "Hyderabad", "Kolkata",
                    "Ahmedabad", "Surat", "Jaipur" };

            String lowerAddress = address.toLowerCase();
            for (String city : knownCities) {
                if (lowerAddress.contains(city.toLowerCase())) {
                    return city;
                }
            }

            String[] parts = address.split("[,;]");
            for (String part : parts) {
                String cleaned = part.trim();
                if (cleaned.length() > 2 && cleaned.length() < 20) {
                    cleaned = cleanAddressPart(cleaned);
                    if (doesCityExistInDatabase(cleaned)) {
                        return cleaned;
                    }
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isValidState(String state) {
        if (state == null || state.trim().isEmpty())
            return false;
        String[] validStates = {
                // States (28)
                "andhra pradesh", "arunachal pradesh", "assam", "bihar", "chhattisgarh",
                "goa", "gujarat", "haryana", "himachal pradesh", "jharkhand", "karnataka",
                "kerala", "madhya pradesh", "maharashtra", "manipur", "meghalaya", "mizoram",
                "nagaland", "odisha", "punjab", "rajasthan", "sikkim", "tamil nadu",
                "telangana", "tripura", "uttar pradesh", "uttarakhand", "west bengal",

                // Union Territories (8)
                "andaman and nicobar islands", "chandigarh", "dadra and nagar haveli and daman and diu",
                "delhi", "jammu and kashmir", "ladakh", "lakshadweep", "puducherry",

                // Common abbreviations and alternate names
                "up", "mp", "ap", "tn", "wb", "hp", "uk", "jk",
                "ncr", "nct of delhi", "national capital territory of delhi",
                "orissa", // Old name for Odisha
                "bombay", // Historical reference
                "madras", // Historical reference for Tamil Nadu
                "calcutta", // Historical reference for West Bengal
                "pondicherry" // Alternate name for Puducherry
        };

        String lowerState = state.toLowerCase().trim();
        for (String valid : validStates) {
            if (lowerState.equals(valid)) {
                return true;
            }
        }
        return false;
    }

    private String extractStateFromAddress(String address) {
        try {
            String[] knownStates = {
                    "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh",
                    "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka",
                    "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram",
                    "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                    "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal",
                    "Delhi", "Chandigarh", "Jammu and Kashmir", "Ladakh", "Puducherry",
                    "Andaman and Nicobar Islands", "Dadra and Nagar Haveli and Daman and Diu", "Lakshadweep"
            };

            String lowerAddress = address.toLowerCase();

            for (String state : knownStates) {
                if (lowerAddress.contains(state.toLowerCase())) {
                    return state;
                }
            }

            if (lowerAddress.contains("pune") || lowerAddress.contains("mumbai") ||
                    lowerAddress.contains("nagpur") || lowerAddress.contains("nashik") ||
                    lowerAddress.contains("aurangabad") || lowerAddress.contains("solapur")) {
                return "Maharashtra";
            }

            if (lowerAddress.contains("bangalore") || lowerAddress.contains("bengaluru") ||
                    lowerAddress.contains("mysore") || lowerAddress.contains("hubli") ||
                    lowerAddress.contains("mangalore") || lowerAddress.contains("belgaum")) {
                return "Karnataka";
            }

            if (lowerAddress.contains("chennai") || lowerAddress.contains("madras") ||
                    lowerAddress.contains("coimbatore") || lowerAddress.contains("madurai") ||
                    lowerAddress.contains("salem") || lowerAddress.contains("tirupur")) {
                return "Tamil Nadu";
            }

            if (lowerAddress.contains("hyderabad") || lowerAddress.contains("warangal") ||
                    lowerAddress.contains("nizamabad") || lowerAddress.contains("karimnagar")) {
                return "Telangana";
            }

            if (lowerAddress.contains("ahmedabad") || lowerAddress.contains("surat") ||
                    lowerAddress.contains("vadodara") || lowerAddress.contains("rajkot") ||
                    lowerAddress.contains("gandhinagar")) {
                return "Gujarat";
            }

            if (lowerAddress.contains("kolkata") || lowerAddress.contains("calcutta") ||
                    lowerAddress.contains("howrah") || lowerAddress.contains("durgapur") ||
                    lowerAddress.contains("asansol")) {
                return "West Bengal";
            }

            if (lowerAddress.contains("jaipur") || lowerAddress.contains("jodhpur") ||
                    lowerAddress.contains("udaipur") || lowerAddress.contains("kota") ||
                    lowerAddress.contains("ajmer")) {
                return "Rajasthan";
            }

            if (lowerAddress.contains("lucknow") || lowerAddress.contains("kanpur") ||
                    lowerAddress.contains("agra") || lowerAddress.contains("varanasi") ||
                    lowerAddress.contains("allahabad") || lowerAddress.contains("meerut") ||
                    lowerAddress.contains("ghaziabad") || lowerAddress.contains("noida")) {
                return "Uttar Pradesh";
            }

            if (lowerAddress.contains("bhopal") || lowerAddress.contains("indore") ||
                    lowerAddress.contains("gwalior") || lowerAddress.contains("jabalpur")) {
                return "Madhya Pradesh";
            }

            if (lowerAddress.contains("patna") || lowerAddress.contains("gaya") ||
                    lowerAddress.contains("muzaffarpur") || lowerAddress.contains("bhagalpur")) {
                return "Bihar";
            }

            if (lowerAddress.contains("bhubaneswar") || lowerAddress.contains("cuttack") ||
                    lowerAddress.contains("rourkela") || lowerAddress.contains("puri")) {
                return "Odisha";
            }

            if (lowerAddress.contains("thiruvananthapuram") || lowerAddress.contains("kochi") ||
                    lowerAddress.contains("kozhikode") || lowerAddress.contains("thrissur") ||
                    lowerAddress.contains("ernakulam")) {
                return "Kerala";
            }

            if (lowerAddress.contains("chandigarh") || lowerAddress.contains("ludhiana") ||
                    lowerAddress.contains("amritsar") || lowerAddress.contains("jalandhar")) {
                return "Punjab";
            }

            if (lowerAddress.contains("gurgaon") || lowerAddress.contains("gurugram") ||
                    lowerAddress.contains("faridabad") || lowerAddress.contains("panipat")) {
                return "Haryana";
            }

            if (lowerAddress.contains("raipur") || lowerAddress.contains("bhilai") ||
                    lowerAddress.contains("bilaspur")) {
                return "Chhattisgarh";
            }

            if (lowerAddress.contains("ranchi") || lowerAddress.contains("jamshedpur") ||
                    lowerAddress.contains("dhanbad")) {
                return "Jharkhand";
            }

            if (lowerAddress.contains("guwahati") || lowerAddress.contains("silchar") ||
                    lowerAddress.contains("dibrugarh")) {
                return "Assam";
            }

            if (lowerAddress.contains("dehradun") || lowerAddress.contains("haridwar") ||
                    lowerAddress.contains("rishikesh")) {
                return "Uttarakhand";
            }

            if (lowerAddress.contains("shimla") || lowerAddress.contains("dharamshala") ||
                    lowerAddress.contains("manali")) {
                return "Himachal Pradesh";
            }

            if (lowerAddress.contains("panaji") || lowerAddress.contains("margao") ||
                    lowerAddress.contains("vasco")) {
                return "Goa";
            }

            if (lowerAddress.contains("imphal")) {
                return "Manipur";
            }

            if (lowerAddress.contains("shillong")) {
                return "Meghalaya";
            }

            if (lowerAddress.contains("aizawl")) {
                return "Mizoram";
            }

            if (lowerAddress.contains("kohima")) {
                return "Nagaland";
            }

            if (lowerAddress.contains("gangtok")) {
                return "Sikkim";
            }

            if (lowerAddress.contains("agartala")) {
                return "Tripura";
            }

            if (lowerAddress.contains("itanagar")) {
                return "Arunachal Pradesh";
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String[] extractCityAndState(String address, String apiKey) {
        try {
            String[] parts = address.split("[,;]");
            for (String part : parts) {
                String cleaned = part.trim();
                if (cleaned.length() > 2) {
                    String[] cityState = findInOnewayTripTable(cleaned);
                    if (cityState != null) {
                        return cityState;
                    }
                }
            }

            String[] googleResult = getGoogleCityAndState(address, apiKey);
            if (googleResult != null) {
                String[] dbMatch = findCityStatePairInDatabase(googleResult[0], googleResult[1]);
                if (dbMatch != null) {
                    return dbMatch;
                }
            }

            String cityOnly = findNearestSubcity(address, apiKey);
            if (!cityOnly.equals("Unknown City")) {
                return new String[] { cityOnly, "Unknown State" };
            }

            return new String[] { "Unknown City", "Unknown State" };

        } catch (Exception e) {
            e.printStackTrace();
            return new String[] { "Unknown City", "Unknown State" };
        }
    }

    private String[] findInOnewayTripTable(String location) {
        try {
            String[] result = tripService.findLocationInOnewayTrip(location);
            if (result != null) {
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private CityStateResult getGoogleCityAndState(String address) {
        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&key=" + API_KEY;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            if (results.size() == 0) {
                return new CityStateResult("Unknown City", "Unknown State", false, "No Google results");
            }

            JsonNode result = results.get(0);
            JsonNode addressComponents = result.path("address_components");

            String city = null;
            String state = null;

            String[] cityLevels = { "locality", "sublocality_level_1", "administrative_area_level_3",
                    "administrative_area_level_2" };
            for (String level : cityLevels) {
                city = extractCityFromComponents(addressComponents, level);
                if (city != null && !city.isEmpty()) {
                    break;
                }
            }

            state = extractCityFromComponents(addressComponents, "administrative_area_level_1");

            if (city != null && state != null) {
                city = normalizeCityName(city);
                state = normalizeStateName(state);

                if (!city.equals(state) && isValidCity(city) && isValidState(state)) {
                    return new CityStateResult(city, state, true, "Google API extraction");
                }
            }

            return new CityStateResult("Unknown City", "Unknown State", false, "Incomplete Google data");

        } catch (Exception e) {
            e.printStackTrace();
            return new CityStateResult("Unknown City", "Unknown State", false, "Google API error");
        }
    }

    private boolean isValidCity(String city) {
        if (city == null || city.trim().isEmpty())
            return false;
        String[] invalidCityValues = { "maharashtra", "karnataka", "tamil nadu", "gujarat", "rajasthan", "delhi",
                "state", "india" };
        String lowerCity = city.toLowerCase().trim();
        for (String invalid : invalidCityValues) {
            if (lowerCity.equals(invalid)) {
                return false;
            }
        }
        return true;
    }

    private String[] getGoogleCityAndState(String address, String apiKey) {
        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            if (results.size() == 0) {
                return null;
            }

            JsonNode result = results.get(0);
            JsonNode addressComponents = result.path("address_components");

            String city = null;
            String state = null;

            String[] cityLevels = { "locality", "sublocality_level_1", "administrative_area_level_3" };
            for (String level : cityLevels) {
                city = extractCityFromComponents(addressComponents, level);
                if (city != null && !city.isEmpty()) {
                    break;
                }
            }

            state = extractCityFromComponents(addressComponents, "administrative_area_level_1");

            if (city != null && state != null) {
                return new String[] { city, state };
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String[] findCityStatePairInDatabase(String city, String state) {
        try {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String getMultiLevelCityFromGoogle(String address, String apiKey) {
        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode results = root.path("results");

            if (results.size() == 0) {
                return "Administrative Area not found.";
            }

            JsonNode result = results.get(0);
            JsonNode addressComponents = result.path("address_components");

            String[] priorityLevels = {
                    "locality",
                    "sublocality_level_1",
                    "administrative_area_level_3",
                    "administrative_area_level_2",
                    "administrative_area_level_1"
            };

            for (String level : priorityLevels) {
                String cityName = extractCityFromComponents(addressComponents, level);
                if (cityName != null && !cityName.isEmpty()) {
                    return cityName;
                }
            }

            return "Administrative Area not found.";

        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving city name.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error retrieving city name.";
        }
    }

    private String extractCityFromComponents(JsonNode addressComponents, String targetType) {
        for (JsonNode component : addressComponents) {
            JsonNode types = component.path("types");
            if (types.isArray()) {
                for (JsonNode type : types) {
                    if (type.asText().equals(targetType)) {
                        return component.path("long_name").asText();
                    }
                }
            }
        }
        return null;
    }

    private String findCityInDatabase(String cityName) {
        try {
            if (doesCityExistInDatabase(cityName)) {
                return cityName;
            }

            String normalizedCity = normalizeCityName(cityName);
            if (normalizedCity != null && doesCityExistInDatabase(normalizedCity)) {
                return normalizedCity;
            }

            String partialMatch = findPartialCityMatch(cityName);
            if (partialMatch != null) {
                return partialMatch;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String parseAddressAndFindDatabaseMatch(String address) {
        if (address == null || address.trim().isEmpty()) {
            return "Unknown City";
        }

        String[] parts = address.split("[,;]");

        List<String> cleanParts = new ArrayList<>();
        for (String part : parts) {
            String cleaned = cleanAddressPart(part.trim());
            if (isValidLocationPart(cleaned)) {
                cleanParts.add(cleaned);
            }
        }

        for (String part : cleanParts) {
            String dbMatch = findCityInDatabase(part);
            if (dbMatch != null) {
                return dbMatch;
            }
        }

        String googleCity = getMultiLevelCityFromGoogle(address, API_KEY);
        if (googleCity != null && !googleCity.equals("Administrative Area not found.")) {
            String dbMatch = findCityInDatabase(googleCity);
            if (dbMatch != null) {
                return dbMatch;
            }
        }

        String similarMatch = findMostSimilarCity(cleanParts);
        if (similarMatch != null) {
            return similarMatch;
        }

        if (!cleanParts.isEmpty()) {
            return cleanParts.get(0);
        }

        return "Unknown City";
    }

    private String cleanAddressPart(String part) {
        if (part == null)
            return "";

        part = part.replaceAll("\\b\\d+\\b", "");
        part = part.replaceAll("(?i)^(near|opp|opposite|behind|front of)\\s+", "");
        part = part.replaceAll("[^a-zA-Z\\s]", "");

        return part.trim();
    }

    private boolean isValidLocationPart(String part) {
        if (part == null || part.length() < 2)
            return false;

        String[] skipWords = { "floor", "wing", "block", "phase", "unit", "flat", "apartment" };

        String lowerPart = part.toLowerCase();
        for (String skip : skipWords) {
            if (lowerPart.equals(skip)) {
                return false;
            }
        }

        return true;
    }

    private String normalizeCityName(String cityName) {
        if (cityName == null)
            return null;

        cityName = cityName.trim();
        if (cityName.equalsIgnoreCase("Bengaluru")) {
            return "Bangalore";
        }
        if (cityName.equalsIgnoreCase("Mumbai")) {
            return "Mumbai";
        }
        if (cityName.equalsIgnoreCase("Bombay")) {
            return "Mumbai";
        }
        if (cityName.equalsIgnoreCase("Bangalore")) {
            return "Bengaluru";
        }

        return cityName;
    }

    private String normalizeStateName(String stateName) {
        if (stateName == null)
            return null;

        stateName = stateName.trim();
        if (stateName.equalsIgnoreCase("Karnataka")) {
            return "Karnataka";
        }
        if (stateName.equalsIgnoreCase("Maharashtra")) {
            return "Maharashtra";
        }

        return stateName;
    }

    private boolean doesCityExistInDatabase(String cityName) {
        try {
            Cities city = citiesService.findByName(cityName);
            boolean exists = city != null;
            return exists;
        } catch (Exception e) {
            return false;
        }
    }

    private String findPartialCityMatch(String cityName) {
        try {
            List<Cities> cities = citiesService.findBySimilarName(cityName);
            if (cities != null && !cities.isEmpty()) {
                String matchedCity = cities.get(0).getName();
                return matchedCity;
            }
        } catch (Exception e) {
        }
        return null;
    }

    private CityStateResult findSimilarCityInDatabase(String city, String state) {
        try {
            List<String> allCities = tripService.getAllUniqueCities();

            String bestMatch = null;
            int bestSimilarity = 0;

            for (String dbCity : allCities) {
                int similarity = calculateStringSimilarity(city.toLowerCase(), dbCity.toLowerCase());
                if (similarity > bestSimilarity && similarity >= 80) {
                    bestSimilarity = similarity;
                    bestMatch = dbCity;
                }
            }

            if (bestMatch != null) {
                String[] cityStateFromDb = tripService.findLocationInOnewayTrip(bestMatch);
                if (cityStateFromDb != null) {
                    return new CityStateResult(cityStateFromDb[0], cityStateFromDb[1], true,
                            "Similar city match: " + bestSimilarity + "% similarity");
                }
            }

            return new CityStateResult("Unknown City", "Unknown State", false, "No similar city found");

        } catch (Exception e) {
            e.printStackTrace();
            return new CityStateResult("Unknown City", "Unknown State", false, "Error in similarity search");
        }
    }

    private CityStateResult findBestMatchInDatabase(String[] addressParts) {
        for (String part : addressParts) {
            String cleaned = cleanAddressPart(part.trim());
            if (cleaned.length() > 2) {
                String[] cityState = tripService.findLocationInOnewayTrip(cleaned);
                if (cityState != null && cityState.length >= 2) {
                    String city = cityState[0];
                    String state = cityState[1];
                    if (city != null && state != null && !city.equals(state) && isValidCity(city)
                            && isValidState(state)) {
                        return new CityStateResult(city, state, true, "Fallback match");
                    }
                }
            }
        }
        return new CityStateResult("Unknown City", "Unknown State", false, "No fallback match");
    }

    private String findMostSimilarCity(List<String> addressParts) {
        try {
            List<Cities> allCities = citiesService.getAllCities();
            if (allCities == null || allCities.isEmpty()) {
                return null;
            }

            String bestMatch = null;
            int bestScore = 0;

            for (String part : addressParts) {
                for (Cities city : allCities) {
                    int similarity = calculateStringSimilarity(part.toLowerCase(), city.getName().toLowerCase());
                    if (similarity > bestScore && similarity > 60) {
                        bestScore = similarity;
                        bestMatch = city.getName();
                    }
                }
            }

            return bestMatch;
        } catch (Exception e) {
            return null;
        }
    }

    private int calculateStringSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null)
            return 0;
        if (s1.equals(s2))
            return 100;

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0)
            return 100;

        int distance = levenshteinDistance(s1, s2);
        return (int) (((double) (maxLength - distance) / maxLength) * 100);
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    public static class CityStateResult {
        private String city;
        private String state;
        private boolean success;
        private String method;

        public CityStateResult(String city, String state, boolean success, String method) {
            this.city = city;
            this.state = state;
            this.success = success;
            this.method = method;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return String.format("CityStateResult{city='%s', state='%s', success=%s, method='%s'}",
                    city, state, success, method);
        }
    }
}