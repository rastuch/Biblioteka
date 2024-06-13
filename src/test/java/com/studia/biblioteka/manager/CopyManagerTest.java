package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.CopyRepo;
import com.studia.biblioteka.dao.entity.Book;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.enums.CopyStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CopyManagerTest {

    @Mock
    private CopyRepo copyRepo;

    @InjectMocks
    private CopyManager copyManager;

    private Copy copy;

    @Before
    public void setUp() {
        Book book = new Book(1L, "Clean Code", "Robert C. Martin", "Programming");
        copy = new Copy(1L, book, CopyStatus.AVAILABLE, "Shelf A");

        when(copyRepo.findById(1L)).thenReturn(Optional.of(copy));
        when(copyRepo.findAll()).thenReturn(Arrays.asList(copy));
        when(copyRepo.save(any(Copy.class))).thenReturn(copy);
        when(copyRepo.findAllByBook_Id(1L)).thenReturn(Arrays.asList(copy));
    }

    @Test
    public void whenFindById_thenReturnCopy() {
        Optional<Copy> found = copyManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(copy, found.get());
    }

    @Test
    public void whenFindAll_thenReturnAllCopies() {
        Iterable<Copy> copies = copyManager.findAll();
        assertTrue(copies.iterator().hasNext());
        assertEquals(copy, copies.iterator().next());
    }

    @Test
    public void whenSave_thenReturnSavedCopy() {
        Copy savedCopy = copyManager.save(copy);
        assertNotNull(savedCopy);
        assertEquals(copy.getId(), savedCopy.getId());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        copyManager.delete(1L);
        verify(copyRepo).deleteById(1L);
    }

    @Test
    public void whenFindAllBookCopies_thenReturnAllBookCopies() {
        Iterable<Copy> copies = copyManager.findAllBookCopies(1L);
        assertTrue(copies.iterator().hasNext());
        assertEquals(copy, copies.iterator().next());
    }
}
