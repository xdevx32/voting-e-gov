package com.example.egov.voting.prototype.demo.egov.voting;


import javax.persistence.*;

@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public final String name;
    public final String party;
    public final String number;


    public Candidate(String name, String party, String number) {
        this.name = name;
        this.party = party;
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

    public String getParty() {
        return party;
    }

    public String getNumber() {
        return number;
    }
}