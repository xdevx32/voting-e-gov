package com.nbu.evote.repository;

import com.nbu.evote.entity.Citizen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CitizenRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CitizenRepository citizenRepository;

    private Citizen citizen;

    @BeforeEach
    void setUp() {
        citizen = new Citizen();
        citizen.setName("Ivan Petrov");
        citizen.setEGN("8501011234");
        citizen.setUniqueVoteId("VOTE-123-ABC");
        citizen.setCity("Sofia");
        citizen.setDayOfBirth(LocalDate.of(1985, 1, 1));
        citizen.setHasVoted(false);
    }

    @Test
    @DisplayName("Should save and retrieve citizen")
    void save_ShouldPersistCitizen() {
        Citizen savedCitizen = citizenRepository.save(citizen);

        assertNotNull(savedCitizen.getId());
        assertEquals("Ivan Petrov", savedCitizen.getName());
    }

    @Test
    @DisplayName("Should find citizen by EGN and UniqueVoteId")
    void findByEGNAndUniqueVoteId_ShouldReturnCitizen() {
        entityManager.persist(citizen);
        entityManager.flush();

        Citizen found = citizenRepository.findByEGNAndUniqueVoteId("VOTE-123-ABC", "8501011234");

        assertNotNull(found);
        assertEquals("Ivan Petrov", found.getName());
        assertEquals("8501011234", found.getEGN());
        assertEquals("VOTE-123-ABC", found.getUniqueVoteId());
    }

    @Test
    @DisplayName("Should return null when citizen not found by credentials")
    void findByEGNAndUniqueVoteId_ShouldReturnNullWhenNotFound() {
        Citizen found = citizenRepository.findByEGNAndUniqueVoteId("INVALID", "INVALID");

        assertNull(found);
    }

    @Test
    @DisplayName("Should return null when only EGN matches")
    void findByEGNAndUniqueVoteId_ShouldReturnNullWhenOnlyEGNMatches() {
        entityManager.persist(citizen);
        entityManager.flush();

        Citizen found = citizenRepository.findByEGNAndUniqueVoteId("WRONG-VOTE-ID", "8501011234");

        assertNull(found);
    }

    @Test
    @DisplayName("Should return null when only UniqueVoteId matches")
    void findByEGNAndUniqueVoteId_ShouldReturnNullWhenOnlyVoteIdMatches() {
        entityManager.persist(citizen);
        entityManager.flush();

        Citizen found = citizenRepository.findByEGNAndUniqueVoteId("VOTE-123-ABC", "WRONG-EGN");

        assertNull(found);
    }

    @Test
    @DisplayName("Should find citizen by ID")
    void findById_ShouldReturnCitizen() {
        Citizen saved = entityManager.persist(citizen);
        entityManager.flush();

        Optional<Citizen> found = citizenRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Ivan Petrov", found.get().getName());
    }

    @Test
    @DisplayName("Should delete citizen")
    void delete_ShouldRemoveCitizen() {
        Citizen saved = entityManager.persist(citizen);
        entityManager.flush();

        citizenRepository.delete(saved);
        entityManager.flush();

        Optional<Citizen> found = citizenRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find all citizens")
    void findAll_ShouldReturnAllCitizens() {
        Citizen citizen2 = new Citizen();
        citizen2.setName("Maria Ivanova");
        citizen2.setEGN("9001019876");
        citizen2.setUniqueVoteId("VOTE-456-DEF");

        entityManager.persist(citizen);
        entityManager.persist(citizen2);
        entityManager.flush();

        Iterable<Citizen> citizens = citizenRepository.findAll();
        long count = citizens.spliterator().getExactSizeIfKnown();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should update citizen")
    void update_ShouldModifyCitizen() {
        Citizen saved = entityManager.persist(citizen);
        entityManager.flush();

        saved.setName("Ivan Petrov Updated");
        citizenRepository.save(saved);
        entityManager.flush();

        Optional<Citizen> found = citizenRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Ivan Petrov Updated", found.get().getName());
    }
}
