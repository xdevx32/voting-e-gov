package com.nbu.evote.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class CitizenDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "EGN is required")
    @Pattern(regexp = "^\\d{10}$", message = "EGN must be exactly 10 digits")
    private String egn;

    private LocalDate dayOfBirth;

    private String city;

    private String uniqueVoteId;

    private Boolean hasVoted;

    public CitizenDTO() {
    }

    public CitizenDTO(Long id, String name, String egn, LocalDate dayOfBirth, String city, String uniqueVoteId, Boolean hasVoted) {
        this.id = id;
        this.name = name;
        this.egn = egn;
        this.dayOfBirth = dayOfBirth;
        this.city = city;
        this.uniqueVoteId = uniqueVoteId;
        this.hasVoted = hasVoted;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniqueVoteId() {
        return uniqueVoteId;
    }

    public void setUniqueVoteId(String uniqueVoteId) {
        this.uniqueVoteId = uniqueVoteId;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}
