package com.sun.blog.DAO;

import com.sun.blog.domain.Types;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TypesDao {

    @Insert("insert into types(name,count,id) values(#{name},#{count},#{id})")
    int save(Types types);

    @Update("update types set count=count+1 where name=#{name}")
    int increaseCount(String name);

    @Update("update types set count=count-1 where name=#{name}")
    int decreaseCount(String name);

    @Select("select * from types where count>0")
    ArrayList<Types> TypesList();

    @Select("select count from types where name=#{name}")
    Integer getCount(String name);

    @Select("select * from types where id=#{id}")
    Types getTypeById(String id);

    @Select("select id from types limit 1")
    String getFirstTypeId();

    @Select("select name from types limit 1")
    String getFirstTypeName();
}
