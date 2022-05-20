package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.repository.PublisherRepository;
import com.example.noticiasquentinhas.repository.SubscriberRepository;
import com.example.noticiasquentinhas.repository.UserRepository;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
import java.util.Arrays;

@Controller
public class registerController {

    private UserService userService;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public registerController(UserService userService) {
        super();
        this.userService = userService;
    }

    public void registerOnRepository(UserRegistrationDto userRegistrationDto){
        User user = new User(userRegistrationDto.getName(),userRegistrationDto.getEmail(),passwordEncoder.encode(userRegistrationDto.getPassword()),
                true, userRegistrationDto.getRole());
        System.out.println("User:");
        System.out.println(user);
        userService.save(userRegistrationDto);
    }

    @GetMapping(path = "/register")
    public String returnToIndex(Model model) throws MalformedURLException {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        System.out.println("Entered");
        model.addAttribute("newUser", userRegistrationDto);
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("newUser") UserRegistrationDto userRegistrationDto){
        registerOnRepository(userRegistrationDto);
        return "redirect:/";
    }
}
