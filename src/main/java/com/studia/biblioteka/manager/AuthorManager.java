package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.AuthorRepo;
import com.studia.biblioteka.dao.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorManager {
    private final AuthorRepo authorRepo;
    @Autowired
    public AuthorManager(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public Optional<Author> findById(Long id) {
        return authorRepo.findById(id);
    }
    public Iterable<Author> findAll() {
        return authorRepo.findAll();
    }

    public Author save(Author author) {
        return authorRepo.save(author);
    }
    public void delete(Long id) {
        authorRepo.deleteById(id);
    }
}
