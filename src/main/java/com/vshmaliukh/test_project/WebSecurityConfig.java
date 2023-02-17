package com.vshmaliukh.test_project;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;


@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Value("${app.rememberMe.time:1000}")
    private int rememberMeTime;

    @Value("${app.rememberMe.cookieName:rememberMe}")
    private String rememberMeCookieName;

    @Value("${app.rememberMe.key:uniqueAndSecret}")
    private String rememberMeKey;

    private final MyUserDetailsService userDetailsService;

    public WebSecurityConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        return new CustomRememberMeServices(rememberMeKey, userDetailsService, new InMemoryTokenRepositoryImpl());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/")
                .permitAll()
                .anyRequest().authenticated()
                    .and()
                .formLogin()
                .defaultSuccessUrl("/hello")
                .permitAll()
                    .and()
                .rememberMe()
                .rememberMeServices(rememberMeServices())
                .key(rememberMeKey)
                .tokenValiditySeconds(rememberMeTime)
                .rememberMeParameter(rememberMeCookieName)
                    .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .permitAll()
        ;
        //Remember Me cookie contains the following data:
        //username – to identify the logged-in principal
        //expirationTime – to expire the cookie; default is 2 weeks
        //MD5 hash – of the previous 2 values – username and expirationTime, plus the password and the predefined key

        return http.build();
    }

}
