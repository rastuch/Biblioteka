package com.studia.biblioteka.dao;

import com.studia.biblioteka.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long>, JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE " +
            "(u.email LIKE %:searchTerm% OR " +
            "u.firstName LIKE %:searchTerm% OR " +
            "u.lastName LIKE %:searchTerm% OR " +
            "u.phoneNumber LIKE %:searchTerm%) OR " +
            "u.role IN :roles")
    Iterable<User> findAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContainingOrRoleIsIn(
            @Param("searchTerm") String searchTerm,
            @Param("roles") List<String> roles);
    @Query("SELECT u FROM User u WHERE " +
            "(u.email LIKE %:searchTerm% OR " +
            "u.firstName LIKE %:searchTerm% OR " +
            "u.lastName LIKE %:searchTerm% OR " +
            "u.phoneNumber LIKE %:searchTerm%)")
    Iterable<User> findAllByEmailContainingOrFirstNameContainingOrLastNameContainingOrPhoneNumberContaining(
            @Param("searchTerm") String searchTerm);
    Iterable<User> findAllByRoleIsIn(List<String> role);
}