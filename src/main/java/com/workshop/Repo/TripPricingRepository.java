package com.workshop.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Tripprice;
import com.workshop.Entity.onewayTrip;

@Repository
public interface TripPricingRepository extends JpaRepository<Tripprice, Long> {
    // Additional query methods can be defined here if needed

    



}
