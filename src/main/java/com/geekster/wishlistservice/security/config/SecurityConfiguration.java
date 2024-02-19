package com.geekster.wishlistservice.security.config;

import com.geekster.wishlistservice.entity.Role;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Authentication and authorizations for API requests are configured here.
 * The sessions are stateless, therefore no cookies are retained in this application.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("In SecurityFilterChain.class");

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(configurer -> {
                    configurer
                            .requestMatchers("/api/customers/register", "/api/customers/authenticate")
                            .permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/items").hasAuthority(Role.RoleType.ADMIN.name())
                            .requestMatchers(HttpMethod.PUT, "/api/items/**").hasAuthority(Role.RoleType.ADMIN.name())
                            .requestMatchers(HttpMethod.DELETE, "/api/items/**").hasAuthority(Role.RoleType.ADMIN.name())
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}