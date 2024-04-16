package com.studia.biblioteka.dao;
import jakarta.persistence.*;

import java.time.LocalDate;

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

    // getters and setters
}
