package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
public class publisherController {

    @GetMapping(path = "/publisher/")
    public String returnToPublisherIndex(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",((UserDetails) authentication.getPrincipal()));
        return "publisher/index";
    }

}
