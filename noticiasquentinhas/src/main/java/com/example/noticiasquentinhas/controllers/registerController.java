package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.forms.UserRegistrationDto;
import com.example.noticiasquentinhas.service.SecurityService;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;

@Controller
public class registerController {

    private UserService userService;

    private SecurityService securityService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public registerController(UserService userService, SecurityService securityService) {
        super();
        this.userService = userService;
        this.securityService = securityService;
    }

    /**
     * Register the new user to the repository
     * @param userRegistrationDto
     * @return
     */
    public String registerOnRepository(UserRegistrationDto userRegistrationDto){
        User user = new User(userRegistrationDto.getFirstName(),userRegistrationDto.getLastName(),userRegistrationDto.getEmail(),passwordEncoder.encode(userRegistrationDto.getPassword()),
                true, userRegistrationDto.getRole());
        try{
            userService.save(userRegistrationDto);
        } catch (Exception e){
            return "redirect:/register?createUser=fail";
        }
        try{
            securityService.doLogin(userRegistrationDto.getEmail(),userRegistrationDto.getPassword());
        } catch (Exception e){
            return "redirect:/register?createUser=fail";
        }
        return "redirect:/";
    }

    /**
     * block the user if it is logged in or return to the register page if not
     * @param name
     * @param model
     * @return
     * @throws MalformedURLException
     */
    @GetMapping(path = "/register")
    public String returnToIndex(@RequestParam(name="createUser", required = false, defaultValue = "none") String name, Model model) throws MalformedURLException {
        return blockUserRegisterPage(model, name);
    }

    /**
     * Save the user in the repository
     * @param userRegistrationDto
     * @return
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("newUser") UserRegistrationDto userRegistrationDto){
        return registerOnRepository(userRegistrationDto);
    }


    /**
     * Check if the user is authenticated
     * @return
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
     * Redirect the user to the correct page
     * @param model
     * @param name
     * @return
     */
    public String blockUserRegisterPage(Model model, String name) {
        switch (isAuthenticated().charAt(1)){
            case 'P':
                return "redirect:/publisher/";
            case 'S':
                return "redirect:/subscriber/";
            default:
                UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
                model.addAttribute("newUser", userRegistrationDto);
                model.addAttribute("createUser",name);
                return "auth/register";
        }
    }
}
