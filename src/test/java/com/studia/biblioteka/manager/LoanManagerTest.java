package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.LoanRepo;
import com.studia.biblioteka.dao.entity.Loan;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dao.enums.LoanStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoanManagerTest {

    @Mock
    private LoanRepo loanRepo;

    @InjectMocks
    private LoanManager loanManager;

    private Loan loan;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        Copy copy = new Copy();
        copy.setId(1L);

        loan = Loan.builder()
                .id(1L)
                .copy(copy)
                .user(user)
                .loanDate(LocalDate.now())
                .returnDate(LocalDate.now().plusDays(14))
                .status(LoanStatus.IN_PROGRESS)
                .maxReturnDate(LocalDate.now().plusDays(14))
                .build();

        when(loanRepo.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepo.findAll()).thenReturn(Arrays.asList(loan));
        when(loanRepo.save(any(Loan.class))).thenReturn(loan);
    }

    @Test
    public void whenFindById_thenReturnLoan() {
        Optional<Loan> found = loanManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(loan, found.get());
    }

    @Test
    public void whenFindAll_thenReturnAllLoans() {
        Iterable<Loan> loans = loanManager.findAll();
        assertTrue(loans.iterator().hasNext());
        assertEquals(loan, loans.iterator().next());
    }

    @Test
    public void whenSave_thenReturnSavedLoan() {
        Loan savedLoan = loanManager.save(loan);
        assertNotNull(savedLoan);
        assertEquals(loan.getId(), savedLoan.getId());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        loanManager.delete(1L);
        verify(loanRepo).deleteById(1L);
    }
}
