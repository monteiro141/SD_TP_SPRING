package com.example.noticiasquentinhas.controllers;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
import java.net.URL;

@Controller
public class loginController {

    @GetMapping(path = "/login")
    public String returnToIndex(Model model) throws MalformedURLException {
        return "login";
    }



}
