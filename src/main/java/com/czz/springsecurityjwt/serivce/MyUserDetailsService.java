package com.czz.springsecurityjwt.serivce;

import com.czz.springsecurityjwt.entity.Custom;
import com.czz.springsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:59:00
 * @description :
 */
@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Custom custom = userRepository.findByLogin(login);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(custom.getRole());
        return new User(custom.getLogin(),custom.getPassword(), Collections.singletonList(authority));
    }


    public void authentication(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        }catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
