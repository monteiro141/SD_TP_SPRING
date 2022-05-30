package com.example.noticiasquentinhas.controllers;



import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.repository.NewsRepository;
import com.example.noticiasquentinhas.service.EmailService;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import com.example.noticiasquentinhas.smtp.EmailDetails;
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

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.details;

@Controller
public class publisherController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private TopicService topicService;

    @Autowired private EmailService emailService;


    public publisherController(UserService userService, NewsService newsService, TopicService topicService, EmailService emailService) {
        this.userService = userService;
        this.newsService = newsService;
        this.topicService = topicService;
        this.emailService = emailService;
    }

    @GetMapping({"/publisher/", "/publisher/{id}"})
    public String returnToPublisherIndex(Model model,@PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","home");
        id = id == null? 0: id;
        model.addAttribute("idPage",id);
        model.addAttribute("newsList",newsService.getNews(id,10));
        model.addAttribute("hasNextPage",newsService.getNews(id+1,10).size());
        model.addAttribute("pathImage",userService.search(authentication.getName()).getProfilePicPath());
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        model.addAttribute("newsForm", new NewsForm());
        model.addAttribute("topicsList",topicService.topicsList());
        model.addAttribute("create",name);
        model.addAttribute("topicsSize", topicService.topicsList().size());
        return "publisher/index";
    }

    @PostMapping("/publisher/createNewsButton")
    public String creatingANews(@RequestParam(value = "fileImage") MultipartFile multipartFile,
                                @ModelAttribute("newsForm") NewsForm newsForm){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            News newsSaved = newsService.save(newsForm,
                    userService.search(authentication.getName()),
                    topicService.search(newsForm.getTopic()));
            if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().equals("")) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                newsSaved.setNewsThumbnail(fileName);
                String uploadDir = "news-thumbnail/" + newsSaved.getNews_id();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filepath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filepath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            newsService.saveEditNew(newsSaved);
            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setSubject("Nova noticia no topico - " + newsForm.getTopic());

            emailDetails.setMsgBody("<html> " +
                    "<body>" +
                    "<img src='cid:image'/>" +
                    "<p>Venha ja visitar o nosso site!</p>" +
                    "<p>A notícia aguarda por si, mais quentinha era impossível...</p><br>" +
                    "<p>Atenciosamente,</p>" +
                    "<p>Noticias Quentinhas.</p>" +
                    "</body>" +
                    "</html>");
            emailDetails.setRecipient("shekwodjek@gmail.com");
            emailDetails.setAttachment(newsSaved.getProfilePicPath());
            //String status = emailService.sendMailWithAttachment(emailDetails);
            //System.out.println("status: " + status);

        } catch (Exception e){
            return "redirect:/publisher/createNews?create=fail";
        }
        return "redirect:/publisher/createNews?create=success";
    }

    @GetMapping({"/publisher/listNews", "/publisher/listNews/{id}"})
    public String returnToPublisherListNews(Model model, @PathVariable(value="id",required = false) Integer id) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","listNews");
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        id = id == null? 0: id;
        model.addAttribute("idPage",id);
        model.addAttribute("newsList",newsService.getPublisherNews(authentication.getName(),id,10));
        model.addAttribute("hasNextPage",newsService.getPublisherNews(authentication.getName(),id+1,10).size());
        return "publisher/index";
    }

    @GetMapping(path = "/publisher/editNews/{id}")
    public String returnToPublisherEditNews(@RequestParam(name="edit", required = false, defaultValue = "none") String name, @PathVariable(value="id") Integer id,Model model) throws MalformedURLException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("loggedInUser",(userService.currentUserName(authentication.getName())));
        model.addAttribute("linkPath","editNews");
        model.addAttribute("edit",name);
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        //Optional<News> optional = newsService.findNew(id);
        News newsEdit = newsService.findNew(id);
        //newsEdit = optional.get();
        model.addAttribute("theNew",newsEdit);
        return "publisher/index";
    }

    @PostMapping("/publisher/editNewsButton")
    public String saveNew(@ModelAttribute("theNew") News news,
                          @RequestParam(value = "fileImage") MultipartFile multipartFile){
        try{
            String oldThumbnail = newsService.findNew(news.getNews_id()).getNewsThumbnail();
            News newsSaved = newsService.saveEditNew(news);
            if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().equals("")) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                newsSaved.setNewsThumbnail(fileName);
                String uploadDir = "news-thumbnail/" + newsSaved.getNews_id();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    try {
                        Files.createDirectories(uploadPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filepath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filepath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                newsSaved.setNewsThumbnail(oldThumbnail);
            }
            newsService.saveEditNew(newsSaved);
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
        model.addAttribute("profileSmallPic",userService.search(authentication.getName()).getProfilePicPath());
        model.addAttribute("userData",userRegistrationDto);
        model.addAttribute("fileImagePath",userRegistrationDto.getProfilePicPath());
        return "publisher/index";
    }

    @PostMapping("/publisher/saveProfile")
    public String saveNew(@ModelAttribute("userData") UserRegistrationDto userRegistrationDto,
                          @ModelAttribute("fileImagePath") String fileImagePath,
                          @RequestParam(value = "fileImage") MultipartFile multipartFile){
        User fetchedUser = userService.search(userRegistrationDto.getEmail());
        fetchedUser.setFirstName(userRegistrationDto.getFirstName());
        fetchedUser.setLastName(userRegistrationDto.getLastName());
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
                Files.copy(inputStream, filepath,StandardCopyOption.REPLACE_EXISTING);
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
