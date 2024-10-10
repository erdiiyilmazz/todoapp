package com.erdi.todoapp.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;

    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                    @NonNull HttpServletResponse response, 
                                    @NonNull FilterChain filterChain) 
                                    throws ServletException, IOException {
        try {
            final Optional<String> token = extractTokenFromRequest(request);
            if (token.isPresent()) {
                SecurityContextHolder.getContext().setAuthentication(getAuthenticationFromToken(token.get()));
            } else {
                SecurityContextHolder.clearContext();
            }
            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }

    private Authentication getAuthenticationFromToken(String token) {

        final String username = tokenProvider.getUserIdFromJWT(token);

        return new UsernamePasswordAuthenticationToken(username, null, null);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {

        final String bearerPrefix = "Bearer ";
        final String authHeaderKey = "Authorization";

        return Optional.ofNullable(request.getHeader(authHeaderKey))
                .filter(authHeader -> authHeader.startsWith(bearerPrefix))
                .map(authHeader -> authHeader.substring(bearerPrefix.length()));
    }
}
