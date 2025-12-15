package com.workshop.Service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workshop.Entity.Visitors;
import com.workshop.Repo.VisitorRepo;

@Service
public class VisitorService {
    

    @Autowired
    private VisitorRepo visitorRepo;

    public Visitors createFirstPage(Visitors visitors){
        visitors.setVisitDate(LocalDate.now());
        return this.visitorRepo.save(visitors);
        
    }



    public Visitors createSecondPage(int id, Visitors visitors){

        Visitors v = this.visitorRepo.findById(id).get();
        v.setBaseAmount(visitors.getBaseAmount());
        v.setPath(visitors.getPath());

        return this.visitorRepo.save(v);


        
    }

    public Visitors createThridPage(int id, Visitors visitors){
        Visitors v = this.visitorRepo.findById(id).get();
        v.setPath(visitors.getPath());
        return this.visitorRepo.save(v);



    }


    public List<Visitors> getAllVisitor(){
        return this.visitorRepo.findAll();
    }
}
