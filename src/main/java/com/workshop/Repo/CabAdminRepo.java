package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.workshop.Entity.CabAdmin;

@Repository
public interface CabAdminRepo extends JpaRepository<CabAdmin, Long>{

}
