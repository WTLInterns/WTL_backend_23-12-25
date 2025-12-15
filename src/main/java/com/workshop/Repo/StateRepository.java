package com.workshop.Repo;

import  com.workshop.Entity.States;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<States, Long> {
}

