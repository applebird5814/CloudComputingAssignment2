package com.example.cloudass2.dao;

import com.example.cloudass2.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeDao extends JpaRepository<Type,Integer> {

    Optional<Type> findById(int id);
}
