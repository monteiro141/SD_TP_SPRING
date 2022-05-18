package com.example.noticiasquentinhas.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
public class subscriberController {

    @GetMapping(path = "/subscriber/")
    public String returnToSubscriberIndex(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication");
        System.out.println(authentication.getName());
        System.out.println(authentication.getDetails());
        System.out.println(authentication.getPrincipal().toString());
        System.out.println();
        model.addAttribute("loggedInUser",((UserDetails) authentication.getPrincipal()));
        return "subscriber/index";
    }
}
