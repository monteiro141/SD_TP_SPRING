package com.example.noticiasquentinhas.index;

import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.repository.PublisherRepository;
import com.example.noticiasquentinhas.repository.SubscriberRepository;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private TopicsRepository topicsRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private SubscriberRepository subscriberRepository;

    @GetMapping(path = "/")
    public String returnToIndex(Model model){

        return "index";
    }

    @GetMapping("/login")
    public String redirectToLogin(Model model){
        return "login";
    }
}
