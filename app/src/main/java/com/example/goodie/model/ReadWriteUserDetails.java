package com.example.goodie.model;

public class ReadWriteUserDetails {
    public String username, email, password, type, date;

    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String email, String password, String type, String date) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.date = date;
    }
}
