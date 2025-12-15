package com.workshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.VendorCabs;
import com.workshop.Entity.VendorDrivers;

@Repository
public interface VendorDriverRepo extends JpaRepository<VendorDrivers, Integer>{
    List<VendorDrivers> findByVendorId(Long vendorId);

    VendorDrivers findByContactNo(String contactNo);

    public boolean existsByContactNo(String contactNo);

}
