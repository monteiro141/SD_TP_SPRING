package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import com.example.noticiasquentinhas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Topics search(String name){
        Topics topics = topicsRepository.findByName(name);
        return topics;
    }

    @Override
    public Collection<Topics> topicsList() {
        return topicsRepository.findAllTopics();
    }


}
