package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.Copy;
import org.springframework.data.repository.CrudRepository;

public interface CopyRepo extends CrudRepository<Copy, Long> {
    void deleteAllByBook_Id(Long bookId);
}
