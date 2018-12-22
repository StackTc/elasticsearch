package com.qinwell.elasticsearch.entity.user;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * app_user
 * @author 
 */
@Document(indexName = "stacktc", type = "test")
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