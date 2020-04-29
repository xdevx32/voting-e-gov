package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebAppController {
    private String appMode;

    @Autowired
    private PartyService partyService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private BallotService ballotService;

    Citizen currentCitizen;

    @Autowired
    public WebAppController(Environment environment){
        appMode = environment.getProperty("app-mode");
    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        ArrayList<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();

        model.addAttribute("parties" , parties);


        return "../static/index-bs";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        return "../static/admin";
    }

    @RequestMapping(value = "/vote", method= RequestMethod.GET)
    public String vote(Model model, Citizen citizen){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Angel");
        model.addAttribute("mode", appMode);

        citizen = new Citizen();

        return "../static/voting-page";
    }


    @RequestMapping(value = "vote-validated", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST} )
    public String detailRefresh(Model model, Citizen citizen) {

        ArrayList<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();
        Citizen real_citizen = new Citizen();
        if(citizen != null) {
            real_citizen = this.citizenService.getCitizenByUniqueVoteIdAndEGN(citizen.getUniqueVoteId(), citizen.getEGN());
        }

        Party party = new Party();
        model.addAttribute("parties" , parties);
        model.addAttribute("party", party);
        model.addAttribute("citizen", real_citizen);
        currentCitizen = real_citizen;

        return "../static/voting-page";
    }


    @RequestMapping(method = RequestMethod.POST, value = "vote-success")
    public String voteSuccess(Model model, @ModelAttribute Party party){
        party = partyService.getParty(party.getId());
        model.addAttribute("party", party);

        Ballot ballot = new Ballot();

        ballot.setParty(party);
        ballot.setCitizen(currentCitizen);
        ballot.setDate(LocalDate.now().plusDays(1));
        ballot.setTime(LocalTime.now().plusHours(2));
        ballotService.addBallot(ballot);

        assert currentCitizen != null;
        currentCitizen.setBallot(ballot);
        party.addBallot(ballot);
        citizenService.updateCitizen(currentCitizen);

        return "../static/vote-success";
    }

    @RequestMapping("/barchart")
    public String barChart(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("mode", appMode);
        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .map(Party::getName)
                .collect(Collectors.toList());

        List<Integer> partyBallotsCountList = partiesList.stream()
                .map(Party::getBallotsCount)
                .collect(Collectors.toList());

//        //test
//        List<Integer> partyBallotsCountList = new ArrayList<>();
//        partyBallotsCountList.add(400);
//        partyBallotsCountList.add(1500);
//        partyBallotsCountList.add(700);
//        partyBallotsCountList.add(345);
//        partyBallotsCountList.add(420);
//        partyBallotsCountList.add(100);

        model.addAttribute("partiesNamesList", partyNamesList);
        model.addAttribute("ballotsCountList", partyBallotsCountList);

        return "../static/bar-charts";
    }
}