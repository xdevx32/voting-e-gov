package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import com.nbu.evote.repository.PartyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

    @Mock
    private PartyRepository partyRepository;

    @InjectMocks
    private PartyService partyService;

    private Party party;

    @BeforeEach
    void setUp() {
        party = new Party();
        party.setId(1L);
        party.setName("Democratic Party");
        party.setNumber("1");
        party.setSlogan("For a better future!");
        party.setImageUrl("http://example.com/party.jpg");
        party.setGroupImageUrl("http://example.com/group.jpg");
    }

    @Test
    @DisplayName("Should return all parties")
    void getAllParties_ShouldReturnAllParties() {
        Party party2 = new Party();
        party2.setId(2L);
        party2.setName("Republic Party");

        when(partyRepository.findAll()).thenReturn(Arrays.asList(party, party2));

        ArrayList<Party> result = partyService.getAllParties();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(partyRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no parties exist")
    void getAllParties_ShouldReturnEmptyListWhenNoParties() {
        when(partyRepository.findAll()).thenReturn(Arrays.asList());

        ArrayList<Party> result = partyService.getAllParties();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return party by ID")
    void getParty_ShouldReturnPartyById() {
        when(partyRepository.findById(1L)).thenReturn(Optional.of(party));

        Party result = partyService.getParty(1L);

        assertNotNull(result);
        assertEquals("Democratic Party", result.getName());
        assertEquals("1", result.getNumber());
    }

    @Test
    @DisplayName("Should throw exception when party not found")
    void getParty_ShouldThrowExceptionWhenNotFound() {
        when(partyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            partyService.getParty(999L);
        });
    }

    @Test
    @DisplayName("Should add a new party")
    void addParty_ShouldSaveParty() {
        partyService.addParty(party);

        verify(partyRepository, times(1)).save(party);
    }

    @Test
    @DisplayName("Should update an existing party")
    void updateParty_ShouldUpdateParty() {
        party.setSlogan("Updated Slogan");

        partyService.updateParty(party);

        verify(partyRepository, times(1)).save(party);
    }

    @Test
    @DisplayName("Should delete party by ID")
    void deleteParty_ShouldDeleteParty() {
        when(partyRepository.findById(1L)).thenReturn(Optional.of(party));

        partyService.deleteParty(1L);

        verify(partyRepository, times(1)).delete(party);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent party")
    void deleteParty_ShouldThrowExceptionWhenNotFound() {
        when(partyRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            partyService.deleteParty(999L);
        });
    }

    @Test
    @DisplayName("Should find party by name")
    void getPartyByName_ShouldReturnParty() {
        when(partyRepository.findByName("Democratic Party")).thenReturn(party);

        Party result = partyService.getPartyByName("Democratic Party");

        assertNotNull(result);
        assertEquals("Democratic Party", result.getName());
    }

    @Test
    @DisplayName("Should return null when party not found by name")
    void getPartyByName_ShouldReturnNullWhenNotFound() {
        when(partyRepository.findByName("Non-existent Party")).thenReturn(null);

        Party result = partyService.getPartyByName("Non-existent Party");

        assertNull(result);
    }

    @Test
    @DisplayName("Should track ballots count")
    void getParty_ShouldHaveCorrectBallotsCount() {
        when(partyRepository.findById(1L)).thenReturn(Optional.of(party));

        Party result = partyService.getParty(1L);

        assertEquals(0, result.getBallotsCount());
    }
}
