package com.czz.springsecurityjwt.config;

import com.czz.springsecurityjwt.exception.JwtAuthenticationEntryPoint;
import com.czz.springsecurityjwt.filter.JwtRequestFilter;
import com.czz.springsecurityjwt.serivce.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author : czz
 * @version : 1.0.0
 * @create : 2020-11-07 14:47:00
 * @description :
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启在Controller层@PreAuthorize("hasAnyRole('USER')")等等..注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("authenticationPath:"+authenticationPath);
            //关闭跨域
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement()
                //设置session无状态
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //不需要权限的请求
                .and()
                .authorizeRequests()
                .antMatchers(authenticationPath).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)

                //页面关闭
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(rawPassword.toString());
            }
        });
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //AuthenticationTokenFilter 将以下的路径忽略
        web
                .ignoring()
                .antMatchers(HttpMethod.POST, authenticationPath)
                //忽略静态资源请求
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js");

    }
}
