package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.repository.PublisherRepository;
import com.example.noticiasquentinhas.repository.SubscriberRepository;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Controller
public class IndexController {
    @Autowired
    public NewsRepository newsRepository;
    @Autowired
    public TopicsRepository topicsRepository;
    @Autowired
    public PublisherRepository publisherRepository;
    @Autowired
    public SubscriberRepository subscriberRepository;

    TestRestTemplate restTemplate;
    URL base;

    @GetMapping(path = "/**")
    public String returnToIndex(Model model) throws MalformedURLException {
        //restTemplate = new TestRestTemplate("user", "password");
        //base = new URL("http://localhost:" + 8080);
        return "index";
    }

    @GetMapping(path = "/publisherHome")
    public String returnPublisherHome(Model model) throws MalformedURLException {
        //restTemplate = new TestRestTemplate("user", "password");
        //base = new URL("http://localhost:" + 8080);
        return "publisherHome";
    }

    public NewsRepository getNewsRepository() {
        return newsRepository;
    }

    public void setNewsRepository(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public TopicsRepository getTopicsRepository() {
        return topicsRepository;
    }

    public void setTopicsRepository(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    public PublisherRepository getPublisherRepository() {
        return publisherRepository;
    }

    public void setPublisherRepository(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public SubscriberRepository getSubscriberRepository() {
        return subscriberRepository;
    }

    public void setSubscriberRepository(SubscriberRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }
}
