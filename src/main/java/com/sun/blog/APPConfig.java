package com.sun.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.blog.Interceptor.AccessControlInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;

@Configuration
@MapperScan(basePackages = "com.sun.blog.DAO")
public class APPConfig implements WebMvcConfigurer {
    @Autowired
    private ServletContext context;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AccessControlInterceptor interceptor=new AccessControlInterceptor();
        registry.addInterceptor(interceptor).addPathPatterns("/admin/**");
        registry.addInterceptor(interceptor).addPathPatterns("/Editor/**");
    }

    @Bean
    public String RootPath() {
        try {
            String rootPath = ResourceUtils.getURL("classpath:static/").getPath();
            return rootPath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public Gson addGson(){
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/","/Blog/getBlogsPage/1");
    }

    public void addAttr(String name,Object obj){
        context.setAttribute(name,obj);
    }

    public void addAttr(){
        System.out.println(context.getClass().getName());
    }
}
