package com.Security.Authentication.Service;

import com.Security.Authentication.Model.User;
import com.Security.Authentication.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user= userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("user not found"));


        return user;
    }
}

// private final UserService userService;
//     @SuppressWarnings("unused")
//     private final PasswordEncoder passwordEncoder; // Add PasswordEncoder

//     // Injecting UserService and PasswordEncoder
//     public CustomUserDetailsService(UserService userService, PasswordEncoder passwordEncoder) {
//         this.userService = userService;
//         this.passwordEncoder = passwordEncoder;
//     }
// Find user by username (email in this case)



// Optional<User> userOptional = userService.getUsers().stream()
// .filter(user -> user.getEmail().equals(username))  // Match email
// .findFirst();

// if (userOptional.isPresent()) {
// User user = userOptional.get();  // Get the custom User object

// // Return Spring Security User with actual password
// return org.springframework.security.core.userdetails.User.builder()
//     .username(user.getEmail())  // Use email as the username
//     .password(user.getPassword())  // Use actual password
//     .roles("USER")  // You can add more roles if necessary
//     .build();
// } else {
// throw new UsernameNotFoundException("User not found with username: " + username);
// }