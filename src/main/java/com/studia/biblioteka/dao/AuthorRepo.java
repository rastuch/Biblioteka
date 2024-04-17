package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepo extends CrudRepository<Author, Long> {
}
