package com.czz.springsecurityjwt.repository;

import com.czz.springsecurityjwt.entity.Custom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:54:00
 * @description :
 */
public interface UserRepository extends JpaRepository<Custom ,String> {
    Custom findByLogin(String login);
}
