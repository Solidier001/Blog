package com.sun.blog.DAO;

import com.sun.blog.domain.Blog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Repository
public interface BlogDao {
    @Insert("insert into blog(blogId,filePath,lableString,tittle,indexPicture,typeString,tagString,publishDate,important,shareinfor,appreciateOpen,commentOpen,abstractString)" +
            " values (#{blogId},#{filePath},#{lableString},#{tittle},#{indexPicture},#{typeString},#{tagString},#{publishDate},#{important},#{shareinfo},#{appreciateOpen},#{commentOpen},#{abstractString} )")
    void save(Blog blog);

    @Select("select * from blog where id=#{id}")
    Blog getBlogById(int id);

    @Select("select id,tittle,publishDate,indexPicture,typeString,abstractString from blog limit #{start},#{length}")
    ArrayList<Map<String,Object>> getListItems(int start,int length);

    @Select("select id,tittle,publishDate,important,typeString from blog limit #{start},#{length}")
    ArrayList<Map<String,Object>> getAdminBlogsListItems(int start,int length);

    @Select("select count(id) from blog")
    int getNumberOfBlogs();

    @Select("select typeString from blog where id=#{id}")
    String getTypeByid(int id);

    @Select("select tagString from blog where id=#{id}")
    String getTagsByid(int id);

    @Select("select filePath from blog where id=#{id}")
    String getFile(int id);

    @Update("delete from blog where id=#{id}")
    void delete(int id);

    @Update("update blog set lableString = #{lableString}, tittle = #{tittle}, indexPicture = #{indexPicture}, typeString = #{typeString}, tagString = #{tagString}, important = #{important}, shareinfor = #{shareinfo} where id = #{id}")
    void update(Blog blog);

    @Select("select id,tittle,publishDate,indexPicture,typeString,abstractString from blog where typeString = #{typeString} and id>=(select id from blog where typeString = #{typeString} limit #{start},1)limit #{length}")
    ArrayList<Map<String,Object>> getListItemsByType(int start,int length,String typeString);

    @Select("select id,tittle,publishDate,indexPicture,typeString,abstractString from blog where blogId in (select blogId from blog_tag where tagid=#{tagid}) and id>=(select id from blog limit #{start},1)limit #{length}")
    ArrayList<Map<String,Object>> getListItemsByTag(int start,int length,String tagid);

    @Select("select id,tittle,publishDate,lableString from blog where TO_DAYS(publishDate)=TO_DAYS(#{date})")
    ArrayList<Map<String,Object>> getFiled(String date);
}
