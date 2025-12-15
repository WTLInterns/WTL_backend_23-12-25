package com.workshop.DTO;

public class StateDTO{
    private Long id;
    private String name;

    public StateDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
