package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Integer> {
}