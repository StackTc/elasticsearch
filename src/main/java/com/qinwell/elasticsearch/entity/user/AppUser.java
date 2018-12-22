package com.qinwell.elasticsearch.entity.user;

import java.io.Serializable;

/**
 * app_user
 * @author 
 */
public class AppUser implements Serializable {
    private String username;

    private String password;

    private static final long serialVersionUID = 1L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}