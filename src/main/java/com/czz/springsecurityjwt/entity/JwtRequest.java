package com.czz.springsecurityjwt.entity;

import java.io.Serializable;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 13:57:00
 * @description :
 * JwtRequest 请求封装，主要包含username和password字段，前台发后台的时候发json，@RequestBody可以直接转换。
 */
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;

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
