package com.vshmaliukh.test_project;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vshmaliukh.test_project.entities.User;
import com.vshmaliukh.test_project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;


public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    @Autowired
    private UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private PersistentTokenRepository tokenRepository;
    private String key;

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
        this.key = key;
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        Object principal = successfulAuthentication.getPrincipal();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
        String username = user.getUsername();
        logger.debug("Creating new persistent login for user " + username);
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
        try {
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to save persistent token", e);
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        User userFromRepository = userRepository.findByName(user.getUsername());
        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, userFromRepository, authoritiesMapper.mapAuthorities(user.getAuthorities()));
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()}, getTokenValiditySeconds(), request, response);
    }

}