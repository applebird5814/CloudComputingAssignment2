package com.example.cloudass2.Service;

import com.example.cloudass2.Entity.Type;

import java.util.List;

/**
 * @author miaos
 */
public interface TypesService {
    void addType(Type type);

    List<Type> getAllTypes();

    Type getById(String id);
}
