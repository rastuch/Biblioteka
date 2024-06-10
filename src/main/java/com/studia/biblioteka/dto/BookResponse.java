package com.studia.biblioteka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String authors;
    private String category;
    private Boolean isAvailable;

    public BookResponse() {}

    public BookResponse(Long id, String title, String authors, String category, Boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.category = category;
        this.isAvailable = isAvailable;
    }
}
