package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.DriveAdmin;

@Repository
public interface DriverAdminRepo extends JpaRepository<DriveAdmin, Integer>{


   DriveAdmin findByContactNo(String ContactNo);
}
