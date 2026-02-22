package com.codes.auth_service.controller;

import com.codes.auth_service.dto.RegisterRequest;
import com.codes.auth_service.dto.TokenResponse;
import com.codes.auth_service.service.IAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest registerRequest) {
        final TokenResponse tokenResponse = authService.register(registerRequest);

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody RegisterRequest loginRequest){
        final TokenResponse token = authService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken){
        return this.authService.refreshToken(authToken);
    }
}
