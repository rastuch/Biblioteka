package com.studia.biblioteka.dao.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal amount;
    private String reason;

    public Fine(Long id, User user, BigDecimal amount, String reason) {
        this.id = id;
        this.user = user;
        this.amount = amount;
        this.reason = reason;
    }

    public Fine() {

    }

}
