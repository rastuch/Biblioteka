package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String biography;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;

    public Author() {

    }
    public Author(Long id, String name, String biography, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.books = books;
    }
    // getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
