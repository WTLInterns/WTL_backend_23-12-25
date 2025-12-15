package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {
    
}