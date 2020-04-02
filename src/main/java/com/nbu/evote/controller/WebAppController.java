package com.nbu.evote.controller;

import com.nbu.evote.CSVReaderAndParser;
import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyService;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

@Controller
public class WebAppController {
    private String appMode;

    @Autowired
    private PartyService partyService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private BallotService ballotService;

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


        return "index-bs";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        return "admin";
    }

    @RequestMapping(value = "/vote", method= RequestMethod.GET)
    public String vote(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Angel");
        model.addAttribute("mode", appMode);

        ArrayList<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();

        model.addAttribute("parties" , parties);

        return "voting-page";
    }

    @RequestMapping(value = "/vote-success")
    public String voteSuccess(Model model, @ModelAttribute(value="party") Party party){
        model.addAttribute(party);
        System.out.println(party);
        // TODO fix , get the party object properly
        model.addAttribute("test", 1);
        model.addAttribute("party", party);
        return "vote-success";
    }



    @RequestMapping(value = "plsSaveMe")
    public String testSaving() {
            // Finally. this thing works.
        Citizen citizen = citizenService.getCitizen(1);

        Party party = partyService.getParty(412);

        Ballot b = new Ballot();
        b.setParty(party);
        b.setLocalDate(LocalDate.now());
        b.setCitizen(citizen);
        citizen.setBallot(b);
        //???
        HashSet<Ballot> ballotHashSet = new HashSet<>();
        ballotHashSet.add(b);
        party.setBallots(ballotHashSet);
        ballotService.addBallot(b);



        b.setCitizen(citizen);
        citizen.setBallot(b);



        citizenService.addCitizen(citizen);








        Party anotherParty = new Party();
        anotherParty.setName("BSP");
        anotherParty.setNumber("42");
        partyService.addParty(anotherParty);



        Citizen misho = new Citizen();
        misho.setName("MISHO");
        citizenService.addCitizen(misho);
        return "admin";
    }

    @RequestMapping(value = "invokeUpload")
    public String addCSVToDatabase() throws IOException, CsvValidationException {
        CSVReaderAndParser.invoke("template_for_party-realexample.csv");
        return "admin";
    }
}