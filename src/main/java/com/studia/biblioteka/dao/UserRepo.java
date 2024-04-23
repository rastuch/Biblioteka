package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long>, JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}