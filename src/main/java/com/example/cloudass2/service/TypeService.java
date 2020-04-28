package com.example.cloudass2.service;

import com.example.cloudass2.Entity.Type;

import java.util.List;

public interface TypeService {
    void addType(Type type);

    List<Type> getAllTypes();

    Type getById(String id);
}
