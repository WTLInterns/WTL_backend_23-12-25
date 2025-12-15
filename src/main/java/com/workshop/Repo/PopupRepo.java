package com.workshop.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.workshop.Entity.Popup;

@Repository
public interface PopupRepo extends JpaRepository<Popup, Integer>{

	
	
}
