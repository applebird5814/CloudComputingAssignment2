package com.example.cloudass2.Dao;

import com.example.cloudass2.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDao extends JpaRepository<User,String> {

    Optional<User> findByUsernameOrMailAddress(String username,String mailAddress);

    Optional<User> findByUsernameAndPassword(String username,String password);

    Optional<User> findByMailAddressAndPassword(String mailAddress, String password);

    Optional<User> findById(String id);
}
