package com.czz.springsecurityjwt.controller;

import com.czz.springsecurityjwt.entity.JwtRequest;
import com.czz.springsecurityjwt.entity.JwtResponse;
import com.czz.springsecurityjwt.serivce.MyUserDetailsService;
import com.czz.springsecurityjwt.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 15:49:00
 * @description :
 */
@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;


    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authRequest) throws Exception {
        System.out.println("username:"+authRequest.getUsername()+" password:"+authRequest.getPassword());
        myUserDetailsService.authentication(authRequest.getUsername(),authRequest.getPassword());
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @GetMapping("/token")
    public User getAuthenticationUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (User) myUserDetailsService.loadUserByUsername(username);
    }
}
