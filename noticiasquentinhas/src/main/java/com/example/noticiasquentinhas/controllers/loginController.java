package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;
import java.util.ArrayList;

@Controller
public class loginController {

    /**
     * verify if the login is already done. If it is redirect to the correct page of the user. if not redirect to the login page
     * @param model
     * @return
     * @throws MalformedURLException
     */
    @GetMapping(path = "/login")
    public String returnToIndex(Model model) throws MalformedURLException {
        return blockUserLoginPage(model);
    }

    /**
     * Check if the user is authenticated
     * @return the role of the user or " " if the user isn't authenticated
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
     * Block the login page depending if the user is authenticated or not
     * @param model
     * @return
     */
    public String blockUserLoginPage(Model model) {
        switch (isAuthenticated().charAt(1)){
            case 'P':
                return "redirect:/publisher/";
            case 'S':
                return "redirect:/subscriber/";
            default:
                return "auth/login";
        }
    }


}
