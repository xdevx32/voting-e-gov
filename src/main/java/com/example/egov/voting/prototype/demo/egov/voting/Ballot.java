package com.example.egov.voting.prototype.demo.egov.voting;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import javax.persistence.*;


/***
 *
 * Клас представляващ всяка бюлетина като отделна единица.
 *
 */

@Entity
@Table(name = "ballot")
public class Ballot {

    @Id
    private Long id;

    LocalDate localDate;

    @OneToOne
    Citizen citizen;

    @ManyToOne
    Party party;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}