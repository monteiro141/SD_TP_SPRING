package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.Topics;

import java.util.Collection;

public interface TopicService {
    public Topics search(String name);
    public Iterable<Topics> topicsList();
    public Topics save(TopicForm topicForm);
}
