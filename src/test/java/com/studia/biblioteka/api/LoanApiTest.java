package com.studia.biblioteka.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studia.biblioteka.NoSecurityConfig;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.Loan;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dao.enums.LoanStatus;
import com.studia.biblioteka.dto.NewLoan;
import com.studia.biblioteka.manager.CopyManager;
import com.studia.biblioteka.manager.LoanManager;
import com.studia.biblioteka.manager.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoanApi.class)
@Import(NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoanApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanManager loanManager;
    @MockBean
    private CopyManager copyManager;
    @MockBean
    private UserManager userManager;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Loan loan;
    private Copy copy;
    private User user;

    @Before
    public void setup() {
        loan = new Loan();
        loan.setId(1L);
        loan.setLoanDate(LocalDate.now());
        loan.setMaxReturnDate(LocalDate.now().plusDays(30));
        loan.setStatus(LoanStatus.IN_PROGRESS);


        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        copy = new Copy();
        copy.setId(1L);
        copy.setStatus(CopyStatus.AVAILABLE);

    }

    @Test
    public void getById_Success() throws Exception {
        given(loanManager.findById(1L)).willReturn(Optional.of(loan));

        mockMvc.perform(get("/api/loan").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(loan.getId()));
    }

    @Test
    public void getById_NotFound() throws Exception {
        given(loanManager.findById(99999L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/loan").param("id", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllLoans() throws Exception {
        given(loanManager.findAll()).willReturn(Arrays.asList(loan));

        mockMvc.perform(get("/api/loan/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(loan.getId()));
    }


    @Test
    public void addLoan_Success() throws Exception {
        NewLoan newLoan = new NewLoan();
        newLoan.setCopyId(copy.getId());
        newLoan.setUserId(user.getId());

        given(copyManager.findById(copy.getId())).willReturn(Optional.of(copy));
        given(userManager.findById(user.getId())).willReturn(Optional.of(user));
        given(loanManager.save(any(Loan.class))).willReturn(loan);
        given(copyManager.save(any(Copy.class))).willReturn(copy);

        mockMvc.perform(post("/api/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newLoan)))
                .andExpect(status().isOk())
                .andExpect(content().string("New Loan Added successfully"));
    }

    @Test
    public void closeLoan_Success() throws Exception {
        given(loanManager.findById(any())).willReturn(Optional.of(loan));
        given(copyManager.save(any())).willReturn(loan.getCopy());
        given(loanManager.save(any(Loan.class))).willReturn(loan);

        mockMvc.perform(get("/api/loan/closeLoan").param("id", "1"))
                .andExpect(status().isOk());
    }


    @Test
    public void deleteLoan_Success() throws Exception {
        given(loanManager.findById(loan.getId())).willReturn(Optional.of(loan));
        doNothing().when(loanManager).delete(anyLong());

        mockMvc.perform(delete("/api/loan").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getLoanStatuses() throws Exception {
        mockMvc.perform(get("/api/loan/statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value(LoanStatus.IN_PROGRESS.toString()));
    }
}
