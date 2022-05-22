package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Set;

public interface NewsRepository extends CrudRepository<News, Integer> {
    ArrayList<News> findAllByPublisher_Email(String email);
    ArrayList<News> findAll();
}
