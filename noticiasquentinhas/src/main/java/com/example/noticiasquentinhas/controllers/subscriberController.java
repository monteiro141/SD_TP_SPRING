package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        return "subscriber/index";
    }

    @PostMapping("/subscriber/topic")
    public String getSubscribedTopicsForm(@ModelAttribute("checkedTopics") TopicFormSubscriber topicFormSubscriber) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (Topics t: topicService.getTopicsByName(topicFormSubscriber)) {
            t.addSubscriber(userService.search(authentication.getName()));
            topicService.save(t);
        }

        return "redirect:/";
    }

    @GetMapping("/subscriber/news")
    public String returnToSubscriberNews(@RequestParam(name="topicName", required = false, defaultValue = "none") String topic,
            @RequestParam(name="date1", required = false, defaultValue = "none") String date1,
            @RequestParam(name="date2", required = false, defaultValue = "none") String date2,
            @RequestParam(name="valid", required = false, defaultValue = "none") String validation,
            Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        model.addAttribute("linkPath","news");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicsSize", topicsList.size());
        model.addAttribute("topicToSearch", new TopicFormSearch());
        if(validation.equals("true")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime datetime1 = LocalDateTime.parse(date1,formatter);
            LocalDateTime datetime2 = LocalDateTime.parse(date2,formatter);
            ArrayList<News> newsFromTimeStamp = newsService.getNewsFromTimestamp(topicService.search(topic),datetime1,datetime2);
            model.addAttribute("newsFromTimeStamp",newsFromTimeStamp);
            model.addAttribute("newsFromTimeStampSize",newsFromTimeStamp.size());
        }
        model.addAttribute("validation",validation);
        return "subscriber/index";
    }

    @PostMapping("/subscriber/news")
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
                return "redirect:/subscriber/news?"+"topicName="+topicFormsearch.getName()+"&date1="+stringDate1+"&date2="+stringDate2+"&valid=true";
        }catch (Exception e){
            return "redirect:/";
        }
        return "redirect:/subscriber/news?valid=false";
    }

    @GetMapping("/subscriber/lastNews")
    public String returnToSubscriberLastNews(Model model,
             @RequestParam(name="topic", required = false, defaultValue = "none") String topic) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","lastNews");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicForm", new TopicForm());
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        if(!topic.equals("none")){
            News lastNews= newsService.getLastNewsFromTopic(topic);
            if(lastNews != null) {
                model.addAttribute("lastNew", lastNews);
                model.addAttribute("validation", "true");
            }else{
                model.addAttribute("validation", "false");
            }

        }
        return "subscriber/index";
    }

    @PostMapping("/subscriber/lastNews")
    public String returnToSubscriberLastNews(@ModelAttribute("checkedTopics") TopicForm topicForm) throws MalformedURLException {
        if(!topicForm.getName().equals(""))
            return "redirect:/subscriber/lastNews?topic=" + topicForm.getName();
        return "redirect:/";
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
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


    @GetMapping(path = "/subscriber/profile")
    public String returnToPublisherAddTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","profile");
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(userService.search(authentication.getName()));
        model.addAttribute("userData",userRegistrationDto);
        model.addAttribute("fileImagePath",userRegistrationDto.getProfilePicPath());
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        return "subscriber/index";
    }

    @PostMapping("/subscriber/saveProfile")
    public String saveNew(@ModelAttribute("userData") UserRegistrationDto userRegistrationDto,
                          @ModelAttribute("fileImagePath") String fileImagePath,
                          @RequestParam(value = "fileImage") MultipartFile multipartFile){
        User fetchedUser = userService.search(userRegistrationDto.getEmail());
        User savedUser;
        System.out.println(multipartFile.getOriginalFilename());
        System.out.println(fileImagePath);
        if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().equals("")){
            String fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            System.out.println(fileName);
            fetchedUser.setProfilePic(fileName);
            String uploadDir= "profile-pics/"+ fetchedUser.getId();
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)){
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try(InputStream inputStream = multipartFile.getInputStream()) {
                Path filepath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filepath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(userRegistrationDto.getPassword() != null && !userRegistrationDto.getPassword().equals("")){
            savedUser = userService.save(fetchedUser,userRegistrationDto.getPassword());
        }else{
            savedUser = userService.save(fetchedUser);
        }

        System.out.println(savedUser);


        return "redirect:/";
    }



}
