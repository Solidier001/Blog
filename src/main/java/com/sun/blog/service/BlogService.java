package com.sun.blog.service;

import com.google.gson.Gson;
import com.sun.blog.DAO.BlogDao;
import com.sun.blog.DAO.TagsDao;
import com.sun.blog.DAO.TypesDao;
import com.sun.blog.domain.Blog;
import com.sun.blog.domain.Tags;
import com.sun.blog.domain.Types;
import com.sun.blog.utils.UuidUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class BlogService {

    /*private Gson gson;
    private String rootPath;
    private BlogDao dao;
    private StringBuffer filePathStringUtil;
    private TypesDao typesDao;*/

    protected Gson gson;
    protected String rootPath;
    protected BlogDao dao;
    protected StringBuffer filePathStringUtil;
    protected TypesDao typesDao;

    private TagsDao tagsDao;

    @Autowired
    public BlogService(Gson gson, String rootPath, BlogDao dao, TypesDao typesDao,TagsDao tagsDao) {
        this.gson = gson;
        this.rootPath = rootPath;
        this.dao = dao;
        this.typesDao = typesDao;
        this.filePathStringUtil = new StringBuffer(rootPath.length() * 2);
        this.tagsDao=tagsDao;
    }

    public void saveManuscript(Blog Blog) {
        File file = new File(rootPath, "manuscript");
        String data = gson.toJson(Blog);
        try {
            FileUtils.write(file, data, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Blog getManuscript() {
        File file = new File(rootPath, "manuscript");
        Blog Blog;
        try {
            String blogStr = FileUtils.readFileToString(file, "utf-8");
            Blog = gson.fromJson(blogStr, Blog.class);
        } catch (IOException e) {
            Blog = new Blog();
            e.printStackTrace();
        }
        return Blog;
    }

    public void saveBlog(Blog blog) {
        if (blog.getFilePath() == null||blog.getFilePath().equals("")||blog.getFilePath().equals("null")) publishBlog(blog);
        else updateBlog(blog);
    }

    public Blog getBlogById(int id) {
        return dao.getBlogById(id);
    }

    public HashMap<String, Object> getBlogItems(int pageCount, int count) {
        HashMap<String, Object> map = new HashMap<>(3);
        int start = (pageCount - 1) * 10;
        ArrayList<Map<String, Object>> resoult = dao.getListItems((pageCount - 1) * 10, 10);
        map.put("isOut", resoult.size() < 10 || count >= start + 9);
        map.put("resoult", resoult);
        return map;
    }

    public HashMap<String, Object> getAdminBlogsItems(int pageCount, int count) {
        HashMap<String, Object> map = new HashMap<>(3);
        int start = (pageCount - 1) * 10;
        ArrayList<Map<String, Object>> resoult = dao.getAdminBlogsListItems(start, 10);
        map.put("isOut", resoult.size() < 10 || count >= start + 9);
        map.put("resoult", resoult);
        return map;
    }

    public Integer getNumberOfBlogs(HttpSession session) {
        Integer count;
        if ((count = (Integer) session.getAttribute("count")) != null) ;
        else {
            count = dao.getNumberOfBlogs();
            session.setAttribute("count", count);
        }
        return count;
    }

    public String deleteBlog(int id, HttpSession session) {
        filePathStringUtil.delete(0, filePathStringUtil.length())
                .append(dao.getFile(id))
                .delete(0, 1);
        File blog = new File(rootPath, filePathStringUtil.toString());
        boolean reslut = blog.delete();
        if (!reslut) return "删除失败";
        String type = dao.getTypeByid(id);
        String[] tags = dao.getTagsByid(id).split(",");
        dao.delete(id);
        filePathStringUtil.delete(0, filePathStringUtil.length());
        Integer count = (Integer) session.getAttribute("number");
        count++;
        session.setAttribute("number", count);
        typesDao.decreaseCount(type);
        for (String tag:tags)tagsDao.decreaseCount(tag);
        return "OK";
    }

    public String getBlogContent(String filePath) {
        filePathStringUtil.delete(0, filePathStringUtil.length())
                .append(filePath)
                .delete(0, 1);
        File blog = new File(rootPath, filePathStringUtil.toString());
        try {
            return FileUtils.readFileToString(blog, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void publishBlog(Blog blog) {
        String filePath = filePathStringUtil.
                append(rootPath).
                append("static/blogPage").toString();
        String fileName = UUID.randomUUID().toString();
        File bolgFile = new File(filePath, fileName);
        filePathStringUtil.delete(0, filePathStringUtil.length());
        try {
            FileUtils.write(bolgFile, blog.getContent(), "utf-8");
            filePathStringUtil
                    .append(bolgFile.toURI().toString())
                    .delete(0, "file:".length() + rootPath.length() - 1);
            blog.setFilePath(filePathStringUtil.toString());
            blog.setPublishDate(new Date());
            blog.setBlogId(UuidUtil.getStringUuid());
            dao.save(blog);
            if (typesDao.increaseCount(blog.getTypeString()) < 1) {
                Types types=new Types();
                types.setName(blog.getTypeString());
                typesDao.save(types);
            }
            for (String tag: blog.getTag())
                if(tagsDao.increaseCount(tag)<1)
                    tagsDao.save(new Tags(tag));
            refreshManuscript();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBlog(Blog blog) {
        filePathStringUtil.delete(0, filePathStringUtil.length())
                .append(blog.getFilePath())
                .delete(0, 1);
        File bolgFile = new File(rootPath, filePathStringUtil.toString());
        try {
            FileUtils.write(bolgFile, blog.getContent(), "utf-8");
            String type = dao.getTypeByid(blog.getId());
            String[] tags = dao.getTagsByid(blog.getId()).split(",");
            dao.update(blog);
            if (!type.equals(blog.getTypeString())){
                typesDao.decreaseCount(type);
                if (typesDao.increaseCount(blog.getTypeString()) < 1) {
                    Types types=new Types();
                    types.setName(blog.getTypeString());
                    typesDao.save(types);
                }
                HashSet<String> tagsSet=new HashSet(Arrays.asList(tags));
                for (String tag:blog.getTag()){
                    if (tagsSet.contains(tag))tagsSet.remove(tag);
                    else tagsDao.increaseCount(tag);
                }
                Iterator<String> iterator= tagsSet.iterator();
                while (iterator.hasNext())tagsDao.decreaseCount(iterator.next());
            }
            refreshManuscript();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getListItemsByType(int start, String typeString){
        int count=getNumberOfBlogsBytype(typeString);
        HashMap<String, Object> map = new HashMap<>(3);
        start = (start - 1) * 10;
        ArrayList<Map<String, Object>> resoult = dao.getListItemsByType(start,10, typeString);
        map.put("isOut", resoult.size() < 10 || count == start + 9);
        map.put("resoult", resoult);
        return map;
    }

    public Integer getNumberOfBlogsBytype(String typeString) {;
        return typesDao.getCount(typeString);
    }

    public HashMap<String, Object> getListItemsByTag(int start, String tagId){
        int count=getNumberOfBlogsBytag(tagId);
        HashMap<String, Object> map = new HashMap<>(3);
        start = (start - 1) * 10;
        ArrayList<Map<String, Object>> resoult = dao.getListItemsByTag(start,10, tagId);
        map.put("isOut", resoult.size() < 10 || count >= start + 9);
        map.put("resoult", resoult);
        return map;
    }

    public Integer getNumberOfBlogsBytag(String tagId) {;
        return tagsDao.getCount(tagId);
    }

    private void refreshManuscript() throws IOException {
        File file = new File(rootPath, "manuscript");
        FileUtils.write(file, gson.toJson(new Blog()), "utf8");
    }

}
