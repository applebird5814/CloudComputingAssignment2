package com.example.cloudass2.dao;

import com.example.cloudass2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User,String> {

    Optional<User> findByUsernameOrMailAddress(String username,String mailAddress);

    Optional<User> findByUsernameAndPassword(String username,String password);

    Optional<User> findByMailAddressAndPassword(String mailAddress, String password);

    Optional<User> findById(String id);
}
