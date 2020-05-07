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
import java.util.*;
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

        List<Ballot> ballotsList = ballotService.getBallots();

        //Hardcoded values of section days
        LocalDate firstDay = LocalDate.now();
        LocalDate secondDay = LocalDate.now().plusDays(1);


        List<LocalTime> voteTimeListFirstDay = ballotsList.stream()
                .filter(b -> b.getDate().isEqual(firstDay))
                .map(Ballot::getTime)
                .collect(Collectors.toList());

        List<String> voteTimeListFirstDayStrings = voteTimeListFirstDay.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());

        List<LocalTime> voteTimeListSecondDay = ballotsList.stream()
                .filter(b -> b.getDate().isEqual(secondDay))
                .map(Ballot::getTime)
                .collect(Collectors.toList());


        List<String> voteTimeListSecondDayStrings = voteTimeListSecondDay.stream()
                .map(LocalTime::toString)
                .collect(Collectors.toList());


        // Assigning values to list of hours
        // Example
        // labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"],
        // Output list should look like:
        // data: [16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]

        HashMap<String, Integer> voteCountForFirstDayInHoursFormatted = new HashMap<>();
        voteCountForFirstDayInHoursFormatted.put("9",0);
        voteCountForFirstDayInHoursFormatted.put("10",0);
        voteCountForFirstDayInHoursFormatted.put("11",0);
        voteCountForFirstDayInHoursFormatted.put("12",0);
        voteCountForFirstDayInHoursFormatted.put("13",0);
        voteCountForFirstDayInHoursFormatted.put("14",0);
        voteCountForFirstDayInHoursFormatted.put("15",0);
        voteCountForFirstDayInHoursFormatted.put("16",0);
        voteCountForFirstDayInHoursFormatted.put("17",0);
        voteCountForFirstDayInHoursFormatted.put("18",0);
        voteCountForFirstDayInHoursFormatted.put("19",0);
        voteCountForFirstDayInHoursFormatted.put("20",0);
        voteCountForFirstDayInHoursFormatted.put("21",0);

        HashMap<String, Integer> voteCountForSecondDayInHoursFormatted = new HashMap<>();
        voteCountForSecondDayInHoursFormatted.put("9",0);
        voteCountForSecondDayInHoursFormatted.put("10",0);
        voteCountForSecondDayInHoursFormatted.put("11",0);
        voteCountForSecondDayInHoursFormatted.put("12",0);
        voteCountForSecondDayInHoursFormatted.put("13",0);
        voteCountForSecondDayInHoursFormatted.put("14",0);
        voteCountForSecondDayInHoursFormatted.put("15",0);
        voteCountForSecondDayInHoursFormatted.put("16",0);
        voteCountForSecondDayInHoursFormatted.put("17",0);
        voteCountForSecondDayInHoursFormatted.put("18",0);
        voteCountForSecondDayInHoursFormatted.put("19",0);
        voteCountForSecondDayInHoursFormatted.put("20",0);
        voteCountForSecondDayInHoursFormatted.put("21",0);

        Integer startHour = 9;


        for ( int index=0 ; index<voteTimeListFirstDayStrings.size() ; index++) {

            if (voteTimeListFirstDay.get(index).getHour() == startHour) {

                Integer finalStartHour = startHour;
                Integer numberOfVotesForExactHour = voteTimeListFirstDay.stream()
                        .filter(p -> p.getHour() == finalStartHour)
                        .map(LocalTime::getHour)
                        .reduce(0, (a, b) -> a + b)/startHour;

                if(startHour < 10){
                    String stringToPut = "0".concat(String.valueOf(startHour));
                    voteCountForFirstDayInHoursFormatted.put(stringToPut , numberOfVotesForExactHour);

                } else {
                    voteCountForFirstDayInHoursFormatted.put(String.valueOf(startHour),numberOfVotesForExactHour);
                }
            }

//            if (startHour == 13) {
//                startHour += 2;
//            } else {
                startHour++;
//            }
        }


        HashMap<String, String> map = new HashMap<String, String>();

        voteCountForFirstDayInHoursFormatted.put("09", voteCountForFirstDayInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        TreeMap<String, Integer> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForFirstDayInHoursFormatted);
        Collection<Integer> values = sorted.values();

        List<String> voteTimeListFirstDayStringsSorted = new ArrayList<>();
        ArrayList<Integer> listOfValues = new ArrayList<Integer>(values);
        for (Integer value: listOfValues) {
            voteTimeListFirstDayStringsSorted.add(String.valueOf(value));
        }



        // Second day

        startHour = 9;


        for ( int index=0 ; index<voteTimeListSecondDayStrings.size() ; index++) {

            if (voteTimeListSecondDay.get(index).getHour() == startHour) {

                Integer finalStartHour = startHour;
                Integer numberOfVotesForExactHour = voteTimeListSecondDay.stream()
                        .filter(p -> p.getHour() == finalStartHour)
                        .map(LocalTime::getHour)
                        .reduce(0, (a, b) -> a + b)/startHour;

                if(startHour < 10){
                    String stringToPut = "0".concat(String.valueOf(startHour));
                    voteCountForSecondDayInHoursFormatted.put(stringToPut , numberOfVotesForExactHour);

                } else {
                    voteCountForSecondDayInHoursFormatted.put(String.valueOf(startHour),numberOfVotesForExactHour);
                }
            }

//            if (startHour == 13) {
//                startHour += 2;
//            } else {
                startHour++;
//            }
        }


        map = new HashMap<String, String>();

        voteCountForSecondDayInHoursFormatted.put("09", voteCountForSecondDayInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForSecondDayInHoursFormatted);
        values = sorted.values();

        List<String> voteTimeListSecondDayStringsSorted = new ArrayList<>();
        listOfValues = new ArrayList<Integer>(values);
        for (Integer value: listOfValues) {
            voteTimeListSecondDayStringsSorted.add(String.valueOf(value));
        }

        //End second day


        model.addAttribute("partiesNamesList", partyNamesList);
        model.addAttribute("ballotsCountList", partyBallotsCountList);
        model.addAttribute("ballotsTimelineListFirstDay", voteTimeListFirstDayStringsSorted);
        model.addAttribute("ballotsTimelineListSecondDay", voteTimeListSecondDayStringsSorted);

        return "../static/bar-charts";
    }
}