package com.workshop.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workshop.Entity.B2B;

public interface B2BRepo extends JpaRepository<B2B, Long> {

    Optional<B2B> findByCompanyName(String companyName);

    B2B findByGmail(String gmail);

    B2B findByBusinessGmail(String businessGmail);

}