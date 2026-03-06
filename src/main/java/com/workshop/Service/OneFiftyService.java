package com.workshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.DTO.PricingResponse;
import com.workshop.Entity.OneFifty;
import com.workshop.Entity.roundTrip;
import com.workshop.Repo.OneFiftyRepo;

@Service
public class OneFiftyService {

    @Autowired
    private OneFiftyRepo repo;

    // Parses "City, State" into [city, state]
    private String[] extractCityAndState(String loc) {
        String[] parts = loc.split(",");
        if (parts.length >= 2) {
            return new String[] { parts[0].trim(), parts[1].trim() };
        }
        return new String[] { "Unknown City", "Unknown State" };
    }

    // public PricingResponse applyPricing(String pickupLoc, String dropLoc, int
    // distance) {
    // if (distance < 1 || distance > 150) {
    // throw new IllegalArgumentException("Distance must be between 1 and 150 km");
    // }

    // String[] p = extractCityAndState(pickupLoc);
    // String[] d = extractCityAndState(dropLoc);

    // List<OneFifty> matches =
    // repo.findBySourceStateAndDestinationStateAndSourceCityAndDestinationCityAndMinDistanceLessThanEqualAndMaxDistanceGreaterThanEqual(
    // p[1], d[1], p[0], d[0], distance, distance
    // );

    // OneFifty data = matches.stream().findFirst()
    // .orElseThrow(() -> new RuntimeException(
    // "No pricing data found for route " + pickupLoc + " → " + dropLoc + " at " +
    // distance + "km"));

    // PricingResponse resp = new PricingResponse();
    // resp.setHatchback(data.getHatchback());
    // resp.setSedan(data.getSedan());
    // resp.setSedanpremium(data.getSedanpremium());
    // resp.setSuv(data.getSuv());
    // resp.setSuvplus(data.getSuvplus());
    // resp.setErtiga(data.getErtiga());
    // return resp;
    // }

    public PricingResponse applyPricing(int distance) {
        if (distance < 1 || distance > 150) {
            throw new IllegalArgumentException("Distance must be between 1 and 150 km");
        }

        try {
            // Fetch matching record based only on distance
            List<OneFifty> matches = repo.findByMinDistanceLessThanEqualAndMaxDistanceGreaterThanEqual(distance,
                    distance);

            System.out.println("🔍 Searching for pricing data for distance: " + distance + " km");
            System.out.println("📊 Found " + matches.size() + " matching records");

            if (matches.isEmpty()) {
                System.out.println(
                        "⚠️ No pricing data found for distance: " + distance + " km, returning default pricing");
                return createDefaultPricingResponse(distance);
            }

            OneFifty data = matches.get(0);
            System.out.println("✅ Using pricing data: ID=" + data.getId() + ", Range=" + data.getMinDistance() + "-"
                    + data.getMaxDistance() + " km");

            PricingResponse resp = new PricingResponse();
            resp.setId(data.getId());
            resp.setMinDistance(data.getMinDistance());
            resp.setMaxDistance(data.getMaxDistance());
            resp.setHatchback(data.getHatchback());
            resp.setSedan(data.getSedan());
            resp.setSedanpremium(data.getSedanpremium());
            resp.setSuv(data.getSuv());
            resp.setSuvplus(data.getSuvplus());
            resp.setErtiga(data.getErtiga());
            resp.setElectric(data.getElectric());
            return resp;
        } catch (Exception e) {
            System.err.println("❌ Error in applyPricing for distance " + distance + " km: " + e.getMessage());
            e.printStackTrace();
            return createDefaultPricingResponse(distance);
        }
    }

