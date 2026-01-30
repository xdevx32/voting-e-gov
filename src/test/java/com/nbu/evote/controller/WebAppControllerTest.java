package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyMemberService;
import com.nbu.evote.service.PartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WebAppController.class)
class WebAppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartyService partyService;

    @MockBean
    private CitizenService citizenService;

    @MockBean
    private PartyMemberService partyMemberService;

    @MockBean
    private BallotService ballotService;

    private Party party;
    private Citizen citizen;
    private Ballot ballot;

    @BeforeEach
    void setUp() {
        party = new Party();
        party.setId(1L);
        party.setName("Democratic Party");
        party.setNumber("1");
        party.setSlogan("For a better future!");

        citizen = new Citizen();
        citizen.setId(1L);
        citizen.setName("Ivan Petrov");
        citizen.setEGN("8501011234");
        citizen.setUniqueVoteId("VOTE-123-ABC");
        citizen.setHasVoted(false);

        ballot = new Ballot();
        ballot.setId(1L);
        ballot.setDate(LocalDate.now());
        ballot.setTime(LocalTime.now());
    }

    @Test
    @DisplayName("Should return index page with parties")
    @WithMockUser
    void index_ShouldReturnIndexPage() throws Exception {
        ArrayList<Party> parties = new ArrayList<>(Arrays.asList(party));
        when(partyService.getAllParties()).thenReturn(parties);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("parties"))
                .andExpect(model().attributeExists("datetime"));
    }

    @Test
    @DisplayName("Should return admin page")
    @WithMockUser
    void admin_ShouldReturnAdminPage() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("datetime"));
    }

    @Test
    @DisplayName("Should return vote page")
    @WithMockUser
    void vote_ShouldReturnVotePage() throws Exception {
        mockMvc.perform(get("/vote"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should validate citizen credentials and return voting page")
    @WithMockUser
    void voteValidated_ShouldValidateCitizen() throws Exception {
        ArrayList<Party> parties = new ArrayList<>(Arrays.asList(party));
        when(partyService.getAllParties()).thenReturn(parties);
        when(citizenService.getCitizenByUniqueVoteIdAndEGN("VOTE-123-ABC", "8501011234"))
                .thenReturn(citizen);

        mockMvc.perform(post("/vote-validated")
                        .with(csrf())
                        .param("uniqueVoteId", "VOTE-123-ABC")
                        .param("EGN", "8501011234"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("parties"))
                .andExpect(model().attributeExists("citizen"));
    }

    @Test
    @DisplayName("Should return party info page")
    @WithMockUser
    void partyInfoPage_ShouldReturnPartyInfo() throws Exception {
        when(partyService.getParty(1L)).thenReturn(party);

        mockMvc.perform(get("/party-info-page")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("party"));
    }

    @Test
    @DisplayName("Should return upload parties page")
    @WithMockUser
    void uploadParties_ShouldReturnUploadPage() throws Exception {
        mockMvc.perform(get("/uploadParties"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return upload citizens page")
    @WithMockUser
    void uploadCitizens_ShouldReturnUploadPage() throws Exception {
        mockMvc.perform(get("/uploadCitizens"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return bar chart page with statistics")
    @WithMockUser
    void barChart_ShouldReturnChartPage() throws Exception {
        ArrayList<Party> parties = new ArrayList<>(Arrays.asList(party));
        when(partyService.getAllParties()).thenReturn(parties);
        when(ballotService.getBallots()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/barchart"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("partiesNamesList"))
                .andExpect(model().attributeExists("ballotsCountList"));
    }

    @Test
    @DisplayName("Should redirect unauthenticated users")
    void index_ShouldRedirectUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Should process vote success")
    @WithMockUser
    void voteSuccess_ShouldProcessVote() throws Exception {
        when(partyService.getParty(anyLong())).thenReturn(party);

        mockMvc.perform(post("/vote-success")
                        .with(csrf())
                        .param("id", "1"))
                .andExpect(status().isOk());
    }
}
