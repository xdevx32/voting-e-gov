package com.nbu.evote.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String number;

    @OneToMany
    Set<Ballot> ballots = new HashSet<>();

    public Party() {
    }

    public Party(long partyId) {
        this.id = partyId;
        this.name = "Default name";
        this.number = "44";
        this.ballots = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(Set<Ballot> ballots) {
        this.ballots = ballots;
    }

    @Override
    public String toString() {
        return "Party{" + "id=" + id + ", name=" + name + ", number=" + number + '}';
    }
}