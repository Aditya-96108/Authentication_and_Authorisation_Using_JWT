package com.Security.Authentication.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Security.Authentication.Model.User;

public interface  UserRepository extends JpaRepository<User, String>{
    public Optional<User> findByEmail(String email);
}
