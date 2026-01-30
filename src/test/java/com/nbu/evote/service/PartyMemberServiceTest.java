package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import com.nbu.evote.entity.PartyMember;
import com.nbu.evote.repository.PartyMemberRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartyMemberServiceTest {

    @Mock
    private PartyMemberRepository partyMemberRepository;

    @InjectMocks
    private PartyMemberService partyMemberService;

    private PartyMember partyMember;
    private Party party;

    @BeforeEach
    void setUp() {
        party = new Party();
        party.setId(1L);
        party.setName("Democratic Party");

        partyMember = new PartyMember();
        partyMember.setName("John Smith");
        partyMember.setDayOfBirth(LocalDate.of(1970, 5, 15));
        partyMember.setPortraitUrl("http://example.com/portrait.jpg");
        partyMember.setParty(party);
    }

    @Test
    @DisplayName("Should return all party members")
    void getPartyMembers_ShouldReturnAllMembers() {
        PartyMember member2 = new PartyMember();
        member2.setName("Jane Doe");

        when(partyMemberRepository.findAll()).thenReturn(Arrays.asList(partyMember, member2));

        List<PartyMember> result = partyMemberService.getPartyMembers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(partyMemberRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no party members exist")
    void getPartyMembers_ShouldReturnEmptyListWhenNoMembers() {
        when(partyMemberRepository.findAll()).thenReturn(Arrays.asList());

        List<PartyMember> result = partyMemberService.getPartyMembers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return party member by ID")
    void getPartyMember_ShouldReturnMemberById() {
        when(partyMemberRepository.findById(1L)).thenReturn(Optional.of(partyMember));

        PartyMember result = partyMemberService.getPartyMember(1L);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals(LocalDate.of(1970, 5, 15), result.getDayOfBirth());
    }

    @Test
    @DisplayName("Should throw exception when party member not found")
    void getPartyMember_ShouldThrowExceptionWhenNotFound() {
        when(partyMemberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            partyMemberService.getPartyMember(999L);
        });
    }

    @Test
    @DisplayName("Should add a new party member")
    void addPartyMember_ShouldSaveMember() {
        partyMemberService.addPartyMember(partyMember);

        verify(partyMemberRepository, times(1)).save(partyMember);
    }

    @Test
    @DisplayName("Should update an existing party member")
    void updatePartyMember_ShouldUpdateMember() {
        partyMember.setName("John Smith Updated");

        partyMemberService.updatePartyMember(partyMember);

        verify(partyMemberRepository, times(1)).save(partyMember);
    }

    @Test
    @DisplayName("Should delete party member by ID")
    void deletePartyMember_ShouldDeleteMember() {
        when(partyMemberRepository.findById(1L)).thenReturn(Optional.of(partyMember));

        partyMemberService.deletePartyMember(1L);

        verify(partyMemberRepository, times(1)).delete(partyMember);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent party member")
    void deletePartyMember_ShouldThrowExceptionWhenNotFound() {
        when(partyMemberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            partyMemberService.deletePartyMember(999L);
        });
    }

    @Test
    @DisplayName("Should correctly associate party member with party")
    void addPartyMember_ShouldAssociateWithParty() {
        when(partyMemberRepository.findById(1L)).thenReturn(Optional.of(partyMember));

        PartyMember result = partyMemberService.getPartyMember(1L);

        assertNotNull(result.getParty());
        assertEquals("Democratic Party", result.getParty().getName());
    }

    @Test
    @DisplayName("Should have portrait URL")
    void getPartyMember_ShouldHavePortraitUrl() {
        when(partyMemberRepository.findById(1L)).thenReturn(Optional.of(partyMember));

        PartyMember result = partyMemberService.getPartyMember(1L);

        assertEquals("http://example.com/portrait.jpg", result.getPortraitUrl());
    }
}
