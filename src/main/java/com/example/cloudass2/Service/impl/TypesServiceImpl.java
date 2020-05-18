package com.example.cloudass2.Service.impl;


import com.example.cloudass2.Dao.TypeDao;
import com.example.cloudass2.Entity.Type;

import com.example.cloudass2.Service.TypesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author miaos
 */
@Service("typesServiceImpl")
public class TypesServiceImpl implements TypesService {
    @Autowired
    TypeDao typeDao;

    @Override
    public void addType(Type type) {
        typeDao.saveAndFlush(type);
    }

    @Override
    public List<Type> getAllTypes() {
        return typeDao.findAll();
    }

    @Override
    public Type getById(String id) {
        return typeDao.findById(Integer.parseInt(id)).get();
    }
}
