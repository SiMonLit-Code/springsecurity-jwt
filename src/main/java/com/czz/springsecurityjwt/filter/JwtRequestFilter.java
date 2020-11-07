package com.czz.springsecurityjwt.filter;

import com.czz.springsecurityjwt.serivce.MyUserDetailsService;
import com.czz.springsecurityjwt.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 15:04:00
 * @description :JwtRequestFilter，过滤JWT请求，验证"Bearer token"格式，校验Token是否正确
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String requestHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        //JWT报文表头的格式是“Bearer token”，去除Bearer，直接获取token
        if (requestHeader != null && requestHeader.startsWith("Bearer")){
            jwtToken = requestHeader.substring(7);
            try{
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        }else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        //校验
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)){
                //授权用户
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //将授权成功的用户保存到SecurityContextHolder，交给SpringSecurity，可以全局获取到
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }else {
            logger.warn("username is null or AuthenticationObject is null");
        }
        filterChain.doFilter(request,response);
    }
}
