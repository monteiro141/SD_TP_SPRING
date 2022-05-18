package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.Publishers;
import com.example.noticiasquentinhas.entities.Subscribers;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Controller

public class IndexController {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TopicsRepository topicsRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private UserRepository userRepository;

    TestRestTemplate restTemplate;
    URL base;

    @GetMapping(path = "/**")
    public String returnToIndex(Model model) throws MalformedURLException {
        //restTemplate = new TestRestTemplate("user", "password");
        //base = new URL("http://localhost:" + 8080);
        return getUserLoginPage();
    }

    private String isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return "  ";
        }

        return authentication.getAuthorities().toString();
    }

    //@GetMapping("/loginUser")
    public String getUserLoginPage() {
        switch (isAuthenticated().charAt(1)){
            case 'P':
                return "redirect:/publisher/";
            case 'S':
                return "redirect:/subscriber/";
            default:
                return "index";
        }
    }
}
