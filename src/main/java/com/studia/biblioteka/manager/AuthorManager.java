package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.AuthorRepo;
import com.studia.biblioteka.dao.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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


    @EventListener(ApplicationReadyEvent.class)
    public void fillDbHelper() {
        save(new Author(1L, "Henryk", "Sienkiewicz", "ur. 5 maja 1846 w Woli Okrzejskiej, zm. 15 listopada 1916 w Vevey",null));
        save(new Author(2L, "Adam", "Mickiewicz", "ur. 13 grudnia/24 grudnia 1798 w Zaosiu lub Nowogródku, zm. 26 listopada 1855 w Stambule",null));

    }
}

