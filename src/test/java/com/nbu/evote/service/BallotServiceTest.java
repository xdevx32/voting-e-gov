package com.nbu.evote.service;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.repository.BallotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BallotServiceTest {

    @Mock
    private BallotRepository ballotRepository;

    @InjectMocks
    private BallotService ballotService;

    private Ballot ballot;
    private Citizen citizen;
    private Party party;

    @BeforeEach
    void setUp() {
        citizen = new Citizen();
        citizen.setId(1L);
        citizen.setName("Ivan Petrov");

        party = new Party();
        party.setId(1L);
        party.setName("Democratic Party");

        ballot = new Ballot();
        ballot.setId(1L);
        ballot.setDate(LocalDate.now());
        ballot.setTime(LocalTime.of(10, 30));
        ballot.setCitizen(citizen);
        ballot.setParty(party);
    }

    @Test
    @DisplayName("Should return all ballots")
    void getBallots_ShouldReturnAllBallots() {
        Ballot ballot2 = new Ballot();
        ballot2.setId(2L);
        ballot2.setDate(LocalDate.now());

        when(ballotRepository.findAll()).thenReturn(Arrays.asList(ballot, ballot2));

        List<Ballot> result = ballotService.getBallots();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ballotRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no ballots exist")
    void getBallots_ShouldReturnEmptyListWhenNoBallots() {
        when(ballotRepository.findAll()).thenReturn(Arrays.asList());

        List<Ballot> result = ballotService.getBallots();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return ballot by ID")
    void getBallot_ShouldReturnBallotById() {
        when(ballotRepository.findById(1L)).thenReturn(Optional.of(ballot));

        Ballot result = ballotService.getBallot(1L);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getDate());
        assertNotNull(result.getCitizen());
        assertNotNull(result.getParty());
    }

    @Test
    @DisplayName("Should throw exception when ballot not found")
    void getBallot_ShouldThrowExceptionWhenNotFound() {
        when(ballotRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            ballotService.getBallot(999L);
        });
    }

    @Test
    @DisplayName("Should add a new ballot")
    void addBallot_ShouldSaveBallot() {
        ballotService.addBallot(ballot);

        verify(ballotRepository, times(1)).save(ballot);
    }

    @Test
    @DisplayName("Should update an existing ballot")
    void updateBallot_ShouldUpdateBallot() {
        ballot.setTime(LocalTime.of(14, 0));

        ballotService.updateBallot(ballot);

        verify(ballotRepository, times(1)).save(ballot);
    }

    @Test
    @DisplayName("Should delete ballot by ID")
    void deleteBallot_ShouldDeleteBallot() {
        when(ballotRepository.findById(1L)).thenReturn(Optional.of(ballot));

        ballotService.deleteBallot(1L);

        verify(ballotRepository, times(1)).delete(ballot);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent ballot")
    void deleteBallot_ShouldThrowExceptionWhenNotFound() {
        when(ballotRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            ballotService.deleteBallot(999L);
        });
    }

    @Test
    @DisplayName("Should correctly associate ballot with citizen")
    void addBallot_ShouldAssociateCitizen() {
        when(ballotRepository.findById(1L)).thenReturn(Optional.of(ballot));

        Ballot result = ballotService.getBallot(1L);

        assertEquals("Ivan Petrov", result.getCitizen().getName());
    }

    @Test
    @DisplayName("Should correctly associate ballot with party")
    void addBallot_ShouldAssociateParty() {
        when(ballotRepository.findById(1L)).thenReturn(Optional.of(ballot));

        Ballot result = ballotService.getBallot(1L);

        assertEquals("Democratic Party", result.getParty().getName());
    }

    @Test
    @DisplayName("Should return time as string")
    void getTimeString_ShouldReturnFormattedTime() {
        assertEquals("10:30", ballot.getTimeString());
    }
}
