package com.studia.biblioteka.dao.entity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Book> books;

    public Category(Long id, String name, Set<Book> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Category() {

    }

    // getters and setters

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
