package com.workshop.CarRental.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.CarRental.Entity.CarRentalUser;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRentalUser, Integer>{
    

    public Optional<CarRentalUser> findByPhone(String phone);

    public Optional<CarRentalUser> findByEmail(String email);

    public boolean existsByPhone(String phone);
}
