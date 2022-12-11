package com.sun.blog.controller;

import com.sun.blog.service.BlogService;
import com.sun.blog.service.TagService;
import com.sun.blog.service.TypeService;
import com.sun.blog.service.another.SubTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/Blog")
public class BlogController {
    @Autowired
    private BlogService service;

    @Resource(name = "subTagService")
    private SubTagService tagService;

    @Resource(name = "typeService")
    private TypeService Typeservice;

    @RequestMapping("/readBlog/{id}")
    public String readBlog(@PathVariable("id") int id, Model model){
        model.addAttribute("blog",service.getBlogById(id));
        return "/blog";
    }

    @RequestMapping("/getBlogsPage/{pageCount}")
    public String getBlogsPage(@PathVariable int pageCount, Model model, HttpSession session){
        Integer count=service.getNumberOfBlogs(session);
        HashMap<String, Object> map=service.getBlogItems(pageCount,count);
        ArrayList<Map<String, Object>> blogs= (ArrayList<Map<String, Object>>) map.get("resoult");
        boolean isOut= (boolean) map.get("isOut");
        model.addAttribute("blogs",blogs);
        model.addAttribute("count",count);
        model.addAttribute("isOut",isOut);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("typesList",Typeservice.TypesList());
        model.addAttribute("tagsList",tagService.showApartTags());
        return "/index";
    }

    @RequestMapping("/classifyBlogs/{type}/{pageCount}")
    public String classifyBlogs(@PathVariable int pageCount,@PathVariable("type") String type,Model model){
        HashMap<String ,Object> map=service.getListItemsByType(pageCount,type);
        ArrayList<Map<String, Object>> blogs= (ArrayList<Map<String, Object>>) map.get("resoult");
        boolean isOut= (boolean) map.get("isOut");
        model.addAttribute("blogs",blogs);
        model.addAttribute("isOut",isOut);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("typesList",Typeservice.TypesList());
        return "/types";
    }

    @RequestMapping("/tagBlogs/{id}/{pageCount}")
    public String tagBlogs(@PathVariable("pageCount") int pageCount,@PathVariable("id") String tagId,Model model){
        HashMap<String ,Object> map=service.getListItemsByTag(pageCount,tagId);
        ArrayList<Map<String, Object>> blogs= (ArrayList<Map<String, Object>>) map.get("resoult");
        boolean isOut= (boolean) map.get("isOut");
        model.addAttribute("blogs",blogs);
        model.addAttribute("isOut",isOut);
        model.addAttribute("pageCount",pageCount);
        model.addAttribute("tagId",tagId);
        model.addAttribute("tagsList",tagService.showAllTags());
        return "/tags";
    }

}
