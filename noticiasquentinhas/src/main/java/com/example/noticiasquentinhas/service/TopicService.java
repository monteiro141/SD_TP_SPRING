package com.example.noticiasquentinhas.service;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.Topics;

import java.util.Collection;

public interface TopicService {
    Topics search(String name);
    Collection<Topics> topicsList();
    Topics save(TopicForm topicForm);
}
