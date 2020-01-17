package com.example.egov.voting.prototype.demo.egov.voting;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Citizen {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Ballot ballot;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }
}
