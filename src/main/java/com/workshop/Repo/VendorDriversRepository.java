package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workshop.Entity.VendorDrivers;

@Repository
public interface VendorDriversRepository extends JpaRepository<VendorDrivers, Integer> {
}