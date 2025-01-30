package com.Security.Authentication.Service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.Security.Authentication.Model.User;
import com.Security.Authentication.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User createUser(User user)
    {
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
