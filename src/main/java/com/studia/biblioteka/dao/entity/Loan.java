package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Copy copy;

    @ManyToOne
    private User user;

    private LocalDate loanDate;
    private LocalDate returnDate;
    private String status;

    public Loan(Long id, Copy copy, User user, LocalDate loanDate, LocalDate returnDate, String status) {
        this.id = id;
        this.copy = copy;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public Loan() {

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

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
// getters and setters
}