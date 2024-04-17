package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepo extends CrudRepository<Loan, Long> {
}
