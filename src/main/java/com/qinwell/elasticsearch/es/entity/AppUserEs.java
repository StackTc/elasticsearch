package com.qinwell.elasticsearch.es.entity;

import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * <p>
 * Project Name: Qinwell
 * <br>
 * Description:
 * <br>
 * File Name: AppUserEs
 * <br>
 * Copyright: Copyright (C) 2015 All Rights Reserved.
 * <br>
 * Company: 杭州勤淮科技有限公司
 * <br>
 *
 * @author lintiancheng
 * @create time：2018/12/23 1:07 AM
 * @version: v1.0
 */


@Document(indexName = "stacktc", type = "test")
public class AppUserEs implements Serializable {

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}