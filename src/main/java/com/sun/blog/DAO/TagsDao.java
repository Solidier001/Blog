package com.sun.blog.DAO;

import com.sun.blog.domain.Tags;
import com.sun.blog.domain.Types;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TagsDao {

    @Insert("insert into tags(name,count,tagId) values(#{name},#{count},#{tagId})")
    int save(Tags types);

    @Update("update tags set count=count+1 where name=#{name}")
    int increaseCount(String name);

    @Update("update tags set count=count-1 where name=#{name}")
    int decreaseCount(String name);

    @Select("select * from tags where count>0")
    ArrayList<Tags> TypesList();

    @Select("select * from tags where count>0 limit 0,5")
    ArrayList<Tags> TypesList5();

    @Select("select count from tags where tagId=#{tagId}")
    Integer getCount(String tagId);

    @Select("select * from tags where id=#{id}")
    Tags getTypeById(String id);

    @Select("select tagId from tags limit 1")
    String getFirstTagId();

}
