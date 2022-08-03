package com.authentication.authentication.interceptor;

import com.authentication.authentication.service.AuthService;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private  final AuthService authService;


    public AuthorizationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader =request.getHeader("Authorization");
        if(authorizationHeader==null || !authorizationHeader.startsWith("Bearer"))
            System.out.println("sorry nto working fine");
        request.setAttribute("user",authService.getUserFromToken(authorizationHeader.substring(7)));
        return true;
    }
}
