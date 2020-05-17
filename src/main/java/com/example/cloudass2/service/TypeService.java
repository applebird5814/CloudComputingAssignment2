package com.example.cloudass2.service;

import com.example.cloudass2.entity.Type;

import java.util.List;

/**
 * @author miaos
 */
public interface TypeService {
    void addType(Type type);

    List<Type> getAllTypes();

    Type getById(String id);
}
