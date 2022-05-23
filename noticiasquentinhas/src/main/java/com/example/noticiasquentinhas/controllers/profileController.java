package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.TopicForm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

@Controller
public class profileController {

    public profileController(){

    }

    @GetMapping(path = "/profile")
    public String returnToPublisherAddTopic(Model model) throws MalformedURLException {
        return getUserProfilePage();
    }

    private String isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return "  ";
        }

        return authentication.getAuthorities().toString();
    }

    private String getUserProfilePage() {
        return switch (isAuthenticated().charAt(1)) {
            case 'P' -> "redirect:/publisher/profile";
            case 'S' -> "redirect:/subscriber/profile";
            default -> "index";
        };
    }
}
