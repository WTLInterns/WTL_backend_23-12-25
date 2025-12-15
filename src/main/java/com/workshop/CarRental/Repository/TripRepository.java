package com.workshop.CarRental.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.CarRental.DTO.Trip;


@Repository
public interface TripRepository extends JpaRepository<Trip, String> {
    List<Trip> findByDriverIdAndStatus(String driverId, Trip.TripStatus status);
}
