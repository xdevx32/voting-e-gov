package com.nbu.evote.dto;

import java.util.List;
import java.util.Map;

public class VoteStatisticsDTO {

    private List<String> partyNames;
    private List<Integer> voteCounts;
    private Integer totalVotes;
    private Map<String, Integer> votesByHour;
    private String dateOfVote;

    public VoteStatisticsDTO() {
    }

    public VoteStatisticsDTO(List<String> partyNames, List<Integer> voteCounts, Integer totalVotes, Map<String, Integer> votesByHour, String dateOfVote) {
        this.partyNames = partyNames;
        this.voteCounts = voteCounts;
        this.totalVotes = totalVotes;
        this.votesByHour = votesByHour;
        this.dateOfVote = dateOfVote;
    }

    public List<String> getPartyNames() {
        return partyNames;
    }

    public void setPartyNames(List<String> partyNames) {
        this.partyNames = partyNames;
    }

    public List<Integer> getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(List<Integer> voteCounts) {
        this.voteCounts = voteCounts;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Map<String, Integer> getVotesByHour() {
        return votesByHour;
    }

    public void setVotesByHour(Map<String, Integer> votesByHour) {
        this.votesByHour = votesByHour;
    }

    public String getDateOfVote() {
        return dateOfVote;
    }

    public void setDateOfVote(String dateOfVote) {
        this.dateOfVote = dateOfVote;
    }
}
