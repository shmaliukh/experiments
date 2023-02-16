package com.vshmaliukh.test_project;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;

import static com.vshmaliukh.test_project.WebSecurityConfig.REMEMBER_ME_COOKIE_NAME;
import static com.vshmaliukh.test_project.WebSecurityConfig.REMEMBER_ME_TOKEN_VALIDITY_IN_SECONDS;

public class CookieExpiryRefresher implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        refreshRememberMeCookie(request, response);
    }

    private static void refreshRememberMeCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean cookiesContainsRememberMe = Arrays.stream(cookies)
                .anyMatch(cookie -> cookie.getName().contentEquals(REMEMBER_ME_COOKIE_NAME));
        for (Cookie cookie : cookies) {
            if (cookie.getName().contentEquals(REMEMBER_ME_COOKIE_NAME)
                    || (cookiesContainsRememberMe && cookie.getName().contentEquals(REMEMBER_ME_COOKIE_NAME))) {
                refreshCookie(REMEMBER_ME_TOKEN_VALIDITY_IN_SECONDS, response, cookie);
            }
        }
    }

    private static void refreshCookie(int timeInSeconds, HttpServletResponse response, Cookie cookie) {
        cookie.setMaxAge(timeInSeconds);
        response.addCookie(cookie);
    }

}