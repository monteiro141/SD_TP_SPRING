package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.forms.TopicForm;
import com.example.noticiasquentinhas.forms.TopicFormSearch;
import com.example.noticiasquentinhas.forms.TopicFormSubscriber;
import com.example.noticiasquentinhas.forms.UserRegistrationDto;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    /**
     * Home page for the Subscriber and deals with pagination
     * @param model
     * @param id the number of the page
     * @return
     * @throws MalformedURLException
     */
    @GetMapping({"/subscriber/", "/subscriber/{id}"})
    public String returnToSubscriberIndex(Model model,
        @PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","home");
        id = id == null? 0: id;
        model.addAttribute("idPage",id);
        model.addAttribute("newsList",newsService.getNews(id,10));
        model.addAttribute("hasNextPage",newsService.getNews(id+1,10).size());
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        return "subscriber/index";
    }

    /**
     * Do the operation: subscribe a topic
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Receive the topics that the user selected
     * @param topicFormSubscriber
     * @return
     * @throws MalformedURLException
     */
    @PostMapping("/subscriber/topic")
    public String getSubscribedTopicsForm(@ModelAttribute("checkedTopics") TopicFormSubscriber topicFormSubscriber) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (Topics t: topicService.getTopicsByName(topicFormSubscriber)) {
            t.addSubscriber(userService.search(authentication.getName()));
            topicService.save(t);
        }

        return "redirect:/";
    }

    /**
     * Do the operation of: news from a topic with a specific timestamp
     * @param id the topic id
     * @param page the page id
     * @param date1 the first date
     * @param date2 the second date
     * @param validation if dates are valid or not
     * @param model
     * @return the correct page
     * @throws MalformedURLException
     */
    @GetMapping({"/subscriber/news/{id}", "/subscriber/news", "/subscriber/news/{id}/{page}"})
    public String returnToSubscriberNews(@PathVariable(value="id",required = false) Integer id,
            @PathVariable(value="page",required = false) Integer page,
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

            page = page == null? 0 : page;
            List<News> newsFromTimeStamp = newsService.getNewsFromTimestamp(topicService.getTopicByID(id),datetime1,datetime2,page,10);

            model.addAttribute("idPage",page);
            model.addAttribute("hasNextPage",newsService.getNewsFromTimestamp(topicService.getTopicByID(id),datetime1,datetime2,page+1,10).size());
            model.addAttribute("partLink", "date1="+date1+"&date2="+date2+"&valid=true");
            model.addAttribute("newsFromTimeStamp",newsFromTimeStamp);
            model.addAttribute("newsFromTimeStampSize",newsFromTimeStamp.size());
        }
        model.addAttribute("validation",validation);
        return "subscriber/index";
    }

    /**
     * Receive the data that the user entered in the form
     * @param topicFormsearch the data that the user entered
     * @return the user to the correct option
     * @throws MalformedURLException
     */
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
            System.out.println(stringDate1);
            if(datetime1.isBefore(datetime2) || !datetime1.equals(datetime2))
                return "redirect:/subscriber/news/"+ topicService.search(topicFormsearch.getName()).getTopic_id()+"?date1="+stringDate1+"&date2="+stringDate2+"&valid=true";
        }catch (Exception e){
            return "redirect:/subscriber/news?valid=false";
        }
        return "redirect:/subscriber/news?valid=false";
    }

    /**
     * Do the operation of: last news of a specific topic
     * @param model
     * @param id the topic id
     * @return
     * @throws MalformedURLException
     */
    @GetMapping({"/subscriber/lastNews/{id}", "/subscriber/lastNews"})
    public String returnToSubscriberLastNews(Model model, @PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","lastNews");
        ArrayList<Topics> topicsList = topicService.topicsList();
        model.addAttribute("topicsList", topicsList);
        model.addAttribute("topicsListSize", topicsList.size());
        model.addAttribute("topicForm", new TopicForm());
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());

        if(id!=null){
            News lastNews= newsService.getLastNewsFromTopic(id);
            if(lastNews != null) {
                model.addAttribute("lastNew", lastNews);
                model.addAttribute("validation", "true");
            }else{
                model.addAttribute("validation", "false");
            }

        }
        return "subscriber/index";
    }

    /**
     * Receive the data that the user entered for the last news from a topic operation
     * @param topicForm the topic that the user entered
     * @return
     * @throws MalformedURLException
     */
    @PostMapping("/subscriber/lastNews")
    public String returnToSubscriberLastNews(@ModelAttribute("checkedTopics") TopicForm topicForm) throws MalformedURLException {
        if(!topicForm.getName().equals(""))
            return "redirect:/subscriber/lastNews/" + topicService.search(topicForm.getName()).getTopic_id();
        return "redirect:/";
    }

    /**
     * Do the operation of: remove a subscribed topic
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Receive the topic that the user wants to unsubscribe
     * @param topicForm
     * @return
     * @throws MalformedURLException
     */
    @PostMapping("/subscriber/removeTopic")
    public String getSubscribedTopicsForm(@ModelAttribute("removeTopic") TopicForm topicForm) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Topics topicToUnsubscribe = topicService.search(topicForm.getName());
        topicToUnsubscribe.removeSubscriber(userService.search(authentication.getName()));
        topicService.save(topicToUnsubscribe);

        return "redirect:/";
    }

    /**
     * Shows the subscriber profile
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * update the user profile after the user click "Alterar"
     * @param userRegistrationDto
     * @param fileImagePath
     * @param multipartFile
     * @return
     */
    @PostMapping("/subscriber/saveProfile")
    public String saveNew(@ModelAttribute("userData") UserRegistrationDto userRegistrationDto,
                          @ModelAttribute("fileImagePath") String fileImagePath,
                          @RequestParam(value = "fileImage") MultipartFile multipartFile){
        User fetchedUser = userService.search(userRegistrationDto.getEmail());
        fetchedUser.setFirstName(userRegistrationDto.getFirstName());
        fetchedUser.setLastName(userRegistrationDto.getLastName());
        User savedUser;
        if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().equals("")){
            String fileName= StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
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
            userService.save(fetchedUser,userRegistrationDto.getPassword());
        }else{
            userService.save(fetchedUser);
        }


        return "redirect:/";
    }



}
