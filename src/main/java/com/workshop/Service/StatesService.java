package com.workshop.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.States;
import com.workshop.Repo.StateRepository;

import java.util.List;
import java.util.Optional;
@Service
public class StatesService {
    
@Autowired
    private StateRepository statesreRepository;


  public Optional<States> getStateById(Long id) {
        return statesreRepository.findById(id);
    }

    public List<States> getAllState(){
        return this.statesreRepository.findAll();
    }


}
