package com.studia.biblioteka.dao;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String role;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Loan> loans;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private Set<Fine> fines;

    // getters and setters
}
