package com.studia.biblioteka.api;

import com.studia.biblioteka.NoSecurityConfig;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.Reservation;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dao.enums.CopyStatus;
import com.studia.biblioteka.dto.ErrorResponse;
import com.studia.biblioteka.dto.NewReservationByCopyId;
import com.studia.biblioteka.dto.SuccessResponse;
import com.studia.biblioteka.manager.CopyManager;
import com.studia.biblioteka.manager.ReservationManager;
import com.studia.biblioteka.manager.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ReservationApi.class)
@Import(NoSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationApiTest {

    @MockBean
    private ReservationManager reservationManager;

    @MockBean
    private CopyManager copyManager;

    @MockBean
    private UserManager userManager;

    @InjectMocks
    private ReservationApi reservationApi;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetById_ReservationExists() {
        long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);

        when(reservationManager.findById(id)).thenReturn(Optional.of(reservation));

        ResponseEntity<Object> response = reservationApi.getById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reservation, response.getBody());
    }

    @Test
    public void testGetById_ReservationNotFound() {
        long id = 1L;

        when(reservationManager.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = reservationApi.getById(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Reservation not found", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testGetAll() {
        Iterable<Reservation> reservations = mock(Iterable.class);

        when(reservationManager.findAll()).thenReturn(reservations);

        Iterable<Reservation> result = reservationApi.getAll();

        assertEquals(reservations, result);
    }

    @Test
    public void testGetAllByUserId() {
        long userId = 1L;
        Iterable<Reservation> reservations = mock(Iterable.class);

        when(reservationManager.findAllByUserId(userId)).thenReturn(reservations);

        Iterable<Reservation> result = reservationApi.getAllByUserId(userId);

        assertEquals(reservations, result);
    }

    @Test
    public void testAddReservation_Success() {
        Reservation reservation = new Reservation();

        when(reservationManager.save(reservation)).thenReturn(reservation);

        ResponseEntity<Object> response = reservationApi.addReservation(reservation);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reservation, response.getBody());
    }

    @Test
    public void testAddReservation_Exception() {
        Reservation reservation = new Reservation();
        String errorMessage = "Error";

        when(reservationManager.save(reservation)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Object> response = reservationApi.addReservation(reservation);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(errorMessage, ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testAddReservationByCopyId_Success() {
        NewReservationByCopyId newReservation = new NewReservationByCopyId();
        newReservation.setCopyId(1L);
        newReservation.setUserId(1L);

        Copy copy = new Copy();
        copy.setId(1L);
        copy.setStatus(CopyStatus.AVAILABLE);

        User user = new User();
        user.setId(1L);

        when(copyManager.findById(newReservation.getCopyId())).thenReturn(Optional.of(copy));
        when(copyManager.save(copy)).thenReturn(copy);
        when(userManager.findById(newReservation.getUserId())).thenReturn(Optional.of(user));


        Reservation reservation = Reservation.builder()
                .reservationDate(LocalDate.now())
                .copy(copy)
                .user(user)
                .status("")
                .build();

        when(reservationManager.save(any(Reservation.class))).thenReturn(reservation);

        ResponseEntity<Object> response = reservationApi.addReservation(newReservation);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testAddReservationByCopyId_CopyNotAvailable() {
        NewReservationByCopyId newReservation = new NewReservationByCopyId();
        newReservation.setCopyId(1L);

        Copy copy = new Copy();
        copy.setStatus(CopyStatus.BORROWED);

        when(copyManager.findById(newReservation.getCopyId())).thenReturn(Optional.of(copy));

        ResponseEntity<Object> response = reservationApi.addReservation(newReservation);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Copy not found or is not available", response.getBody());
    }

    @Test
    public void testAddReservationByCopyId_UserNotFound() {
        NewReservationByCopyId newReservation = new NewReservationByCopyId();
        newReservation.setCopyId(1L);
        newReservation.setUserId(1L);

        Copy copy = new Copy();
        copy.setId(1L);
        copy.setStatus(CopyStatus.AVAILABLE);

        when(copyManager.findById(newReservation.getCopyId())).thenReturn(Optional.of(copy));
        when(userManager.findById(newReservation.getUserId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = reservationApi.addReservation(newReservation);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }

    @Test
    public void testUpdateReservation_Success() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationManager.findById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(reservationManager.save(reservation)).thenReturn(reservation);

        ResponseEntity<Object> response = reservationApi.updateReservation(reservation);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reservation, response.getBody());
    }

    @Test
    public void testUpdateReservation_NotFound() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationManager.findById(reservation.getId())).thenReturn(Optional.empty());

        ResponseEntity<Object> response = reservationApi.updateReservation(reservation);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Reservation not found", ((ErrorResponse) response.getBody()).getMessage());
    }

    @Test
    public void testDeleteReservation_Success() {
        long id = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(id);


        Copy copy = new Copy();
        copy.setId(1L);
        copy.setStatus(CopyStatus.RESERVED);
        reservation.setCopy(copy);

        when(copyManager.findById(reservation.getCopy().getId())).thenReturn(Optional.of(copy));
        when(reservationManager.findById(id)).thenReturn(Optional.of(reservation));

        ResponseEntity<Object> response = reservationApi.deleteReservation(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation deleted successfully", ((SuccessResponse) response.getBody()).getMessage());
    }

    @Test
    public void testDeleteReservation_NotFound() {
        long id = 1L;

        when(reservationManager.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = reservationApi.deleteReservation(id);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Reservation not found", ((ErrorResponse) response.getBody()).getMessage());
    }
}
