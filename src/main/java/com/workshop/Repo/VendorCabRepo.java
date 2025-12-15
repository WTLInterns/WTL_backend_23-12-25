package com.workshop.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.VendorCabs;

@Repository
public interface VendorCabRepo extends JpaRepository<VendorCabs, Integer>{
    List<VendorCabs> findByVendorId(Long vendorId);

}
