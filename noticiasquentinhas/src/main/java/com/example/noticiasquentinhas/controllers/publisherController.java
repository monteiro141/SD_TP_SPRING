package com.example.noticiasquentinhas.controllers;



import com.example.noticiasquentinhas.entities.*;
import com.example.noticiasquentinhas.forms.NewsForm;
import com.example.noticiasquentinhas.forms.TopicForm;
import com.example.noticiasquentinhas.forms.UserRegistrationDto;
import com.example.noticiasquentinhas.service.EmailService;
import com.example.noticiasquentinhas.service.NewsService;
import com.example.noticiasquentinhas.service.TopicService;
import com.example.noticiasquentinhas.service.UserService;
import com.example.noticiasquentinhas.forms.EmailDetails;
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
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

    /**
     * Home page for the publisher and deals with pagination
     * @param model
     * @param id the number of the page
     * @return
     * @throws MalformedURLException
     */
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


    /**
     * Page to do the add a topic operation
     * @param name if the topic was added("success") or not ("fail")
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Receive topic that the user entered
     * @param topicForm
     * @return
     */
    @PostMapping(path = "/publisher/addTopicButton")
    public String creatingANews(@ModelAttribute("addTopic") TopicForm topicForm){
        try{
            topicService.save(topicForm);
        } catch (Exception e){
            return "redirect:/publisher/addTopic?create=fail";
        }
        return "redirect:/publisher/addTopic?create=success";
    }

    /**
     * Show all topics to the publisher
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Create News page for the create news operation
     * @param name
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * receive the news data that the user has entered
     * @param multipartFile
     * @param newsForm
     * @return
     */
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
            sendEmail(newsSaved.getTopics_news());
        } catch (Exception e){
            return "redirect:/publisher/createNews?create=fail";
        }
        return "redirect:/publisher/createNews?create=success";
    }

    /**
     * List all news that the publisher done
     * @param model
     * @param id the number of the page
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Operation for the publisher to edit the news
     * @param name
     * @param id the number of the page
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Receive the news edited and save the news
     * @param news
     * @param multipartFile
     * @return
     */
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

    /**
     * Shows the publisher profile
     * @param model
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * update the user profile after the user click "Alterar"
     * @param userRegistrationDto
     * @param fileImagePath
     * @param multipartFile
     * @return
     */
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

    /**
     * Send email to the subscriber that is subscribed to a topic
     * @param topic
     */
    public void sendEmail(Topics topic){
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Nova noticia no topico - " + topic.getName());

        emailDetails.setMsgBody("<html> " +
                "<body>" +
                "<img src='cid:image'/>" +
                "<p>Venha já visitar o nosso site!</p>" +
                "<p>A notícia aguarda por si, mais quentinha era impossível...</p><br>" +
                "<p>Atenciosamente,</p>" +
                "<p>Notícias Quentinhas.</p>" +
                "</body>" +
                "</html>");


        ArrayList<String> usersToSendEmail = userService.getUsersWithSubscribedTopic(topic.getTopic_id());

        final Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            for (String email: usersToSendEmail) {
                emailDetails.setRecipient(email);
                String status = emailService.sendMailWithAttachment(emailDetails);
                System.out.println("LOG [SEND-EMAIL] "+status);
            }
                }
        );

    }

}
