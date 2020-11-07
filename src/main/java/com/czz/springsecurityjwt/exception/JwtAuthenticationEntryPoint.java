package com.czz.springsecurityjwt.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:49:00
 * @description :JwtAuthenticationEntryPoint，实现AuthenticationEntryPoint类，返回认证不通过的信息
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = 1L;
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
    }
}
