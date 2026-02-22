package com.codes.auth_service.service.impl;


import com.codes.auth_service.dto.LoginRequest;
import com.codes.auth_service.dto.RegisterRequest;
import com.codes.auth_service.dto.TokenResponse;
import com.codes.auth_service.model.Token;
import com.codes.auth_service.model.User;
import com.codes.auth_service.repository.ITokenRepository;
import com.codes.auth_service.repository.IUserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl {

    private final PasswordEncoder passwordEncoder;
    private ITokenRepository tokenRepository;
    private IUserRepository userRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthServiceImpl(ITokenRepository tokenRepository,  IUserRepository userRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    public TokenResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .password( this.passwordEncoder.encode(registerRequest.password()))
                .build();

        User userSaved = this.userRepository.save(user);
        String token = this.jwtService.generateToken(user);
        String tokenRefresh = this.jwtService.generateRefreshToken(user);

        this.saveUserToken(userSaved, tokenRefresh);

        return new TokenResponse(token, tokenRefresh);
    };

    public TokenResponse login(LoginRequest loginRequest) {
        this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()
                )
        );

        com.codes.auth_service.model.User user = this.userRepository.findByEmail(loginRequest.email()).orElseThrow();

        String jwtToken = this.jwtService.generateToken(user);
        String tokenRefresh = this.jwtService.generateRefreshToken(user);
        this.revokeAllUserTokens(user);
        this.saveUserToken(user, tokenRefresh);

        return new TokenResponse(jwtToken, tokenRefresh);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserToken = this.tokenRepository.findAllValidIsFalseOrRevokedIsFalseByUserId(user.getId());

        if (!validUserToken.isEmpty()) {
            for (Token token : validUserToken) {
                token.setRevoked(true);
                token.setExpired(true);
            }

            this.tokenRepository.saveAll(validUserToken);
        }
    }
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(Token.ETokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        this.tokenRepository.save(token);
    }
}
