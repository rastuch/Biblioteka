package com.studia.biblioteka.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class NewFineByUserId {
    Long userId;
    BigDecimal amount;
    String reason;

    public NewFineByUserId(Long userId, BigDecimal amount, String reason) {
        this.userId = userId;
        this.amount = amount;
        this.reason = reason;
    }

    public NewFineByUserId() {
    }
}
