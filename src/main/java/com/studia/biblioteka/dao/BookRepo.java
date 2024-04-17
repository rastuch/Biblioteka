package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepo extends CrudRepository<Book, Long> {
}
