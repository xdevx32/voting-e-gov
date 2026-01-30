package com.nbu.evote.service;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.exception.CitizenNotFoundException;
import com.nbu.evote.exception.DuplicateVoteException;
import com.nbu.evote.exception.PartyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class VotingService {

    private final CitizenService citizenService;
    private final PartyService partyService;
    private final BallotService ballotService;

    @Autowired
    public VotingService(CitizenService citizenService, PartyService partyService, BallotService ballotService) {
        this.citizenService = citizenService;
        this.partyService = partyService;
        this.ballotService = ballotService;
    }

    /**
     * Validates citizen credentials for voting.
     *
     * @param uniqueVoteId The unique vote identifier
     * @param egn          The citizen's EGN (personal identification number)
     * @return The citizen if valid credentials
     * @throws CitizenNotFoundException if citizen not found
     * @throws DuplicateVoteException   if citizen has already voted
     */
    public Citizen validateCitizen(String uniqueVoteId, String egn) {
        Citizen citizen = citizenService.getCitizenByUniqueVoteIdAndEGN(uniqueVoteId, egn);

        if (citizen == null) {
            throw new CitizenNotFoundException("No citizen found with the provided credentials. Please check your EGN and Vote ID.");
        }

        if (citizen.getHasVoted() != null && citizen.getHasVoted()) {
            throw new DuplicateVoteException("You have already cast your vote. Each citizen can only vote once.");
        }

        if (citizen.getBallot() != null) {
            throw new DuplicateVoteException("A ballot has already been recorded for this citizen.");
        }

        return citizen;
    }

    /**
     * Casts a vote for a specific party.
     *
     * @param citizen The citizen casting the vote
     * @param partyId The ID of the party being voted for
     * @return The created ballot
     * @throws PartyNotFoundException if party not found
     * @throws DuplicateVoteException if citizen has already voted
     */
    @Transactional
    public Ballot castVote(Citizen citizen, Long partyId) {
        // Double-check the citizen hasn't voted
        if (citizen.getHasVoted() != null && citizen.getHasVoted()) {
            throw new DuplicateVoteException("You have already cast your vote. Each citizen can only vote once.");
        }

        if (citizen.getBallot() != null) {
            throw new DuplicateVoteException("A ballot has already been recorded for this citizen.");
        }

        Party party = partyService.getParty(partyId);
        if (party == null) {
            throw new PartyNotFoundException("Party with ID " + partyId + " not found.");
        }

        // Create and save the ballot
        Ballot ballot = new Ballot();
        ballot.setParty(party);
        ballot.setCitizen(citizen);
        ballot.setDate(LocalDate.now());
        ballot.setTime(LocalTime.now());

        ballotService.addBallot(ballot);

        // Update citizen's voting status
        citizen.setBallot(ballot);
        citizen.setHasVoted(true);
        citizenService.updateCitizen(citizen);

        // Add ballot to party
        party.addBallot(ballot);
        partyService.updateParty(party);

        return ballot;
    }

    /**
     * Checks if a citizen has already voted.
     *
     * @param citizenId The citizen's ID
     * @return true if the citizen has voted, false otherwise
     */
    public boolean hasVoted(Long citizenId) {
        try {
            Citizen citizen = citizenService.getCitizen(citizenId);
            return (citizen.getHasVoted() != null && citizen.getHasVoted()) || citizen.getBallot() != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
