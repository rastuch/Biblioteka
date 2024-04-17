package com.studia.biblioteka.dao.entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Copy copy;

    @ManyToOne
    private User user;

    private LocalDate reservationDate;
    private String status;

    public Reservation(Long id, Copy copy, User user, LocalDate reservationDate, String status) {
        this.id = id;
        this.copy = copy;
        this.user = user;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public Reservation() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// getters and setters
}
