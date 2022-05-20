package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;

@Controller
public class publisherController {

    private UserService userService;


    private NewsService newsService;


    private TopicService topicService;


    public publisherController(UserService userService, NewsService newsService, TopicService topicService) {
        this.userService = userService;
        this.newsService = newsService;
        this.topicService = topicService;
    }

    @GetMapping(path = "/publisher/")
    public String returnToPublisherIndex(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","home");
        return "publisher/index";
    }

    @GetMapping(path = "/publisher/addTopic")
    public String returnToPublisherAddTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","addTopic");
        model.addAttribute("addTopic",new TopicForm());
        return "publisher/index";
    }

    @PostMapping(path = "/publisher/addTopic")
    public String creatingANews(@ModelAttribute("addTopic") TopicForm topicForm){
        System.out.println("Topic:");
        System.out.println(topicForm.getName());
        topicService.save(topicForm);
        return "redirect:/";
    }


    @GetMapping(path = "/publisher/searchTopic")
    public String returnToPublishersearchTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","searchTopic");
        model.addAttribute("listTopics", (topicService.topicsList()));
        return "publisher/index";
    }

    @GetMapping(path = "/publisher/createNews")
    public String returnToPublisherCreateNews(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","createNews");
        model.addAttribute("newsForm", new NewsForm());
        model.addAttribute("topicsList",topicService.topicsList());
        return "publisher/index";
    }

    @PostMapping("/publisher/createNews")
    public String creatingANews(@ModelAttribute("newsForm") NewsForm newsForm){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        newsService.save(newsForm,
                userService.search(userService.currentUserName(authentication.getName())),
                topicService.search(newsForm.getTopic()));
        return "redirect:/";
    }


    @GetMapping(path = "/publisher/listNews")
    public String returnToPublisherListNews(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","listNews");
        return "publisher/index";
    }




}
