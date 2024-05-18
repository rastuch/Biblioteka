package com.studia.biblioteka.dto;

import lombok.Getter;

@Getter
public class UserLogin {
    private String email;
    private String password;

    public String setEmail(String email) {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
