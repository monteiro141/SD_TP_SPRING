package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.TopicFormSearch;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class nonSubscriberController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private TopicService topicService;

    @GetMapping("/{id}")
    public String Index(Model model,@PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        model.addAttribute("linkPath","home");
        id = id == null? 0: id;
        model.addAttribute("idPage",id);
        model.addAttribute("newsList",newsService.getNews(id,10));
        model.addAttribute("hasNextPage",newsService.getNews(id+1,10).size());
        return "index";
    }

    @GetMapping({"/news/{id}", "/news","/news/{id}/{page}"})
    public String returnToSubscriberNews(@PathVariable(value="id",required = false) Integer id,
                                         @PathVariable(value="page",required = false) Integer page,
                                         @RequestParam(name="date1", required = false, defaultValue = "none") String date1,
                                         @RequestParam(name="date2", required = false, defaultValue = "none") String date2,
                                         @RequestParam(name="valid", required = false, defaultValue = "none") String validation,
                                         Model model) throws MalformedURLException {
        model.addAttribute("linkPath","news");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicsSize", topicsList.size());
        model.addAttribute("topicToSearch", new TopicFormSearch());
        if(validation.equals("true")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime datetime1 = LocalDateTime.parse(date1,formatter);
            LocalDateTime datetime2 = LocalDateTime.parse(date2,formatter);

            page = page == null? 0 : page;
            List<News> newsFromTimeStamp = newsService.getNewsFromTimestamp(topicService.getTopicByID(id),datetime1,datetime2,page,10);

            model.addAttribute("idPage",page);
            model.addAttribute("hasNextPage",newsService.getNewsFromTimestamp(topicService.getTopicByID(id),datetime1,datetime2,page+1,10).size());
            model.addAttribute("partLink", "date1="+date1+"&date2="+date2+"&valid=true");
            model.addAttribute("newsFromTimeStamp",newsFromTimeStamp);
            model.addAttribute("newsFromTimeStampSize",newsFromTimeStamp.size());
        }
        model.addAttribute("validation",validation);
        return "index";
    }

    @PostMapping("/news")
    public String getSubscribedTopicsForm(@ModelAttribute("checkedTopics") TopicFormSearch topicFormsearch) throws MalformedURLException {
        try{
            topicFormsearch.setDate1(topicFormsearch.getDate1().replace("T"," "));
            topicFormsearch.setDate2(topicFormsearch.getDate2().replace("T"," "));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime datetime1 = LocalDateTime.parse(topicFormsearch.getDate1(),formatter);
            LocalDateTime datetime2 = LocalDateTime.parse(topicFormsearch.getDate2(),formatter);
            String stringDate1 = topicFormsearch.getDate1();
            String stringDate2 = topicFormsearch.getDate2();
            if(datetime1.isBefore(datetime2))
                return "redirect:/news/"+ topicService.search(topicFormsearch.getName()).getTopic_id()+"?date1="+stringDate1+"&date2="+stringDate2+"&valid=true";
        }catch (Exception e){
            return "redirect:/";
        }
        return "redirect:/news?valid=false";
    }

    @GetMapping({"/lastNews/{id}", "/lastNews"})
    public String returnToSubscriberLastNews(Model model, @PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("linkPath","lastNews");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicsListSize", topicsList.size());
        model.addAttribute("topicForm", new TopicForm());

        if(id!=null){
            News lastNews= newsService.getLastNewsFromTopic(id);
            if(lastNews != null) {
                model.addAttribute("lastNew", lastNews);
                model.addAttribute("validation", "true");
            }else{
                model.addAttribute("validation", "false");
            }

        }
        return "index";
    }

    @PostMapping("/lastNews")
    public String returnToSubscriberLastNews(@ModelAttribute("checkedTopics") TopicForm topicForm) throws MalformedURLException {
        if(!topicForm.getName().equals(""))
            return "redirect:/lastNews/" + topicService.search(topicForm.getName()).getTopic_id();
        return "redirect:/";
    }
}
