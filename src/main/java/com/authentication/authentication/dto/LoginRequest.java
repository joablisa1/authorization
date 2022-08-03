package com.authentication.authentication.dto;

import com.authentication.authentication.model.Token;
import com.authentication.authentication.model.User;

public class LoginRequest {
    private Long id;
    private String email;
    private String password;

    public LoginRequest(Long id, String email, String password, Token token){
        this.id=id;
        this.email=email;
        this.password=password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
