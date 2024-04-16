package com.studia.biblioteka.dao;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal amount;
    private String reason;
    private LocalDate imposedDate;

    // getters and setters
}
