package com.workshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.City;
@Repository

public interface CityRepo extends JpaRepository<City, Integer> {

	
	City findByCity(String cityname);
	
	List<City> findByState(String state);
}
