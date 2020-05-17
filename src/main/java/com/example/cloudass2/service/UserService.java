package com.example.cloudass2.service;

import com.example.cloudass2.entity.User;

import java.util.Optional;

/**
 * @author miaos
 */
public interface UserService {

    User getUser(String id);

    boolean createUser(User user);

    void deleteUser(User user);

    Optional<User> login(String username, String password);
}
