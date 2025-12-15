package com.workshop.Entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "states")
public class States {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
 
    @Column(name = "country_id")
    private int  countryId;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<Cities> cities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryCode(int countryId) {
        this.countryId = countryId;
    }

    public List<Cities> getCities() {
        return cities;
    }

    public void setCities(List<Cities> cities) {
        this.cities = cities;
    }

    // Getters and Setters

    


}
