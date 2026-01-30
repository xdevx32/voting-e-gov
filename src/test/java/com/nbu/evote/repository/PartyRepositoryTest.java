package com.nbu.evote.repository;

import com.nbu.evote.entity.Party;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PartyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PartyRepository partyRepository;

    private Party party;

    @BeforeEach
    void setUp() {
        party = new Party();
        party.setName("Democratic Party");
        party.setNumber("1");
        party.setSlogan("For a better future!");
        party.setImageUrl("http://example.com/party.jpg");
        party.setGroupImageUrl("http://example.com/group.jpg");
    }

    @Test
    @DisplayName("Should save and retrieve party")
    void save_ShouldPersistParty() {
        Party savedParty = partyRepository.save(party);

        assertNotNull(savedParty.getId());
        assertEquals("Democratic Party", savedParty.getName());
    }

    @Test
    @DisplayName("Should find party by name")
    void findByName_ShouldReturnParty() {
        entityManager.persist(party);
        entityManager.flush();

        Party found = partyRepository.findByName("Democratic Party");

        assertNotNull(found);
        assertEquals("Democratic Party", found.getName());
        assertEquals("1", found.getNumber());
    }

    @Test
    @DisplayName("Should return null when party not found by name")
    void findByName_ShouldReturnNullWhenNotFound() {
        Party found = partyRepository.findByName("Non-existent Party");

        assertNull(found);
    }

    @Test
    @DisplayName("Should find party by ID")
    void findById_ShouldReturnParty() {
        Party saved = entityManager.persist(party);
        entityManager.flush();

        Optional<Party> found = partyRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Democratic Party", found.get().getName());
    }

    @Test
    @DisplayName("Should delete party")
    void delete_ShouldRemoveParty() {
        Party saved = entityManager.persist(party);
        entityManager.flush();

        partyRepository.delete(saved);
        entityManager.flush();

        Optional<Party> found = partyRepository.findById(saved.getId());
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Should find all parties")
    void findAll_ShouldReturnAllParties() {
        Party party2 = new Party();
        party2.setName("Republic Party");
        party2.setNumber("2");

        entityManager.persist(party);
        entityManager.persist(party2);
        entityManager.flush();

        Iterable<Party> parties = partyRepository.findAll();
        long count = parties.spliterator().getExactSizeIfKnown();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should update party")
    void update_ShouldModifyParty() {
        Party saved = entityManager.persist(party);
        entityManager.flush();

        saved.setSlogan("New and improved slogan!");
        partyRepository.save(saved);
        entityManager.flush();

        Optional<Party> found = partyRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("New and improved slogan!", found.get().getSlogan());
    }

    @Test
    @DisplayName("Should track ballots count as zero initially")
    void getBallotsCount_ShouldReturnZeroInitially() {
        Party saved = entityManager.persist(party);
        entityManager.flush();

        Optional<Party> found = partyRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(0, found.get().getBallotsCount());
    }
}
