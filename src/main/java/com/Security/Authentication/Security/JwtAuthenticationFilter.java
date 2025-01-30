package com.Security.Authentication.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);  
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, java.io.IOException {

        String requestHeader = request.getHeader("Authorization");
        logger.info("Authorization Header: {}", requestHeader);  // Log the header for debugging

        String username = null;
        String token = null;

        // Check if the Authorization header is present and starts with "Bearer "
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            // Extract token from the header
            token = requestHeader.substring(7);
            try {
                // Retrieve username from the token
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                logger.warn("Illegal Argument while fetching the username from token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                logger.warn("The JWT token has expired: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                logger.warn("The JWT token is malformed: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Token processing error: {}", e.getMessage());
            }
        } else {
            // Log when the header is missing or incorrect
            if (requestHeader == null) {
                logger.warn("Authorization header is missing.");
            } else {
                logger.warn("Authorization header does not start with 'Bearer '. Header: {}", requestHeader);
            }
        }

        // If username is extracted, and no authentication is present in the SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the username
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the JWT token
            Boolean isValidToken = this.jwtHelper.validateToken(token, userDetails);
            if (isValidToken) {
                // If valid, set authentication in SecurityContext
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentication successful for user: {}", username);
            } else {
                logger.warn("JWT Token validation failed for user: {}", username);
            }
        } else {
            // If token is not valid or no username, log that as well
            if (username == null) {
                logger.warn("No valid username extracted from the JWT token.");
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
