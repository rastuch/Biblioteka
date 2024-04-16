package com.studia.biblioteka.dao;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @ManyToMany
    private Set<Author> authors;

    @ManyToOne
    private Category category;

    // getters and setters
}
