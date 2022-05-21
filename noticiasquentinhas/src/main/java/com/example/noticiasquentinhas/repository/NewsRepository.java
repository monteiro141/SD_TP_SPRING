package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface NewsRepository extends CrudRepository<News, Integer> {
    Iterable<News> findAllByPublisher_Email(String email);
}
