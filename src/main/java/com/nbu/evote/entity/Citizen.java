package com.nbu.evote.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @OneToOne
    private Ballot ballot;

    public Citizen() {
    }

    public Citizen(long citizenId) {
        this.id = citizenId;
        this.name = "Default name";
        this.ballot = new Ballot();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }
}
