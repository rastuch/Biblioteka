package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CopyManager {
    private final CopyRepo copyRepo;
    @Autowired
    public CopyManager(CopyRepo copyRepo){
        this.copyRepo = copyRepo;
    }

    public Optional<Copy> findById(Long id) {
        return copyRepo.findById(id);
    }
    public Iterable<Copy> findAll() {
        return copyRepo.findAll();
    }

    public Copy save(Copy copy) {
        return copyRepo.save(copy);
    }
    public void delete(Long id) {
        copyRepo.deleteById(id);
    }
    public void deleteAllBookCopies(Long bookId) {deleteAllBookCopies(bookId);}

    @EventListener(ApplicationReadyEvent.class)
    public void fillDbHelper() {
//        save(Copy.builder().book(Book.builder().id(1L).build()).location("Dział Literatura Polska, 4 półka").status(CopyStatus.AVAILABLE).build());
//        save(Copy.builder().book(Book.builder().id(2L).build()).location("Dział SF, 4 półka").status(CopyStatus.AVAILABLE).build());
//        save(Copy.builder().book(Book.builder().id(2L).build()).location("Dział SF, 4 półka").status(CopyStatus.AVAILABLE).build());
    }
}
