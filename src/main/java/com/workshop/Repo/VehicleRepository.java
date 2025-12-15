package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.OutSourceCarCab;

@Repository
public interface VehicleRepository extends JpaRepository<OutSourceCarCab, Long>{

}
