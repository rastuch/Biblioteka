package com.studia.biblioteka.dao;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String biography;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;

    // getters and setters
}
