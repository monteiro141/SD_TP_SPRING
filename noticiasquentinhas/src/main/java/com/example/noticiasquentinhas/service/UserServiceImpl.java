package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.forms.UserRegistrationDto;
import com.example.noticiasquentinhas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    /**
     * Save a user
     * @param registrationDto the user details
     * @return the user
     */
    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),registrationDto.getLastName(),registrationDto.getEmail(),passwordEncoder.encode(registrationDto.getPassword()),
                true,registrationDto.getRole());

        return userRepository.save(user);
    }

    /**
     * Save a user
     * @param user
     * @return
     */
    @Override
    public User save(User user){
        return userRepository.save(user);
    }

    /**
     * Save a new user's password
     * @param user the user
     * @param password the new password
     * @return the user
     */
    @Override
    public User save(User user, String password){
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    /**
     * Search a user by email
     * @param email the user's email
     * @return the user
     */
    @Override
    public User search(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Load a user by email
     * @param username the user email
     * @return the user details
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRole()));
    }

    /**
     * Return the current user' name
     * @param email
     * @return the user's name
     */
    @Override
    public String currentUserName(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user.getFirstName();
    }

    /**
     * Get current user's role
     * @param email the user email
     * @return the user's role
     */
    @Override
    public String currentUserRole(String email){
        User user = userRepository.findByEmail(email);
        if(user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return user.getRole();
    }

    /**
     * Get all the user's email that subscribed a specific topic
     * @param topicID the topic id
     * @return the list of user's emails
     */
    @Override
    public ArrayList<String> getUsersWithSubscribedTopic(Integer topicID){
        return userRepository.findAllByTopics_subscriber(topicID);
    }


    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String role){
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

}
