package com.studia.biblioteka.dao.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.studia.biblioteka.dao.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    private LoanStatus status;
    private LocalDate maxReturnDate;

    public Loan(Long id, Copy copy, User user, LocalDate loanDate, LocalDate returnDate, LoanStatus status, LocalDate maxReturnDate) {
        this.id = id;
        this.copy = copy;
        this.user = user;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
        this.maxReturnDate = maxReturnDate;
    }

    public Loan() {

    }

}