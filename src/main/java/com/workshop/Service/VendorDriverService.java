package com.workshop.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.VendorCabs;
import com.workshop.Entity.VendorDrivers;
import com.workshop.Repo.VendorDriverRepo;

@Service
public class VendorDriverService {

	
	@Autowired
	private VendorDriverRepo vendorDriverRepo;
	
	
	
	public VendorDrivers addVendorsDriver(VendorDrivers vendorDrivers) {
		// Preserve password provided by caller (e.g., last 6 of DL set in controller)
		return this.vendorDriverRepo.save(vendorDrivers);
	}
	
	public Optional<VendorDrivers> getVendorDriversById(int vendorDriverId) {
		return this.vendorDriverRepo.findById(vendorDriverId);
	}
	
	public List<VendorDrivers> getAllDrivers(){
		return this.vendorDriverRepo.findAll();
	}
	
	public List<VendorDrivers> getOrder(Long vendorId) {
	    return this.vendorDriverRepo.findByVendorId(vendorId);
	}

	public VendorDrivers updateLocationDriver(int vendorDriverId, double latitude, double longitude) {
		VendorDrivers vendorDrivers = this.vendorDriverRepo.findById(vendorDriverId)
			.orElseThrow(() -> new RuntimeException("Driver not found with id: " + vendorDriverId));
		
		vendorDrivers.setDriverLatitude(latitude);
		vendorDrivers.setDriverLongitude(longitude);
		
		return this.vendorDriverRepo.save(vendorDrivers);
	}

	public VendorDrivers findById(int vendorDriverId){
		VendorDrivers v = this.vendorDriverRepo.findById(vendorDriverId).orElse(null);
		return v;
	}

}
