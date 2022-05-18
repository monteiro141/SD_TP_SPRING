package com.example.noticiasquentinhas.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.MalformedURLException;

@Controller
public class loginController {

    @GetMapping(path = "/login")
    public String returnToIndex(Model model) throws MalformedURLException {
        return "auth/login";
    }

}
