package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.BookRepo;
import com.studia.biblioteka.dao.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookManager {
    private final BookRepo bookRepo;

    @Autowired
    public BookManager(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Optional<Book> findById(Long id) {
        return bookRepo.findById(id);
    }

    public Iterable<Book> findAll() {
        return bookRepo.findAll();
    }

    public Book save(Book book) {
        return bookRepo.save(book);
    }

    public void delete(Long id) {
        bookRepo.deleteById(id);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDbHelper() {
        save(Book.builder().title("Dziady").authors("Adam Mickiewicz").category("Sztuka Teatralna").build());
        save(Book.builder().title("Kongres futurologiczny").authors("Stanisław Lem").category("SF").build());
    }

    public Iterable<Book> findByKeyword(String search) {
       return bookRepo.findAllByKeyword(search);
    }
}
