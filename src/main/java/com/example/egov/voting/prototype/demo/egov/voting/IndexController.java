package com.example.egov.voting.prototype.demo.egov.voting;

import java.util.TimeZone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String showIndex(Model model) {
        // Set default GMT+0:00 time zone
        TimeZone timeZone;
        timeZone = TimeZone.getTimeZone("GMT+0:00");
        TimeZone.setDefault(timeZone);
        return "index";
    }

    // Cyrilic support
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
