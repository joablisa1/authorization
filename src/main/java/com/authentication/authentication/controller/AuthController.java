package com.authentication.authentication.controller;

import com.authentication.authentication.dto.*;
import com.authentication.authentication.model.Token;
import com.authentication.authentication.model.User;
import com.authentication.authentication.service.AuthService;
import com.authentication.authentication.service.Login;
import jdk.nashorn.internal.ir.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    AuthService authService;
    public AuthController(AuthService userService){
        this.authService=authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RequestUser requestUser){
       User user=authService.register(requestUser);
       return new RegisterResponse(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail());
    }
    @PostMapping("/login")
    public LoginResponse loginRequest(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Login login=authService.login(loginRequest.getEmail(),loginRequest.getPassword());
        Cookie  cookie=new Cookie("refresh_token",login.getRefreshToken().getToken());
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        cookie.setPath("/api");
        response.addCookie(cookie);
        return new LoginResponse(login.getAccessToken().getToken());
    }
    @GetMapping("/user")
    public UserResponse user(HttpServletRequest request){
        User user=(User)request.getAttribute("user");
        return new UserResponse(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail());
    }
    @PostMapping("/refresh")
    public RefreshResponse refresh(@CookieValue("refresh_token") String refreshToken){
        return new RefreshResponse(authService.refreshAccess(refreshToken).getAccessToken().getToken());
    }
    @PostMapping("/logout")
    public LogoutResponse logout(@CookieValue("refresh_token") String refreshToken,HttpServletResponse response){
        Cookie cookie = new Cookie("refresh token",null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return  new LogoutResponse("success");


    }
}

