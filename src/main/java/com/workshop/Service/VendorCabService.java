package com.workshop.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Vendor;
import com.workshop.Entity.VendorCabs;
import com.workshop.Repo.VendorCabRepo;
import com.workshop.Repo.VendorRepository;

@Service
public class VendorCabService {
	
	@Autowired
	private VendorCabRepo vendorCabRepo;
	
	@Autowired
	private VendorRepository vendorRepository;
	
	public VendorCabs saveVendorCab(VendorCabs vendorCabs) {
		
		return this.vendorCabRepo.save(vendorCabs);
	}
	
	public Optional<VendorCabs> getVendorCabsById(int vendorCabId) {
		return this.vendorCabRepo.findById(vendorCabId);
	}
	
	public List<VendorCabs> getOrder(Long vendorId) {
	    return this.vendorCabRepo.findByVendorId(vendorId);
	}

	
//	public List<VendorCabs> getOrder(Long id) {
//	    List<VendorCabs> allOrders = this.vendorCabRepo.findAll();
//	    return allOrders.stream()
//	            .filter(vendor -> !vendor.getVendor().getId().equals(id))
//	            .collect(Collectors.toList());
//	}






}
