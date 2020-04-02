package com.nbu.evote.entity;


import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;


/***
 *
 * Клас представляващ всяка бюлетина като отделна единица.
 *
 */

@Entity
@Table(name = "ballot")
public class Ballot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //AUTO WORKS!>>>>>
    private long id;

    LocalDate localDate;

    @OneToOne
    Citizen citizen;

    @ManyToOne
    Party party;

    public Ballot() {
    }

    public Ballot(long ballotId) {
        this.id = ballotId;
        this.localDate = LocalDate.now();
        this.citizen = new Citizen();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
