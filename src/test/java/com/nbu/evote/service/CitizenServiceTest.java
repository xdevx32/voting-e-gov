package com.nbu.evote.service;

import com.nbu.evote.entity.Citizen;
import com.nbu.evote.repository.CitizenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitizenServiceTest {

    @Mock
    private CitizenRepository citizenRepository;

    @InjectMocks
    private CitizenService citizenService;

    private Citizen citizen;

    @BeforeEach
    void setUp() {
        citizen = new Citizen();
        citizen.setId(1L);
        citizen.setName("Ivan Petrov");
        citizen.setEGN("8501011234");
        citizen.setUniqueVoteId("VOTE-123-ABC");
        citizen.setCity("Sofia");
        citizen.setDayOfBirth(LocalDate.of(1985, 1, 1));
        citizen.setHasVoted(false);
    }

    @Test
    @DisplayName("Should return all citizens")
    void getCitizens_ShouldReturnAllCitizens() {
        Citizen citizen2 = new Citizen();
        citizen2.setId(2L);
        citizen2.setName("Maria Ivanova");

        when(citizenRepository.findAll()).thenReturn(Arrays.asList(citizen, citizen2));

        List<Citizen> result = citizenService.getCitizens();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(citizenRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no citizens exist")
    void getCitizens_ShouldReturnEmptyListWhenNoCitizens() {
        when(citizenRepository.findAll()).thenReturn(Arrays.asList());

        List<Citizen> result = citizenService.getCitizens();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return citizen by ID")
    void getCitizen_ShouldReturnCitizenById() {
        when(citizenRepository.findById(1L)).thenReturn(Optional.of(citizen));

        Citizen result = citizenService.getCitizen(1L);

        assertNotNull(result);
        assertEquals("Ivan Petrov", result.getName());
        assertEquals("8501011234", result.getEGN());
    }

    @Test
    @DisplayName("Should throw exception when citizen not found")
    void getCitizen_ShouldThrowExceptionWhenNotFound() {
        when(citizenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            citizenService.getCitizen(999L);
        });
    }

    @Test
    @DisplayName("Should add a new citizen")
    void addCitizen_ShouldSaveCitizen() {
        citizenService.addCitizen(citizen);

        verify(citizenRepository, times(1)).save(citizen);
    }

    @Test
    @DisplayName("Should update an existing citizen")
    void updateCitizen_ShouldUpdateCitizen() {
        citizen.setName("Ivan Petrov Updated");

        citizenService.updateCitizen(citizen);

        verify(citizenRepository, times(1)).save(citizen);
    }

    @Test
    @DisplayName("Should delete citizen by ID")
    void deleteCitizen_ShouldDeleteCitizen() {
        when(citizenRepository.findById(1L)).thenReturn(Optional.of(citizen));

        citizenService.deleteCitizen(1L);

        verify(citizenRepository, times(1)).delete(citizen);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent citizen")
    void deleteCitizen_ShouldThrowExceptionWhenNotFound() {
        when(citizenRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            citizenService.deleteCitizen(999L);
        });
    }

    @Test
    @DisplayName("Should find citizen by unique vote ID and EGN")
    void getCitizenByUniqueVoteIdAndEGN_ShouldReturnCitizen() {
        when(citizenRepository.findByEGNAndUniqueVoteId("VOTE-123-ABC", "8501011234"))
                .thenReturn(citizen);

        Citizen result = citizenService.getCitizenByUniqueVoteIdAndEGN("VOTE-123-ABC", "8501011234");

        assertNotNull(result);
        assertEquals("Ivan Petrov", result.getName());
        assertEquals("VOTE-123-ABC", result.getUniqueVoteId());
    }

    @Test
    @DisplayName("Should return null when citizen not found by credentials")
    void getCitizenByUniqueVoteIdAndEGN_ShouldReturnNullWhenNotFound() {
        when(citizenRepository.findByEGNAndUniqueVoteId("INVALID", "INVALID"))
                .thenReturn(null);

        Citizen result = citizenService.getCitizenByUniqueVoteIdAndEGN("INVALID", "INVALID");

        assertNull(result);
    }
}
