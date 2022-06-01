package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.service.NewsService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Controller

public class IndexController {

    private NewsService newsService;

    /**
     * Constructor for the index controller
     * @param newsService
     */
    public IndexController(NewsService newsService){
        this.newsService=newsService;
    }

    /**
     * Return the user to index page
     * @param model
     * @return the place to redirect
     * @throws MalformedURLException
     */
    @GetMapping(path = "/")
    public String returnToIndex(Model model) throws MalformedURLException {
        return getUserLoginPage(model);
    }

    /**
     * Verify if a user is authenticated
     * @return the class of the user(Subscriber, Publisher or " " if the user isn't authenticated)
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
     * Redirect the user to the specific login page
     * @param model
     * @return the page to redirect
     */
    public String getUserLoginPage(Model model) {
        switch (isAuthenticated().charAt(1)){
            case 'P':
                return "redirect:/publisher/";
            case 'S':
                return "redirect:/subscriber/";
            default:
                ArrayList<News> newsList = publisherController.lastTenNews(newsService.listAllNews());
                Collections.reverse(newsList);
                model.addAttribute("newsList",newsList);
                //return "index";
                return "redirect:/0";
        }
    }
}
