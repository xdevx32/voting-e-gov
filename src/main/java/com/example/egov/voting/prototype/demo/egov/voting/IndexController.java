package com.example.egov.voting.prototype.demo.egov.voting;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.catalina.UserDatabase;
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

        /* Just running some code */
//
//        //Create the authorities for the user
//
//            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//            //Instantiate the user (with a class implementing UserDetails)
//
//            UserDetails user = new User("user@example.com", "s3cr3t", authorities);
//            //Save the user somewhere useful. The JdbcUserDetailsManager can save a user to a database easily.
//
//        JdbcUserDetailsManager jdbcUserDetailsManager;
//        jdbcUserDetailsManager.addGroupAuthority();
//            //Create a UsernamePasswordAuthenticationToken
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
//            //Add the Authentication to the SecurityContext
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);

        /* end "some code" */
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
