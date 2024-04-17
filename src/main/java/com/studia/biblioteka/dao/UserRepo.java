package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}