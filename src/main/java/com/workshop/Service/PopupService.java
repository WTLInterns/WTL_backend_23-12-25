package com.workshop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Popup;
import com.workshop.Repo.PopupRepo;

@Service
public class PopupService {

	
	@Autowired
	PopupRepo repo;
	
	public Popup save(Popup popup) {
		return repo.save(popup);
		
	}
}
