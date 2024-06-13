package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.BookRepo;
import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.BookResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class BookManagerTest {

    @Mock
    private BookRepo bookRepo;
    @Mock
    private CopyRepo copyRepo;

    @Autowired
    private BookManager bookManager;

    private Book book;
    private Copy copy;

    @Before
    public void setUp() {
        bookManager = new BookManager(bookRepo, copyRepo);
        book = new Book(1L, "Effective Java", "Joshua Bloch", "Programming");
        copy = new Copy(1L, book, CopyStatus.AVAILABLE, "Shelf A");

        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book));
        when(bookRepo.save(any(Book.class))).thenReturn(book);
        when(copyRepo.findAllByBook_Id(1L)).thenReturn(Arrays.asList(copy));
    }

    @Test
    public void whenFindById_thenBookShouldBeFound() {
        Optional<Book> found = bookManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(book, found.get());
    }

    @Test
    public void whenFindAll_thenAllBooksShouldBeFound() {
        Iterable<Book> books = bookManager.findAll();
        assertNotNull(books);
        assertTrue(books.iterator().hasNext());
        assertEquals(book, books.iterator().next());
    }

    @Test
    public void whenSave_thenBookShouldBeSaved() {
        Book savedBook = bookManager.save(book);
        assertNotNull(savedBook);
        assertEquals(book.getId(), savedBook.getId());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        bookManager.delete(1L);
        verify(bookRepo).deleteById(1L);
    }

    @Test
    public void whenConvertToBookResponse_thenShouldReturnProperResponse() {
        BookResponse bookResponse = bookManager.convertToBookResponse(book);
        assertNotNull(bookResponse);
        assertEquals(book.getId(), bookResponse.getId());
        assertTrue(bookResponse.getIsAvailable());
    }

    @Test
    public void whenFindByKeyword_thenBooksShouldBeFound() {
        when(bookRepo.findAllByKeyword("Effective")).thenReturn(Arrays.asList(book));
        Iterable<Book> books = bookManager.findByKeyword("Effective");
        assertNotNull(books);
        assertTrue(books.iterator().hasNext());
        assertEquals(book, books.iterator().next());
    }
}
