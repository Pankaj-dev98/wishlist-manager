package com.geekster.wishlistservice.service;

import com.geekster.wishlistservice.entity.Role;
import com.geekster.wishlistservice.entity.User;
import com.geekster.wishlistservice.repository.RoleRepository;
import com.geekster.wishlistservice.repository.UserRepository;
import com.geekster.wishlistservice.utils.dto.AuthenticationRequest;
import com.geekster.wishlistservice.utils.dto.AuthenticationResponse;
import com.geekster.wishlistservice.utils.dto.RegisterRequest;
import com.geekster.wishlistservice.utils.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service class is delegated the responsibility of registering new users and validating existing users.
 * Each new user of the application is registered as a customer and may henceforth, be allowed to login using his password to receive JWT tokens
 * and manipulate their wishlists using the mentioned tokens.
 *
 * A user is bound to their email(case-insensitive). An email can't be used to register multiple customer entities.
 * Note: Emails are always persisted in a lower case.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerService customerService;
    private final RoleRepository roleRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        request.setEmail(request.getEmail().toLowerCase());

        if(repository.findByEmail(request.getEmail()).isPresent())
            throw new UserAlreadyExistsException(request.getEmail() + " is already registered!");

        var role = roleRepository.findByRole(Role.RoleType.CUSTOMER);
        User user = User.builder()
                .name(request.getFirstName().concat(" ").concat(request.getLastName()))
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .build();

        repository.save(user);
        String jwtToken = jwtService.generateToken(user);

        customerService.registerNewCustomer(request);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        request.setEmail(request.getEmail().toLowerCase());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}