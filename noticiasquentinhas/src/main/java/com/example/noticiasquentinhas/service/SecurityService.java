package com.example.noticiasquentinhas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    @Autowired
    @Lazy
    AuthenticationManager authManager;

    private Logger logger = LoggerFactory.getLogger(SecurityService.class);

    /**
     * so the login after
     * @param username the user email
     * @param password the user password
     */
    public void doLogin(String username, String password) {

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        Authentication authentication = authManager.authenticate(authRequest);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug(String.format("Auto login successfully!", username));
        }
    }

}
