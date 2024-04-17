package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepo extends CrudRepository<Category, Long> {
}
