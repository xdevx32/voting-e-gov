package com.nbu.evote.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class VoteRequest {

    @NotBlank(message = "EGN is required")
    @Pattern(regexp = "^\\d{10}$", message = "EGN must be exactly 10 digits")
    private String egn;

    @NotBlank(message = "Unique Vote ID is required")
    private String uniqueVoteId;

    private Long partyId;

    public VoteRequest() {
    }

    public VoteRequest(String egn, String uniqueVoteId, Long partyId) {
        this.egn = egn;
        this.uniqueVoteId = uniqueVoteId;
        this.partyId = partyId;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getUniqueVoteId() {
        return uniqueVoteId;
    }

    public void setUniqueVoteId(String uniqueVoteId) {
        this.uniqueVoteId = uniqueVoteId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }
}
