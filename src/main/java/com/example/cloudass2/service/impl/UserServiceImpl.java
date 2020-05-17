package com.example.cloudass2.service.impl;


import com.example.cloudass2.dao.UserDao;
import com.example.cloudass2.entity.User;
import com.example.cloudass2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String id) {
        return userDao.findById(id).get();
    }

    @Override
    public boolean createUser(User user) {
        if(userDao.findByUsernameOrMailAddress(user.getUsername(),user.getMailAddress()).isPresent()) {
            return false;
        }
        userDao.saveAndFlush(user);
        return true;
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public Optional<User> login(String username, String password) {
        return userDao.findByUsernameAndPassword(username,password);
    }
}
