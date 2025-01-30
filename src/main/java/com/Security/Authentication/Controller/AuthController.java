package com.Security.Authentication.Controller;

import com.Security.Authentication.Service.CustomUserDetailsService;
import com.Security.Authentication.Service.UserService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.Security.Authentication.Model.JwtRequest;
import com.Security.Authentication.Model.JwtResponse;
import com.Security.Authentication.Model.User;
import com.Security.Authentication.Security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        // Authenticate the user
        this.doAuthenticate(request.getEmail(), request.getPassword());

        // Retrieve user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generate the token
        String token = this.helper.generateToken(userDetails);

        // Build and return the response
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(email, password);

        try {
            // Authenticate using AuthenticationManager
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            // Log the exception and throw
            logger.error("Authentication failed for user: {}", email);
            throw new BadCredentialsException("Invalid Username or Password!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Invalid credentials provided!";
    }
    @PostMapping("/create-user")
    public User createUser(@RequestBody User user)
    {
        return userService.createUser(user);
    }
}

