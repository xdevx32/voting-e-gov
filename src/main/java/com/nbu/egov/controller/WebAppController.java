package com.nbu.egov.controller;

import com.nbu.egov.entity.Party;
import com.nbu.egov.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Date;

@Controller
public class WebAppController {
    private String appMode;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    public WebAppController(Environment environment){
        appMode = environment.getProperty("app-mode");
    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

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
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        //model.addAttribute("parties" , partyRepository.findAll());
        ArrayList<Party> parties = new ArrayList<>();
        parties.add(new Party("Алтернативна социаллиберална партия", "4"));
        parties.add(new Party("Българска социалдемократическа партия", "3"));
        parties.add(new Party("Българска средна класа", "6"));
        parties.add(new Party("БЗНС – Народен съюз", "19"));
        parties.add(new Party("ГЕРБ", "44"));
        parties.add(new Party("Движение „Да, България!“", "22"));
        parties.add(new Party("Национално движение за права и свободи", "12"));
        parties.add(new Party("Национален идеал за единство", "23"));
        parties.add(new Party("Нова зора", "54"));
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
}