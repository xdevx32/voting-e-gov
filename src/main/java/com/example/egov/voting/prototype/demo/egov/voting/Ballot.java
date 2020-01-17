package com.example.egov.voting.prototype.demo.egov.voting;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

/***
 *
 * Клас представляващ всеки вот като отделна единица.
 *
 */

@Entity
public class Ballot {

    @Id
    private Long id;

    LocalDate localDate;

    Citizen citizen;

    Candidate candidate;

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

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
}
