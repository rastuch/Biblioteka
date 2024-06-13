package com.studia.biblioteka.api;

import com.studia.biblioteka.NoSecurityConfig;
import com.studia.biblioteka.dao.entity.Fine;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewFineByUserId;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.FineManager;
import com.studia.biblioteka.manager.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(FineApi.class)
@Import(NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class FineApiTest {

    @MockBean
    private FineManager fineManager;

    @MockBean
    private UserManager userManager;

    @InjectMocks
    private FineApi fineApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById_FineExists() {
        long id = 1L;
        Fine fine = new Fine();
        fine.setId(id);

        when(fineManager.findById(id)).thenReturn(Optional.of(fine));

        ResponseEntity<Object> response = fineApi.getById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fine, response.getBody());
    }

    @Test
    public void testGetById_FineNotFound() {
        long id = 1L;

        when(fineManager.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = fineApi.getById(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Fine not found", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testGetAll() {
        Iterable<Fine> fines = mock(Iterable.class);

        when(fineManager.findAll()).thenReturn(fines);

        Iterable<Fine> result = fineApi.getAll();

        assertEquals(fines, result);
    }

    @Test
    public void testGetAllByUserId() {
        long userId = 1L;
        Iterable<Fine> fines = mock(Iterable.class);

        when(fineManager.findAllByUserId(userId)).thenReturn(fines);

        Iterable<Fine> result = fineApi.getAllByUserId(userId);

        assertEquals(fines, result);
    }

    @Test
    public void testAddFine_Success() {
        Fine fine = new Fine();

        when(fineManager.save(fine)).thenReturn(fine);

        ResponseEntity<Object> response = fineApi.addFine(fine);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fine, response.getBody());
    }

    @Test
    public void testAddFine_Exception() {
        Fine fine = new Fine();
        String errorMessage = "Error";

        when(fineManager.save(fine)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> response = fineApi.addFine(fine);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errorMessage, ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testAddFineByUserId_UserExists() {
        NewFineByUserId newFine = new NewFineByUserId();
        newFine.setUserId(1L);
        newFine.setReason("Late return");
        newFine.setAmount(BigDecimal.valueOf(100.0));


        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        given(userManager.findById(user.getId())).willReturn(Optional.of(user));

        Fine fine = Fine.builder()
                .user(userManager.findById(newFine.getUserId()).get())
                .reason(newFine.getReason())
                .amount(newFine.getAmount())
                .build();

        when(userManager.findById(newFine.getUserId())).thenReturn(Optional.of(fine.getUser()));
        when(fineManager.save(any(Fine.class))).thenReturn(fine);

        ResponseEntity<Object> response = fineApi.addFineByUserId(newFine);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fine, response.getBody());
    }

    @Test
    public void testAddFineByUserId_UserNotFound() {
        NewFineByUserId newFine = new NewFineByUserId();
        newFine.setUserId(1L);

        when(userManager.findById(newFine.getUserId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = fineApi.addFineByUserId(newFine);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void testUpdateFine_Success() {
        Fine fine = new Fine();
        fine.setId(1L);

        when(fineManager.findById(fine.getId())).thenReturn(Optional.of(fine));
        when(fineManager.save(fine)).thenReturn(fine);

        ResponseEntity<Object> response = fineApi.updateFine(fine);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(fine, response.getBody());
    }

    @Test
    public void testUpdateFine_NotFound() {
        Fine fine = new Fine();
        fine.setId(1L);

        when(fineManager.findById(fine.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = fineApi.updateFine(fine);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Fine not found", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testDeleteFine_Success() {
        long id = 1L;
        Fine fine = new Fine();
        fine.setId(id);

        when(fineManager.findById(id)).thenReturn(Optional.of(fine));

        ResponseEntity<Object> response = fineApi.deleteFine(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fine deleted successfully", ((SuccessResponse) response.getBody()).getMessage());
    }

    @Test
    public void testDeleteFine_NotFound() {
        long id = 1L;

        when(fineManager.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = fineApi.deleteFine(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Fine not found", ((ErrorResponse) response.getBody()).getMessage());
    }
}
