package com.studia.biblioteka.dao.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
@Table(name = "app_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String password;
    private String phoneNumber;


    @OneToMany(mappedBy = "user")
    private Set<Loan> loans;

    @OneToMany(mappedBy = "user")
    private Set<Reservation> reservations;

    @OneToMany(mappedBy = "user")
    private Set<Fine> fines;

    public User(Long id, String firstName,String lastName, String email, String role, String password,String phoneNumber, Set<Loan> loans, Set<Reservation> reservations, Set<Fine> fines) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.loans = loans;
        this.reservations = reservations;
        this.fines = fines;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public User() {

    }

    // getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public Set<Loan> getLoans() {
        return loans;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Set<Fine> getFines() {
        return fines;
    }
//builder

}
