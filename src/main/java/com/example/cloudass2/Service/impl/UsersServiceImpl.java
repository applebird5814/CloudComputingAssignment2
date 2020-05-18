package com.example.cloudass2.Service.impl;


import com.example.cloudass2.Dao.UserDao;
import com.example.cloudass2.Entity.User;
import com.example.cloudass2.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service("usersServiceImpl")
public class UsersServiceImpl implements UsersService {

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
