package com.example.noticiasquentinhas.controllers;

import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import org.mockito.internal.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.util.*;

@Controller
public class publisherController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
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
        Collections.reverse(newsList);
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
        model.addAttribute("topicsSize", topicService.topicsList().size());
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
        model.addAttribute("topicsSize", topicService.topicsList().size());
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
        ArrayList<News> newsList = newsService.listNewsUser(authentication.getName());
        Collections.reverse(newsList);
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


    @GetMapping(path = "/publisher/profile")
    public String returnToPublisherAddTopic(Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","profile");
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(userService.search(authentication.getName()));

        model.addAttribute("userData",userRegistrationDto);
        model.addAttribute("fileImagePath",userRegistrationDto.getProfilePicPath());
        return "publisher/index";
    }

    @PostMapping("/publisher/saveProfile")
    public String saveNew(@ModelAttribute("userData") UserRegistrationDto userRegistrationDto,
                          @ModelAttribute("fileImagePath") String fileImagePath,
                          @RequestParam(value = "fileImage") MultipartFile multipartFile){
        System.out.println(userRegistrationDto);
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
                Files.copy(inputStream, filepath,StandardCopyOption.REPLACE_EXISTING);
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
