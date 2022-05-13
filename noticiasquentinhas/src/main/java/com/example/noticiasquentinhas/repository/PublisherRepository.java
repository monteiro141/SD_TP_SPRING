package com.example.noticiasquentinhas.repository;

import com.example.noticiasquentinhas.entities.Publishers;
import org.springframework.data.repository.CrudRepository;

public interface PublisherRepository extends CrudRepository<Publishers,Integer> {
}
