package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.entity.Category;
import com.studia.biblioteka.dao.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryManager {
    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryManager(CategoryRepo categoryRepo){
        this.categoryRepo = categoryRepo;
    }

    public Optional<Category> findById(Long id) {
        return categoryRepo.findById(id);
    }
    public Iterable<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category save(Category category) {
        return categoryRepo.save(category);
    }
    public void delete(Long id) {
        categoryRepo.deleteById(id);
    }
}
