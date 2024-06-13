package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.ReservationRepo;
import com.studia.biblioteka.dao.entity.Reservation;
import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ReservationManagerTest {

    @Mock
    private ReservationRepo reservationRepo;

    @InjectMocks
    private ReservationManager reservationManager;

    private Reservation reservation;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        Copy copy = new Copy();
        copy.setId(1L);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        reservation.setCopy(copy);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus("Active");

        when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepo.findAll()).thenReturn(Arrays.asList(reservation));
        when(reservationRepo.findAllByUserId(1L)).thenReturn(Arrays.asList(reservation));
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservation);
    }

    @Test
    public void whenFindById_thenReturnReservation() {
        Optional<Reservation> found = reservationManager.findById(1L);
        assertTrue(found.isPresent());
        assertEquals(reservation, found.get());
    }

    @Test
    public void whenFindAll_thenReturnAllReservations() {
        Iterable<Reservation> reservations = reservationManager.findAll();
        assertTrue(reservations.iterator().hasNext());
        assertEquals(reservation, reservations.iterator().next());
    }

    @Test
    public void whenFindAllByUserId_thenReturnUserReservations() {
        Iterable<Reservation> reservations = reservationManager.findAllByUserId(1L);
        assertTrue(reservations.iterator().hasNext());
        assertEquals(reservation, reservations.iterator().next());
    }

    @Test
    public void whenSave_thenReturnSavedReservation() {
        Reservation savedReservation = reservationManager.save(reservation);
        assertNotNull(savedReservation);
        assertEquals(reservation.getId(), savedReservation.getId());
    }

    @Test
    public void whenDelete_thenShouldInvokeDelete() {
        reservationManager.delete(1L);
        verify(reservationRepo).deleteById(1L);
    }
}
