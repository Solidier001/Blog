package com.sun.blog;


import com.sun.blog.DAO.BlogDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Map;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private BlogDao blogDao;
    @Test
    void contextLoads() throws InterruptedException {
        ArrayList<Map<String,Object>> list=blogDao.getFiled("2021-07-27");
        System.out.println(list);
    }

}
