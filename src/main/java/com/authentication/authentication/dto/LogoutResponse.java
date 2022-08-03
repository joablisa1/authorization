package com.authentication.authentication.dto;

public class LogoutResponse {
    private String mesage;

    public LogoutResponse(String mesage) {
        this.mesage = mesage;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }
}
