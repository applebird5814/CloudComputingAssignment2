package com.example.cloudass2.Dao;

import com.example.cloudass2.Entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeDao extends JpaRepository<Type,Integer> {

    Optional<Type> findById(int id);
}
