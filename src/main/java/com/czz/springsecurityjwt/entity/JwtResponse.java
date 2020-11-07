package com.czz.springsecurityjwt.entity;

import java.io.Serializable;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:00:00
 * @description :JwtResponse，相应封装，主要包含jwtToken字段，直接返回对象即可
 */
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String jwtToken;
    public JwtResponse(String jwttoken) {
        this.jwtToken = jwttoken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
