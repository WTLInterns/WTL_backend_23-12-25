package com.workshop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Tripprice;
import com.workshop.Repo.TripPricingRepository;


@Service
public class TripRateService {
    


@Autowired
    private TripPricingRepository tripPricingRepository;

    // Other methods...

    public void updateTripPricing(Tripprice tripPricing) {
        tripPricingRepository.save(tripPricing); // This will save or update based on the ID
    }






}
