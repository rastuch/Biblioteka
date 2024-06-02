package com.studia.biblioteka.dto;

import com.studia.biblioteka.dao.entity.Copy;
import com.studia.biblioteka.dao.entity.User;
import com.studia.biblioteka.dao.enums.LoanStatus;

import java.time.LocalDate;

public class LoanResponse {
    public Long id;
    public Copy copyId;
    public User userId;
    public LocalDate loanDate;
    public LocalDate returnDate;
    public LoanStatus status;
    public LocalDate maxReturnDate;
}
