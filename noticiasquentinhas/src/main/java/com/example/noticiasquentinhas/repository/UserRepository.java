package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
