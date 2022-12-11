package com.sun.blog.service;

import com.sun.blog.DAO.TypesDao;
import com.sun.blog.domain.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TypeService {
    @Autowired
    private TypesDao dao;

    public int save(Types types){
        return dao.save(types);
    }

    public int increaseCount(String name){
        return dao.increaseCount(name);
    }

    public ArrayList<Types> TypesList(){
        return dao.TypesList();
    }
}
