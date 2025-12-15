package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Enquiry;


@Repository
public interface EnquiryRepo extends JpaRepository<Enquiry, Integer>{
    

}
