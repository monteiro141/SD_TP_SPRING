package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.NewsForm;
import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import java.util.Optional;

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
        ArrayList<News> newsList = lastTenNews(newsService.listAllNews());
        model.addAttribute("newsList",newsList);
        return "publisher/index";
    }

    public static ArrayList<News> lastTenNews(Iterable<News> currentList){
        ArrayList<News> finalList = new ArrayList<>();
        currentList.forEach((news -> {
            if(finalList.size() < 10)
                finalList.add(news);
            else{
                finalList.remove(0);
                finalList.add(news);
            }
        }));
        return finalList;
    }

    @GetMapping(path = "/publisher/addTopic")
    public String returnToPublisherAddTopic(@RequestParam(name="create", required = false, defaultValue = "none") String name, Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","addTopic");
        model.addAttribute("addTopic",new TopicForm());
        model.addAttribute("create",name);
        return "publisher/index";
    }

    @PostMapping(path = "/publisher/addTopicButton")
    public String creatingANews(@ModelAttribute("addTopic") TopicForm topicForm){
        try{
            topicService.save(topicForm);
        } catch (Exception e){
            return "redirect:/publisher/addTopic?create=fail";
        }
        return "redirect:/publisher/addTopic?create=success";
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
    public String returnToPublisherCreateNews(@RequestParam(name="create", required = false, defaultValue = "none") String name, Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","createNews");
        model.addAttribute("newsForm", new NewsForm());
        model.addAttribute("topicsList",topicService.topicsList());
        model.addAttribute("create",name);
        return "publisher/index";
    }

    @PostMapping("/publisher/createNewsButton")
    public String creatingANews(@ModelAttribute("newsForm") NewsForm newsForm){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            newsService.save(newsForm,
                    userService.search(authentication.getName()),
                    topicService.search(newsForm.getTopic()));
        } catch (Exception e){
            return "redirect:/publisher/createNews?create=fail";
        }
        return "redirect:/publisher/createNews?create=success";
    }

    @GetMapping(path = "/publisher/listNews")
    public String returnToPublisherListNews(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","listNews");
        Iterable<News> newsList = newsService.listNewsUser(authentication.getName());
        model.addAttribute("newsList",newsList);
        return "publisher/index";
    }

    @GetMapping(path = "/publisher/editNews/{id}")
    public String returnToPublisherEditNews(@RequestParam(name="edit", required = false, defaultValue = "none") String name, @PathVariable(value="id") Integer id,Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","editNews");
        model.addAttribute("edit",name);
        Optional<News> optional = newsService.findNew(id);
        News newsEdit = null;
        if(optional.isPresent()){
            newsEdit = optional.get();
        }
        model.addAttribute("theNew",newsEdit);
        return "publisher/index";
    }

    @PostMapping("/publisher/editNewsButton")
    public String saveNew(@ModelAttribute("theNew") News news){
        newsService.saveEditNew(news);
        try{
            newsService.saveEditNew(news);
        } catch (Exception e){
            return "redirect:/publisher/editNews/"+news.getNews_id()+"?edit=fail";
        }
        return "redirect:/";
    }
}
