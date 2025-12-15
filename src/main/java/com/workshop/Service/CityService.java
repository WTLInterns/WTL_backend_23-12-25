package com.workshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.City;
import com.workshop.Repo.CityRepo;

@Service
public class CityService {

	
	@Autowired
	CityRepo repo;
	
	
	
	public City findCityByName(String cityName) {
		return repo.findByCity(cityName);
	}
	
	
	
	public List<City> findCityByState(String statename) {
		return repo.findByState(statename);
	}
	
	public List<City> findAll(){
		return repo.findAll();
	}
}
