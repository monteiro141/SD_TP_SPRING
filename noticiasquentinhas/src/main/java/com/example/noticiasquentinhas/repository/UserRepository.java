package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.News;
import com.example.noticiasquentinhas.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}


