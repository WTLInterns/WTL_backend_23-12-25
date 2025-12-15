package com.workshop.Repo;

import com.workshop.Entity.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CitiesRepository extends JpaRepository<Cities, Long> {
    List<Cities> findByStateId(Long stateId);

    Cities findByNameIgnoreCase(String name);

    List<Cities> findByNameContainingIgnoreCase(String name);

}