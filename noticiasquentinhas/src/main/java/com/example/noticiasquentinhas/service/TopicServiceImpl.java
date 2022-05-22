package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.TopicFormSubscriber;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import com.example.noticiasquentinhas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class TopicServiceImpl implements TopicService{


    @Autowired
    private TopicsRepository topicsRepository;

    public TopicServiceImpl(TopicsRepository topicsRepository) {
        super();
        this.topicsRepository = topicsRepository;
    }

    public Topics save(TopicForm topicform){
        Topics topic = new Topics();
        topic.setName(topicform.getName());
        return topicsRepository.save(topic);
    }

    public Topics save(Topics topics){
        return topicsRepository.save(topics);
    }

    public Topics search(String name){
        Topics topics = topicsRepository.findByName(name);
        return topics;
    }

    @Override
    public ArrayList<Topics> topicsList() {
        return topicsRepository.findAll();
    }


    public ArrayList<Topics> getUnsubscribedTopics(User Subscriber){
        return topicsRepository.findTopicsBySubscribersIsNotContaining(Subscriber);
        //return new ArrayList<>();
    }

    public ArrayList<Topics> getSubscribedTopics(User Subscriber){
        return topicsRepository.findTopicsBySubscribersIsContaining(Subscriber);

    }

    public ArrayList<Topics> getTopicsByName(TopicFormSubscriber topicFormSubscribers){
        ArrayList<Topics> topicsArrayList = new ArrayList<>();
        for (String t : topicFormSubscribers.getName() ) {
            topicsArrayList.add(search(t));
        }
        return topicsArrayList;
    }


}
