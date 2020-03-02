package com.example.egov.voting.prototype.demo.egov.voting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Date;

@Controller
public class WebAppController {
    private String appMode;

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
}