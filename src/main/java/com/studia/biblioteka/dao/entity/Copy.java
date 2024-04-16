package com.studia.biblioteka.dao;
import jakarta.persistence.*;

@Entity
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Book book;
    private String status;
    private String location;

    // getters and setters
}
