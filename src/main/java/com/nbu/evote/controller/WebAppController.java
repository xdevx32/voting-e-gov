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
        citizenService.updateCitizen(currentCitizen);

        return "../static/vote-success";
    }

    @RequestMapping("/barchart")
    public String barChart(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);
        model.addAttribute("mykey", "myvalue");
        ArrayList myList = new ArrayList();
        Party p = partyService.getParty(42);
        myList.add(p);

        Party p2 = partyService.getParty(43);
        myList.add(p2);
        model.addAttribute(myList);

        return "../static/bar-charts";
    }
}