package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.TopicFormSubscriber;
import com.example.noticiasquentinhas.entities.Topics;
import com.example.noticiasquentinhas.entities.User;

import java.util.ArrayList;
import java.util.Collection;


public interface TopicService {
    public Topics search(String name);
    public ArrayList<Topics> topicsList();
    public Topics save(TopicForm topicForm);
    public Topics save(Topics topics);
    public ArrayList<Topics> getUnsubscribedTopics(User Subscriber);
    public ArrayList<Topics> getSubscribedTopics(User Subscriber);
    public ArrayList<Topics> getTopicsByName(TopicFormSubscriber topicFormSubscribers);
}
