package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Fine;
import org.springframework.data.repository.CrudRepository;

public interface FineRepo extends CrudRepository<Fine, Long> {
    Iterable<Fine> findAllByUserId(Long userId);
}
