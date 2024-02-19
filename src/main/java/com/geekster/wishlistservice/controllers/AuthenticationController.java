package com.geekster.wishlistservice.controllers;

import com.geekster.wishlistservice.service.AuthenticationService;
import com.geekster.wishlistservice.utils.dto.AuthenticationRequest;
import com.geekster.wishlistservice.utils.dto.AuthenticationResponse;
import com.geekster.wishlistservice.utils.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  This RestController has 2 methods: to create a new account and to authenticate an existing account.
 *  Valid requests to these endpoints will return a JWT token that's valid for 24hr post creation.
 */
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
