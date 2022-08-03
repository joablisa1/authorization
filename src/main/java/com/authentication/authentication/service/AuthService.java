package com.authentication.authentication.service;

import com.authentication.authentication.dto.RequestUser;
import com.authentication.authentication.error.UserNotFoundError;
import com.authentication.authentication.model.Token;
import com.authentication.authentication.model.User;
import com.authentication.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String accessTokenSecret;
    private final String refreshTokenSecret;

    public AuthService(UserRepository repository,
                       PasswordEncoder passwordEncoder,
                       @Value("${application.security.access-token-secret}") String accessTokenSecret,
                       @Value("${application.security.refresh-token-secret}") String refreshTokenSecret) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
    }
    public User register(RequestUser requestUser) {
        if (!Objects.equals(requestUser.getPassword(), requestUser.getConfirmPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password do not match");
        return repository.save(
                User.of(requestUser.getFirstName(),
                        requestUser.getLastName(),
                        requestUser.getEmail(),
                        passwordEncoder.encode(requestUser.getPassword()))
        );
    }
    public Login login(String email, String password) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid credentials");
        }
        return Login.of(user.getId(), accessTokenSecret, refreshTokenSecret);
    }

    public User getUserFromToken(String token) {
        return repository.findById(Token.from(token, accessTokenSecret)).get();
    }

    public Login refreshAccess(String refreshToken) {
        Long userId = Token.from(refreshToken, refreshTokenSecret);
        return Login.of(userId, accessTokenSecret, Token.of(refreshToken));

    }
}
