package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
@Table(name = "app_user")
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

    public User(Long id, String name, String email, String role, String password, Set<Loan> loans, Set<Reservation> reservations, Set<Fine> fines) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.loans = loans;
        this.reservations = reservations;
        this.fines = fines;
    }

    public User() {

    }

    // getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoans(Set<Loan> loans) {
        this.loans = loans;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void setFines(Set<Fine> fines) {
        this.fines = fines;
    }

    //builder

}
