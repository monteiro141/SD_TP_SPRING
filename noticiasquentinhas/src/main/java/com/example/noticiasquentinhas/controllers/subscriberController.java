package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
public class subscriberController {

    @Autowired
    private UserService userService;

    @GetMapping(path = "/subscriber/")
    public String returnToSubscriberIndex(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication");
        System.out.println(authentication.getName());
        System.out.println(authentication.getDetails());
        System.out.println(authentication.getPrincipal().toString());
        System.out.println();
        System.out.println(userService.currentUserName(authentication.getName()));
        System.out.println(userService.currentUserRole(authentication.getName()));
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        return "subscriber/index";
    }
}
