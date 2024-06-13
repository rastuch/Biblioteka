package com.studia.biblioteka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class NewLoan {
    public Long userId;
    public Long copyId;

    public NewLoan() {
    }

    public NewLoan(Long userId, Long copyId) {
        this.userId = userId;
        this.copyId = copyId;
    }
}
