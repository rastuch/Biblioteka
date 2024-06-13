package com.studia.biblioteka.dto;

import com.studia.biblioteka.dao.enums.CopyStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewCopy {
    public Long bookId;
    public CopyStatus status;
    public String location;

    public NewCopy(Long bookId, CopyStatus status, String location) {
        this.bookId = bookId;
        this.status = status;
        this.location = location;
    }

    public NewCopy() {

    }
}
