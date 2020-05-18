package com.example.cloudass2.Service;

import com.example.cloudass2.Entity.User;

import java.util.Optional;

/**
 * @author miaos
 */
public interface UsersService {

    User getUser(String id);

    boolean createUser(User user);

    void deleteUser(User user);

    Optional<User> login(String username, String password);
}
