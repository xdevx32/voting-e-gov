package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.exception.CitizenNotFoundException;
import com.nbu.evote.exception.DuplicateVoteException;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyMemberService;
import com.nbu.evote.service.PartyService;
import com.nbu.evote.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Controller
public class WebAppController {

    private static final Logger logger = LoggerFactory.getLogger(WebAppController.class);

    private Party selectedParty;
    private Party clickedParty;
    private String appMode;

    private final PartyService partyService;
    private final CitizenService citizenService;
    private final PartyMemberService partyMemberService;
    private final BallotService ballotService;
    private final VotingService votingService;

    // Thread-safe storage for current citizen during voting session
    private Citizen currentCitizen;

    @Autowired
    public WebAppController(Environment environment, PartyService partyService,
                           CitizenService citizenService, PartyMemberService partyMemberService,
                           BallotService ballotService, VotingService votingService) {
        this.appMode = environment.getProperty("app-mode");
        this.partyService = partyService;
        this.citizenService = citizenService;
        this.partyMemberService = partyMemberService;
        this.ballotService = ballotService;
        this.votingService = votingService;
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String index(Model model, @ModelAttribute Party party) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Guest");
        model.addAttribute("mode", appMode);

        List<Party> parties = partyService.getAllParties();
        parties.sort(Comparator.comparing(Party::getNumber));
        model.addAttribute("parties", parties);
        clickedParty = party;

        logger.info("Index page accessed, displaying {} parties", parties.size());
        return "../static/index-bs";
    }

