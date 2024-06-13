package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.FineRepo;
import com.studia.biblioteka.dao.entity.Fine;
import com.studia.biblioteka.dao.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FineManagerTest {

    @Mock
    private FineRepo fineRepo;

    @InjectMocks
    private FineManager fineManager;

    private Fine fine;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);

        fine = new Fine();
        fine.setId(1L);
        fine.setUser(user);
        fine.setAmount(new BigDecimal("50.00"));
        fine.setReason("Late return");

        when(fineRepo.findById(1L)).thenReturn(Optional.of(fine));
        when(fineRepo.findAll()).thenReturn(Arrays.asList(fine));
        when(fineRepo.findAllByUserId(1L)).thenReturn(Arrays.asList(fine));
        when(fineRepo.save(any(Fine.class))).thenReturn(fine);
    }

    @Test
    public void whenFindById_thenReturnFine() {
        Optional<Fine> found = fineManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(fine, found.get());
    }

    @Test
    public void whenFindAll_thenReturnAllFines() {
        Iterable<Fine> fines = fineManager.findAll();
        assertTrue(fines.iterator().hasNext());
        assertEquals(fine, fines.iterator().next());
    }

    @Test
    public void whenFindAllByUserId_thenReturnUserFines() {
        Iterable<Fine> fines = fineManager.findAllByUserId(1L);
        assertTrue(fines.iterator().hasNext());
        assertEquals(fine, fines.iterator().next());
    }

    @Test
    public void whenSave_thenReturnSavedFine() {
        Fine savedFine = fineManager.save(fine);
        assertNotNull(savedFine);
        assertEquals(fine.getId(), savedFine.getId());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        fineManager.delete(1L);
        verify(fineRepo).deleteById(1L);
    }
}
