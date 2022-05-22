package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.TopicFormSubscriber;
import com.example.noticiasquentinhas.entities.Topics;
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

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Controller
public class subscriberController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private TopicService topicService;

    public subscriberController(UserService userService, NewsService newsService, TopicService topicService) {
        this.userService = userService;
        this.newsService = newsService;
        this.topicService=topicService;
    }



    @GetMapping(path = "/subscriber/")
    public String returnToSubscriberIndex(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","home");
        ArrayList<News> newsList = publisherController.lastTenNews(newsService.listAllNews());
        Collections.reverse(newsList);
        model.addAttribute("newsList",newsList);
        return "subscriber/index";
    }



    @GetMapping("/subscriber/topic")
    public String returnToSubscriberTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","topic");
        ArrayList<Topics> topicsList = topicService.getUnsubscribedTopics(userService.search(authentication.getName()));
        model.addAttribute("topicslist", topicsList);
        model.addAttribute("checkedTopics", new TopicFormSubscriber());
        model.addAttribute("topicsSize", topicsList.size());
        System.out.println(topicsList.toString());
        return "subscriber/index";
    }

    @PostMapping("/subscriber/topic")
    public String getSubscribedTopicsForm(@ModelAttribute("checkedTopics") TopicFormSubscriber topicFormSubscriber) throws MalformedURLException {
        System.out.println(Arrays.toString(topicFormSubscriber.getName()));
        System.out.println(topicService.getTopicsByName(topicFormSubscriber));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (Topics t: topicService.getTopicsByName(topicFormSubscriber)) {
            t.addSubscriber(userService.search(authentication.getName()));
            topicService.save(t);
        }

        return "redirect:/";
    }

    @GetMapping("/subscriber/news")
    public String returnToSubscriberNews(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","news");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicsSize", topicsList.size());
        model.addAttribute("topicToSearch", new TopicForm());
        return "subscriber/index";
    }

    @GetMapping("/subscriber/lastNews")
    public String returnToSubscriberLastNews(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","lastNews");

        return "subscriber/index";
    }

    @GetMapping("/subscriber/removeTopic")
    public String returnToSubscriberRemoveTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","removeTopic");
        ArrayList<Topics> subscribedTopics = topicService.getSubscribedTopics(userService.search(authentication.getName()));
        model.addAttribute("subscribedTopics", subscribedTopics);
        model.addAttribute("removeTopic", new TopicForm());
        model.addAttribute("topicsSize", subscribedTopics.size());
        return "subscriber/index";
    }

    @PostMapping("/subscriber/removeTopic")
    public String getSubscribedTopicsForm(@ModelAttribute("removeTopic") TopicForm topicForm) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Topics topicToUnsubscribe = topicService.search(topicForm.getName());
        topicToUnsubscribe.removeSubscriber(userService.search(authentication.getName()));
        topicService.save(topicToUnsubscribe);

        return "redirect:/";
    }



}
