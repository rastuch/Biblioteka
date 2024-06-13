package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.Reservation;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dao.enums.CopyStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReservationRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepo reservationRepo;

    private User user;
    private Copy copy;
    private Reservation reservation1, reservation2;

    @Before
    public void setUp() {
        user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("Anna");
        user.setLastName("Kowalska");
        entityManager.persist(user);

        copy = Copy.builder()
                .location("Shelf A")
                .status(CopyStatus.AVAILABLE)
                .build();
        entityManager.persist(copy);

        reservation1 = Reservation.builder()
                .user(user)
                .copy(copy)
                .reservationDate(LocalDate.now())
                .status("Active")
                .build();
        reservation2 = Reservation.builder()
                .user(user)
                .copy(copy)
                .reservationDate(LocalDate.now().minusDays(1))
                .status("Completed")
                .build();
        entityManager.persist(reservation1);
        entityManager.persist(reservation2);
        entityManager.flush();
    }

    @Test
    public void whenFindAllByUserId_thenReturnReservations() {
        Iterable<Reservation> reservations = reservationRepo.findAllByUserId(user.getId());
        assertThat(reservations).containsExactlyInAnyOrder(reservation1, reservation2);
    }
}
