package com.studia.biblioteka.dto;

import com.studia.biblioteka.dao.enums.CopyStatus;

public class NewCopy {
    public Long bookId;
    public CopyStatus status;
    public String location;
}
