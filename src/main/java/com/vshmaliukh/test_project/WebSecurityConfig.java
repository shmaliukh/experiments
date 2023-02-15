package com.vshmaliukh.test_project;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
public class WebSecurityConfig {
    public static final String REMEMBER_ME_KEY_STR = "uniqueAndSecret";

//    @Value("${app.rememberMeTime}")
//    int rememberMeTime;

    final MyUserDetailsService userDetailsService;


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
        return new CustomRememberMeServices(REMEMBER_ME_KEY_STR, userDetailsService, new InMemoryTokenRepositoryImpl());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        MyUserDetailsService userDetailsService1 = userDetailsService;
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
                .key(REMEMBER_ME_KEY_STR)
                .tokenValiditySeconds(10)
                .rememberMeParameter("remember-me")
                    .and()
                .logout()
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .permitAll()
        ;
        ;
        //Remember Me cookie contains the following data:
        //username – to identify the logged-in principal
        //expirationTime – to expire the cookie; default is 2 weeks
        //MD5 hash – of the previous 2 values – username and expirationTime, plus the password and the predefined key

        return http.build();
    }

}
