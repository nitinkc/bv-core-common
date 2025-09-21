package com.bit.velocity.common.security.filter;

import com.bit.velocity.common.security.SecurityContextHolder;
import com.bit.velocity.common.security.UserContext;
import com.bit.velocity.common.security.jwt.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT authentication filter that extracts and validates JWT tokens from requests.
 * Sets up Spring Security context and BitVelocity user context.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenService jwtTokenService;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String token = extractTokenFromRequest(request);
            
            if (token != null && !jwtTokenService.isTokenExpired(token)) {
                UserContext userContext = jwtTokenService.extractUserContext(token);
                
                // Set BitVelocity security context
                SecurityContextHolder.setUserContext(userContext);
                
                // Set Spring Security context
                setSpringSecurityContext(request, userContext);
                
                log.debug("Authentication successful for user: {}", userContext.getUsername());
            }
        } catch (Exception e) {
            log.warn("JWT authentication failed: {}", e.getMessage());
            // Don't throw exception, let the request proceed without authentication
        }
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            // Clear security context to prevent memory leaks in thread pools
            SecurityContextHolder.clear();
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
        }
    }
    
    /**
     * Extract JWT token from Authorization header
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }
    
    /**
     * Set Spring Security authentication context
     */
    private void setSpringSecurityContext(HttpServletRequest request, UserContext userContext) {
        List<SimpleGrantedAuthority> authorities = userContext.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        
        userContext.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        
        UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(
                        userContext.getUsername(), 
                        null, 
                        authorities);
        
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        org.springframework.security.core.context.SecurityContextHolder.setContext(context);
    }
}