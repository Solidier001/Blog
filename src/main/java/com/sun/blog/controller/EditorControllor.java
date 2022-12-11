package com.sun.blog.controller;

import com.google.gson.Gson;
import com.sun.blog.DAO.BlogDao;
import com.sun.blog.domain.Blog;
import com.sun.blog.service.BlogService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/Editor")
public class EditorControllor {


    @Autowired
    private BlogDao dao;

    @Resource
    private String rootPath;

    @Autowired
    private Gson gson;

    @Resource(name = "blogService")
    private BlogService service;

    @RequestMapping("/saveManuscript")
    public String saveManuscript(Blog blog,@RequestParam("abstract") StringBuilder abs) {
        if(abs.length()<=59)abs.append("...");
        else abs.replace(59,abs.length(),"...");
        blog.setAbstractString(abs.toString());
        service.saveManuscript(blog);
        return "redirect:/Editor/publishPage";
    }
    @RequestMapping("/publishPage")
    public String publishPage(Model model) {
        Blog blog=service.getManuscript();
        model.addAttribute("blog",blog);
        model.addAttribute("update",false);
        return "/admin/publisher";
    }

    @RequestMapping("/publish")
    public String publish(Blog Blog, @RequestParam("abstract") StringBuilder abs, HttpSession session) {
        if(abs.length()<=59)abs.append("...");
        else abs.replace(59,abs.length(),"...");
        Blog.setAbstractString(abs.toString());
        service.saveBlog(Blog);
        Integer count= (Integer) session.getAttribute("number");
        count++;
        session.setAttribute("number",count);
        return "redirect:/Editor/publishPage";
    }

    @RequestMapping("adminBlogsPage/{pageCount}")
    public String adminBlogsPage(@PathVariable("pageCount") int pageCount,Model model,HttpSession session){
        int count= (int) session.getAttribute("number");
        HashMap<String, Object> map=service.getAdminBlogsItems(pageCount,count);
        ArrayList<Map<String,Object>> list= (ArrayList<Map<String, Object>>) map.get("resoult");
        boolean isOut= (boolean) map.get("isOut");
        model.addAttribute("blogs",list);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("isOut",isOut);
        return "/admin/admin";
    }

    @ResponseBody
    @RequestMapping("deleteBlog/{id}")
    public String deleteBlog(@PathVariable("id") int id,HttpSession session){
        return service.deleteBlog(id,session);
    }

    @RequestMapping("editorPage/{id}")
    public String editorPage(Model model,@PathVariable("id") int id) {
        Blog blog=service.getBlogById(id);
        blog.setContent(service.getBlogContent(blog.getFilePath()));
        model.addAttribute("blog",blog);
        model.addAttribute("update",true);
        return "/admin/publisher";
    }
}
