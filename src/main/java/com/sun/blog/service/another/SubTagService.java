package com.sun.blog.service.another;

import com.sun.blog.DAO.TagsDao;
import com.sun.blog.domain.Tags;
import com.sun.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SubTagService extends TagService {
    @Autowired
    private TagsDao  dao;

    public ArrayList<Tags> showApartTags(){
        return dao.TypesList5();
    }

    public ArrayList<Tags> showAllTags(){
        return dao.TypesList();
    }
}
