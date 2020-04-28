package com.example.cloudass2.service;

import com.example.cloudass2.Entity.User;

import java.util.Optional;

public interface UserService {

    User getUser(String id);

    boolean createUser(User user);

    void deleteUser(User user);

    Optional<User> login(String username, String password);
}
