package com.example.prasanna.sltuseageapp.Models;

import com.example.prasanna.sltuseageapp.Interfaces.LoginListener;

/**
 * Created by prasanna on 6/11/17.
 */

public class User {
    private Long id;
    private String user_id;
    private String user_status;
    private String password;

    public User(String user_id, String password, String user_status) {
        this.user_id = user_id;
        this.password = password;
        this.user_status = user_status;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
