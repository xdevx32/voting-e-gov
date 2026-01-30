package com.nbu.evote.service;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.exception.CitizenNotFoundException;
import com.nbu.evote.exception.DuplicateVoteException;
import com.nbu.evote.exception.PartyNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotingServiceTest {

    @Mock
    private CitizenService citizenService;

    @Mock
    private PartyService partyService;

    @Mock
    private BallotService ballotService;

    private VotingService votingService;

    private Citizen citizen;
    private Party party;

    @BeforeEach
    void setUp() {
        votingService = new VotingService(citizenService, partyService, ballotService);

        citizen = new Citizen();
        citizen.setId(1L);
        citizen.setName("Ivan Petrov");
        citizen.setEGN("8501011234");
        citizen.setUniqueVoteId("VOTE-123-ABC");
        citizen.setCity("Sofia");
        citizen.setDayOfBirth(LocalDate.of(1985, 1, 1));
        citizen.setHasVoted(false);

        party = new Party();
        party.setId(1L);
        party.setName("Democratic Party");
        party.setNumber("1");
    }

    @Test
    @DisplayName("Should validate citizen with correct credentials")
    void validateCitizen_ShouldReturnCitizenWhenCredentialsAreValid() {
        when(citizenService.getCitizenByUniqueVoteIdAndEGN("VOTE-123-ABC", "8501011234"))
                .thenReturn(citizen);

        Citizen result = votingService.validateCitizen("VOTE-123-ABC", "8501011234");

        assertNotNull(result);
        assertEquals("Ivan Petrov", result.getName());
        assertEquals("8501011234", result.getEGN());
    }

    @Test
    @DisplayName("Should throw CitizenNotFoundException when citizen not found")
    void validateCitizen_ShouldThrowExceptionWhenCitizenNotFound() {
        when(citizenService.getCitizenByUniqueVoteIdAndEGN("INVALID", "INVALID"))
                .thenReturn(null);

        assertThrows(CitizenNotFoundException.class, () -> {
            votingService.validateCitizen("INVALID", "INVALID");
        });
    }

    @Test
    @DisplayName("Should throw DuplicateVoteException when citizen has already voted")
    void validateCitizen_ShouldThrowExceptionWhenAlreadyVoted() {
        citizen.setHasVoted(true);
        when(citizenService.getCitizenByUniqueVoteIdAndEGN("VOTE-123-ABC", "8501011234"))
                .thenReturn(citizen);

        assertThrows(DuplicateVoteException.class, () -> {
            votingService.validateCitizen("VOTE-123-ABC", "8501011234");
        });
    }

    @Test
    @DisplayName("Should throw DuplicateVoteException when citizen has ballot")
    void validateCitizen_ShouldThrowExceptionWhenBallotExists() {
        Ballot existingBallot = new Ballot();
        existingBallot.setId(1L);
        citizen.setBallot(existingBallot);

        when(citizenService.getCitizenByUniqueVoteIdAndEGN("VOTE-123-ABC", "8501011234"))
                .thenReturn(citizen);

        assertThrows(DuplicateVoteException.class, () -> {
            votingService.validateCitizen("VOTE-123-ABC", "8501011234");
        });
    }

    @Test
    @DisplayName("Should cast vote successfully")
    void castVote_ShouldCreateBallotAndUpdateCitizen() {
        when(partyService.getParty(1L)).thenReturn(party);

        Ballot result = votingService.castVote(citizen, 1L);

        assertNotNull(result);
        assertEquals(party, result.getParty());
        assertEquals(citizen, result.getCitizen());
        assertNotNull(result.getDate());
        assertNotNull(result.getTime());

        verify(ballotService, times(1)).addBallot(any(Ballot.class));
        verify(citizenService, times(1)).updateCitizen(citizen);
        verify(partyService, times(1)).updateParty(party);

        assertTrue(citizen.getHasVoted());
        assertNotNull(citizen.getBallot());
    }

    @Test
    @DisplayName("Should throw DuplicateVoteException when trying to cast vote twice")
    void castVote_ShouldThrowExceptionWhenVotingTwice() {
        citizen.setHasVoted(true);

        assertThrows(DuplicateVoteException.class, () -> {
            votingService.castVote(citizen, 1L);
        });

        verify(ballotService, never()).addBallot(any(Ballot.class));
    }

    @Test
    @DisplayName("Should throw PartyNotFoundException when party not found")
    void castVote_ShouldThrowExceptionWhenPartyNotFound() {
        when(partyService.getParty(999L)).thenReturn(null);

        assertThrows(PartyNotFoundException.class, () -> {
            votingService.castVote(citizen, 999L);
        });
    }

    @Test
    @DisplayName("Should check if citizen has voted - returns true when voted")
    void hasVoted_ShouldReturnTrueWhenCitizenHasVoted() {
        citizen.setHasVoted(true);
        when(citizenService.getCitizen(1L)).thenReturn(citizen);

        boolean result = votingService.hasVoted(1L);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should check if citizen has voted - returns false when not voted")
    void hasVoted_ShouldReturnFalseWhenCitizenHasNotVoted() {
        citizen.setHasVoted(false);
        when(citizenService.getCitizen(1L)).thenReturn(citizen);

        boolean result = votingService.hasVoted(1L);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should return false when citizen does not exist")
    void hasVoted_ShouldReturnFalseWhenCitizenNotFound() {
        when(citizenService.getCitizen(999L)).thenThrow(new IllegalArgumentException("Not found"));

        boolean result = votingService.hasVoted(999L);

        assertFalse(result);
    }

    @Test
    @DisplayName("Should check if citizen has ballot - returns true")
    void hasVoted_ShouldReturnTrueWhenBallotExists() {
        Ballot ballot = new Ballot();
        ballot.setId(1L);
        citizen.setBallot(ballot);
        citizen.setHasVoted(false);

        when(citizenService.getCitizen(1L)).thenReturn(citizen);

        boolean result = votingService.hasVoted(1L);

        assertTrue(result);
    }
}
