package com.example.noticiasquentinhas.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Controller

public class IndexController {


    @GetMapping(path = "/")
    public String returnToIndex(Model model) throws MalformedURLException {
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
