package com.studia.biblioteka.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Entity
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal amount;
    private String reason;
    private LocalDate imposedDate;

    public Fine(Long id, User user, BigDecimal amount, String reason, LocalDate imposedDate) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.reason = reason;
        this.imposedDate = imposedDate;
    }

    public Fine() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setImposedDate(LocalDate imposedDate) {
        this.imposedDate = imposedDate;
    }
// getters and setters
}
