package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.forms.TopicForm;
import com.example.noticiasquentinhas.forms.TopicFormSubscriber;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;
import com.example.noticiasquentinhas.repository.TopicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TopicServiceImpl implements TopicService{


    @Autowired
    private TopicsRepository topicsRepository;

    public TopicServiceImpl(TopicsRepository topicsRepository) {
        super();
        this.topicsRepository = topicsRepository;
    }

    /**
     * Save a specific topic
     * @param topicform the topicsform that contains the topic name
     * @return the topic
     */
    public Topics save(TopicForm topicform){
        Topics topic = new Topics();
        topic.setName(topicform.getName());
        return topicsRepository.save(topic);
    }

    /**
     * Save a topic
     * @param topics the topic
     * @return the topic
     */
    public Topics save(Topics topics){
        return topicsRepository.save(topics);
    }

    /**
     * Search a specific topic by name
     * @param name the topic's name
     * @return the topic
     */
    public Topics search(String name){
        Topics topics = topicsRepository.findByName(name);
        return topics;
    }

    /**
     * Find all topics
     * @return all topics
     */
    @Override
    public ArrayList<Topics> topicsList() {
        return topicsRepository.findAll();
    }

    /**
     * Get all the topics that a user is not subscribed to
     * @param Subscriber the user
     * @return the topics
     */
    public ArrayList<Topics> getUnsubscribedTopics(User Subscriber){
        return topicsRepository.findTopicsBySubscribersIsNotContaining(Subscriber);
    }

    /**
     * Get all the topics that a user is subscribed to
     * @param Subscriber
     * @return
     */
    public ArrayList<Topics> getSubscribedTopics(User Subscriber){
        return topicsRepository.findTopicsBySubscribersIsContaining(Subscriber);

    }

    /**
     * Get all topics by name
     * @param topicFormSubscribers contains multiple topics name
     * @return the topics
     */
    public ArrayList<Topics> getTopicsByName(TopicFormSubscriber topicFormSubscribers){
        ArrayList<Topics> topicsArrayList = new ArrayList<>();
        for (String t : topicFormSubscribers.getName() ) {
            topicsArrayList.add(search(t));
        }
        return topicsArrayList;
    }

    /**
     * Get a topic by id
     * @param id the topic's id
     * @return the topic
     */
    public Topics getTopicByID(Integer id){
        return topicsRepository.findById(id).get();
    }


}
