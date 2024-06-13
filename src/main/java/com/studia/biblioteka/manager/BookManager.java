package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.BookRepo;
import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

@Service
public class BookManager {
    private final BookRepo bookRepo;
    private final CopyRepo copyRepo;

    @Autowired
    public BookManager(BookRepo bookRepo, CopyRepo copyRepo) {
        this.bookRepo = bookRepo;
        this.copyRepo = copyRepo;
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

    public BookResponse convertToBookResponse(Book book) {
        Iterable<Copy> bookCopies = copyRepo.findAllByBook_Id(book.getId());
        boolean available = false;
        for (Copy copy : bookCopies) {
            if (copy.getStatus() == CopyStatus.AVAILABLE) {
                available = true;
                break;
            }
        }
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authors(book.getAuthors())
                .category(book.getCategory())
                .isAvailable(available)
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void resetSequence() {

    }

    public Iterable<Book> findByKeyword(String search) {
       return bookRepo.findAllByKeyword(search);
    }
}
