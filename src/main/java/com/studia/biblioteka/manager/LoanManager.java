package com.studia.biblioteka.manager;

import com.studia.biblioteka.dao.FineRepo;
import com.studia.biblioteka.dao.LoanRepo;
import com.studia.biblioteka.dao.entity.Fine;
import com.studia.biblioteka.dao.entity.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanManager {
    private final LoanRepo loanRepo;
    @Autowired
    public LoanManager(LoanRepo loanRepo){
        this.loanRepo = loanRepo;
    }

    public Optional<Loan> findById(Long id) {
        return loanRepo.findById(id);
    }
    public Iterable<Loan> findAll() {
        return loanRepo.findAll();
    }

    public Loan save(Loan loan) {
        return loanRepo.save(loan);
    }
    public void delete(Long id) {
        loanRepo.deleteById(id);
    }
}
