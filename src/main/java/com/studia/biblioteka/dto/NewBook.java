package com.studia.biblioteka.dto;

import com.studia.biblioteka.dao.entity.Book;

public class NewBook {
    private String bookTitle;
    private String categoryName;
    private String authorFirstName;
    private String authorLastName;
    private String authorBiography;

    public NewBook(String bookTitle, String categoryName, String authorFirstName, String authorLastName, String authorBiography) {
        this.bookTitle = bookTitle;
        this.categoryName = categoryName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorBiography = authorBiography;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorBiography() {
        return authorBiography;
    }

    public void setAuthorBiography(String authorBiography) {
        this.authorBiography = authorBiography;
    }

    public NewBook() {
    }

}
