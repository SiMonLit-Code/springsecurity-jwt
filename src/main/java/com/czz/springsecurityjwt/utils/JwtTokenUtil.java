package com.czz.springsecurityjwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:05:00
 * @description :JWT工具类，生成/验证/是否过期token
 */
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 将从Token中Subject
     * @param token
     * @return
     */
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 将从Token中ExpirationDate
     * @param token
     * @return
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 将从Token中Body查询到的Claims进行转换
     * @param token
     * @param claimsTFunction
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsTFunction.apply(claims);
    }

    /**
     * 查询Token中Body
     * @param token
     * @return
     */
    public Claims getAllClaimsFromToken(String token){
//        Map<String, Claim> claims = JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getClaims();
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 判断token是否过期
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * 生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerationToken(claims, userDetails.getUsername());
    }

    /**
     * 组装Token
     * @param claims
     * @param subject
     * @return
     */
    private String doGenerationToken(Map<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 校验token
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
