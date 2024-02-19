package com.geekster.wishlistservice.security.config;

import com.geekster.wishlistservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This filter is added before the authentication filter.
 * For users' that request authorization with an existing JWT token, access is granted within this filter after performing integrity tests
 * on the token.
 *
 *
 */
@Component
// Creates a constructor for any final fields
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest  request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain         filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = extractTokenFromHeader(authHeader);
        userEmail = jwtService.extractUsername(jwtToken);
        System.out.println("In custom filter..email: " + userEmail);

        // User isn't connected yet -> We need to fetch the user from the database.
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            System.out.println("user details in filter:" + userDetails);
            if(jwtService.isTokenValid(jwtToken, userDetails)) {
                userDetails.getAuthorities().forEach(e -> System.out.println(e.getAuthority()));
                // User is valid -> Update the security context and send the request to dispatcher servlet.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private static String extractTokenFromHeader(String header) {
        int i = 0;
        while(header.charAt(i) != ' ') {
            i++;
        }
        return header.substring(i + 1);
    }
}
