package com.example.egov.voting.prototype.demo.egov.voting;

import javax.persistence.*;

@Entity
public class Party {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private final String name;
    private final String number;


    public Party(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public String getNumber() {
        return number;
    }
}