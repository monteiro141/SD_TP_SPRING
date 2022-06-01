package com.example.noticiasquentinhas.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorRedirectController implements ErrorController {
    /**
     * Handles with erros case and redirect to index
     * @return the place to redirect
     */
    @RequestMapping("/error")
    public String handleError() {
        return "redirect:/";
    }
}
