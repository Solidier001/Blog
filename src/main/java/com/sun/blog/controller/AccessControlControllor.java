package com.sun.blog.controller;

import com.sun.blog.DAO.BlogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/Access")
public class AccessControlControllor {

    @Autowired
    private BlogDao dao;

    @RequestMapping("Login")
    public String login(String password, HttpSession session) {
        if (password.equals("123456")) {
            session.setAttribute("user","user");
            session.setAttribute("number",dao.getNumberOfBlogs());
            return "redirect:/Editor/adminBlogsPage/1";
        } else return "redirect:/index.html";
    }
}
