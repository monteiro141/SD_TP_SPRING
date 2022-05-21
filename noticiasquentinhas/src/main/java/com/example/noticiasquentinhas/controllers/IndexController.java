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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Controller

public class IndexController {

    private NewsService newsService;

    public IndexController(NewsService newsService){
        this.newsService=newsService;
    }

    @GetMapping(path = "/")
    public String returnToIndex(Model model) throws MalformedURLException {
        return getUserLoginPage(model);
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
    public String getUserLoginPage(Model model) {
        switch (isAuthenticated().charAt(1)){
            case 'P':
                return "redirect:/publisher/";
            case 'S':
                return "redirect:/subscriber/";
            default:
                ArrayList<News> newsList = publisherController.lastTenNews(newsService.listAllNews());
                model.addAttribute("newsList",newsList);
                return "index";
        }
    }
}
