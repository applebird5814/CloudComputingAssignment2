package com.example.cloudass2.service.impl;


import com.example.cloudass2.dao.TypeDao;
import com.example.cloudass2.entity.Type;

import com.example.cloudass2.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("typeServiceImpl")
public class TypeServiceImpl implements TypeService {
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
