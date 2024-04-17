package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
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

    public Book(Long id, String title, Set<Author> authors, Category category) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.category = category;
    }

    public Book() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
// getters and setters
}
