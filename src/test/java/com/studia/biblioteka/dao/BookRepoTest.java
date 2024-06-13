package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Book;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepo bookRepo;

    private Book book;

    @Before
    public void setUp() {
        book = Book.builder()
                .title("Effective Java")
                .authors("Joshua Bloch")
                .category("Programming")
                .build();
        entityManager.persist(book);
        entityManager.flush();
    }

    @Test
    public void whenFindAllByKeyword_thenReturnMatchingBooks() {
        // Test dla tytułu
        List<Book> foundBooksByTitle = bookRepo.findAllByKeyword("Effective");
        assertThat(foundBooksByTitle).hasSize(1).contains(book);

        // Test dla autorów
        List<Book> foundBooksByAuthors = bookRepo.findAllByKeyword("Bloch");
        assertThat(foundBooksByAuthors).hasSize(1).contains(book);

        // Test dla kategorii
        List<Book> foundBooksByCategory = bookRepo.findAllByKeyword("Programming");
        assertThat(foundBooksByCategory).hasSize(1).contains(book);
    }
}
