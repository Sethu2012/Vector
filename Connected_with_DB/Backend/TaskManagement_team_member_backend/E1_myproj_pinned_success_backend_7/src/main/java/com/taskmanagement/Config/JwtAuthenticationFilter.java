package com.taskmanagement.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final UserDetailsService userDetailsService;
    private final String jwtSecret = "UXcKK7SD/3rCi0uQkVZeJhXNWu4Gi+uTk4bF9ryRKzGEcoMfk4rOmdZr9l3qitn2uF7csNPKWS2zlAbDAkcdTg==";

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = null;
        String authHeader = request.getHeader("Authorization");
        String username = null;

        // Check the Authorization header first
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            logger.debug("JWT token found in Authorization header for request: {}", request.getRequestURI());
        } 
        // If not found in header, check the query parameter
        else {
            jwt = request.getParameter("token");
            if (jwt != null) {
                logger.debug("JWT token found in query parameter for request: {}", request.getRequestURI());
            } else {
                logger.debug("No JWT token found in request for: {}", request.getRequestURI());
            }
        }

        // If a token was found (from header or query), validate it
        if (jwt != null) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(Base64.getDecoder().decode(jwtSecret))
                        .parseClaimsJws(jwt)
                        .getBody();
                username = claims.getSubject();
                logger.debug("JWT token parsed successfully for user: {}", username);
            } catch (Exception e) {
                logger.error("Failed to parse JWT token: {}", e.getMessage(), e);
            }
        }

        // Set authentication if token is valid
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Authentication set for user: {}", username);
            } catch (Exception e) {
                logger.error("Failed to load user details for username: {}", username, e);
            }
        }

        filterChain.doFilter(request, response);
    }
}