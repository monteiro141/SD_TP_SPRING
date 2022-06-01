package com.example.noticiasquentinhas.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
public class profileController {

    public profileController(){

    }

    /**
     * Redirect to the profile page
     * @param model
     * @return
     * @throws MalformedURLException
     */
    @GetMapping(path = "/profile")
    public String returnToPublisherAddTopic(Model model) throws MalformedURLException {
        return getUserProfilePage();
    }

    /**
     * Check if the user is authenticated
     * @return the user role or " " if the user doesn't has one
     */
    private String isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return "  ";
        }

        return authentication.getAuthorities().toString();
    }

    /**
     * Check the role of the user and redirect to the correct profile page
     * @return the place to redirect
     */
    private String getUserProfilePage() {
        return switch (isAuthenticated().charAt(1)) {
            case 'P' -> "redirect:/publisher/profile";
            case 'S' -> "redirect:/subscriber/profile";
            default -> "index";
        };
    }
}
