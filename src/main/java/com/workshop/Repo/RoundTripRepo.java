package com.workshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.roundTrip;

@Repository
public interface RoundTripRepo extends JpaRepository<roundTrip, Long>{
	
	
	List<Trip> findBySourceCityAndDestinationCity(String to, String from);

	List<roundTrip>findBySourceStateAndDestinationStateAndSourceCityAndDestinationCity(String sourceState, String destinationState, String sourceCity, String destinationCity);

}