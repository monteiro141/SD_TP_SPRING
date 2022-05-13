package com.example.noticiasquentinhas.index;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import com.example.noticiasquentinhas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TopicsRepository topicsRepository;

    @GetMapping(path = "/")
    public String returnToIndex(Model model){

        return "index";
    }

    @GetMapping("/login")
    public String redirectToLogin(Model model){
        return "login";
    }
}