    private PricingResponse createDefaultPricingResponse(int distance) {
        System.out.println("🔧 Creating default pricing response for distance: " + distance + " km");

        PricingResponse resp = new PricingResponse();
        resp.setId(0);
        resp.setMinDistance(distance);
        resp.setMaxDistance(distance);

        // Default pricing based on distance ranges
        if (distance <= 10) {
            resp.setHatchback(460);
            resp.setSedan(450);
            resp.setSedanpremium(480);
            resp.setElectric(resp.getSedanpremium());
            resp.setSuv(2500);
            resp.setSuvplus(2600);
            resp.setErtiga(500);
        } else if (distance <= 20) {
            resp.setHatchback(650);
            resp.setSedan(700);
            resp.setSedanpremium(750);
            resp.setElectric(resp.getSedanpremium());
            resp.setSuv(2500);
            resp.setSuvplus(2600);
            resp.setErtiga(800);
        } else if (distance <= 30) {
            resp.setHatchback(800);
            resp.setSedan(900);
            resp.setSedanpremium(1000);
            resp.setElectric(resp.getSedanpremium());
            resp.setSuv(2500);
            resp.setSuvplus(2700);
            resp.setErtiga(1100);
        } else if (distance <= 50) {
            resp.setHatchback(distance * 15);
            resp.setSedan(distance * 18);
            resp.setSedanpremium(distance * 20);
            resp.setElectric(resp.getSedanpremium());
            resp.setSuv(distance * 25);
            resp.setSuvplus(distance * 27);
            resp.setErtiga(distance * 22);
        } else {
            // For distances > 50km, use per-km rates
            resp.setHatchback(distance * 14);
            resp.setSedan(distance * 16);
            resp.setSedanpremium(distance * 18);
            resp.setElectric(resp.getSedanpremium());
            resp.setSuv(distance * 22);
            resp.setSuvplus(distance * 24);
            resp.setErtiga(distance * 20);
        }

        System.out.println(
                "💰 Default pricing created - Hatchback: ₹" + resp.getHatchback() + ", Sedan: ₹" + resp.getSedan());
        return resp;
    }

    public PricingResponse updatePricingWithParams(int id,
            int minDistance, int maxDistance,
            int hatchback, int sedan, int sedanpremium,
            int suv, int suvplus, int ertiga, int electric) {

        OneFifty existing = repo.findById(id).orElse(null);

        if (existing == null) {
            throw new RuntimeException("Pricing record not found with ID: " + id);
        }

        existing.setMinDistance(minDistance);
        existing.setMaxDistance(maxDistance);
        existing.setHatchback(hatchback);
        existing.setSedan(sedan);
        existing.setSedanpremium(sedanpremium);
        existing.setElectric(electric);
        existing.setSuv(suv);
        existing.setSuvplus(suvplus);
        existing.setErtiga(ertiga);

        OneFifty updated = repo.save(existing);

        PricingResponse response = new PricingResponse();
        response.setHatchback(updated.getHatchback());
        response.setSedan(updated.getSedan());
        response.setSedanpremium(updated.getSedanpremium());
        response.setElectric(updated.getElectric());
        response.setSuv(updated.getSuv());
        response.setSuvplus(updated.getSuvplus());
        response.setErtiga(updated.getErtiga());

        return response;
    }

    public PricingResponse createPricingWithParams(int minDistance, int maxDistance,
            int hatchback, int sedan, int sedanpremium,
            int suv, int suvplus, int ertiga, int electric) {

        OneFifty newEntry = new OneFifty();
        newEntry.setMinDistance(minDistance);
        newEntry.setMaxDistance(maxDistance);
        newEntry.setHatchback(hatchback);
        newEntry.setSedan(sedan);
        newEntry.setSedanpremium(sedanpremium);
        newEntry.setElectric(electric);
        newEntry.setSuv(suv);
        newEntry.setSuvplus(suvplus);
        newEntry.setErtiga(ertiga);

        OneFifty saved = repo.save(newEntry);

        PricingResponse response = new PricingResponse();
        response.setHatchback(saved.getHatchback());
        response.setSedan(saved.getSedan());
        response.setSedanpremium(saved.getSedanpremium());
        response.setElectric(saved.getElectric());
        response.setSuv(saved.getSuv());
        response.setSuvplus(saved.getSuvplus());
        response.setErtiga(saved.getErtiga());

        return response;
    }

}
