package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.TopicForm;
import com.example.noticiasquentinhas.entities.Topics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Set;

public interface TopicsRepository extends CrudRepository<Topics, Integer> {
    public Topics findByName(String name);
    public Topics save(TopicForm topicForm);
}
