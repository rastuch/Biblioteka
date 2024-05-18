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
    private String firstName;
    private String lastName;
    private String biography;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books;

    public Author() {

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Author(Long id, String firstName, String lastName,String biography, Set<Book> books) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.books = books;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
// getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
