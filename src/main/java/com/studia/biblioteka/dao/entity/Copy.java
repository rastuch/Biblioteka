package com.studia.biblioteka.dao.entity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Book book;
    private String status;
    private String location;

    public Copy(Long id, Book book, String status, String location) {
        this.id = id;
        this.book = book;
        this.status = status;
        this.location = location;
    }

    public Copy() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLocation(String location) {
        this.location = location;
    }
// getters and setters
}
