package com.czz.springsecurityjwt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 16:28:00
 * @description :
 */
@Entity
public class Custom implements Serializable {
    @Id
    @Column
    private Long id;
    @Column
    private String login;
    @Column
    private String password;
    @Column
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