    @RequestMapping(value = "/party-info-page", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String setClickedParty(Model model, @ModelAttribute Party party) {
        party = partyService.getParty(party.getId());
        clickedParty = party;
        model.addAttribute("party", party);
        logger.info("Party info page accessed for party: {}", party.getName());
        return "../static/party-info-page";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Admin");
        model.addAttribute("mode", appMode);
        logger.info("Admin dashboard accessed");
        return "../static/admin";
    }

    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public String vote(Model model) {
        logger.info("Vote page accessed");
        return "../static/voting-page";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "../static/login";
    }

    @RequestMapping(value = "vote-validated", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String detailRefresh(Model model, Citizen citizen, RedirectAttributes redirectAttributes) {
        ArrayList<Party> parties = partyService.getAllParties();
        model.addAttribute("parties", parties);
        model.addAttribute("party", new Party());

        if (citizen == null || citizen.getUniqueVoteId() == null || citizen.getEGN() == null) {
            model.addAttribute("error", "Please provide both EGN and Unique Vote ID");
            return "../static/voting-page";
        }

        try {
            // Validate citizen using the VotingService (includes duplicate vote check)
            Citizen validatedCitizen = votingService.validateCitizen(
                citizen.getUniqueVoteId(),
                citizen.getEGN()
            );

            model.addAttribute("citizen", validatedCitizen);
            currentCitizen = validatedCitizen;
            logger.info("Citizen validated successfully: {}", validatedCitizen.getName());

        } catch (CitizenNotFoundException e) {
            logger.warn("Citizen validation failed - not found: EGN={}", citizen.getEGN());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("citizen", null);
        } catch (DuplicateVoteException e) {
            logger.warn("Duplicate vote attempt detected: EGN={}", citizen.getEGN());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("citizen", null);
        }

        return "../static/voting-page";
    }

    @RequestMapping(method = RequestMethod.POST, value = "vote-success")
    public String voteSuccess(Model model, @ModelAttribute Party party, RedirectAttributes redirectAttributes) {
        try {
            if (currentCitizen == null) {
                logger.error("Vote attempt without validated citizen");
                redirectAttributes.addFlashAttribute("error", "Please validate your credentials first");
                return "redirect:/vote";
            }

            // Use VotingService to cast vote with duplicate prevention
            Ballot ballot = votingService.castVote(currentCitizen, party.getId());

            selectedParty = partyService.getParty(party.getId());
            model.addAttribute("party", selectedParty);

            logger.info("Vote cast successfully by citizen {} for party {}",
                currentCitizen.getName(), selectedParty.getName());

            // Clear current citizen after successful vote
            currentCitizen = null;

            return "../static/vote-success-animation";

        } catch (DuplicateVoteException e) {
            logger.warn("Duplicate vote prevented: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "../static/error";
        } catch (Exception e) {
            logger.error("Error casting vote: {}", e.getMessage());
            model.addAttribute("error", "An error occurred while processing your vote. Please try again.");
            return "../static/error";
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "vote-successs")
    public String voteSuccesss(Model model) {
        model.addAttribute("party", selectedParty);
        return "../static/vote-success";
    }

    @RequestMapping("/uploadParties")
    public String uploadParties(Model model) {
        return "../static/normal-table-parties";
    }

    @RequestMapping("/uploadCitizens")
    public String uploadCitizens(Model model) {
        return "../static/normal-table-citizens";
    }

    @RequestMapping("/barchart")
    public String barChart(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("mode", appMode);

        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .sorted(Comparator.comparing(Party::getBallotsCount, Comparator.reverseOrder()))
                .map(Party::getName)
                .collect(toList());

        List<Integer> partyBallotsCountList = partiesList.stream()
                .map(Party::getBallotsCount)
                .sorted(Comparator.reverseOrder())
                .collect(toList());

        List<Ballot> ballotsList = ballotService.getBallots();

        // Process ballots for visualization (demo data manipulation)
        List<Ballot> heads = ballotsList.stream()
                .filter(i -> i.getId() % 2 == 0)
                .collect(toList());
        heads.forEach(f -> f.setDate(LocalDate.now()));

        List<Ballot> tails = ballotsList.stream()
                .filter(i -> i.getId() % 2 != 0)
                .collect(toList());
        tails.forEach(f -> f.setDate(LocalDate.now().minusYears(1)));

        ballotsList = Stream.concat(heads.stream(), tails.stream()).collect(toList());

        LocalDate currentYear = LocalDate.now();
        LocalDate lastYear = LocalDate.now().minusYears(1);

        List<LocalTime> voteTimeListFirstDay = ballotsList.stream()
                .filter(b -> b.getDate().isEqual(currentYear))
                .map(Ballot::getTime)
                .collect(toList());

        List<LocalTime> voteTimeListSecondDay = ballotsList.stream()
                .filter(b -> b.getDate().isEqual(lastYear))
                .map(Ballot::getTime)
                .collect(toList());

        // Process vote counts by hour
        List<String> voteTimeListCurrentYearStringsSorted = processVotesByHour(voteTimeListFirstDay);
        List<String> voteTimeListPreviousYearStringsSorted = processVotesByHour(voteTimeListSecondDay);

        // Prepare pie chart data
        HashMap<Integer, String> pieChartData = new HashMap<>();
        for (int i = 0; i < partyBallotsCountList.size(); i++) {
            pieChartData.put(partyBallotsCountList.get(i), partyNamesList.get(i));
        }

        Integer totalBallotsCastedForSection = ballotService.getBallots().size();
        logger.info("Total ballots for section: {}", totalBallotsCastedForSection);

        model.addAttribute("partiesNamesList", partyNamesList);
        model.addAttribute("ballotsCountList", partyBallotsCountList);
        model.addAttribute("ballotsTimelineListFirstDay", voteTimeListCurrentYearStringsSorted);
        model.addAttribute("ballotsTimelineListSecondDay", voteTimeListPreviousYearStringsSorted);
        model.addAttribute("dateOfVoteFromBackend", currentYear.toString());
        model.addAttribute("totalBallotsCastedForSection", totalBallotsCastedForSection);
        model.addAttribute("pieChartData", pieChartData);

        return "../static/bar-charts";
    }

    /**
     * Helper method to process votes by hour for chart visualization.
     */
    private List<String> processVotesByHour(List<LocalTime> voteTimes) {
        HashMap<String, Integer> voteCountByHour = new HashMap<>();
        for (int hour = 9; hour <= 21; hour++) {
            voteCountByHour.put(String.valueOf(hour), 0);
        }

        for (LocalTime time : voteTimes) {
            int hour = time.getHour();
            if (hour >= 9 && hour <= 21) {
                String hourKey = String.valueOf(hour);
                voteCountByHour.put(hourKey, voteCountByHour.get(hourKey) + 1);
            }
        }

        voteCountByHour.put("09", voteCountByHour.remove("9"));
        TreeMap<String, Integer> sorted = new TreeMap<>(voteCountByHour);

        List<String> result = new ArrayList<>();
        for (Integer value : sorted.values()) {
            result.add(String.valueOf(value));
        }
        return result;
    }
}
