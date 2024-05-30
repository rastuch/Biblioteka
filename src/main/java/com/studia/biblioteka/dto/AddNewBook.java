package com.studia.biblioteka.dto;

import lombok.Builder;

@Builder
public class AddNewBook {
    public String title;
    public String authors;
    public String category;
    public Integer countOfCopy;
    public String copyLocation;
}
